package controller;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.mail.Folder;
import javax.mail.MessagingException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Oliver on 4/12/2017.
 */
public class MappingController implements Initializable {
    public VBox mappingContainer;

    public void initialize(URL location, ResourceBundle resources) {
        TemplateController.instance.title.setText("Mapping");

        init();
    }

    private void init() {
        try {
            Folder[] originFolders = TemplateController.originServer.getStore().getDefaultFolder().list("*");
            Folder[] targetFolders = TemplateController.targetServer.getStore().getDefaultFolder().list("*");

            for(Folder folder:originFolders) {
                HBox hbox = new HBox();
                Label label = new Label(folder.getFullName() + " -> ");
                ComboBox selection = new ComboBox(FXCollections.observableArrayList(targetFolders));
                hbox.getChildren().addAll(label, selection);
                mappingContainer.getChildren().add(hbox);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
