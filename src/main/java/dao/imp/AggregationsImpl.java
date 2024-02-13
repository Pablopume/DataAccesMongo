package dao.imp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
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
import static com.mongodb.client.model.Filters.*;
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
                    unwind("$orders")
                    , group("$orders.table_id"
                            , sum("table", 0))
                    , sort(Sorts.descending("table"))
                    , project(exclude("table"))
                    , limit(1)))
                    .into(new ArrayList<>());

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
                            and(Filters.eq("_id", "$_id"), Filters.eq("table_id", "$orders.table_id")),
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
                            and(Filters.eq("date", "$orders.date"), Filters.eq("menuItemId", "$orders.orderItemList.menuItemId")),
                            sum("menuItem", "$orders.orderItemList.quantity")
                    ),
                    match(eq("menuItem", 1)),
                    addFields(new Field<>("menuItem", "$_id.menuItemId"), new Field<>("date", "$_id.date")),
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

            List<Document> documents = customers.aggregate(Arrays.asList(
                    unwind("$orders"),
                    unwind("$orders.orderItemList"),
                    lookup("menuitems", "orders.orderItemList.menuItemId", "id", "menuitems"),
                    unwind("$menuitems"),
                    addFields(new Field("total", new Document("$multiply", Arrays.asList("$orders.orderItemList.quantity", "$menuitems.price")))),
                    group("$orders.date", sum("total", "$total"))
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
                    unwind("$orders.orderItemList"),
                    lookup("menuitems", "orders.orderItemList.menuItemId", "id", "menuitems"),
                    unwind("$menuitems"),
                    addFields(new Field("total", new Document("$multiply", Arrays.asList("$orders.orderItemList.quantity", "$menuitems.price")))),
                    group("$first_name", sum("total", "$total")),
                    sort(Sorts.descending("first_name","total")),
                    limit(1)

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


    public Either<OrderError, String> ex2a() {
        //name of brawlers with id between 16000017 and 16000030
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("brawl");
            MongoCollection<Document> collection = db.getCollection("brawlers");

            List<Document> documents = collection.aggregate(List.of(
                    match(and(gte("id", 16000017), lt("id", 16000030))), project(Projections.fields(Projections.include("name"), Projections.excludeId()))

            )).into(new ArrayList<>());

            String json = documents.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));

        }
        return result;
    }

    public Either<OrderError, String> ex2b() {
        Either<OrderError, String> result;
        //name of gadgets with id between 23000255 and 23000264
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("brawl");
            MongoCollection<Document> collection = db.getCollection("brawlers");

            List<Document> documents = collection.aggregate(Arrays.asList(
                    Aggregates.unwind("$gadgets"),
                    Aggregates.match(Filters.and(
                            Filters.gte("gadgets.id", 23000255),
                            Filters.lte("gadgets.id", 23000264)
                    )),
                    Aggregates.project(Projections.fields(
                            Projections.excludeId(),
                            Projections.include("gadgets.name")
                    ))
            )).into(new ArrayList<>());
            String json = documents.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));

        }
        return result;
    }

    public Either<OrderError, String> ex2c() {
        //name of brawler with 2 gadgets
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("brawl");
            MongoCollection<Document> collection = db.getCollection("brawlers");
            List<Document> documents = collection.aggregate(Arrays.asList(
                    Aggregates.unwind("$gadgets"),
                    Aggregates.group("$name", Accumulators.sum("totalGadgets", 1)),
                    Aggregates.match(Filters.eq("totalGadgets", 2))
            )).into(new ArrayList<>());

            String json = documents.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }

    public Either<OrderError, String> ex2d() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("brawl");
            MongoCollection<Document> collection = db.getCollection("brawlers");
//name of brawler with star power "SHELL SHOCK"
            List<Document> documents = collection.aggregate(Arrays.asList(
                    Aggregates.match(Filters.eq("starPowers.name", "SHELL SHOCK")),
                    Aggregates.project(Projections.fields(
                            Projections.excludeId(),
                            Projections.include("name")
                    ))
            )).into(new ArrayList<>());

            String json = documents.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);

        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }


    public Either<OrderError, String> ex2e() {
        Either<OrderError, String> result;
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("brawl");
            MongoCollection<Document> collection = db.getCollection("brawlers");
            List<Document> documents = collection.aggregate(Arrays.asList(
                    Aggregates.match(Filters.and(
                            Filters.eq("starPowers.name", "SLICK BOOTS"),
                            Filters.eq("gadgets.name", "SPEEDLOADER")
                    )),
                    Aggregates.project(Projections.fields(
                            Projections.excludeId(),
                            Projections.include("name")
                    )))).into(new ArrayList<>());
            String json = documents.stream().map(Document::toJson).collect(Collectors.joining(","));
            result = Either.right(json);
        } catch (Exception e) {
            result = Either.left(new OrderError(e.getMessage()));
        }
        return result;
    }
}
