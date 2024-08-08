package edu.northeastern.course.TheCodeCommandos.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

import edu.northeastern.course.TheCodeCommandos.Models.Board;
import edu.northeastern.course.TheCodeCommandos.Models.Card;
import edu.northeastern.course.TheCodeCommandos.Models.Model;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DashboardController implements Initializable {

    public Text user_name;
    public Label login_date;
    public ListView<String> cards_listview;
    public FlowPane boards_container;
    public Button create_new_board_btn;

    // Everytime a window is loaded, the initialize() method is called
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bindData();
        initBoards();
        initDueCards();
        create_new_board_btn.setOnAction(e -> toNewBoard());
        cards_listview.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY)) {
                if (event.getClickCount() == 2) {
                    Stage stage = (Stage)user_name.getScene().getWindow();
                    Model.getInstance().getViewFactory().closeStage(stage);
                    Model.getInstance().getViewFactory().showTableWindow();
                }
            }
        });
    }

    // Change the welcome name and today info by property binding
    public void bindData() {
        user_name.textProperty().bind(Bindings.concat("Hi, ").concat(Model.getInstance().getMember().getFirstName()));
        login_date.setText("Today, " + LocalDate.now());
    }

    // Get boards from database and display them in the dashboard
    private void initBoards() {
        boards_container.getChildren().clear();
        boards_container.setHgap(10);
        boards_container.setVgap(10);
        boards_container.setPadding(new Insets(10, 10, 10, 10));
        Model.getInstance().getBoards().clear();
        Model.getInstance().setBoards();
        PriorityQueue<Board> orderedBoards = new PriorityQueue<>(Board::compareTo);
        orderedBoards.addAll(Model.getInstance().getBoards());

        int len = orderedBoards.size();
        for (int i = 0; i < len; i++) {
            Board b = orderedBoards.poll();
            if (b != null) {
                VBox vBox = new VBox();
                vBox.setPadding(new Insets(25, 25, 25, 25));
                vBox.setStyle("-fx-border-color: black");
                Label board_label = new Label(b.getBoardTitle());
                Text description = new Text("\nDescription: " + b.getDescription() + "\n");
                Text dueDate = new Text("Due date: " + b.dueDateProperty().getValue().toString());
                vBox.getChildren().addAll(board_label, description, dueDate);
                vBox.setOnMouseClicked(e -> toBoard(b.getBoardTitle()));
                boards_container.getChildren().add(vBox);
            }
        }
    }

    // Get all the Done cards from database and display them in the listview
    private void initDueCards() {
        Model.getInstance().getAllCards().clear();
        Model.getInstance().setAllCards();
        PriorityQueue<Card> orderedAllCards = new PriorityQueue<>(Card::compareTo);
        orderedAllCards.addAll(Model.getInstance().getAllCards());

        int len = orderedAllCards.size();
        for (int i = 0; i < len; i++) {
            Card c = orderedAllCards.poll();
            if (c != null && !c.getStatus().equals("Done"))
                cards_listview.getItems().add(c.getCardName());
        }
    }

    // Close the current window and turn to new board window
    private void toNewBoard() {
        Stage stage = (Stage)create_new_board_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showNewBoardWindow();
    }

    // Close the current window and open the board window
    private void toBoard(String boardTitle) {
        Board selectedBoard = Model.getInstance().getBoards().stream()
                .filter(b -> b.getBoardTitle().equals(boardTitle))
                .findFirst()
                .orElse(null);

        if (selectedBoard != null) {
            Model.getInstance().setCurrentBoard(selectedBoard);
            Stage stage = (Stage)user_name.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(stage);
            Model.getInstance().getViewFactory().showBoardWindow();
        } else {
            System.err.println("Selected board not found.");
        }
    }

}

