package application;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/MainWindow.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.setResizable(false);
		primaryStage.show();

		primaryStage.setOnCloseRequest(e -> {
			System.out.println("Exited with click");
			System.exit(0);
			Platform.exit();
		});

	}

	public static void main(String[] args) {
		System.out.println(System.getProperty("sun.arch.data.model") + "Bit JRE");
		
		launch(args);

		//
		//21011970
		//
		//XD99
		
		System.exit(0);
		Platform.exit();
		
		
	}

	

}
