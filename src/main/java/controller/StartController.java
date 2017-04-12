package controller;

import helper.WizardInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Oliver on 4/12/2017.
 */
public class StartController implements WizardInterface {

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void next(ActionEvent event) {
        TemplateController.instance.back.setVisible(true);
        TemplateController.instance.switchView("origin");
    }

    public void back(ActionEvent event) {
        TemplateController.instance.switchView("start");
    }
}
