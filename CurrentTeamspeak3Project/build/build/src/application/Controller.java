package application;

import java.awt.Label;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import org.json.simple.JSONObject;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.VirtualServer;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;

public class Controller implements Initializable{
	
	public static TS3Config config = new TS3Config();
	public static TS3Query query = new TS3Query(config);
	public static TS3Api api = query.getApi();
	public static TS3EventAdapter MusikOnMove = new TS3EventAdapter() {};	
	
    @FXML
    private Text infoBox;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField uIdTextField;
    @FXML
    private TextField ipAdressTextField;
    @FXML
    private TextField serverQueryNameTextField;
    @FXML
    private TextField serverQueryPasswordTextField;
    @FXML
    private TextField audioFileNameTextField;
    @FXML
    private TextField audioFileLenghtTextField;
    @FXML
    private TextField serverPortTextField;
    @FXML
    private Tooltip test;
    @FXML
    private Label userNameLabel;
    
	
    public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("GUI2.fxml"));
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
	}
	
    @FXML
    private void startStopMusikOnMove(ActionEvent event) throws Exception {
    	String startStopText = ((Button)event.getSource()).getText();
    	System.out.println("button pressed");
    	if(startStopText.equals("Start MusikOnMove")){
    		FileInputOutput writerReaderObject = new FileInputOutput();
    		
    				
    		writerReaderObject.writeAudioBatch(audioFileNameTextField.getText());
        	playMusikOnMove moveMusikObject = new playMusikOnMove();  
        	MusikOnMove = moveMusikObject.playMusicOnClientMove(api, userNameTextField.getText().toString(), (Integer.parseInt(audioFileLenghtTextField.getText().toString())));
        	api.addTS3Listeners(MusikOnMove);
        	infoBox.setText("Now playing .mp3 file after channel switch");
    		((Button)event.getSource()).setText("Stop MusikOnMove");
    	} else {
    		api.removeTS3Listeners(MusikOnMove);
        	infoBox.setText("Stopped playing .mp3 file after channel switch");
        	((Button)event.getSource()).setText("Start MusikOnMove");
    	}
    	//Every time Button is pressed audio file name can be changed
    }
    
    @FXML
    private void empty(ActionEvent event){
    	test.setText("Hallo Welt");
    	userNameTextField.setTooltip(test);

    }
    
    @FXML
    private void saveData(ActionEvent event) {
		JSONObject output = new JSONObject();
		output.put("userName", userNameTextField.getText().toString());
		output.put("audioFileName", audioFileNameTextField.getText().toString());
		output.put("audioLenght", audioFileLenghtTextField.getText().toString());
		output.put("uId", uIdTextField.getText().toString());
		output.put("tsIpAdress", ipAdressTextField.getText().toString());
		output.put("serverQueryName", serverQueryNameTextField.getText().toString());
		output.put("serverQueryPw", serverQueryPasswordTextField.getText().toString());
		output.put("serverPort", serverPortTextField.getText().toString());
		FileInputOutput inOut = new FileInputOutput();
		inOut.writeFile(output);
    }
    
    @FXML
    private void connectToServer(ActionEvent event){
    	startAPI();
    	infoBox.setText("Sucessfully connected to Server: " + ipAdressTextField.getText() + ":" + serverPortTextField.getText());
    }
    
    @FXML
    private void exitSystem(ActionEvent event){
    	System.exit(0);
    }
    
	@Override
	//Read data from file
	public void initialize(URL location, ResourceBundle resources) {
		FileInputOutput inOut = new FileInputOutput();
		JSONObject json = new JSONObject();
		json = inOut.readFile();
		userNameTextField.setText((String)json.get("userName"));
		serverPortTextField.setText((String)json.get("serverPort"));
		uIdTextField.setText((String)json.get("uId"));
		ipAdressTextField.setText((String)json.get("tsIpAdress"));
		serverQueryNameTextField.setText((String)json.get("serverQueryName"));
		serverQueryPasswordTextField.setText((String)json.get("serverQueryPw"));
		audioFileNameTextField.setText((String)json.get("audioFileName"));
		audioFileLenghtTextField.setText((String) json.get("audioLenght")); /// UNSAFE !!!!!!!! requires number value at audiolenght
		System.out.println("Started GUI with values");
	}

	public void startAPI(){
		// TsConection Settings
		config.setHost(ipAdressTextField.getText().toString()); 
		config.setDebugLevel(Level.ALL);

		query.connect();
		api.login(serverQueryNameTextField.getText().toString(), serverQueryPasswordTextField.getText().toString());
		api.selectVirtualServerByPort(Integer.valueOf(serverPortTextField.getText().toString()));
		api.setNickname(serverQueryNameTextField.getText().toString());
		api.registerAllEvents();
		api.sendServerMessage("QueryTester is now online!");
    }
	
}