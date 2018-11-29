package login;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import miscellaneous.FileInputOutput;
import serverControllers.ServerWindowController;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import clientControllers.MainWindowController;
import customFxmlElements.IpAdressTextField;
import customFxmlElements.PortTextField;


public class LoginController implements Initializable {
	
	@FXML private AnchorPane rootPane;
	@FXML private Text infoBox;
	@FXML private TextField userNameTextField;
	@FXML private TextField uIdTextField;
	@FXML private TextField serverQueryNameTextField;
	@FXML private TextField serverQueryPasswordTextField;
	@FXML private IpAdressTextField serverIpAdressTextField;
	@FXML private PortTextField serverPortTextField;
	@FXML private Button serverLoginButton;
	@FXML private Button clientLoginButton;
	@FXML private TextField ts3PathTextField;
	@FXML private Button ts3PathFileChooser;
	
	@FXML
	private void clientLogin(ActionEvent event) throws IOException {
		if(!serverIpAdressTextField.isValid() || !serverPortTextField.isValid()){
			infoBox.setText("Not all text fileds are valid");
			return;
		}
		
		ConnectToServer connect = ConnectToServer.getInstance( 	serverIpAdressTextField.getText(),
																serverQueryNameTextField.getText(),
																serverQueryPasswordTextField.getText(),
																Integer.parseInt(serverPortTextField.getText()),
																userNameTextField.getText(),
																uIdTextField.getText()
		);
		// only one instance of connect is possible
		if(connect == null){
			return;
		}
		connect.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {
				clientLoginFunctionality(connect);
			}
		});
		
		connect.setOnFailed(e -> {	infoBox.setText("Could not connect to Server");
				 					clientLoginButton.setGraphic(null);
		});

		
		infoBox.setText("Trying to connect to server ....");
		Image icon = new Image(this.getClass().getResourceAsStream("/waiting.gif"));
		clientLoginButton.setGraphic(new ImageView(icon));
		new Thread(connect).start();
	}

	@FXML
	public void serverLogin(ActionEvent e) {	
		if(!serverIpAdressTextField.isValid() || !serverPortTextField.isValid()){
			infoBox.setText("Not all text fileds are valid");
			return;
		} 
		
		ConnectToServer connect = ConnectToServer.getInstance(	
				serverIpAdressTextField.getText(), 
				serverQueryNameTextField.getText(),
				serverQueryPasswordTextField.getText(),
				Integer.parseInt(serverPortTextField.getText()),
				userNameTextField.getText(),
				uIdTextField.getText()
				);
		
		if(connect == null){
			return;
		}
		
		connect.setOnSucceeded(event -> {
			serverLoginFunctionality(connect);
		});
		
		connect.setOnFailed(event -> { 	serverLoginButton.setGraphic(null);
										infoBox.setText("Could not connect to Server");
		});
		
		infoBox.setText("Trying to connect to server ....");
		Image icon = new Image(this.getClass().getResourceAsStream("/waiting.gif"));
		serverLoginButton.setGraphic(new ImageView(icon));
		new Thread(connect).start();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FileInputOutput inOut = FileInputOutput.getInstance();
		JSONObject json = new JSONObject();
		try {
			json = inOut.readFile("LoginData");
			userNameTextField.setText((String) json.get("userName"));
			serverPortTextField.setText((String) json.get("serverPort"));
			uIdTextField.setText((String) json.get("uId"));
			serverIpAdressTextField.setText((String) json.get("tsIpAdress"));
			serverQueryNameTextField.setText((String) json.get("serverQueryName"));
			serverQueryPasswordTextField.setText((String) json.get("serverQueryPw"));
			ts3PathTextField.setText((String) json.get("ts3Path"));
		} catch (IOException | ParseException | NullPointerException e) {
			infoBox.setText("Could not read file: LoginData");
		}

		serverPortTextField.checkValidity();
		serverIpAdressTextField.checkValidity();
		
		Ts3Client client = new Ts3Client();
		try {
			client.startTs3Client(ts3PathTextField.getText().toString());
		} catch (Exception e) {
			infoBox.setText("Teamspeak3Client path could not be interpreted");
		}
	}

	public void saveLogin(ActionEvent e) {
		FileInputOutput inOut = FileInputOutput.getInstance();
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

	private void serverLoginFunctionality(ConnectToServer connect) {
		FXMLLoader serverLoader = new FXMLLoader();
		serverLoader.setLocation(getClass().getResource("/ServerWindow.fxml"));
		try {
			serverLoader.load();
			ServerWindowController serverMainWindowController = serverLoader.getController();
			serverMainWindowController.setApi(connect.get());
			serverMainWindowController.setIpAdress(serverIpAdressTextField.getText());
			serverMainWindowController.setServerPort(Integer.parseInt(serverPortTextField.getText()));
		} catch (IOException | InterruptedException | ExecutionException expection) {
			System.out.println(" -- exception thrown --");
			System.out.println(rootPane);
			System.out.println(this.rootPane);
			for(int i = 0; i < rootPane.getChildren().size(); i++) {
				System.out.println(rootPane.getChildren().get(i).getId());
			}
			expection.printStackTrace();
		}
		Parent p = serverLoader.getRoot();
		rootPane.getChildren().setAll(p);
		rootPane.getScene().getWindow().sizeToScene();
	}
	
	private void clientLoginFunctionality(ConnectToServer connect) {
		FXMLLoader clientLoader = new FXMLLoader();
		clientLoader.setLocation(getClass().getResource("/MainWindow.fxml"));
		try {
			clientLoader.load();
			MainWindowController mainWindowController = clientLoader.getController();
			// Pass Data to new Controller
			mainWindowController.setIpAdress(serverIpAdressTextField.getText());
			mainWindowController.setServerPort((serverPortTextField.getText()));
			mainWindowController.setServerQueryName(serverQueryNameTextField.getText());
			mainWindowController.setServerQueryPassword(serverQueryPasswordTextField.getText());
			mainWindowController.setInfoBoxText("Succesfully connected to server: " + serverIpAdressTextField.getText() + ":" + serverPortTextField.getText());
			mainWindowController.setUserName(userNameTextField.getText());
			mainWindowController.setUId(uIdTextField.getText());
			mainWindowController.setApi(connect.get());
			mainWindowController.setApi(connect.get());
		} catch (IOException | InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
		// Load the next FXML File in the same Window
		Parent p = clientLoader.getRoot();
		rootPane.getChildren().setAll(p);
		rootPane.getScene().getWindow().sizeToScene();

		// New Window
		// Parent p = Loader.getRoot();
		// Stage stage = new Stage();
		// stage.setScene(new Scene(p));
		// stage.show();
	}
	
	@FXML
	public void choseTs3Path() {
		FileChooser chooser = new FileChooser();
		File file = chooser.showOpenDialog(new Stage());
		if(file != null) {
			this.ts3PathTextField.setText(file.getAbsolutePath());			
		}
	}
	
	public void exit() {
		Platform.exit();
		System.exit(0);
	}
}
