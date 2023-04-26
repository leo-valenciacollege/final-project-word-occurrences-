module FirstJavaFXProject {
	requires javafx.controls;
	requires javafx.fxml;
	requires org.junit.jupiter.api; //must be added
	requires java.sql;
	
	opens application to javafx.graphics, javafx.fxml;
}
