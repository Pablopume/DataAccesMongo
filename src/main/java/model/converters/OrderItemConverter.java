package model.converters;

import model.modelo.OrderItem;
import org.bson.Document;

public class OrderItemConverter {
    public OrderItem fromDocument(Document document) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(document.getInteger("quantity"));
        orderItem.setMenuItemId(document.getInteger("menuItemId"));
        return orderItem;
    }
}
