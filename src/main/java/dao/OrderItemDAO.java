package dao;

import io.vavr.control.Either;
import model.modelo.OrderItem;
import model.errors.OrderError;
import org.bson.types.ObjectId;

import java.util.List;

public interface OrderItemDAO {
    Either<OrderError, List<OrderItem>> getAll();


    Either<OrderError, List<OrderItem>> get(ObjectId id);

}
