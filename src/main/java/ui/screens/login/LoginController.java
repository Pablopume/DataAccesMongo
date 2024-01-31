package ui.screens.login;

import jakarta.inject.Inject;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.modelo.Credentials;
import services.LoginServices;
import services.impl.HibernateServicesImpl;
import ui.screens.common.BaseScreenController;

public class LoginController extends BaseScreenController {
    public PasswordField txtPassword;
    public TextField txtUserName;
    private final LoginServices services;
    public AnchorPane pantallaLogin;
    private final HibernateServicesImpl hibernateServices;

    @Inject
    public LoginController(LoginServices services, HibernateServicesImpl hibernateServices) {

        this.services = services;
        this.hibernateServices = hibernateServices;
    }

    public void doLogin(ActionEvent actionEvent) {
        Credentials credentials = services.getByNameAndPassword(txtUserName.getText(), txtPassword.getText());

        if (credentials != null) {
            getPrincipalController().onLoginDone(credentials);
            pantallaLogin.setVisible(false);

        }
    }

    public void loadData() {
        hibernateServices.fromSqlToMongo();
    }
}
