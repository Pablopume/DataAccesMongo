package dao;

import io.vavr.control.Either;
import model.errors.OrderError;

public interface HibernateDao {
    Either<OrderError, Boolean> fromSqlToMongo();
}
