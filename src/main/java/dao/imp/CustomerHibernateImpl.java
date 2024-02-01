package dao.imp;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.Constants;
import dao.CustomerDAO;
import dao.JPAUtil;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import lombok.extern.log4j.Log4j2;
import model.converters.CustomerConverter;
import model.modelo.Customer;
import model.errors.CustomerError;
import model.modelHibernate.CustomersEntity;
import model.modelHibernate.OrderItemsEntity;
import model.modelHibernate.OrdersEntity;
import io.vavr.control.Either;
import org.bson.Document;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class CustomerHibernateImpl implements CustomerDAO {
    private final CustomerConverter customerConverter;
    private final JPAUtil jpautil;
    private EntityManager em;

    @Inject
    public CustomerHibernateImpl(CustomerConverter customerConverter, JPAUtil jpautil) {
        this.customerConverter = customerConverter;
        this.jpautil = jpautil;
    }

    @Override
    public Either<CustomerError, List<Customer>> add(Customer customer) {
        return null;
    }

    @Override
    public Either<CustomerError, Integer> delete(Customer customer, boolean delete) {
        Either<CustomerError, Integer> result;

        if (!delete) {
            em = jpautil.getEntityManager();
            EntityTransaction transaction = em.getTransaction();

            try {
                transaction.begin();
                CustomersEntity customersEntity = em.find(CustomersEntity.class, customer.getId());
                if (customersEntity != null) {
                    em.remove(customersEntity);
                }

                transaction.commit();
                result = Either.right(0);
            } catch (PersistenceException e) {
                transaction.rollback();
                if (e instanceof RollbackException) {
                    result = Either.left(new CustomerError(2, Constants.DUPLICATED_USER_NAME));

                } else {
                    result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
                }
            } finally {
                em.close();
            }
        } else {
            result = deleteRelationsWithCustomers(customer);
        }
        return result;
    }


    public Either<CustomerError, List<Customer>> update(Customer customer) {
        Either<CustomerError, List<Customer>> result;
        em = jpautil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            CustomersEntity customersEntity = em.find(CustomersEntity.class, customer.getId());
            if (customersEntity != null) {
                customersEntity.setFirstName(customer.getFirst_name());
                customersEntity.setLastName(customer.getLast_name());
                customersEntity.setEmail(customer.getEmail());
                customersEntity.setPhone(customer.getPhone());
                customersEntity.setDateOfBirth(Date.valueOf(customer.getDate_of_birth()));
                em.merge(customersEntity);
            }
            transaction.commit();
            List<Customer> updatedCustomers = getAll().get(); // Implementa este método según tus necesidades

            result = Either.right(updatedCustomers);
        } catch (PersistenceException e) {
            transaction.rollback();
            result = Either.left(new CustomerError(0, Constants.ERROR_WHILE_RETRIEVING_ORDERS));
        } finally {
            em.close();
        }

        return result;
    }

    public Either<CustomerError, List<Customer>> getAll() {
        try (MongoClient mongo = MongoClients.create("mongodb://informatica.iesquevedo.es:2323")) {
            MongoDatabase db = mongo.getDatabase("PabloSerrano_Restaurant");
            MongoCollection<Document> customersCollection = db.getCollection("customers");

            List<Document> customersDocuments = customersCollection.find().into(new ArrayList<>());

            List<Customer> customersList = customersDocuments.stream()
                    .map(customerConverter::fromDocument)
                    .toList();

            if (customersList.isEmpty()) {
                return Either.left(new CustomerError(0, "Error while retrieving customers"));
            } else {
                return Either.right(customersList);
            }
        } catch (Exception e) {
            e.printStackTrace();  // Manejo de la excepción (puedes personalizarlo según tus necesidades)
            return Either.left(new CustomerError(0, "Error while retrieving customers"));
        }
    }


    private Either<CustomerError, Integer> deleteRelationsWithCustomers(Customer customer) {
        em = jpautil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            CustomersEntity customersEntity = em.find(CustomersEntity.class, customer.getId());
            if (customersEntity == null) {
                return Either.left(new CustomerError(1, Constants.ERROR_DELETING_CUSTOMER));
            }

            Collection<OrdersEntity> orders = customersEntity.getOrdersById();
            for (OrdersEntity order : orders) {
                Collection<OrderItemsEntity> orderItems = order.getOrderItemsByOrderId();
                orderItems.forEach(em::remove);
                em.remove(order);
            }

            em.remove(customersEntity);
            transaction.commit();
            return Either.right(0);
        } catch (PersistenceException e) {

            if (transaction.isActive()) {
                transaction.rollback();

            }


        } finally {
            em.close();
        }
        return Either.right(0);
    }


}
