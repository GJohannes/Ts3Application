package miscellaneous;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.spi.AudioFileReader;
import javax.swing.JEditorPane;

import org.json.simple.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.VirtualServer;
import com.sun.javafx.collections.MappingChange.Map;

import clientFunctions.LookUserInChannel;

import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;

public class AppMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		// Parent root =
		// FXMLLoader.load(getClass().getResource("/MainWindow.fxml")); Start
		// after login
		Pane root = (Pane) FXMLLoader.load(getClass().getResource("/Login.fxml"));

		primaryStage.setScene(new Scene(root));
		primaryStage.show();

		// Terminate Program on Close Window
		primaryStage.setOnCloseRequest(e -> {
			System.exit(0);
			Platform.exit();
		});

	}

	public static void main(String[] args) {
		System.out.println("adhasd");
		
		launch(args);

		
//
//		Ts3Client test = new Ts3Client();
//		
//		test.startTs3Client("C:/Program Files/TeamSpeak 3 Client/ts3client_win64.exe");
		
//		String uId = "giwLzOcXMB1fi+DVtV6uSDzCqGo=";
//		TS3Config config = new TS3Config();
//		TS3Query query = new TS3Query(config);
//		TS3Api api = query.getApi();
//		// TsConection Settings
//		config.setHost("127.0.0.1");
//		config.setDebugLevel(Level.ALL);
//
//		query.connect();
//
//		api.login("QueryTester", "cdgT5HY9");
//		api.selectVirtualServerByPort(Integer.valueOf(9987));
//		api.setNickname("QueryTester");
//		api.registerAllEvents();
//		api.sendServerMessage("QueryTester is now online!");
//		
//		System.out.println(api.getClientsByName("Zephira").get(0).getChannelId());
//		
//		
//		System.out.println(api.getClientByUId(uId));
//		
//		
//		LookUserInChannel test = new LookUserInChannel();
	}
}
