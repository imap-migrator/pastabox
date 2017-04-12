package controller;

import helper.WizardInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import model.Server;

import javax.mail.MessagingException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;

/**
 * Created by Oliver on 4/12/2017.
 */
public class OriginController implements WizardInterface {
    public TextField server;
    public TextField port;
    public CheckBox secure;
    public TextField username;
    public TextField password;

    private boolean originSet = false;

    public void initialize(URL location, ResourceBundle resources) {
        TemplateController.instance.title.setText("Origin server");
    }

    @FXML
    public void next(ActionEvent event) {
        if(!originSet) {
            configureServer(TemplateController.originServer);
        } else {
            configureServer(TemplateController.targetServer);
            TemplateController.instance.switchView("mapping");
        }
    }

    public void back(ActionEvent event) {
        if(!originSet) {
            reset();
            TemplateController.instance.back.setVisible(false);
            TemplateController.instance.switchView("start");
        } else {
            originSet = false;
            reset();
            TemplateController.instance.title.setText("Origin server");
        }
    }

    private void configureServer(Server credentials) {
        credentials.setServer(server.getText());
        credentials.setPort(port.getText());
        credentials.setSecure(secure.isSelected());
        credentials.setUsername(username.getText());
        credentials.setPassword(password.getText());

        try {
            credentials.init();
            TemplateController.instance.title.setText("Target server");
            originSet = true;
            reset();
        } catch (MessagingException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Authentication failed");
            alert.setHeaderText("Your credentials are not correct.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        } catch (GeneralSecurityException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Security issue");
            alert.setHeaderText("The server does not understand secure connections.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void reset() {
        server.setText("");
        port.setText("");
        secure.setSelected(false);
        username.setText("");
        password.setText("");
    }
}
