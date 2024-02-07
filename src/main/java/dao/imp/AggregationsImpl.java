package dao.imp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dao.AggregationsDAO;
import io.vavr.control.Either;
import model.errors.OrderError;
import org.bson.Document;

public class AggregationsImpl implements AggregationsDAO {

    @Override
    public Either<OrderError, String> a() {
        Either<OrderError, String> result;

        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> menuItems = db.getCollection("menuitems");
        } catch (Exception e) {
            result= Either.left(new OrderError(e.getMessage()));

        }
        @Override
        public Either<OrderError, String> b () {
            return null;
        }

        @Override
        public Either<OrderError, String> c () {
            return null;
        }

        @Override
        public Either<OrderError, String> d () {
            return null;
        }

        @Override
        public Either<OrderError, String> e () {
            return null;
        }

        @Override
        public Either<OrderError, String> f () {
            return null;
        }

        @Override
        public Either<OrderError, String> g () {
            return null;
        }

        @Override
        public Either<OrderError, String> h () {
            return null;
        }

        @Override
        public Either<OrderError, String> i () {
            return null;
        }

        @Override
        public Either<OrderError, String> j () {
            return null;
        }

        @Override
        public Either<OrderError, String> k () {
            return null;
        }

        @Override
        public Either<OrderError, String> l () {
            return null;
        }

        @Override
        public Either<OrderError, String> m () {
            return null;
        }
    }
