package model.modelHibernate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.modelo.OrderItem;
@Data
@NoArgsConstructor
@Entity
@Table(name = "order_items", schema = "pabloserrano_restaurant")
public class OrderItemsEntity {

    public OrderItemsEntity( int menuItemId, int quantity) {

        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    @Basic
    @Column(name = "order_id", nullable = false, insertable = false, updatable = false)
    private int orderId;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_item_id", nullable = false)
    private int orderItemId;

    @Basic
    @Column(name = "menu_item_id", nullable = false)
    private int menuItemId;
    @Basic
    @Column(name = "quantity", nullable = false)
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    private OrdersEntity ordersByOrderId;
    @ManyToOne
    @JoinColumn(name = "menu_item_id", referencedColumnName = "menu_item_id", nullable = false, insertable = false, updatable = false)
    private MenuItemsEntity menuItemsByMenuItemId;

    public OrderItem toOrderItem() {
        return new OrderItem(orderItemId,orderId, menuItemsByMenuItemId.toMenuItem().getId(), quantity);
    }

}
