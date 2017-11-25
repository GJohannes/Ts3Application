package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class popUpController implements Initializable {

	@FXML
	private Button emptyButton;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Image icon = new Image(this.getClass().getResourceAsStream("/penguin.gif"));
		emptyButton.setGraphic(new ImageView(icon));
	}

}
