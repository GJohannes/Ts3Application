package serverControllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.theholywaffle.teamspeak3.TS3Api;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import serverFunctions.ServerLogger;

public class ServerMainWindowController implements Initializable {
	
	@FXML private AnchorPane rootPane;
	@FXML private Button serverLogger;
	@FXML private Text ipAdress;	
	@FXML private Text serverPort;
	
	private TS3Api api;
	
	public void setIpAdress(String ipAdress) {
		this.ipAdress.setText(ipAdress);
	}

	public void setServerPort(int port) {
		serverPort.setText(Integer.toString(port));
	}

	@FXML
	public void logging(){
		ServerLogger logger = ServerLogger.getInstance(api);
		logger.startServerLogging();
	}
	
	public void testThis(){
		System.out.println("TEst");
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

	public void setApi(TS3Api ts3Api) {
		this.api = ts3Api;
	}
}
