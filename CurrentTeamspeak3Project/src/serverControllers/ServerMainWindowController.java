package serverControllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.github.theholywaffle.teamspeak3.TS3Api;

import customFxmlElements.BooleanButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import miscellaneous.TestClass;
import serverFunctions.ServerLogger;

public class ServerMainWindowController implements Initializable {
	
	@FXML private AnchorPane rootPane;
	@FXML private BooleanButton serverLoggerButton;
	@FXML private Text ipAdress;	
	@FXML private Text serverPort;
	@FXML private BooleanButton testButton;
	
	private TS3Api api;
	ServerLogger logger;
	
	public void setIpAdress(String ipAdress) {
		this.ipAdress.setText(ipAdress);
	}

	public void setServerPort(int port) {
		serverPort.setText(Integer.toString(port));
	}

	@FXML
	public void logging(){
		// if already active the stop logging by setting the log instance to null
		if(serverLoggerButton.isNowActive()) {
			// get the instance and set it to null
			this.logger = ServerLogger.getInstance(api);
			this.logger.startServerLogging();
			System.out.println(logger);
		} else {
			// if logging was previously deactive it is hereby activated
			logger.stopServerLogging();
		}
	}
	
	@FXML
	public void test(){
		System.out.println(Math.random());
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("hello world init");
		serverLoggerButton.setActiveText("Currently Active -- Stop Logging");
		serverLoggerButton.setDeActiveText("Deactive -- Start Logging");
	}

	public void setApi(TS3Api ts3Api) {
		this.api = ts3Api;
	}
	
	public ServerMainWindowController() {
//		System.out.println("hello world");
//		serverLoggerButton.setActiveText("Start Logging");
//		serverLoggerButton.setDeActiveText("Stop Logging");
	}
	
}
