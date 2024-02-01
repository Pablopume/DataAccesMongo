package services.impl;

import dao.MenuItemDAO;
import dao.OrderItemDAO;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.modelo.MenuItem;
import model.modelo.OrderItem;
import model.errors.OrderError;
import services.OrderItemService;

import java.util.Collections;
import java.util.List;

public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemDAO dao;
    private final MenuItemDAO menuItemDAO;
    @Inject
    public OrderItemServiceImpl(OrderItemDAO dao, MenuItemDAO menuItemDAO) {
        this.dao = dao;
        this.menuItemDAO = menuItemDAO;
    }

    @Override
    public Either<OrderError, List<OrderItem>> getAll() {
        return dao.getAll();
    }


    @Override
    public double getTotalPrice(int id) {
        List<OrderItem> orderItems = getOrdersById(id);
        double totalPrice = 0;
        double price = 0;
        for (OrderItem orderItem : orderItems) {
            List<MenuItem> menuItems = menuItemDAO.getAll().get();
            for (MenuItem menuItem : menuItems) {
                if (orderItem.getMenuItemId() == menuItem.getId()) {
                    price = menuItem.getPrice();
                    break;
                }
            }
            totalPrice += price * orderItem.getQuantity();

        }

        return Math.round(totalPrice * 100.0) / 100.0;
    }

    public List<OrderItem> getOrdersById(int id) {
        return dao.get(id).getOrElse(Collections.emptyList());

    }


    public int getAutoId() {
        Either<OrderError, List<OrderItem>> result = dao.getAll();
        if (result.isLeft()) {
            return 0;
        } else {
            List<OrderItem> allOrders = result.get();
            return allOrders.size();
        }
    }
}
