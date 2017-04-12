package controller;

import helper.WizardInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.Server;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Oliver on 4/12/2017.
 */
public class TemplateController implements Initializable {
    public static TemplateController instance;

    public static Server originServer;
    public static Server targetServer;

    public ScrollPane container;
    public Label title;
    public Button next;
    public Button back;

    private WizardInterface childController;

    public void initialize(URL location, ResourceBundle resources) {
        switchView("start");
        instance = this;
        originServer = new Server();
        targetServer = new Server();
        back.setVisible(false);
    }

    public void switchView(String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/fxml/view/" + viewName + ".fxml"
            )
            );

            container.setContent((Node)loader.load());

            childController = loader.<WizardInterface>getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void next(ActionEvent event) {
        childController.next(event);
    }

    @FXML
    public void back(ActionEvent event) {
        childController.back(event);
    }
}
