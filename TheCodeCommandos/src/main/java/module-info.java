module edu.northeastern.course.TheCodeCommandos {
	requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql; 
    requires org.xerial.sqlitejdbc;
	requires javafx.graphics;
	requires javafx.base;


    opens edu.northeastern.course.TheCodeCommandos to javafx.fxml;
    exports edu.northeastern.course.TheCodeCommandos;
    exports edu.northeastern.course.TheCodeCommandos.Controllers;
    exports edu.northeastern.course.TheCodeCommandos.Models;
    exports edu.northeastern.course.TheCodeCommandos.Views;
}
