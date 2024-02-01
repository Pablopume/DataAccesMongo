package services;

import io.vavr.control.Either;
import model.modelo.OrderItem;
import model.errors.OrderError;
import org.bson.types.ObjectId;

import java.util.List;

public interface OrderItemService {
    Either<OrderError, List<OrderItem>> getAll();
    double getTotalPrice(ObjectId id);

    List<OrderItem> getOrdersById(ObjectId id);
    int getAutoId();
}
