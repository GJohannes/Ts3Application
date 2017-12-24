package login;



import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import miscellaneous.FileInputOutput;
import serverControllers.ServerMainWindowController;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

import clientControllers.MainWindowController;

public class LoginController implements Initializable {


	@FXML
	private AnchorPane rootPane;

	@FXML
	private Text infoBox;
	
	@FXML
	private TextField userNameTextField;
	
	@FXML
	private TextField uIdTextField;
	
	@FXML
	private TextField serverQueryNameTextField;
	
	@FXML
	private TextField serverQueryPasswordTextField;
	
	@FXML
	private TextField serverIpAdressTextField;
	
	@FXML
	private TextField serverPortTextField;
	
	@FXML
	private Button serverLoginButton;
	
	@FXML
	private Button clientLoginButton;
	
	@FXML
	private TextField ts3PathTextField;
	
	@FXML
	private void clientLogin(ActionEvent event) throws IOException {
		infoBox.setText("Trying to connect to server ....");
		Image icon = new Image(this.getClass().getResourceAsStream("/waiting.gif"));
		clientLoginButton.setGraphic(new ImageView(icon));
		
		Task<TS3Api> connectToServer = new Task<TS3Api>() {
            @Override
            protected TS3Api call() throws Exception {
              
            	TS3Config config = new TS3Config();
        		TS3Query query = new TS3Query(config);
        		TS3Api api = query.getApi();

        		// Ts3Conection Settings
        		config.setHost(serverIpAdressTextField.getText().toString()); 
        		config.setDebugLevel(Level.ALL);
        		
        		query.connect();
        		
        		api.login(serverQueryNameTextField.getText().toString(), serverQueryPasswordTextField.getText().toString());
        		api.selectVirtualServerByPort(Integer.valueOf(serverPortTextField.getText().toString()));
        		api.setNickname(serverQueryNameTextField.getText().toString());
        		api.registerAllEvents();
        		api.sendServerMessage("QueryTester is now online!");
        
        		return api;
            }
        };
       
        
        connectToServer.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
            	try {
					if(connectToServer.get().getConnectionInfo() == null){
						infoBox.setText("Could not connect to Server");
						serverLoginButton.setGraphic(null);
						return;
					}
				} catch (InterruptedException | ExecutionException e1) {
					e1.printStackTrace();
				} 
            	
            	FXMLLoader Loader = new FXMLLoader();
        		Loader.setLocation(getClass().getResource("/MainWindow.fxml"));
        		try {
					Loader.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
        		MainWindowController mainWindowController = Loader.getController();
        		
        		// Pass Data to new Controller
        		mainWindowController.setUserName(userNameTextField.getText());
        		mainWindowController.setUId(uIdTextField.getText());
        		mainWindowController.setIpAdress(serverIpAdressTextField.getText());
        		mainWindowController.setServerPort(serverPortTextField.getText());
        		mainWindowController.setServerQueryName(serverQueryNameTextField.getText());
        		mainWindowController.setServerQueryPassword(serverQueryPasswordTextField.getText());
        		mainWindowController.setInfoBoxText("Succesfully connected to server: " + serverIpAdressTextField.getText() + ":" + serverPortTextField.getText());
        		try {
					mainWindowController.setApi(connectToServer.get());
				} catch (InterruptedException | ExecutionException e1) {
					e1.printStackTrace();
				} 
        		
        		//Load the next FXML File in the same Window
        		Parent p = Loader.getRoot();
        		
        		rootPane.getChildren().setAll(p);
        		rootPane.getScene().getWindow().sizeToScene();
        		
        		// New Window
//        		 Parent p = Loader.getRoot();
//        		 Stage stage = new Stage();
//        		 stage.setScene(new Scene(p));
//        		 stage.show();
            }
        });
        new Thread(connectToServer).start();
	}

	@FXML
	public void serverLogin(ActionEvent e){
		ConnectToServer con = new ConnectToServer(	serverIpAdressTextField.getText(), 
													serverQueryNameTextField.getText(),
													serverQueryPasswordTextField.getText(),
													Integer.parseInt(serverPortTextField.getText()),
													rootPane
													
		);
		new Thread(con).start();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FileInputOutput inOut = new FileInputOutput();
		JSONObject json = new JSONObject();
		try {
			json = inOut.readFile("LoginData");
			userNameTextField.setText((String)json.get("userName"));
			serverPortTextField.setText((String)json.get("serverPort"));
			uIdTextField.setText((String)json.get("uId"));
			serverIpAdressTextField.setText((String)json.get("tsIpAdress"));
			serverQueryNameTextField.setText((String)json.get("serverQueryName"));
			serverQueryPasswordTextField.setText((String)json.get("serverQueryPw"));
			ts3PathTextField.setText((String)json.get("ts3Path"));
		} catch (IOException | ParseException | NullPointerException e) {
			infoBox.setText("Could not read file: LoginData");
			e.printStackTrace();
		}
		
		Ts3Client client = new Ts3Client();
		try {
			client.startTs3Client(ts3PathTextField.getText().toString());
		} catch (Exception e) {
			infoBox.setText("Teamspeak3Client path wrong");
			e.printStackTrace();
		}
		
		
	}
	
	

	
	
	public void saveLogin(ActionEvent e){
		FileInputOutput inOut = new FileInputOutput();
		JSONObject json = new JSONObject();
		json.put("userName", userNameTextField.getText().toString());
		json.put("serverPort", serverPortTextField.getText().toString());
		json.put("uId", uIdTextField.getText().toString());
		json.put("tsIpAdress", serverIpAdressTextField.getText().toString());
		json.put("serverQueryName", serverQueryNameTextField.getText().toString());
		json.put("serverQueryPw", serverQueryPasswordTextField.getText().toString());
		json.put("ts3Path", ts3PathTextField.getText().toString());		
		try {
			inOut.writeFile(json, "LoginData");
		} catch (IOException | NullPointerException e1) {
			infoBox.setText("Could not save login data");
			e1.printStackTrace();
		}
		
		
	}
	
	public void exit(){
		Platform.exit();
		System.exit(0);
	}
}
