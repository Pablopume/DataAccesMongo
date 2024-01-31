package dao.imp;

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
import model.modelo.Order;
import model.modelo.OrderItem;
import model.errors.OrderError;
import model.modelHibernate.OrdersEntity;
import model.modelHibernate.RestaurantTablesEntity;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Named("OrderDB")
public class OrderHibernateImpl implements OrdersDAO {

    private final JPAUtil jpautil;
    private EntityManager em;

    @Inject
    public OrderHibernateImpl(JPAUtil jpautil) {

        this.jpautil = jpautil;

    }
    @Override
    public Either<OrderError, List<Order>> getAll() {
        Either<OrderError, List<Order>> result;


        try {
            em = jpautil.getEntityManager();
            List<OrdersEntity> ordersEntities = em.createQuery("from OrdersEntity", OrdersEntity.class).getResultList();

            List<Order> ordersList = ordersEntities.stream()
                    .map(OrdersEntity::toOrder)
                    .toList();

            if (ordersList.isEmpty()) {
                result = Either.left(new OrderError( "Error while retrieving orders"));
            } else {
                result = Either.right(ordersList);
            }
        } finally {
            if (em != null) em.close();
        }

        return result;
    }


    @Override
    public Either<OrderError, List<Order>> getAll(int id) {
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
        Either<OrderError, Integer> result;
        em= jpautil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            OrdersEntity existingOrder = em.find(OrdersEntity.class, order.getId());
            existingOrder.getOrderItemsByOrderId().forEach(orderItemsEntity -> em.createQuery("delete from OrderItemsEntity where orderId = :orderId")
                    .setParameter("orderId", order.getId())
                    .executeUpdate());
            existingOrder.setOrderDate(Timestamp.valueOf(order.getDate()));
            existingOrder.setOrderItemsByOrderId(new ArrayList<>(order.getOrderItemList().stream().map(OrderItem::toOrderItemsEntity).toList()));
            existingOrder.getOrderItemsByOrderId().forEach(orderItemsEntity -> orderItemsEntity.setOrdersByOrderId(existingOrder));
            existingOrder.getOrderItemsByOrderId().forEach(em::persist);
            existingOrder.setRestaurantTablesByTableId(em.find(RestaurantTablesEntity.class, order.getTable_id()));
            em.merge(existingOrder);
            tx.commit();
            result = Either.right(0);
        } catch (PersistenceException e) {
            log.error(e.getMessage());
            tx.rollback();
            result = Either.left(new OrderError(Constants.ERROR_UPDATING_ORDER));
        } finally {
            em.close();
        }

        return result;
    }



    @Override
    public Either<OrderError, Order> save(Order order) {

        Either<OrderError, Order> result;
        em = jpautil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            OrdersEntity order2 = order.toOrdersEntity();
            order2.setOrderItemsByOrderId(order.getOrderItemList().stream().map(OrderItem::toOrderItemsEntity).toList());
            order2.getOrderItemsByOrderId().forEach(orderItemsEntity -> orderItemsEntity.setOrdersByOrderId(order2));
            em.persist(order2);
            tx.commit();
            result = Either.right(order);
        } catch (PersistenceException e) {
            log.error(e.getMessage());
            em.getTransaction().rollback();
            result = Either.left(new OrderError(Constants.ERROR_SAVING_ORDERS));
        } finally {
            em.close();
        }

        return result;
    }

}
