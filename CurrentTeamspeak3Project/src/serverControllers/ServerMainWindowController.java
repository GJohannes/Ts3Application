package serverControllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.theholywaffle.teamspeak3.TS3Api;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class ServerMainWindowController implements Initializable {
	
	@FXML
	private AnchorPane rootPane;
	
	@FXML
	private Button somethingButton;
	
	@FXML
	public void doSomething(){
		System.out.println("done");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	public void setApi(TS3Api ts3Api) {
		System.out.println("asdas");
		
	}

}
