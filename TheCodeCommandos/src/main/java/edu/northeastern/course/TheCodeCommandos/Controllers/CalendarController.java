package edu.northeastern.course.TheCodeCommandos.Controllers;

import edu.northeastern.course.TheCodeCommandos.Models.Board;
import edu.northeastern.course.TheCodeCommandos.Models.Card;
import edu.northeastern.course.TheCodeCommandos.Models.Model;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class CalendarController {

    @FXML
    public GridPane dayContainer;

    @FXML
    public Label monthLabel;

    public YearMonth currentYearMonth;

    @FXML
    public void initialize() {
        currentYearMonth = YearMonth.now();
        drawCalendar(currentYearMonth);


        dayContainer.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.getStylesheets().add(getClass().getResource("/styles/Calendar.css").toExternalForm());
            }
        });
    }

    @FXML
    public void handlePreviousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        drawCalendar(currentYearMonth);
    }

    @FXML
    public void handleNextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        drawCalendar(currentYearMonth);
    }
    
    // Create the calendar
    public void drawCalendar(YearMonth yearMonth) {
        Model.getInstance().getBoards().clear();
        Model.getInstance().getAllCards().clear();
        dayContainer.getChildren().clear();
        DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        monthLabel.setText(yearMonth.format(monthYearFormatter));

        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int daysInMonth = yearMonth.lengthOfMonth();
        DayOfWeek firstDayOfWeek = firstDayOfMonth.getDayOfWeek();

        String[] daysOfWeek = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < daysOfWeek.length; i++) {
            Label dayLabel = new Label(daysOfWeek[i]);
            dayLabel.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
            dayContainer.add(dayLabel, i, 0);
        }

        int column = firstDayOfWeek.getValue() % 7;
        int row = 1;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = firstDayOfMonth.plusDays(day - 1);
            VBox dayBox = createDayBox(date);

            dayContainer.add(dayBox, column, row);

            column++;
            if (column == 7) {
                column = 0;
                row++;
            }
        }
    }
    
    
    // Create the daybox
    public VBox createDayBox(LocalDate date) {
        VBox dayBox = new VBox();
        dayBox.setSpacing(5);
        dayBox.getStyleClass().add("day-box");

        Label dayLabel = new Label(Integer.toString(date.getDayOfMonth()));

        if (LocalDate.now().equals(date)) {
            dayLabel.getStyleClass().add("current-day");
        }

        Model.getInstance().setAllCards();
        for (Card c: Model.getInstance().getAllCards()) {
            if (c.dueDateProperty().getValue().equals(date)) {
                dayLabel.getStyleClass().add("card-label");
                dayLabel.setOnMouseClicked(e -> handleEventClick(date));
                break;
            }
        }

        Model.getInstance().setBoards();
        for (Board b: Model.getInstance().getBoards()) {
            if (b.dueDateProperty().getValue().equals(date)) {
                if (dayLabel.getStyleClass().contains("card-label")) {
                    dayLabel.getStyleClass().add("card-board-label");
                } else {
                    dayLabel.getStyleClass().add("board-label");
                }
                dayLabel.setOnMouseClicked(e -> handleEventClick(date));
                break;
            }
        }

        dayBox.getChildren().add(dayLabel);

        return dayBox;
    }
    
    // Create event show dialog
    public void handleEventClick(LocalDate date) {
        Stage dialog = new Stage();
        dialog.setTitle("Boards/cards due on " + date.toString());

        ListView<String> dialogListView = new ListView<>();
        dialogListView.setPadding(new Insets(10, 10, 10, 10));

        Model.getInstance().getBoards().clear();
        Model.getInstance().setBoards();
        for (Board b: Model.getInstance().getBoards()) {
            if (b.dueDateProperty().getValue().equals(date)) {
                Label boardDueLabel = new Label("Board:   " + b.getBoardTitle());
                if (!dialogListView.getItems().contains(boardDueLabel.getText())) {
                    dialogListView.getItems().add(boardDueLabel.getText());
                }
            }
        }

        Model.getInstance().getAllCards().clear();
        Model.getInstance().setAllCards();
        for (Card c: Model.getInstance().getAllCards()) {
            if (c.dueDateProperty().getValue().equals(date)) {
                Label cardDueLabel = new Label("Card:     " + c.getCardName() + " from board " + c.getBoardForCard());
                if (!dialogListView.getItems().contains(cardDueLabel.getText())) {
                    dialogListView.getItems().add(cardDueLabel.getText());
                }
            }
        }

        Scene dialogScene = new Scene(dialogListView, 300, 200);
        dialog.setScene(dialogScene);
        dialog.show();
    }
}