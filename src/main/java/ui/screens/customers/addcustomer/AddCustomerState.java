package ui.screens.customers.addcustomer;

import lombok.Data;
import model.modelo.Customer;

import java.util.List;

@Data
public class AddCustomerState {
    private final List<Customer> listCustomers;
    private final String error;

}
