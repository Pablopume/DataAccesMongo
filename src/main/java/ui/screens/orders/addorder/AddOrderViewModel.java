package ui.screens.orders.addorder;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import model.modelo.Order;
import services.MenuItemService;
import services.OrderServices;

import java.util.ArrayList;
import java.util.List;
@Data
public class AddOrderViewModel {


    private final OrderServices services;
    private final MenuItemService menuItemService;

    private final ObjectProperty<AddOrderState> state;

    @Inject
    public AddOrderViewModel(OrderServices services, MenuItemService menuItemService) {
        this.menuItemService = menuItemService;

        this.state = new SimpleObjectProperty<>(new AddOrderState(new ArrayList<>(), null));
        this.services = services;

    }

    public void voidState() {
        state.set(new AddOrderState(null, null));
    }

    public ReadOnlyObjectProperty<AddOrderState> getState() {
        return state;
    }

    public void loadState() {
        List<Order> listOrd = new ArrayList<>();
            state.set(new AddOrderState(listOrd, null));
        }
    }

