package model.modelo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import model.modelHibernate.OrdersEntity;
import model.modelHibernate.RestaurantTablesEntity;
import org.bson.types.ObjectId;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Order {
    private ObjectId id;
    private LocalDateTime date;
    private ObjectId customer_id;
    private int table_id;
    private List<OrderItem> orderItemList;


    public Order(LocalDateTime date, int table_id, List<OrderItem> orderItemList) {
        this.date = date;
        this.table_id = table_id;
        this.orderItemList = orderItemList;
    }

    public Order(LocalDateTime date, ObjectId customer_id, int table_id, List<OrderItem> orderItemList) {
        this.date = date;
        this.customer_id = customer_id;
        this.table_id = table_id;
        this.orderItemList = orderItemList;
    }

    public Order(LocalDateTime date, ObjectId customer_id, int table_id) {
        this.date = date;
        this.customer_id = customer_id;
        this.table_id = table_id;
    }

    public Order(ObjectId id, LocalDateTime date, ObjectId customer_id, int table_id) {
        this.id = id;
        this.date = date;
        this.customer_id = customer_id;
        this.table_id = table_id;
    }



    public String toStringTextFile() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return id + ";" + date.format(formatter) + ";" + customer_id + ";" + table_id;
    }
}
