package ui.screens.orders.addorder;

import common.Constants;
import jakarta.inject.Inject;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.modelo.Credentials;
import model.modelo.Order;
import model.modelo.OrderItem;

import org.bson.types.ObjectId;
import ui.screens.common.BaseScreenController;

import java.time.LocalDateTime;

public class AddOrderController extends BaseScreenController {
    @FXML
    public TableView<Order> customersTable;
    @FXML
    public TableColumn<Order, Integer> idOrder;
    @FXML
    public TableColumn<Order, LocalDateTime> orderDate;

    @FXML
    public TableColumn<Order, Integer> customerId;
    @FXML
    public TableColumn<Order, Integer> tableId;
    Credentials actualuser;
    @FXML
    public ComboBox<String> idCustomer;
    @FXML
    public ComboBox<String> table_id;
    public TableColumn<OrderItem, String> menuItem;
    public TableView<OrderItem> ordersXMLTable;
    public TableColumn<OrderItem, Integer> quantity;
    public ComboBox<String> menuItems;
    public TextField quantityItems;


    @Inject
    AddOrderViewModel addOrderViewModel;

    public void initialize() {
        menuItem.setCellValueFactory(cellData ->new SimpleStringProperty(addOrderViewModel.getMenuItemService().get(cellData.getValue().getMenuItemId()).get().getName()));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        addOrderViewModel.voidState();

    }


    public void addOrder() {

        ObjectId selectedCustomerId = actualuser.get_id();
        int selectedTableId = Integer.parseInt(table_id.getValue());
         addOrderViewModel.getServices().createOrder(new Order(LocalDateTime.now(), selectedTableId, ordersXMLTable.getItems()),selectedCustomerId).get();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(Constants.ORDER_ADDED);
        alert.setHeaderText(null);
        alert.setContentText(Constants.THE_ORDER_HAS_BEEN_ADDED);
        alert.showAndWait();

    }

  public void addItem() {
      ObservableList<OrderItem> orderItem = ordersXMLTable.getItems();
       orderItem.add(new OrderItem( Integer.parseInt(quantityItems.getText()), addOrderViewModel.getMenuItemService().getByName(menuItems.getValue()).getId()));
        ordersXMLTable.setItems(orderItem);
    }

    public void removeOrder() {

        ObservableList<OrderItem> orderItemXMLS = ordersXMLTable.getItems();
        OrderItem selectedOrder = ordersXMLTable.getSelectionModel().getSelectedItem();
        orderItemXMLS.remove(selectedOrder);
        ordersXMLTable.setItems(orderItemXMLS);
    }

    @Override
    public void principalLoaded() {
        addOrderViewModel.loadState();
        actualuser = getPrincipalController().getActualUser();
    }
}
