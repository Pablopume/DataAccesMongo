package model.converters;

import model.modelo.Order;
import model.modelo.OrderItem;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderConverter {
    public  Order fromDocument(Document document) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Order order = new Order();
        order.setId(document.getObjectId("_id"));
        order.setDate(LocalDateTime.parse(document.getString("date"), formatter));
        order.setTable_id(document.getInteger("table_id"));
        return order;
    }
}
