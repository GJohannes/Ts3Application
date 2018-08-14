package serverControllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;



import customFxmlElements.BooleanButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import miscellaneous.ExtendedTS3Api;
import serverFunctions.MusikBot;
import serverFunctions.ServerLogger;
import serverFunctions.riotApi.RiotApiNotification;

public class ServerWindowController implements Initializable {
	
	@FXML private AnchorPane rootPane;
	@FXML private BooleanButton serverLoggerButton;
	@FXML private Text ipAdress;	
	@FXML private Text serverPort;
	@FXML private BooleanButton testButton;
	@FXML private BooleanButton musikBotButton;
	@FXML private BooleanButton riotApiButton;
	@FXML private Button vlcPathFileChooser;
	@FXML private TextField vlcPathTextField;
	
	private ExtendedTS3Api api;
	private ServerLogger logger;
	private MusikBot musicBot;
	
	//private RiotApiNotification apiThread;
	RiotApiNotification apiThread;
	Thread thread;
	
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
			
			this.logger = new ServerLogger(api);
			//this.logger = ServerLogger.getInstance(api);
			this.logger.startServerLogging();
		} else {
			// if logging was previously deactive it is hereby activated
			logger.stopServerLogging();
		}
	}
	
	@FXML
	public void toggleMusikBot() {
		if(musikBotButton.isNowActive()) {
			musicBot = new MusikBot();
			musicBot.startMusikBot(api, this.vlcPathTextField.getText());
		} else {
			musicBot.stopMusicBot(api);
		}
		
		
		
	}
	
	@FXML
	public void chooseVlcPathLocation() {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(new Stage());
		if(file != null) {
			this.vlcPathTextField.setText(file.getAbsolutePath());			
		}
	}
	
	
	@FXML
	public void toggleRiotApi() {
		if(riotApiButton.isNowActive()) {
			this.apiThread = new RiotApiNotification(api);
			thread = new Thread(apiThread);
			thread.start();
		} else {
			apiThread.exit();
		}
 
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		serverLoggerButton.setActiveText("Currently Active -- Stop Logging");
		serverLoggerButton.setDeActiveText("Deactive -- Start Logging");
		musikBotButton.setActiveText("Currently Active");
		musikBotButton.setDeActiveText("Music Bot is Deactive");
		riotApiButton.setActiveText("Api check online");
		riotApiButton.setDeActiveText("Api check offline");
		this.vlcPathTextField.setText("C:\\Program Files\\VideoLAN\\VLC\\vlc.exe");
	}

	public void setApi(ExtendedTS3Api ts3Api) {
		this.api = ts3Api;
	}
	
}
