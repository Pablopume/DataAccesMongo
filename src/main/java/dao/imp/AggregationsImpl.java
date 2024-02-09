package dao.imp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import dao.AggregationsDAO;
import io.vavr.control.Either;
import model.errors.OrderError;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.*;
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
                  match(Filters.eq("_id", new ObjectId("65c5f9337a122f76869ffdb3")))
                    ,unwind("$orders")
                    ,project(fields(include("first_name","orders.table_id"), excludeId())))).into(new ArrayList<>());

            String json = document.get(0).toJson();
            result = Either.right(json);


        } catch (Exception e) {
            result= Either.left(new OrderError(e.getMessage()));
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
                    ,project(fields(include("orders.orderItemList")))
                    ,unwind("$orders.orderItemList")
                    ,group("$orders", sum("quantity", 1)),project(exclude("orderItemList.menuItemId")))).into(new ArrayList<>());
            String json = document.get(0).toJson();
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

            ;
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return null;
    }

    @Override
    public Either<OrderError, String> e() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            ;
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return null;
    }

    @Override
    public Either<OrderError, String> f() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            ;
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return null;
    }

    @Override
    public Either<OrderError, String> g() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            ;
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return null;
    }

    @Override
    public Either<OrderError, String> h() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            ;
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return null;
    }

    @Override
    public Either<OrderError, String> i() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            ;
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return null;
    }

    @Override
    public Either<OrderError, String> j() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            ;
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return null;
    }

    @Override
    public Either<OrderError, String> k() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            ;
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return null;
    }

    @Override
    public Either<OrderError, String> l() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            ;
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return null;
    }

    @Override
    public Either<OrderError, String> m() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");

            ;
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return null;
    }
}
