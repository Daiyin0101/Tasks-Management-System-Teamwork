package edu.northeastern.course.TheCodeCommandos.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import edu.northeastern.course.TheCodeCommandos.Models.Card;
import edu.northeastern.course.TheCodeCommandos.Models.Model;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class BoardController implements Initializable {

    public Label board_title_lbl;
    public ListView<BorderPane> to_do_listview;
    public ListView<BorderPane> doing_listview;
    public ListView<BorderPane> done_listview;
    public Text add_to_do_text;
    public Text add_doing_text;
    public Text add_done_text;
    public Button delete_project_btn;
    public Label error_lbl;
    public Label card_error_lbl;
    public Label due_date;

    // Everytime a window is loaded, the initialize() method is called
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Get the chosen Board
        String boardTitle = Model.getInstance().getCurrentBoard().getBoardTitle();
        board_title_lbl.setText(boardTitle);
        due_date.setText("Due :" + Model.getInstance().getCurrentBoard().dueDateProperty().get().toString());
        setAddCard();
        setDeleteBoard();
        populateLists();

    }

    // To populate lists
    private void populateLists() {
        to_do_listview.getItems().clear();
        doing_listview.getItems().clear();
        done_listview.getItems().clear();

        ObservableList<Card> cards = Model.getInstance().getBoardCards();
        cards.clear();
        Model.getInstance().setCardsForBoard(board_title_lbl.getText());
        for (Card card : cards) {
            BorderPane pane = new BorderPane();
            Text cardDisplay = new Text(card.getCardName());
            Button move = new Button();
            move.setStyle("-fx-font-size:10");
            Button delete = new Button("Delete");
            delete.setStyle("-fx-font-size:10");
            delete.setOnAction(e -> {
                card.delete();
                populateLists();
            });
            HBox hBox = new HBox();
            hBox.getChildren().addAll(delete, move);
            hBox.setSpacing(5);
            pane.setLeft(cardDisplay);

            switch (card.getStatus()) {
                case "To-do":
                    pane.setRight(hBox);
                    move.setText("Move to Doing");
                    move.setOnAction(e -> moveCardToNextStatus(card.getCardName(), "Doing"));
                    to_do_listview.getItems().add(pane);
                    break;
                case "Doing":
                    pane.setRight(hBox);
                    move.setText("Move to Done");
                    move.setOnAction(e -> moveCardToNextStatus(card.getCardName(), "Done"));
                    doing_listview.getItems().add(pane);
                    break;
                case "Done":
                    pane.setRight(delete);
                    done_listview.getItems().add(pane);
                    break;
            }
        }
    }
    
    // Add card
    private void setAddCard() {
        add_to_do_text.setOnMouseClicked(e -> {
            Model.getInstance().getViewFactory().addCard().ifPresent(pair -> {
                String cardName = pair.getKey();
                LocalDate dueDate = pair.getValue();
                createNewCard(cardName, dueDate, "To-do");
            });
            populateLists();
        });
        add_doing_text.setOnMouseClicked(e -> {
            Model.getInstance().getViewFactory().addCard().ifPresent(pair -> {
                String cardName = pair.getKey();
                LocalDate dueDate = pair.getValue();
                createNewCard(cardName, dueDate, "Doing");
            });
            populateLists();
        });
        add_done_text.setOnMouseClicked(e -> {
            Model.getInstance().getViewFactory().addCard().ifPresent(pair -> {
                String cardName = pair.getKey();
                LocalDate dueDate = pair.getValue();
                createNewCard(cardName, dueDate, "Done");
            });
            populateLists();
        });
    }
    
    // Create new card
    private void createNewCard(String cardName, LocalDate dueDate, String status) {
        if (cardName.isEmpty() || dueDate == null) {
            card_error_lbl.setText("Please fill out all the input field.");
        } else if (Model.getInstance().getCardNameHashSet().contains(cardName)) {
            card_error_lbl.setText("Card name already exists. Please choose a different card name.");
        } else if (dueDate.isAfter(Model.getInstance().getCurrentBoard().dueDateProperty().getValue())) {
            card_error_lbl.setText("The time you choose is after project due date.\nPlease choose a different time.");
        } else {
            card_error_lbl.setText("");
            new Card(cardName, status, dueDate, Model.getInstance().getCurrentBoard().getBoardTitle()).add();
        }
    }
    
    // Delete the project
    private void setDeleteBoard() {
        delete_project_btn.setOnAction(e -> {
            Model.getInstance().getCurrentBoard().delete();
            board_title_lbl.setText("Project Name");
            to_do_listview.getItems().clear();
            doing_listview.getItems().clear();
            done_listview.getItems().clear();
            error_lbl.setText("Successfully deleted project");
        });
    }

    // Move the card to next status
    private void moveCardToNextStatus(String cardName, String nextStatus) {
        Model.getInstance().getDatabaseDriver().updateCardStatus(cardName, nextStatus);
        populateLists();
    }
}
