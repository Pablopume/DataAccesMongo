package dao;

import io.vavr.control.Either;
import model.modelo.Credentials;

import java.util.List;

public interface LoginDAO {
    Either<String, List<Credentials>> getAll();

}
