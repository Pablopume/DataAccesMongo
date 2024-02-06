package dao;

import io.vavr.control.Either;
import model.modelo.Order;
import model.errors.OrderError;
import org.bson.types.ObjectId;

import java.util.List;

public interface OrdersDAO {
    Either<OrderError, List<Order>> get();
    Either<OrderError, List<Order>> get(ObjectId id);
    Either<OrderError, Integer> delete(Order order);
    Either<OrderError, Order> add(Order order,ObjectId id);
    Either<OrderError, Integer> update(Order c);

}
