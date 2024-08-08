package edu.northeastern.course.TheCodeCommandos.Controllers;

import edu.northeastern.course.TheCodeCommandos.Models.Board;
import edu.northeastern.course.TheCodeCommandos.Models.Card;
import edu.northeastern.course.TheCodeCommandos.Models.Model;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.PriorityQueue;
import java.util.ResourceBundle;

public class TableController implements Initializable {

    public ChoiceBox<String> project_choicebox;
    public GridPane cards_gridpane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupGridPaneTitle();
        setupChoiceBox();
    }
    
    // Create choice box
    private void setupChoiceBox() {
        Model.getInstance().getBoards().clear();
        Model.getInstance().setBoards();
        for (Board b: Model.getInstance().getBoards()) {
            Label project = new Label(b.getBoardTitle());
            project_choicebox.getItems().add(project.getText());
        }
        project_choicebox.valueProperty().addListener((observable, oldValue, newValue) -> setupGridPane(newValue));
    }
    
    // Create grid pane title
    private void setupGridPaneTitle() {
        Label card = new Label("Cards");
        card.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        Label status = new Label("Status");
        status.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        Label dueDate = new Label("Due Date");
        dueDate.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
        Label action = new Label("Action");
        action.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        cards_gridpane.addRow(0, card, status, dueDate, action);
        cards_gridpane.setHgap(110);
        cards_gridpane.setVgap(50);
        cards_gridpane.setPadding(new Insets(30, 40, 30, 40));
    }
    
    // Create the table
    private void setupGridPane(String boardTitle) {
        cards_gridpane.getChildren().clear();
        setupGridPaneTitle();
        Model.getInstance().getBoardCards().clear();
        Model.getInstance().setCardsForBoard(boardTitle);
        PriorityQueue<Card> orderedCards = new PriorityQueue<>(Card::compareTo);
        orderedCards.addAll(Model.getInstance().getBoardCards());

        int len = orderedCards.size();
        for (int i = 1; i <= len; i++) {
            Card c = orderedCards.poll();
            if (c != null) {
                cards_gridpane.add(new Label(c.getCardName()), 0, i);
                cards_gridpane.add(new Label(c.getStatus()), 1, i);
                cards_gridpane.add(new Label(c.dueDateProperty().getValue().toString()), 2, i);
                Button delete = new Button("Delete");
                delete.setOnAction(e -> {
                    new Card(c.getCardName(), c.getStatus(), c.dueDateProperty().getValue(), boardTitle).delete();
                    setupGridPane(boardTitle);
                });
                cards_gridpane.add(delete, 3, i);
            }
        }
    }
}
