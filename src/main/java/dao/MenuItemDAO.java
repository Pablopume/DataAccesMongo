package dao;

import io.vavr.control.Either;
import model.modelo.MenuItem;
import model.errors.OrderError;

import java.util.List;

public interface MenuItemDAO {

    Either<OrderError, List<MenuItem>> getAll();
}
