package model.converters;

import jakarta.inject.Inject;
import model.modelo.Order;
import model.modelo.OrderItem;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderConverter {
    private final OrderItemConverter orderItemConverter;

    @Inject
    public OrderConverter(OrderItemConverter orderItemConverter) {
        this.orderItemConverter = orderItemConverter;
    }

    public Order fromDocument(Document document) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Order order = new Order();
        order.setDate(LocalDateTime.parse(document.getString("date"), formatter));
        order.setTable_id(document.getInteger("table_id"));
        List<Document> orderItemListDocument = (List<Document>) document.get("orderItemList");
        List<OrderItem> orderItemList = new ArrayList<>();
        orderItemListDocument.forEach(orderItemDocument -> {
            OrderItem orderItem = orderItemConverter.fromDocument(orderItemDocument);
            orderItemList.add(orderItem);
        });
        order.setOrderItemList(orderItemList);
        return order;
    }
}
