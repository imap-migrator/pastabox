package helper;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Oliver on 7/4/2017.
 */
public class DialogHelper implements Initializable {
    public JFXDialog dialog;
    public JFXDialog spinner;
    public Label dialogContent;
    public Label dialogTitle;
    public JFXButton acceptButton;
    public JFXCheckBox licenseAccepted;

    public void show(StackPane context, String title, String content) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/fxml/view/dialog.fxml"
                )
        );
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Initialize components
        dialogTitle = (Label)loader.getNamespace().get("dialogTitle");
        dialogContent = (Label)loader.getNamespace().get("dialogContent");
        dialog = (JFXDialog) loader.getNamespace().get("dialog");
        acceptButton = (JFXButton) loader.getNamespace().get("acceptButton");

        dialogTitle.setText(title);
        dialogContent.setText(content);

        // Register listeners
        acceptButton.setOnMouseClicked(event -> dialog.close());

        Platform.runLater(() -> dialog.show(context));
    }

    public FXMLLoader getFXMLLoader(String resourcePath) {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(resourcePath)
        );

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return loader;
    }

    public JFXDialog getSpinner(StackPane context) {
        if(spinner == null) {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/fxml/dialog/spinner.fxml"
                    )
            );
            try {
                loader.load();
                spinner = (JFXDialog) loader.getNamespace().get("dialog");
                spinner.setDialogContainer(context);
                spinner.setOverlayClose(false);
                return spinner;
            } catch (IOException e) {
                e.printStackTrace();
                return new JFXDialog();
            }
        } else {
            return dialog;
        }
    }

    public void initialize(URL location, ResourceBundle resources) {

    }

    public void showLicense(StackPane context) {
        FXMLLoader loader = getFXMLLoader("/fxml/dialog/license.fxml");

        // Initialize components
        licenseAccepted = (JFXCheckBox) loader.getNamespace().get("licenseAccepted");
        acceptButton = (JFXButton) loader.getNamespace().get("acceptButton");
        dialog = (JFXDialog) loader.getNamespace().get("dialog");

        licenseAccepted.setSelected(DataHelper.isLicenseAccepted());

        // Register listeners
        acceptButton.setOnMouseClicked(event -> {
            dialog.close();
            DataHelper.setLicenseAccepted(licenseAccepted.isSelected());
        });

        Platform.runLater(() -> dialog.show(context));
    }

    public void showAbout(StackPane context) {
        FXMLLoader loader = getFXMLLoader("/fxml/dialog/about.fxml");

        // Initialize components
        acceptButton = (JFXButton) loader.getNamespace().get("acceptButton");
        dialog = (JFXDialog) loader.getNamespace().get("dialog");

        // Register listeners
        acceptButton.setOnMouseClicked(event -> dialog.close());

        Platform.runLater(() -> dialog.show(context));
    }

    public void showSettings(StackPane context) {
        FXMLLoader loader = getFXMLLoader("/fxml/dialog/settings.fxml");

        // Initialize components
        acceptButton = (JFXButton) loader.getNamespace().get("acceptButton");
        dialog = (JFXDialog) loader.getNamespace().get("dialog");

        // Register listeners
        acceptButton.setOnMouseClicked(event -> dialog.close());

        Platform.runLater(() -> dialog.show(context));
    }
}
