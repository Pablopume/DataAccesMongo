package ui.screens.orders.addorder;

import lombok.Data;
import model.modelo.Order;

import java.util.List;
@Data
public class AddOrderState {
    private final List<Order> listOrders;
    private final String error;
}
