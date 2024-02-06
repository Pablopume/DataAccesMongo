package dao.imp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import common.Constants;
import dao.HibernateDao;
import dao.JPAUtil;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import lombok.extern.log4j.Log4j2;
import model.LocalDateAdapter;
import model.LocalDateTimeAdapter;
import model.errors.CustomerError;
import model.errors.OrderError;
import model.modelHibernate.CredentialsEntity;
import model.modelHibernate.CustomersEntity;
import model.modelHibernate.MenuItemsEntity;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.modelo.Credentials;
import model.modelo.Customer;
import model.modelo.MenuItem;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Log4j2
public class HibDaoImpl implements HibernateDao {
    private final JPAUtil jpautil;
    private EntityManager em;

    @Inject
    public HibDaoImpl(JPAUtil jpautil) {
        this.jpautil = jpautil;
    }


    public Either<CustomerError, List<CustomersEntity>> getAllCustomer() {
        Either<CustomerError, List<CustomersEntity>> result;

        EntityManager entityManager;
        entityManager = jpautil.getEntityManager();
        try {

            List<CustomersEntity> customersList = entityManager.createNamedQuery("CustomersEntity.getAll", CustomersEntity.class).getResultList();

            result = Either.right(customersList);


        } finally {
            if (em != null) em.close();
        }

        return result;
    }


    public Either<OrderError, List<MenuItemsEntity>> getAllMenuItem() {
        Either<OrderError, List<MenuItemsEntity>> result;


        try {
            em = jpautil.getEntityManager();
            List<MenuItemsEntity> menuItemsEntities = em.createQuery("from MenuItemsEntity", MenuItemsEntity.class).getResultList();
            result = Either.right(menuItemsEntities);

        } finally {
            if (em != null) em.close();
        }

        return result;
    }


    public Either<OrderError, Boolean> fromSqlToMongo() {
        Either<OrderError, Boolean> result;
        List<CustomersEntity> customersEntities = getAllCustomer().get();
        List<MenuItemsEntity> menuItemsEntities = getAllMenuItem().get();

        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customersCollection = db.getCollection("customers");
            MongoCollection<Document> menuItemsCollection = db.getCollection("menuitems");
            MongoCollection<Document> credentialsCollection = db.getCollection("credentials");
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();
            List<Document> documents1 = new ArrayList<>();
            List<Document> documents2 = new ArrayList<>();
            for (CustomersEntity c : customersEntities) {

                Customer customer = c.toCustomer();

                Document document = Document.parse(gson.toJson(customer));

                // Insert the customer
                customersCollection.insertOne(document);
                // Get the generated ID
                ObjectId customerId = (ObjectId) document.get("_id");
                CredentialsEntity credentials1 = c.getCredentialsById();
                Credentials credentials = credentials1.toCredentials();
                Document document1 = Document.parse(gson.toJson(credentials));
                document1.remove("_id");
                document1.append("_id", customerId);
                documents1.add(document1);
            }
            for (MenuItemsEntity m : menuItemsEntities) {
                MenuItem menuItem = m.toMenuItem();
                Document document2 = Document.parse(gson.toJson(menuItem));
                documents2.add(document2);
            }
            result = Either.right(true);
            credentialsCollection.insertMany(documents1);
            menuItemsCollection.insertMany(documents2);
        } catch (Exception e) {
            log.error(e.getMessage());
            result = Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
            return result;
        }
        return result;
    }





}
