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
import model.errors.CustomerError;
import model.errors.OrderError;
import model.modelHibernate.CustomersEntity;
import model.modelHibernate.MenuItemsEntity;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import model.modelo.Customer;
import org.bson.Document;

import java.time.LocalDate;
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
        try {
            em = jpautil.getEntityManager();
            List<CustomersEntity> customersEntities = em.createQuery("from CustomersEntity", CustomersEntity.class).getResultList();
            result = Either.right(customersEntities);


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
            MongoCollection<Document> menuItemsCollection = db.getCollection("menuItems");

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();

            for (CustomersEntity c : customersEntities) {
                Customer customer = c.toCustomer();
                Customer s = Customer.builder()
                        .first_name(customer.getFirst_name())
                        .last_name(c.getLastName())
                        .email(c.getEmail())
                        .phone(c.getPhone())
                        .date_of_birth(customer.getDate_of_birth())
                        .build();

                Document document = Document.parse(gson.toJson(s));
                customersCollection.insertOne(document);
            }

            result = Either.right(true);

        } catch (Exception e) {
            log.error(e.getMessage());
            result = Either.left(new OrderError(Constants.ERROR_WHILE_RETRIEVING_ORDERS));
            return result;
        }
        return result;
    }

}
