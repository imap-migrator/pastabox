import com.jfoenix.controls.JFXDecorator;
import helper.DataHelper;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.UnsupportedEncodingException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("PastaBox");
        primaryStage.getIcons().add(new Image("/images/pastabox.png"));

        Parent root = FXMLLoader.load(getClass().getResource("fxml/template.fxml"));

        JFXDecorator decorator = new JFXDecorator(primaryStage, root, false, false, true);
        decorator.setCustomMaximize(false);

        Scene scene = new Scene(decorator, 900, 600);
        primaryStage.setScene(scene);

        final ObservableList<String> stylesheets = scene.getStylesheets();
        stylesheets.addAll(Main.class.getResource("/style/material.css").toExternalForm());

        primaryStage.show();
    }


    public static void main(String[] args) throws UnsupportedEncodingException {
        new DataHelper();
        launch(args);
    }
}
