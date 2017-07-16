package controller;

import annotation.Controller;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import helper.DataHelper;
import helper.WizardInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import model.Login;
import javax.mail.Folder;
import javax.mail.MessagingException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ResourceBundle;
import java.util.logging.Level;

/**
 * Created by Oliver on 4/12/2017.
 */
@Controller(title="Mapping")
public class MappingController implements WizardInterface {
    public JFXComboBox<Login> account_origin;
    public JFXComboBox<Login> account_target;
    public JFXListView<Folder> folder_origin;
    public JFXListView<Folder> folder_target;
    //  public JFXListView<String> account_to;

    public void initialize(URL location, ResourceBundle resources) {
        // Those lists will always have the same content
        // Populate them at the beginning
        account_origin.setItems(TemplateController.originServer.getLogins());
        account_target.setItems(TemplateController.targetServer.getLogins());

        // Account change listeners
        account_origin.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                selectOriginAccount(newValue);
            }
        } );
        account_target.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectTargetAccount(newValue);
            }
        } );
        folder_origin.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectOriginFolder(newValue);
            }
        } );
        folder_target.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                map();
            }
        } );

        account_origin.getSelectionModel().selectFirst();
        selectOriginAccount(account_origin.getValue());
        account_target.getSelectionModel().selectFirst();
        selectTargetAccount(account_target.getValue());
    }

    private void selectOriginAccount(Login login) {
        folder_origin.setItems(getAccountFolders(login));
        account_target.getSelectionModel().select(login.getMigration().getToLogin());
        folder_origin.setItems(getAccountFolders(login));
    }

    private void selectTargetAccount(Login login) {
        folder_target.setItems(getAccountFolders(login));
        folder_target.setItems(getAccountFolders(login));
    }

    private void selectOriginFolder(Folder folder) {
        folder_target.getSelectionModel().select(account_origin.getSelectionModel().getSelectedItem().getMigration().getFolderMapping().get(folder));
    }

    private ObservableList<Folder> getAccountFolders(Login login) {
        Folder[] folders = new Folder[0];
        if(login != null) {
            try {
                folders = login.getStore().getDefaultFolder().list();
            } catch (MessagingException | GeneralSecurityException e) {
                DataHelper.getLogger(this.getClass())
                        .log(Level.SEVERE, "Could not get account folders", e);
            }
        }
        return FXCollections.observableArrayList(folders);
    }

    private void map() {
        Login originAccount = account_origin.getSelectionModel().getSelectedItem();
        originAccount.getMigration().getFolderMapping().put(folder_origin.getSelectionModel().getSelectedItem(), folder_target.getSelectionModel().getSelectedItem());;
        originAccount.getMigration().setToLogin(account_target.getSelectionModel().getSelectedItem());
    }

    public void clear(ActionEvent actionEvent) {
        Login originAccount = account_origin.getSelectionModel().getSelectedItem();
        originAccount.getMigration().getFolderMapping().remove(
                folder_origin.getSelectionModel().getSelectedItem()
        );
        folder_target.getSelectionModel().clearSelection();
    }

    public void next(ActionEvent event) {
        TemplateController.instance.switchView("migration");
    }

    public void back(ActionEvent event) {
        TemplateController.instance.switchView("server");
    }
}
