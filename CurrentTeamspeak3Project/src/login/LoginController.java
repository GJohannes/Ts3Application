package login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.application.Platform;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import miscellaneous.FileInputOutput;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;



public class LoginController implements Initializable {

	@FXML private AnchorPane rootPane;
	@FXML private Text infoBox;
	@FXML private TextField userNameTextField;
	@FXML private TextField uIdTextField;
	@FXML private TextField serverQueryNameTextField;
	@FXML private TextField serverQueryPasswordTextField;
	@FXML private TextField serverIpAdressTextField;
	@FXML private TextField serverPortTextField;
	@FXML private Button serverLoginButton;
	@FXML private Button clientLoginButton;
	@FXML private TextField ts3PathTextField;

	@FXML
	private void clientLogin(ActionEvent event) throws IOException {
		infoBox.setText("Trying to connect to server ....");
		Image icon = new Image(this.getClass().getResourceAsStream("/waiting.gif"));
		clientLoginButton.setGraphic(new ImageView(icon));
		ConnectToServer con = new ConnectToServer(	serverIpAdressTextField.getText(), 
													serverQueryNameTextField.getText(),
													serverQueryPasswordTextField.getText(),
													Integer.parseInt(serverPortTextField.getText()), 
													rootPane, 
													false

		);
		new Thread(con).start();
	}

	@FXML
	public void serverLogin(ActionEvent e) {
		ConnectToServer con = new ConnectToServer(	serverIpAdressTextField.getText(), 
													serverQueryNameTextField.getText(),
													serverQueryPasswordTextField.getText(),
													Integer.parseInt(serverPortTextField.getText()), 
													rootPane, 
													true

		);
		new Thread(con).start();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FileInputOutput inOut = new FileInputOutput();
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

	public void saveLogin(ActionEvent e) {
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

	public void exit() {
		Platform.exit();
		System.exit(0);
	}
}
