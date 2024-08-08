package edu.northeastern.course.TheCodeCommandos.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import edu.northeastern.course.TheCodeCommandos.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController implements Initializable {
	
    public TextField username_field;
    public TextField password_field;
    public Button login_btn;
    public Label username_label;
    public Label password_label;
    public Label error_label;

    // Everytime a window is loaded, the initialize() method is called
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	login_btn.setOnAction(e -> onLogin());
    }
    
    // Check the username and password, turn to dashboard window if credentials is correct
    public void onLogin() {
    	Stage stage = (Stage)error_label.getScene().getWindow();
        Model.getInstance().evaluateClientCredentials(username_field.getText(), password_field.getText());
        if (Model.getInstance().getMemberLoginSuccessFlag()) {
            Model.getInstance().getViewFactory().closeStage(stage);
            Model.getInstance().getViewFactory().showDashboardWindow();
        } else {
            username_field.setText("");
            password_field.setText("");
            error_label.setText("No such login credentials.");
        }
    }
}
