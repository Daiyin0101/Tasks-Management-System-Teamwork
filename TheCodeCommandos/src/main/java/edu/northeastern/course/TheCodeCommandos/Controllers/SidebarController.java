package edu.northeastern.course.TheCodeCommandos.Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import edu.northeastern.course.TheCodeCommandos.Models.Board;
import edu.northeastern.course.TheCodeCommandos.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class SidebarController implements Initializable {
	
	public Button dashboard_btn;
    public Button members_btn;
    public Button table_btn;
    public Button calendar_btn;
    public Button new_board_btn;
    public Button logout_btn;
    public Button report_btn;
    public ChoiceBox<String> boards_choice_box;
    public Button to_board_btn;

    // Everytime a window is loaded, the initialize() method is called
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBoardsChoiceBox();
    	dashboard_btn.setOnAction(e -> toDashboard());
    	members_btn.setOnAction(e -> toMembers());
    	table_btn.setOnAction(e -> toTable());
    	calendar_btn.setOnAction(e -> toCalendar());
    	new_board_btn.setOnAction(e -> toNewBoard());
        logout_btn.setOnAction(e -> onLogout());
        to_board_btn.setOnAction(e -> toBoard(boards_choice_box.getValue()));
    }

    // Set up sidebar choice box
    private void setBoardsChoiceBox() {
        Model.getInstance().getBoards().clear();
        boards_choice_box.setValue("Your Boards");
        if (Model.getInstance().getBoards().isEmpty())
            Model.getInstance().setBoards();
        for (Board b: Model.getInstance().getBoards()) {
            boards_choice_box.getItems().add(b.getBoardTitle());
        }
    }
    
    // Close the current stage and open the dashboard window
    private void toDashboard() {
    	closeStage();
    	Model.getInstance().getViewFactory().showDashboardWindow();
    }

    // Close the current window and open the member window
    private void toMembers() {
    	closeStage();
    	Model.getInstance().getViewFactory().showMembersWindow();
    }

    // Close the current window and open the table window
    private void toTable() {
    	closeStage();
    	Model.getInstance().getViewFactory().showTableWindow();
    }

    // Close the current window and open the calendar window
    private void toCalendar() {
    	closeStage();
    	Model.getInstance().getViewFactory().showCalendarWindow();
    }
    
    // Close the current window and open the board window
    private void toBoard(String boardTitle) {
        Board selectedBoard = Model.getInstance().getBoards().stream()
                .filter(b -> b.getBoardTitle().equals(boardTitle))
                .findFirst()
                .orElse(null);

        if (selectedBoard != null) {
            Model.getInstance().setCurrentBoard(selectedBoard);
            closeStage();
            Model.getInstance().getViewFactory().showBoardWindow();
        } else {
            System.err.println("Selected board not found.");
        }
    }


    // Close the current window and open the new board window
    private void toNewBoard() {
    	closeStage();
    	Model.getInstance().getViewFactory().showNewBoardWindow();
    }
    
    // Close the current window
    private void closeStage() {
    	Stage stage = (Stage)dashboard_btn.getScene().getWindow();
    	Model.getInstance().getViewFactory().closeStage(stage);
    }

    // Logout and return to the login window
    private void onLogout() {
        Stage stage = (Stage)dashboard_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
        Model.getInstance().setMemberLoginSuccessFlag(false);
    }
}
