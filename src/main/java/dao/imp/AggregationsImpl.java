package dao.imp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Aggregates;
import dao.AggregationsDAO;
import io.vavr.control.Either;
import model.errors.OrderError;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.project;
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
                    Aggregates
                            .sort(descending("price")), limit(1), project(fields(include("description"),excludeId())))).into(new ArrayList<>());

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
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");
            List<Document> document = customers.aggregate(Arrays.asList(
                    Aggregates.lookup("orders", "id", "customerId", "orders"),
                    Aggregates.project(fields(include("name", "tableId", "orders"))))).into(new ArrayList<>());

            String json = document.get(0).toJson();
            return Either.right(json);
        } catch (Exception e) {
            return Either.left(new OrderError(e.getMessage()));
        }
    }

    @Override
    public Either<OrderError, String> c() {
        return null;
    }

    @Override
    public Either<OrderError, String> d() {
        return null;
    }

    @Override
    public Either<OrderError, String> e() {
        return null;
    }

    @Override
    public Either<OrderError, String> f() {
        return null;
    }

    @Override
    public Either<OrderError, String> g() {
        return null;
    }

    @Override
    public Either<OrderError, String> h() {
        return null;
    }

    @Override
    public Either<OrderError, String> i() {
        return null;
    }

    @Override
    public Either<OrderError, String> j() {
        return null;
    }

    @Override
    public Either<OrderError, String> k() {
        return null;
    }

    @Override
    public Either<OrderError, String> l() {
        return null;
    }

    @Override
    public Either<OrderError, String> m() {
        return null;
    }
}
