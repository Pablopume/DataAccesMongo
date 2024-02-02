package ui.screens.customers.deletecustomer;

import common.Constants;
import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.modelo.Customer;
import model.modelo.Order;
import model.modelo.OrderItem;
import ui.screens.common.BaseScreenController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DeleteCustomersController extends BaseScreenController {
    @FXML
    public TableView<Customer> customersTable;
    @FXML
    public TableColumn<Customer, Integer> idCustomerColumn;
    @FXML
    public TableColumn<Customer, String> nameCustomerColumn;
    @FXML
    public TableColumn<Customer, String> surnameCustomerColumn;
    @FXML
    public TableColumn<Customer, String> emailColumn;
    @FXML
    public TableColumn<Customer, String> phoneColumn;
    @FXML
    public TableColumn<Customer, LocalDate> dateOfBirthdayColumn;
    @FXML
    public Button buttonCostumer;
    public TableView ordersCustomer;
    public TableColumn product;
    public TableColumn idProduct;
    public TableColumn price;
    public TableView<Order> orderssTable;
    public TableColumn<Order, Integer> idOrder;
    public TableColumn<Order, LocalDate> orderDate;
    public TableColumn<Order, Integer> customerId;
    public TableColumn<Order, Integer> tableId;

    @Inject
    private DeleteCustomerViewModel deleteCustomerViewModel;

    public void initialize() {
        idCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.ID2));
        nameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.FIRST_NAME2));
        surnameCustomerColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.LAST_NAME2));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.EMAIL2));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>(Constants.PHONE2));
        dateOfBirthdayColumn.setCellValueFactory(new PropertyValueFactory<>("date_of_birth"));
        idOrder.setCellValueFactory(new PropertyValueFactory<>(Constants.ID));
        orderDate.setCellValueFactory(new PropertyValueFactory<>(Constants.DATE));
        customerId.setCellValueFactory(new PropertyValueFactory<>(Constants.CUSTOMER_ID));
        tableId.setCellValueFactory(new PropertyValueFactory<>(Constants.TABLE_ID));
        deleteCustomerViewModel.getState().addListener((observableValue, oldValue, newValue) -> {

                    if (newValue.getError() != null) {
                        getPrincipalController().sacarAlertError(newValue.getError());
                    }
                    if (newValue.getListCustomers() != null) {
                        customersTable.getItems().clear();
                        customersTable.getItems().setAll(newValue.getListCustomers());
                    }

                }

        );
        customersTable.setOnMouseClicked(event -> {
            SelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
            Customer selectedCustomer = selectionModel.getSelectedItem();
            if (selectedCustomer != null) {
                orderssTable.getItems().clear();
                if (selectedCustomer.getOrders() != null && !selectedCustomer.getOrders().isEmpty()) {
                    orderssTable.getItems().setAll(selectedCustomer.getOrders());
                }else {
                    selectedCustomer.setOrders(new ArrayList<>());
                    orderssTable.getItems().setAll(selectedCustomer.getOrders());
                }
                orderssTable.getItems().addAll(selectedCustomer.getOrders());
            }
        });
        deleteCustomerViewModel.voidState();

    }

    @Override
    public void principalLoaded() {
        deleteCustomerViewModel.loadState();
    }

    public void deleteCustomer() {
        SelectionModel<Customer> selectionModel = customersTable.getSelectionModel();
        Customer selectedCustomer = selectionModel.getSelectedItem();

        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Delete Customer");
            alert.setContentText("Do you want to delete this customer?");
            Optional<ButtonType> res = alert.showAndWait();

            res.ifPresent(buttonType -> {
                if (buttonType == ButtonType.YES) {
                    if (selectedCustomer.getOrders() == null || selectedCustomer.getOrders().isEmpty()) {
                        deleteCustomerViewModel.getServices().delete(selectedCustomer, true);
                        Alert a = new Alert(Alert.AlertType.INFORMATION);
                        a.setTitle("Customer deleted");
                        a.setHeaderText(null);
                        a.setContentText("The customer has been deleted successfully");
                        a.show();
                        orderssTable.getItems().clear();
                        customersTable.getItems().remove(selectedCustomer);
                    } else {
                        // Only show this alert once and use the result
                        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION,
                                "The customer has orders, are you sure you want to delete it?",
                                ButtonType.YES, ButtonType.NO).showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.YES) {
                            deleteCustomerViewModel.getServices().delete(selectedCustomer, true);
                            Alert a = new Alert(Alert.AlertType.INFORMATION);
                            a.setTitle("Customer deleted");
                            a.setHeaderText(null);
                            a.setContentText("The customer has been deleted successfully");
                            a.show();
                            orderssTable.getItems().clear();
                            customersTable.getItems().remove(selectedCustomer);
                        }
                    }
                }
            });
        }
    }

}



