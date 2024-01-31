package dao.imp;

import common.Constants;
import dao.CustomerDAO;
import dao.JPAUtil;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import lombok.extern.log4j.Log4j2;
import model.modelo.Customer;
import model.errors.CustomerError;
import model.modelHibernate.CredentialsEntity;
import model.modelHibernate.CustomersEntity;
import model.modelHibernate.OrderItemsEntity;
import model.modelHibernate.OrdersEntity;
import io.vavr.control.Either;
import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
@Log4j2
public class CustomerHibernateImpl implements CustomerDAO {

    private final JPAUtil jpautil;
    private EntityManager em;

    @Inject
    public CustomerHibernateImpl(JPAUtil jpautil) {
        this.jpautil = jpautil;
    }
    @Override
    public Either<CustomerError, List<Customer>> add(Customer customer) {
        Either<CustomerError, List<Customer>> result;
        em = jpautil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            CredentialsEntity credentialsEntity = customer.getCredentials().toCredentialsEntity();
            CustomersEntity customersEntity = customer.toCustomerEntity();
            em.persist(credentialsEntity);
            customersEntity.setId(credentialsEntity.getId());
            em.persist(customersEntity);
            List<Customer> customersList = getAll().getOrElse(Collections.emptyList());
            result = Either.right(customersList);
            transaction.commit();
        } catch (PersistenceException e) {
            transaction.rollback();
            if (e.getCause() instanceof SQLIntegrityConstraintViolationException) {
                result = Either.left(new CustomerError(1, Constants.DUPLICATED_USER_NAME));
            } else {
                result = Either.left(new CustomerError(0, "Error while inserting customer"));
            }
        } finally {
            em.close();
        }

        return result;
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


    @Override
    public Either<CustomerError, List<Customer>> getAll() {
        Either<CustomerError, List<Customer>> result;
        try {
            em = jpautil.getEntityManager();
            List<CustomersEntity> customersEntities = em.createQuery("from CustomersEntity", CustomersEntity.class).getResultList();

            List<Customer> customersList = customersEntities.stream()
                    .map(CustomersEntity::toCustomer)
                    .toList();

            if (customersList.isEmpty()) {
                result = Either.left(new CustomerError(0, "Error while retrieving customers"));
            } else {
                result = Either.right(customersList);
            }
        } finally {
            if (em != null) em.close();
        }

        return result;
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
