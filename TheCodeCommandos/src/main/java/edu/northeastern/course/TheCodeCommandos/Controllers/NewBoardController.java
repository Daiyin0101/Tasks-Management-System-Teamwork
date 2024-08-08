package edu.northeastern.course.TheCodeCommandos.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.ResourceBundle;

import edu.northeastern.course.TheCodeCommandos.Models.Board;
import edu.northeastern.course.TheCodeCommandos.Models.Card;
import edu.northeastern.course.TheCodeCommandos.Models.DatabaseDriver;
import edu.northeastern.course.TheCodeCommandos.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class NewBoardController implements Initializable {

    public Button submit_btn;
    public Button new_card_btn;
    public TextField board_title_textfield;
    public TextArea description_textarea;
    public DatePicker date;
    public ListView<String> to_do_cards_listview;
    public DatabaseDriver databaseDriver;
    public ObservableList<Card> to_do_cards_list = FXCollections.observableArrayList();
    public HashSet<String> card_names_hashSet = new HashSet<>();
    public Label error_lbl;

    // Everytime a window is loaded, the initialize() method is called
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        databaseDriver = new DatabaseDriver();
        submit_btn.setOnAction(e -> submitNewBoard());
        new_card_btn.setOnAction(e -> Model.getInstance().getViewFactory().addCard().ifPresent(pair -> {
            String cardName = pair.getKey();
            LocalDate dueDate = pair.getValue();
            error_lbl.setStyle("-fx-text-fill: red");
            if (cardName.isEmpty() || dueDate == null) {
                error_lbl.setText("Please fill out all the input field.");
            } else if (card_names_hashSet.contains(cardName)) {
                error_lbl.setText("Card name already exists.\nPlease choose a different card name.");
            } else if (date.getValue() == null) {
                error_lbl.setText("Please fill out project due date first.");
            } else if (dueDate.isAfter(date.getValue())) {
                error_lbl.setText("The time you choose is after project \ndue date.\nPlease choose a different time.");
            } else {
                error_lbl.setText("");
                card_names_hashSet.add(cardName);
                to_do_cards_listview.getItems().add(cardName);
                to_do_cards_list.add(new Card(cardName, "To-do", dueDate, board_title_textfield.getText()));
            }
        }));
    }

    // Create a new board and update the database
    private void submitNewBoard() {
        String projectName = board_title_textfield.getText();
        String description = description_textarea.getText();
        LocalDate dueDate = date.getValue();

        if (projectName.isEmpty() || description.isEmpty() || dueDate == null) {
            // Handle empty fields or invalid input
            error_lbl.setStyle("-fx-text-fill: red");
            error_lbl.setText("Please fill all fields.");
            return;
        }

        if (Model.getInstance().getBoardTitleHashSet().contains(projectName)) {
            error_lbl.setStyle("-fx-text-fill: red");
            error_lbl.setText("This project already exists.\n Please change a project name.");
            return;
        }
        new Board(projectName, description, dueDate).add();
        for (Card c : to_do_cards_list) {
            c.setBoardForCard(projectName);
            c.add();
        }
        emptyFields();
        error_lbl.setStyle("-fx-text-fill: blue");
        error_lbl.setText("Successfully submitted!");
    }

    // Empty all text fields
    private void emptyFields() {
        board_title_textfield.setText("");
        description_textarea.setText("");
        date.setValue(null);
        to_do_cards_listview.getItems().clear();
        to_do_cards_list.clear();
        card_names_hashSet.clear();
    }

}
