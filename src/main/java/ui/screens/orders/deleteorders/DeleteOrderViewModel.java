package ui.screens.orders.deleteorders;

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
public class DeleteOrderViewModel {
    private final OrderServices services;
    private final MenuItemService menuItemService;

    private final ObjectProperty<DeleteOrderState> state;



    @Inject
    public DeleteOrderViewModel(OrderServices services, MenuItemService menuItemService) {
        this.menuItemService = menuItemService;


        this.state = new SimpleObjectProperty<>(new DeleteOrderState(new ArrayList<>(), null));
        this.services = services;

    }
    public void voidState() {
        state.set(new DeleteOrderState(null, null));
    }
    public ReadOnlyObjectProperty<DeleteOrderState> getState(){return state;}

    public void loadState() {
        List<Order> listOrd = new ArrayList<>();
            if (!services.getAll().isEmpty()) {
                listOrd = services.getAll().get();
            }
            state.set(new DeleteOrderState(listOrd, null));




        }

}
