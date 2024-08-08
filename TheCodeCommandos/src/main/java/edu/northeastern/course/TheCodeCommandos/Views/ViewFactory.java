package edu.northeastern.course.TheCodeCommandos.Views;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Create a view factory which contains methods to show different page
 */
public class ViewFactory {
	
	public ViewFactory() {}
	
	// Show login window
	public void showLoginWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Login.fxml"));
		createStage(loader);
	}

	// Show dashboard window
	public void showDashboardWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Dashboard.fxml"));
		createStage(loader);
	}

	// Show members window
	public void showMembersWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Members.fxml"));
		createStage(loader);
	}

	// Show table window
	public void showTableWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Table.fxml"));
		createStage(loader);
	}

	// Show calendar window
	public void showCalendarWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Calendar.fxml"));
		createStage(loader);
	}

	// Show board window
	public void showBoardWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Board.fxml"));
		createStage(loader);
	}

	// Show new board window
	public void showNewBoardWindow() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/NewBoard.fxml"));
		createStage(loader);
	}
	
	// Create stage based on loader
	public void createStage(FXMLLoader loader) {
		Scene scene = null;
		try {
			scene = new Scene(loader.load());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.setTitle("Trello");
		stage.show();
	}
	
	// Close stage
	public void closeStage(Stage stage) {
		stage.close();
	}

	// Add a new card to the list
    public Optional<Pair<String, LocalDate>> addCard() {
		Dialog<Pair<String, LocalDate>> dialog = new Dialog<>();
		dialog.setTitle("Add Card");

		GridPane pane = new GridPane();
		pane.setHgap(10);
		pane.setVgap(10);
		pane.setPadding(new Insets(20, 150, 10, 10));

		TextField cardNameField = new TextField();
		DatePicker datePicker = new DatePicker();

		pane.add(new Label("Card Name:"), 0, 0);
		pane.add(cardNameField, 1, 0);
		pane.add(new Label("Due Date:"), 0, 1);
		pane.add(datePicker, 1, 1);

		dialog.getDialogPane().setContent(pane);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == ButtonType.OK) {
				return new Pair<>(cardNameField.getText(), datePicker.getValue());
			}
			return null;
		});

		return dialog.showAndWait();

	}

}
