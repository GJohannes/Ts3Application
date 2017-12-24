package clientControllers;

import java.awt.Label;
import java.awt.Window;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import clientFunctions.LookUserInChannel;
import clientFunctions.MusicOnMove;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import miscellaneous.*;
import javafx.scene.Node;



public class MainWindowController implements Initializable{

	private static TS3Api api;
	
    @FXML
    private Text infoBox;
    @FXML
    private Text userName;
    @FXML
    private Text uId;
    @FXML
    private Text ipAdress;
    @FXML
    private Text serverPort;
    @FXML
    private Text serverQueryName;
    @FXML
    private Text serverQueryPassword;
    @FXML
    private TextField audioFileNameTextField;
    @FXML
    private TextField audioFileLenghtTextField;
    
    @FXML
    private Tooltip test;
    @FXML
    private AnchorPane rootPane;
    
    
    public void setInfoBoxText(String s){
    	infoBox.setText(s);
    }
    
    public void setApi(TS3Api connectedApi){
    	api = connectedApi;
    }
	
    public void setUserName(String s){
    	userName.setText(s);
    }
    
    public void setUId(String s){
    	uId.setText(s);
    }
    
    public void setIpAdress(String s){
    	ipAdress.setText(s);
    }
    
    public void setServerPort(String s){
    	serverPort.setText(s);
    }
    
    public void setServerQueryName(String s) {
		serverQueryName.setText(s);
	}
    
    public void setServerQueryPassword(String s ){
    	serverQueryPassword.setText(s);
    }
    
    @FXML
    private void startStopMusikOnMove(ActionEvent event)  {
    	//playMusikOnMove test = playMusikOnMove.getInstance();
    	FileInputOutput writerReaderObject = new FileInputOutput();
    	MusicOnMove musikMove = new MusicOnMove();
    	
    	try {
			writerReaderObject.writeAudioBatch(audioFileNameTextField.getText().toString(), "StartAudio.bat");
			String startStopText = ((Button)event.getSource()).getText();
	    	if(startStopText.equals("Start MusikOnMove")){ 
	    		api = musikMove.activateMusikOnMove(api, userName.getText().toString(), Integer.valueOf(audioFileLenghtTextField.getText().toString()), true);
	    		((Button)event.getSource()).setText("Stop MusikOnMove");
	    	} else {
	    		api = musikMove.activateMusikOnMove(api, userName.getText().toString(), Integer.valueOf(audioFileLenghtTextField.getText().toString()), false);
	    		((Button)event.getSource()).setText("Start MusikOnMove");
	    	}
		} catch (IOException e) {
			infoBox.setText("Could not write audiofile batch");
			e.printStackTrace();
		}
    }

	@FXML
    private void empty(ActionEvent event){
		LookUserInChannel look = new LookUserInChannel();
		api = look.activateCatchMeIfYouCan(api, userName.getText().toString(), true);
    }
    
    @FXML
    private void saveData(ActionEvent event) {
		JSONObject output = new JSONObject();
		output.put("audioFileName", audioFileNameTextField.getText().toString());
		output.put("audioLenght", audioFileLenghtTextField.getText().toString());
		FileInputOutput inOut = new FileInputOutput();
		try {
			inOut.writeFile(output, "MainData");
		} catch (IOException e) {
			infoBox.setText("Could not safe Data");
			e.printStackTrace();
		}
    }
    
    @FXML
    private void exitSystem(ActionEvent event){
    	Platform.exit();
    	System.exit(0);
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		FileInputOutput inOut = new FileInputOutput();
		JSONObject json = new JSONObject();
	
		try {
		json = inOut.readFile("MainData");
		audioFileNameTextField.setText((String)json.get("audioFileName"));
		audioFileLenghtTextField.setText((String) json.get("audioLenght")); /// UNSAFE !!!!!!!! requires number value at audiolenght
		} catch (IOException | ParseException e) {
			infoBox.setText("Could not Read  file: MainData");
			e.printStackTrace();
		} 	
	}

	
	
}