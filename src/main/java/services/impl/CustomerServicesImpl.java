package services.impl;

import dao.CustomerDAO;
import dao.OrdersDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import model.modelo.Credentials;
import model.modelo.Customer;
import model.modelo.Order;
import model.errors.CustomerError;
import model.errors.OrderError;
import services.CustomerServices;

import java.util.List;

@Singleton
public class CustomerServicesImpl implements CustomerServices {
    private final CustomerDAO customerDAO;


    @Inject
    public CustomerServicesImpl(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;

    }

    @Override
    public Either<CustomerError, List<Customer>> getAll() {
        return customerDAO.getAll();
    }

    public Either<CustomerError, List<Customer>> add(Customer customer) {
        return customerDAO.add(customer);
    }

    public Either<CustomerError, List<Customer>> update(Customer customer) {
        return customerDAO.update(customer);
    }

    @Override
    public Either<CustomerError, Integer> delete(Customer customer, boolean deleteOrders) {
        return customerDAO.delete(customer, deleteOrders);
    }



}

