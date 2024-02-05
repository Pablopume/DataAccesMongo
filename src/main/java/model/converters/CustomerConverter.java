package model.converters;

import jakarta.inject.Inject;
import model.modelo.Customer;
import model.modelo.Order;
import model.modelo.OrderItem;
import org.bson.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class CustomerConverter {
    private final OrderConverter orderConverter;


    @Inject
    public CustomerConverter(OrderConverter orderConverter, OrderItemConverter orderItemConverter) {
        this.orderConverter = orderConverter;

    }

    public Customer fromDocument(Document document) {
        Customer customer = new Customer();
        customer.setId(document.getObjectId("_id"));
        customer.setFirst_name(document.getString("first_name"));
        customer.setLast_name(document.getString("last_name"));
        customer.setEmail(document.getString("email"));
        customer.setPhone(document.getString("phone"));
        customer.setDate_of_birth(LocalDate.parse(document.getString("date_of_birth")));


        List<Document> ordersDocuments = (List<Document>) document.get("orders");
        if (ordersDocuments != null) {
            List<Order> orders = new ArrayList<>();
            for (Document orderDocument : ordersDocuments) {
                Order order = orderConverter.fromDocument(orderDocument);
                orders.add(order);
            }
            customer.setOrders(orders);
        }

        return customer;
    }

}
