package serverControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.theholywaffle.teamspeak3.TS3Api;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import serverFunctions.ServerLogger;
import serverFunctions.UserLoggedInEntity;

public class ServerMainWindowController implements Initializable {
	
	@FXML private AnchorPane rootPane;
	@FXML private Button somethingButton;
	@FXML private Button printListButton;
		
	private TS3Api api;
		
	@FXML
	public void doSomething(){
		ServerLogger logger = ServerLogger.getInstance(api);
		logger.startServerLogging();
		System.out.println("started server logger");
	}
	
	@FXML
	private void printList(){
		System.out.println("hello worlds");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	public void setApi(TS3Api ts3Api) {
		this.api = ts3Api;
	}
}
