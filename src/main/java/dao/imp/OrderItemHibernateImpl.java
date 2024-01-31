package dao.imp;

import dao.JPAUtil;
import dao.OrderItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.modelo.OrderItem;
import model.errors.OrderError;
import model.modelHibernate.OrderItemsEntity;


import java.util.List;

public class OrderItemHibernateImpl implements OrderItemDAO {


private final JPAUtil jpautil;
private EntityManager em;
    @Inject
    public OrderItemHibernateImpl(JPAUtil jpautil) {
        this.jpautil = jpautil;
    }

    @Override
    public Either<OrderError, List<OrderItem>> getAll() {
        Either<OrderError, List<OrderItem>> result;


        try {
            em = jpautil.getEntityManager();
            List<OrderItemsEntity> orderItemEntities = em.createQuery("from OrderItemsEntity", OrderItemsEntity.class).getResultList();

            List<OrderItem> orderItemList = orderItemEntities.stream()
                    .map(OrderItemsEntity::toOrderItem)
                    .toList();

            if (orderItemList.isEmpty()) {
                result = Either.left(new OrderError("Error while retrieving orders"));
            } else {
                result = Either.right(orderItemList);
            }
        } finally {
            if (em != null) em.close();
        }

        return result;
    }

    @Override
    public Either<OrderError, List<OrderItem>> get(int id) {
        Either<OrderError, List<OrderItem>> result;


        try {
            em = jpautil.getEntityManager();
            TypedQuery<OrderItemsEntity> query = em.createQuery(
                    "SELECT oi FROM OrderItemsEntity oi WHERE oi.orderId = :orderId", OrderItemsEntity.class);
            query.setParameter("orderId", id);

            List<OrderItemsEntity> orderItemEntities = query.getResultList();

            if (orderItemEntities.isEmpty()) {
                result = Either.left(new OrderError("No OrderItems found for the given OrderId"));
            } else {
                List<OrderItem> orderItemList = orderItemEntities.stream()
                        .map(OrderItemsEntity::toOrderItem)
                        .toList();
                result = Either.right(orderItemList);
            }
        } finally {
            if (em != null) em.close();
        }

        return result;
    }













}
