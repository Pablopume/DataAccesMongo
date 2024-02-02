package dao.imp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.Constants;
import dao.JPAUtil;
import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import lombok.extern.log4j.Log4j2;
import model.LocalDateAdapter;
import model.LocalDateTimeAdapter;
import model.ObjectIdAdapter;
import model.modelo.Order;
import model.modelo.OrderItem;
import model.errors.OrderError;
import model.modelHibernate.OrdersEntity;
import model.modelHibernate.RestaurantTablesEntity;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Named("OrderDB")
public class OrderHibernateImpl implements OrdersDAO {

    private final JPAUtil jpautil;
    private EntityManager em;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(ObjectId.class, new ObjectIdAdapter())
            .create();
    @Inject
    public OrderHibernateImpl(JPAUtil jpautil) {

        this.jpautil = jpautil;

    }

    @Override
    //Con mongo
    public Either<OrderError, List<Order>> getAll() {
        Either<OrderError, List<Order>> result;

        //METODO CON MONGO
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customers = db.getCollection("customers");


        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        }




        return result;
    }


    @Override
    public Either<OrderError, List<Order>> getAll(ObjectId id) {
        return null;
    }

    public Either<OrderError, Integer> delete(Order order) {
        Either<OrderError, Integer> result;

        try {
            em = jpautil.getEntityManager();
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            try {
                OrdersEntity ordersEntity = em.find(OrdersEntity.class, order.getId());
                em.remove(em.merge(ordersEntity));
                tx.commit();
                result = Either.right(1);
            } catch (Exception e) {
                if (tx.isActive()) tx.rollback();

                result = Either.left(new OrderError("Error deleting order"));
            } finally {
                em.close();
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            result = Either.left(new OrderError(Constants.ERROR_CONNECTING_TO_DATABASE));
        }

        return result;
    }


    @Override
    public Either<OrderError, Integer> update(Order order) {
        return null;


    }


    @Override
    public Either<OrderError, Order> add(Order order) {

        return null;
    }

}
