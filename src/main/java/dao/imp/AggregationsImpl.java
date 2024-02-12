package dao.imp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import dao.AggregationsDAO;
import io.vavr.control.Either;
import model.errors.OrderError;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Accumulators.*;
import static com.mongodb.client.model.Aggregates.*;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.*;
import static com.mongodb.client.model.Sorts.descending;

public class AggregationsImpl implements AggregationsDAO {

    @Override
    public Either<OrderError, String> a() {
        Either<OrderError, String> result;

        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> menuItems = db.getCollection("menuitems");
            List<Document> document = menuItems.aggregate(Arrays.asList(
                    sort(descending("price")), limit(1), project(fields(include("description"), excludeId())))).into(new ArrayList<>());

            String json = document.get(0).toJson();
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));

        }
        return result;
    }

    @Override
    public Either<OrderError, String> b() {
        // Get the orders of a given customer, showing the name of the customer and the
        // id of table
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            List<Document> document = customers.aggregate(Arrays.asList(
                    match(eq("_id", new ObjectId("65c5f9337a122f76869ffdb3")))
                    , unwind("$orders")
                    , project(fields(include("first_name", "orders.table_id"), excludeId())))).into(new ArrayList<>());

            String json = document.get(0).toJson();
            result = Either.right(json);


        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }

    @Override
    public Either<OrderError, String> c() {
        //Get the number of items of each order
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            List<Document> document = customers.aggregate(Arrays.asList(
                    unwind("$orders")
                    , unwind("$orders.orderItemList")
                    , group("$orders.date", sum("quantity", "$orders.orderItemList.quantity")), project(excludeId()))).into(new ArrayList<>());
            String json = document.stream().map(Document::toJson).collect(Collectors.joining(","));

            result = Either.right(json);
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }

    @Override
    public Either<OrderError, String> d() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            List<Document> document = customers.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.orderItemList"),
                    match(eq("orders.orderItemList.menuItemId", 3)),
                    project(include("first_name"))
            )).into(new ArrayList<>());
            String json = document.stream().map(Document::toJson).collect(Collectors.joining(","));

            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }


    @Override
    public Either<OrderError, String> e() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            List<Document> document = customers.aggregate(Arrays.asList(unwind("$orders"), unwind("$orders.orderItemList"), group("$orders.date", avg("average", "$orders.orderItemList.quantity")), project(excludeId()))).into(new ArrayList<>());
            String json = document.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));

        }
        return result;
    }

    @Override
    public Either<OrderError, String> f() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            //get the menuItemId most requested
            List<Document> document = customers.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.orderItemList"),
                    group("$orders.orderItemList.menuItemId", sum("total", "$orders.orderItemList.quantity")),
                    sort(descending("total")),
                    limit(1)
            )).into(new ArrayList<>());
            String json = document.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }

    @Override
    public Either<OrderError, String> g() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
//Show a list with the number of items of each type requested by a selected customer
            List<Document> document = customers.aggregate(Arrays.asList(
                    match(eq("_id", new ObjectId("65c5f9337a122f76869ffdb3"))
                    ), unwind("$orders"), unwind("$orders.orderItemList")
                    , group("$orders.orderItemList.menuItemId"
                            , sum("total", "$orders.orderItemList.quantity"))
                    , project(excludeId()))).into(new ArrayList<>());
            String json = document.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }

    @Override
    public Either<OrderError, String> h() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            List<Document> document = customers.aggregate(Arrays.asList(
                    unwind("$orders"), group("$orders.table_id", sum("table", 0)), sort(Sorts.descending("table")), project(exclude("table")), limit(1))).into(new ArrayList<>());

            String json = document.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }

    @Override
    public Either<OrderError, String> i() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            List<Document> documents = customers.aggregate(Arrays.asList(
                    unwind("$orders"),
                    group(
                            Filters.and(Filters.eq("_id", "$_id"), Filters.eq("table_id", "$orders.table_id")),
                            sum("total", 1)
                    ),
                    sort(descending("total")),
                    group("$_id._id", first("most_requested_table", "$_id.table_id")),
                    project(fields(include("most_requested_table")))
            )).into(new ArrayList<>());

            String json = documents.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }


    @Override
    public Either<OrderError, String> j() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            List<Document> documents = customers.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.orderItemList"),
                    group(
                            Filters.and(Filters.eq("date", "$orders.date"), Filters.eq("menuItemId", "$orders.orderItemList.menuItemId")),
                            sum("menuItem", "$orders.orderItemList.quantity")
                    ),
                    match(eq("menuItem", 1)),
                    addFields(new Field<>("menuItem", "$_id.menuItemId"),new Field<>("date", "$_id.date")),
                    project(excludeId())
            )).into(new ArrayList<>());

            String json = documents.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }


    @Override
    public Either<OrderError, String> k() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            //Price paid for each order (Use $lookup)


            List<Document> documents = customers.aggregate(Arrays.asList(
                    unwind("$orders"),
                    lookup("menuitems", "orders.orderItemList.menuItemId", "id", "menuItems"),
                    unwind("$menuItems"),
                    group("$orders._id", sum("total", "$menuItems.price")),
                    project(fields(excludeId(), include("total"))
                    )
            )).into(new ArrayList<>());


            String json = documents.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }

    @Override
    public Either<OrderError, String> l() {
        //l. Get the name of the customer that has spent more money in the restaurant
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            List<Document> documents = customers.aggregate(Arrays.asList(
                    unwind("$orders"),
                    lookup("menuitems", "orders.orderItemList.menuItemId", "id", "menuItems"),
                    unwind("$menuItems"),
                    group("$orders.customer_id", sum("total", "$menuItems.price")),
                    sort(descending("total")),
                    limit(1),
                    lookup("customers", "_id", "customer_id", "customer"),
                    unwind("$customer"),
                    project(fields(include("first_name"), excludeId())
                    )
            )).into(new ArrayList<>());

            String json = documents.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }

    @Override
    public Either<OrderError, String> m() {
        Either<OrderError, String> result;
        //total amount
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            List<Document> documents = customers.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.orderItemList"), // Unwind orderItemList
                    lookup("menuitems", "orders.orderItemList.menuItemId", "id", "menuItems"),
                    unwind("$menuItems"),
                    group("$orders.customer_id",
                            sum("total", new Document("$multiply", Arrays.asList("$menuItems.price", "$orders.orderItemList.quantity")))), // Multiply price by quantity
                    project(fields(excludeId(), include("total")))
            )).into(new ArrayList<>());

            String json = documents.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }




}
