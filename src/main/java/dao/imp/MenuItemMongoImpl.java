package dao.imp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import dao.MenuItemDAO;
import io.vavr.control.Either;
import model.LocalDateAdapter;
import model.LocalDateTimeAdapter;
import model.ObjectIdAdapter;
import model.modelo.MenuItem;
import model.errors.OrderError;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MenuItemMongoImpl implements MenuItemDAO {


    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();



    @Override
    public Either<OrderError, List<MenuItem>> getAll() {
        Either<OrderError, List<MenuItem>> result;


        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            List<MenuItem> menuItems = db.getCollection("menuitems").find().map(document -> gson.fromJson(document.toJson(), MenuItem.class)).into(new ArrayList<>());

            result = Either.right(menuItems);}
        catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }

    public Either<OrderError, MenuItem> get(int id) {
        Either<OrderError, MenuItem> result;
try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MenuItem menuItem = gson.fromJson(db.getCollection("menuitems").find(new org.bson.Document("id", id)).first().toJson(), MenuItem.class);
            result = Either.right(menuItem);
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }

}
