package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

/* Final Project | Word Occurrences    */

/**
 * 
 * Final Project | Word Occurrences 
 *
 */
public class Main extends Application {
	
	/**
	 * start method to being building the new Stage GUI with JavaFX
	 * 
	 * @param primaryStage Stage passed into method and setting new Stage in a try/catch block
	 *
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			
			Parent root = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("Word Occurrences");
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Main method to launch the Main Scene application
	 * 
	 * @param args Strings passed into the main
	 *
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
