package edu.northeastern.course.TheCodeCommandos.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import edu.northeastern.course.TheCodeCommandos.Models.Member;
import edu.northeastern.course.TheCodeCommandos.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.time.LocalDate;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

public class MembersController implements Initializable {
	public ListView<BorderPane> members_listview;
	public TextField firstName_textField;
	public TextField lastName_textField;
	public TextField username_textField;
	public TextField password_textField;
	public Button register_btn;
	public Label error_lbl;

	// Everytime a window is loaded, the initialize() method is called
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		register_btn.setOnAction(e -> registerNewMember());
		initMembers();
	}

	// Create members in the listview
	private void initMembers() {
		members_listview.getItems().clear();
		Model.getInstance().getAllMembers().clear();
		Model.getInstance().setAllMembers();
		PriorityQueue<Member> orderedMembers = new PriorityQueue<>(Member::compareTo);
		orderedMembers.addAll(Model.getInstance().getAllMembers());

		int len = orderedMembers.size();
		for (int i = 0; i < len; i++) {
			Member m = orderedMembers.poll();
			if (m != null)
				members_listview.getItems().add(createNewBorderPane(m));
		}
	}

	// Register a new member and update the database
	private void registerNewMember() {
		String fName = firstName_textField.getText();
		String lName = lastName_textField.getText();
		String username = username_textField.getText();
		String password = password_textField.getText();
		LocalDate date = LocalDate.now();

		if (fName.isEmpty() || lName.isEmpty() || username.isEmpty() || password.isEmpty()) {
			// Handle empty fields or invalid input
			error_lbl.setStyle("-fx-text-fill: red");
			error_lbl.setText("Please fill all fields.");
			return;
		}

		if (Model.getInstance().getMemberUsernameHashSet().contains(username)) {
			error_lbl.setStyle("-fx-text-fill: red");
			error_lbl.setText("This username is already in use.\n Please choose a different username.");
			return;
		}

		Model.getInstance().getDatabaseDriver().createNewMember(fName, lName, username, password, date);
		emptyFields();
		error_lbl.setStyle("-fx-text-fill: blue");
		error_lbl.setText("Successfully registered!");
		initMembers();
	}

	// Empty all text fields
	private void emptyFields() {
		firstName_textField.setText("");
		lastName_textField.setText("");
		password_textField.setText("");
		username_textField.setText("");
	}

	// Create a new boarder pane based on member
	private BorderPane createNewBorderPane(Member m) {
		FontAwesomeIconView icon = new FontAwesomeIconView();
		icon.setGlyphName("USER");
		icon.setSize("20");
		BorderPane pane = new BorderPane();
		HBox hBox_1 = new HBox();
		Label firstName = new Label("   " + m.getFirstName());
		Label lastName = new Label(" " + m.getLastName());
		hBox_1.getChildren().addAll(icon, firstName, lastName);
		pane.setLeft(hBox_1);
		HBox hBox_2 = new HBox();
		Label date = new Label(m.dateProperty().getValue().toString() + "   ");
		Button delete_btn = new Button("Delete");
		delete_btn.setStyle("-fx-font-size: 10");
		hBox_2.getChildren().addAll(date, delete_btn);
		pane.setRight(hBox_2);
		delete_btn.setOnAction(e -> deleteMember(m));
		return pane;
	}

	// Delete a member and update the database
	private void deleteMember(Member m) {
		m.delete();
		initMembers();
	}
}
