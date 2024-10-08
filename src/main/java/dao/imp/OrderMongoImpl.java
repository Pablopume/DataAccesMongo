package dao.imp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import common.Constants;
import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.extern.log4j.Log4j2;
import model.converters.OrderConverter;
import model.modelo.Order;
import model.errors.OrderError;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Named("OrderDB")
public class OrderMongoImpl implements OrdersDAO {
    private final OrderConverter orderConverter;



    @Inject
    public OrderMongoImpl(OrderConverter orderConverter) {
        this.orderConverter = orderConverter;

    }

    @Override
    //Con mongo
    public Either<OrderError, List<Order>> getAll() {
        Either<OrderError, List<Order>> result;

        //METODO CON MONGO
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            List<Document> customersList = customers.find().into(new ArrayList<>());
            List<Order> orders = new ArrayList<>();
            for (Document customer : customersList) {
                List<Document> ordersDocuments = (List<Document>) customer.get("orders");
                if (ordersDocuments != null) {
                    for (Document orderDocument : ordersDocuments) {
                        Order order = orderConverter.fromDocument(orderDocument);
                        orders.add(order);
                    }
                }
            }
            result = Either.right(orders);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));

        }


        return result;
    }


    @Override
    public Either<OrderError, List<Order>> get(ObjectId id) {
        Either<OrderError, List<Order>> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            Bson filter = Filters.eq("_id", id);
            Document customerDocument = customers.find(filter).first();
            if (customerDocument != null) {
                List<Document> ordersDocuments = (List<Document>) customerDocument.get("orders");
                if (ordersDocuments != null) {
                    List<Order> orders = ordersDocuments.stream()
                            .map(orderConverter::fromDocument)
                            .toList();
                    result = Either.right(orders);
                } else {
                    result = Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
                }
            } else {
                result = Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;
    }

    public Either<OrderError, Integer> delete(Order order) {
        Either<OrderError, Integer> result;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = order.getDate();
        String formattedDate = date.format(formatter);
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            Bson filter = Filters.elemMatch("orders", Filters.regex("date", formattedDate));

            Document customerDocument = customers.find(filter).first();
            if (customerDocument != null) {
                List<Document> ordersDocuments = (List<Document>) customerDocument.get("orders");
                if (ordersDocuments != null) {
                    List<Document> newOrdersDocuments = ordersDocuments.stream()
                            .filter(orderDocument -> !orderDocument.getString("date").equals(formattedDate))
                            .toList();
                    Bson update = Updates.set("orders", newOrdersDocuments);
                    UpdateResult updateResult = customers.updateOne(filter, update);
                    if (updateResult.getModifiedCount() > 0) {
                        result = Either.right(1);
                    } else {
                        result = Either.left(new OrderError("Error while deleting order"));
                    }
                } else {
                    result = Either.left(new OrderError("Error while deleting order"));
                }
            } else {
                result = Either.left(new OrderError("Error while deleting order"));
            }

        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = Either.left(new OrderError("Error while deleting order"));

        }
        return result;
    }


    @Override
    public Either<OrderError, Integer> update(Order order) {
        Either<OrderError, Integer> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = order.getDate().format(formatter);

            Bson filter = Filters.elemMatch("orders", Filters.regex("date", formattedDate));
            Document customerDocument = customers.find(filter).first();

            if (customerDocument != null) {
                List<Document> ordersList = (List<Document>) customerDocument.get("orders");
                for (Document doc : ordersList) {
                    if (doc.getString("date").equals(formattedDate)) {
                        doc.put("table_id", order.getTable_id());
                        doc.put("date", formattedDate);
                        List<Document> orderItemListDocuments = order.getOrderItemList().stream()
                                .map(orderItem -> new Document("quantity", orderItem.getQuantity())
                                        .append("menuItemId", orderItem.getMenuItemId()))
                                .toList();
                        doc.put("orderItemList", orderItemListDocuments);

                        Bson updateFilter = Filters.eq("_id", customerDocument.getObjectId("_id"));
                        Bson updateOperation = Updates.set("orders", ordersList);
                        customers.updateOne(updateFilter, updateOperation);

                        result = Either.right(1);
                        return result;
                    }
                }
                result = Either.left(new OrderError("Pedido no encontrado"));
            } else {
                result = Either.left(new OrderError("Cliente no encontrado"));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = Either.left(new OrderError("Error al actualizar el pedido"));
        }
        return result;
    }






    @Override
    public Either<OrderError, Order> add(Order order, ObjectId id) {
        Either<OrderError, Order> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            Bson filter = Filters.eq("_id", id);


            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = order.getDate().format(formatter);
            Document newOrderDocument = new Document()
                    .append("date", formattedDate)
                    .append("table_id", order.getTable_id());

            List<Document> orderItemListDocuments = order.getOrderItemList().stream()
                    .map(orderItem -> new Document("quantity", orderItem.getQuantity())
                            .append("menuItemId", orderItem.getMenuItemId()))
                    .toList();

            newOrderDocument.append("orderItemList", orderItemListDocuments);
            Bson update = Updates.push("orders", newOrderDocument);
            UpdateResult updateResult = customers.updateOne(filter, update);

            if (updateResult.getModifiedCount() > 0) {
                result = Either.right(order);
            } else {
                result = Either.left(new OrderError("Error while adding order"));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        }
        return result;
    }


}
