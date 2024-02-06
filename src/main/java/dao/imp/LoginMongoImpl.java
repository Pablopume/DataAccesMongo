package dao.imp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dao.LoginDAO;
import io.vavr.control.Either;
import model.LocalDateAdapter;
import model.LocalDateTimeAdapter;
import model.ObjectIdAdapter;
import model.modelo.Credentials;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class LoginMongoImpl implements LoginDAO {



    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();

    @Override
    public Either<String, List<Credentials>> getAll() {
        Either<String, List<Credentials>> response;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customersCollection = db.getCollection("credentials");
            List<Credentials> credentials = customersCollection.find().map(document -> gson.fromJson(document.toJson(), Credentials.class)).into(new ArrayList<>());
            response = Either.right(credentials);

        } catch (Exception e) {
            response = Either.left(e.getMessage());
        }

        return response;

    }
}
