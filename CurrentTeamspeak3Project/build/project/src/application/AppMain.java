package application;

import java.io.IOException;
import java.util.logging.Level;
import org.json.simple.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

public class AppMain extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception{
		primaryStage.show();
		

		
		Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("test.fxml"));
		//Parent root = FXMLLoader.load(Main.class.getClassLoader().getResource("/resources/GUI.fxml"));
		
		
		primaryStage.setScene(new Scene(root));
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		System.out.println(args);
		launch(args);

		//InputStrings
// 		GUI !!!!!		
//		String userName = "Zephira";
//		String audioFileName = "Doh.mp3";
//		int audioLenght = 1500;
//		String tsIpAdress = "localhost";
//		String serverQueryName = "QueryTester";
//		String serverQueryPw = "cdgT5HY9";
//		String uId = "giwLzOcXMB1fi+DVtV6uSDzCqGo=";
//		
//		
//		JSONObject output = new JSONObject();
//		output.put("userName", userName);
//		output.put("audioFileName", audioFileName);
//		output.put("audioLenght", audioLenght);
//		output.put("uId", uId);
//		output.put("tsIpAdress", tsIpAdress);
//		output.put("serverQueryName", serverQueryName);
//		output.put("serverQueryPw", serverQueryPw);
//		FileInputOutput test = new FileInputOutput();
//		test.writeFile(output);
	}

}
