package dao;

import io.vavr.control.Either;
import model.modelo.Customer;
import model.modelo.Order;
import model.errors.CustomerError;
import model.errors.OrderError;

import java.util.List;

public interface CustomerDAO {

    Either<CustomerError, List<Customer>> getAll();
    Either<CustomerError, List<Customer>> add(Customer customer);
    Either<CustomerError, List<Customer>> update(Customer customer);
    Either<CustomerError, Integer> delete(Customer customer, boolean delete);


}

