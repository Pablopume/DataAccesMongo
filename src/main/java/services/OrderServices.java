package services;

import io.vavr.control.Either;
import model.modelo.Order;
import model.errors.OrderError;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.List;

public interface OrderServices {
    Either<OrderError, List<Order>> getAll();
    Either<OrderError, Integer> delete(Order order);

    List<Order> getOrdersByCustomerId(ObjectId id);
    Either<OrderError, Integer> update(Order c);

    Either<OrderError, Order> createOrder(Order order, ObjectId id);
    Either<OrderError, List<Order>> filteredListDate(LocalDate date);




}
