package controller;

import annotation.Controller;
import helper.WizardInterface;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Oliver on 7/13/2017.
 */
@Controller(title="Finish")
public class FinishController implements WizardInterface {
    @Override
    public void next(ActionEvent event) {

    }

    @Override
    public void back(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            TemplateController.instance.back.setVisible(false);
            TemplateController.instance.next.setVisible(true);
            TemplateController.instance.next.setText("Close");
            TemplateController.instance.next.setOnMouseClicked(event -> Platform.exit());
        });

    }
}
