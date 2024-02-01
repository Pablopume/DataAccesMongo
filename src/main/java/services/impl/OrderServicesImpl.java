package services.impl;

import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import model.modelo.Order;
import model.errors.OrderError;
import org.bson.types.ObjectId;
import services.OrderServices;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServicesImpl implements OrderServices {
    private final OrdersDAO ordersDAO;

    @Inject
    public OrderServicesImpl(@Named("OrderDB") OrdersDAO ordersDAO) {
        this.ordersDAO = ordersDAO;
    }

    public Either<OrderError, List<Order>> getAll() {
        return ordersDAO.getAll();
    }

    @Override
    public Either<OrderError, Integer> delete(Order order) {
        return ordersDAO.delete(order);
    }

    public Either<OrderError, Order> createOrder(Order order) {
        return ordersDAO.add(order);
    }

    @Override
    public Either<OrderError, List<Order>> filteredListDate(LocalDate date) {
        Either<OrderError, List<Order>> result = ordersDAO.getAll();
        if (result.isLeft()) {
            return Either.left(result.getLeft());
        } else {
            List<Order> allOrders = result.get();
            List<Order> ordersByDate = allOrders.stream()
                    .filter(order -> order.getDate().toLocalDate().equals(date))
                    .collect(Collectors.toList());
            return Either.right(ordersByDate);
        }
    }

    @Override
    public List<Order> getOrdersByCustomerId(ObjectId id) {
        Either<OrderError, List<Order>> result = ordersDAO.getAll();

        if (result.isLeft()) {
            return Collections.emptyList();
        } else {
            List<Order> allOrders = result.get();
            return allOrders.stream()
                    .filter(order -> order.getCustomer_id() == id)
                    .toList();
        }
    }




    @Override
    public Either<OrderError, Integer> update(Order c) {
        return ordersDAO.update(c);
    }

}
