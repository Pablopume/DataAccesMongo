package services;

import io.vavr.control.Either;
import model.modelo.OrderItem;
import model.errors.OrderError;

import java.util.List;

public interface OrderItemService {
    Either<OrderError, List<OrderItem>> getAll();
    double getTotalPrice(int id);

    List<OrderItem> getOrdersById(int id);
    int getAutoId();
}
