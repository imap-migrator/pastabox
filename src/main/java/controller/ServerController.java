package controller;

import annotation.Controller;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import helper.DataHelper;
import helper.DialogHelper;
import helper.WizardInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Login;
import model.Server;

import javax.mail.MessagingException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * Created by Oliver on 4/12/2017.
 */
@Controller(title="Server")
public class ServerController implements WizardInterface {
    public JFXTextField server;
    public JFXTextField port;
    public JFXCheckBox secure;
    public JFXCheckBox ignoreCertErrors;
    public JFXTextField username;
    public JFXPasswordField password;
    public JFXListView<Login> loginTableView;
    public Label title;

    private boolean originSet = false;

    public void initialize(URL location, ResourceBundle resources) {

        // Make sure all fields are filled.
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Required");
        server.getValidators().add(validator);
        port.getValidators().add(validator);
        username.getValidators().add(validator);
        password.getValidators().add(validator);

        init();
    }

    private void init() {
        reset(false);

        Server savedServer = originSet ? TemplateController.targetServer : TemplateController.originServer;

        title.setText(!originSet ? "What is your current setup?" : "And the new setup?");

        server.setText(savedServer.getServer());
        port.setText(savedServer.getPort());
        secure.setSelected(savedServer.isSecure());
        loginTableView.setItems(savedServer.getLogins());
        loginTableView.refresh();
    }

    @FXML
    public void next(ActionEvent event) {
        if(loginTableView.getItems().size() == 0) {
            new DialogHelper().show(TemplateController.instance.rootContainer, "Validation error", "You must add at least one account to start with.");

            username.getStyleClass().add("error");
            password.getStyleClass().add("error");
            return;
        }
        if(!originSet) {
            originSet = true;
            reset(true);

            init();
        } else {
            TemplateController.instance.switchView("mapping");
        }
    }

    @FXML
    public void back(ActionEvent event) {
        if(!originSet) {
            reset(true);
            TemplateController.instance.back.setVisible(false);
            TemplateController.instance.switchView("start");
        } else {
            originSet = false;
            init();
        }
    }

    @FXML
    public void addLogin(ActionEvent event) {

        if (
                !(
                        server.validate()
                        & port.validate()
                        & username.validate()
                        & password.validate()
                )

        ) {

            new DialogHelper().show(TemplateController.instance.rootContainer, "Validation error", "All fields are required.");
            return;

        }

        Server serverObject = originSet ? TemplateController.targetServer : TemplateController.originServer;
        serverObject.setServer(server.getText());
        serverObject.setPort(port.getText());
        serverObject.setSecure(secure.isSelected());
        serverObject.setIgnoreCertErrors(ignoreCertErrors.isSelected());

        Login login = new Login(serverObject);
        login.setUsername(username.getText());
        login.setPassword(password.getText());

        TemplateController.spinner.show();

        new Thread(() -> {
            try {
                if(login.getStore() != null) {
                    Platform.runLater(() -> {
                        server.setDisable(true);
                        port.setDisable(true);
                        secure.setDisable(true);
                        ignoreCertErrors.setDisable(true);

                        serverObject.getLogins().add(login);
                        reset(false);
                    });

                } else {
                    new DialogHelper().show(TemplateController.instance.rootContainer, "Server error", "Could not get server instance.");
                }
            } catch (MessagingException e) {
                new DialogHelper().show(TemplateController.instance.rootContainer, "Validation error", e.getMessage());
                DataHelper.getLogger(this.getClass())
                        .log(Level.SEVERE, serverObject.getConnectionString(), e);
            } catch (GeneralSecurityException e) {
                new DialogHelper().show(TemplateController.instance.rootContainer, "TLS error", "The server does not understand secure connections. Uncheck the box and try again.");
                DataHelper.getLogger(this.getClass())
                        .log(Level.SEVERE, serverObject.getConnectionString(), e);
            } finally {
                TemplateController.spinner.close();
            }
        }).start();
    }

    private void reset(boolean includingServerDetails) {
        if(includingServerDetails) {
            server.setText("");
            port.setText("");
            secure.setSelected(false);
            ignoreCertErrors.setSelected(false);
            server.setDisable(false);
            port.setDisable(false);
            secure.setDisable(false);
            ignoreCertErrors.setDisable(false);
        }
        username.setText("");
        password.setText("");
        loginTableView.refresh();
    }
}
