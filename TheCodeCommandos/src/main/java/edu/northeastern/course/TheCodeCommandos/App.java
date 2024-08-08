package edu.northeastern.course.TheCodeCommandos;

import edu.northeastern.course.TheCodeCommandos.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
	@Override
	public void start(Stage stage) {
		// Start the app from the login page
		Model.getInstance().getViewFactory().showLoginWindow();
	}
}