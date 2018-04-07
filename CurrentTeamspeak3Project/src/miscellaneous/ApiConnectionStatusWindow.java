package miscellaneous;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ApiConnectionStatusWindow {

	public void showWindow(ExtendedTS3Api api) {
		
		
		Label ipAdressLabel = new Label(api.getConnectedConfigValues().getServerIpAdress());
		Label serverQueryNameLabel = new Label(api.getConnectedConfigValues().getServerQueryName());
		Label serverQueryPasswordLabel = new Label(api.getConnectedConfigValues().getServerQueryPassword());
		Label serverPortLabel = new Label(Integer.toString(api.getConnectedConfigValues().getServerPort()));
		VBox box = new VBox();
		box.setSpacing(20);
		box.getChildren().add(ipAdressLabel);
		box.getChildren().add(serverQueryNameLabel);
		box.getChildren().add(serverQueryPasswordLabel);
		box.getChildren().add(serverPortLabel);
		
		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(box);
		
		Scene secondScene = new Scene(stackPane, 230, 500);

		// New window (Stage)
		Stage newWindow = new Stage();
		newWindow.setTitle("Current Server Connection Info");
		newWindow.setScene(secondScene);

		newWindow.show();

	}
}
