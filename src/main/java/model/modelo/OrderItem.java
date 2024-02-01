package model.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.modelHibernate.OrderItemsEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private int id;
    private int idOrder;
    private int quantity;
    private int menuItemId;

    public OrderItem(int quantity) {

        this.quantity = quantity;
    }

    public OrderItemsEntity toOrderItemsEntity() {
        return new OrderItemsEntity(menuItemId, quantity);
    }
}
