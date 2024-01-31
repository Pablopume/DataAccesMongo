package model.modelHibernate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.modelo.Order;
import model.modelo.OrderItem;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Data
@Entity
@Table(name = "orders", schema = "pabloserrano_restaurant")
public class OrdersEntity {
    public OrdersEntity(Timestamp orderDate, int customerId, int tableId, RestaurantTablesEntity restaurantTablesByTableId, Collection<OrderItemsEntity> orderItemsByOrderId) {
        this.orderDate = orderDate;
        this.customerId = customerId;
        this.tableId = tableId;
        this.restaurantTablesByTableId = restaurantTablesByTableId;
        this.orderItemsByOrderId = orderItemsByOrderId;
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "order_id", nullable = false)
    private int orderId;
    @Basic
    @Column(name = "order_date", nullable = false)
    private Timestamp orderDate;
    @Basic
    @Column(name = "customer_id", nullable = false)
    private int customerId;
    @Basic
    @Column(name = "table_id", nullable = false, insertable = false, updatable = false)
    private int tableId;
    @OneToMany(mappedBy = "ordersByOrderId", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Collection<OrderItemsEntity> orderItemsByOrderId;
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private CustomersEntity customersByCustomerId;
    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "table_number_id", nullable = false)
    private RestaurantTablesEntity restaurantTablesByTableId;

    public Order toOrder() {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemsEntity orderItemsEntity : orderItemsByOrderId) {
            orderItems.add(orderItemsEntity.toOrderItem());
        }
        return new Order(orderId, orderDate.toLocalDateTime(), customersByCustomerId.getId(), restaurantTablesByTableId.getTableNumberId(), orderItems);
    }


}
