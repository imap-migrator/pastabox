package helper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 * Created by Oliver on 4/12/2017.
 */
public interface WizardInterface extends Initializable {
    @FXML
    void next(ActionEvent event);
    @FXML
    void back(ActionEvent event);
}
