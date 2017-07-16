package controller;

import annotation.Controller;
import com.jfoenix.controls.JFXDialog;
import helper.DataHelper;
import helper.DialogHelper;
import helper.WizardInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Migration;
import model.Server;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * Created by Oliver on 4/12/2017.
 */
public class TemplateController implements Initializable {
    public static TemplateController instance;

    public static Server originServer;
    public static Server targetServer;

    public static ObservableList<Migration> migrations = FXCollections.observableArrayList();

    public static JFXDialog spinner;

    public VBox container;
    public Label title;
    public Button next;
    public Button back;

    public StackPane rootContainer;

    private WizardInterface childController;

    public void initialize(URL location, ResourceBundle resources) {
        switchView("start");
        instance = this;
        originServer = new Server();
        targetServer = new Server();
        back.setVisible(false);
        spinner = new DialogHelper().getSpinner(rootContainer);
    }

    public void switchView(String viewName) {
         new Thread(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/fxml/view/" + viewName + ".fxml"
                        )
                );

                Parent node = loader.load();
                childController = loader.<WizardInterface>getController();

                Platform.runLater(() -> {
                    container.getChildren().clear();
                    container.getChildren().add(node);

                    Class<WizardInterface> obj = (Class<WizardInterface>) childController.getClass();
                    if(obj.isAnnotationPresent(Controller.class)) {
                        Controller annotation = (Controller)obj.getAnnotation(Controller.class);
                        title.setText(annotation.title());
                    } else {
                        title.setText("PastaBox");
                    }
                });

            } catch (IOException e) {
                DataHelper.getLogger(this.getClass())
                        .log(Level.SEVERE, "Cannot switch view " + viewName, e);
            }
        }).start();
    }

    @FXML
    public void next(ActionEvent event) {
        childController.next(event);
    }

    @FXML
    public void back(ActionEvent event) {
        childController.back(event);
    }

    public WizardInterface getChildController() {
        return childController;
    }

}
