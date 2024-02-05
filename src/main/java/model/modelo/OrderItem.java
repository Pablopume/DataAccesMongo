package model.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.modelHibernate.OrderItemsEntity;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
public class OrderItem {

    private int quantity;
    private int menuItemId;

    public OrderItem(int quantity) {

        this.quantity = quantity;
    }

    public OrderItem(int quantity, int menuItemId) {
        this.quantity = quantity;
        this.menuItemId = menuItemId;
    }
}
