package controller;

import annotation.Controller;
import helper.DataHelper;
import helper.DialogHelper;
import helper.WizardInterface;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Oliver on 4/12/2017.
 */
@Controller
public class StartController implements WizardInterface {

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void next(ActionEvent event) {
        if(DataHelper.isLicenseAccepted()) {
            TemplateController.instance.back.setVisible(true);
            TemplateController.instance.switchView("server");
        } else {
            new DialogHelper().show(TemplateController.instance.rootContainer, "License", "You must agree to the license in order to use this application");
        }
    }

    public void back(ActionEvent event) {
        TemplateController.instance.switchView("start");
    }

    public void showLicenseDialog(ActionEvent actionEvent) {
        new DialogHelper().showLicense(TemplateController.instance.rootContainer);
    }

    public void showAboutDialog(ActionEvent actionEvent) {
        new DialogHelper().showAbout(TemplateController.instance.rootContainer);
    }

    public void showSettingsDialog(ActionEvent actionEvent) {
        new DialogHelper().showSettings(TemplateController.instance.rootContainer);
    }
}
