package clientControllers;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import clientFunctions.LockUserInChannel;
import clientFunctions.MusicOnMove;
import customFxmlElements.BooleanButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;

import javafx.scene.control.Button;


import javafx.scene.text.Text;

import miscellaneous.*;


public class MainWindowController implements Initializable{

	private static ExtendedTS3Api api;
	
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
    private BooleanButton musicOnMoveButton;
    @FXML
    private BooleanButton lockUserButton;
    
    @FXML
    private Tooltip test;
    @FXML
    private AnchorPane rootPane;
    
    
    public void setInfoBoxText(String s){
    	infoBox.setText(s);
    }
    
    public void setApi(ExtendedTS3Api ts3Api){
    	api = ts3Api;
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
    	FileInputOutput writerReaderObject = new FileInputOutput();
    	MusicOnMove musikMove = new MusicOnMove();
    	
    	if(musicOnMoveButton.isNowActive()) {
    		try {
    			audioFileNameTextField.setDisable(true);
    			audioFileLenghtTextField.setDisable(true);
    	    	File audioFile = new File(audioFileNameTextField.getText());
    			writerReaderObject.writeAudioBatch(audioFile.getAbsolutePath(), "StartAudio.bat");
    			musikMove.startMusicOnMove(api, userName.getText(), Integer.valueOf(audioFileLenghtTextField.getText().toString()));
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    	} else {
    		audioFileNameTextField.setDisable(false);
			audioFileLenghtTextField.setDisable(false);
    		musikMove.stopMusicOnMove(api);
    	}
    }

	@FXML
    private void lockUserInCurrentChannel(ActionEvent event){
		LockUserInChannel lock = new LockUserInChannel();
		if(lockUserButton.isNowActive()) {
			lock.activateCatchMeIfYouCan(api, userName.getText().toString());			
		} else {
			lock.deactivateCatchMeIfYouCan(api);
		}
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
		musicOnMoveButton.setActiveText("Active -- Click to Deactivate");
		musicOnMoveButton.setDeActiveText("Currently Deactive -- Click To Activate");
		
		lockUserButton.setActiveText("Currently Locking a User");
		lockUserButton.setDeActiveText("Not currently locking a user");
		
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