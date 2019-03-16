package miscellaneous;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.servlet.ServletException;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.json.simple.parser.ParseException;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;

import customFxmlElements.IPv4IPv6Validator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import login.LoginController;
import login.Ts3Client;
import serverFunctions.riotApi.RiotApiInterface;
import serverFunctions.riotApi.RiotApiNotification;
import serverFunctions.webServer.StartWebServer;
import serverFunctions.webServer.servlets.overview.HistoryData;

public class AppMain extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		// Parent root =
		// FXMLLoader.load(getClass().getResource("/MainWindow.fxml")); Start
		// after login
		Pane root = (Pane) FXMLLoader.load(getClass().getResource("/Login.fxml"));

		primaryStage.setScene(new Scene(root));
		primaryStage.setTitle("Ts3 Application");
		primaryStage.show();

		// Terminate Program on Close Window
		primaryStage.setOnCloseRequest(e -> {
			System.exit(0);
			Platform.exit();
		});
	}

	public static void main(String[] args) throws IOException, ParseException {
		launch(args);	
		
//		FileInputOutput.getInstance().checkAndCreateRiotApiPersistence();
		
		
		
		
//		System.out.println("Vlc Opening!");
//        Runtime runTime = Runtime.getRuntime();
//        String []command = {"C:\\Program Files\\VideoLAN\\VLC\\vlc.exe", "C:\\Users\\Johannes\\Desktop\\TeamspeakProject\\Doh.mp3"}; 
//          //Replace the D:\\java... with the location of your file
//        Process process = runTime.exec(command);
//		
//		
//		Runtime.getRuntime().exec("C:\\Program Files\\VideoLAN\\VLC\\vlc.exe");
//		//Process wasd = new ProcessBuilder("C:\\Program Files\\VideoLAN\\VLC\\vlc.exe").start();
//		Process wasd2 = new ProcessBuilder("C:\\Program Files\\Mozilla Firefox\\firefox.exe").start();

//		ExtendedTS3Config config = new ExtendedTS3Config("[::1]");
//		ExtendedTS3Query query = new ExtendedTS3Query(config);
//		ExtendedTS3Api api = new ExtendedTS3Api(query);
//
//		config.setHost("127.0.0.1");
//		config.setHost("[::1]");
//		config.setDebugLevel(Level.ALL);
//
//		// throws exception if no connection could be established
//		query.connect();
//		api.logToCommandline("Connected to Server");
//		// api.login(this.serverQueryName, this.serverQueryPassword);
//		// is true if connect was successful
//
//		api.login("Ai_Overlord_Rudolf", "HndeFy2F", "Zephira", "giwLzOcXMB1fi+DVtV6uSDzCqGo=");
//		
//		api.selectVirtualServerByPort(9987);
//		api.setNickname("Ai_Overlord_Rudolf");
//		api.registerAllEvents();
//		api.sendServerMessage(api.getConnectedConfigValues().getclientName() + "is now online!");
//		api.logToCommandline("Logged in to Server Instance");
//	
//		StartWebServer webServerStart = new StartWebServer(api, 8081, 8082);
//		Thread thread = new Thread(webServerStart);
//		try {
//			thread.start();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		RiotApiInterface riotApi = new RiotApiInterface();
//		RiotApiNotification riotN =new RiotApiNotification(null);
//		riotN.addUser("Lymoon");
//		System.out.println("done"+riotN.addUser("Lymoon"));
		// riotApi.getIdByNickName("", "");
//		try {
//			Process process = new ProcessBuilder("C:\\Program Files\\VideoLAN\\VLC\\vlc.exe","https://www.youtube.com/watch?v=zWmx56kvLUA").start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		ExtendedTS3EventAdapter extendedTS3EventAdapter = new ExtendedTS3EventAdapter(
//				AllExistingEventAdapter.GREETING_MESSAGE) {
//			@Override
//			public void onClientJoin(ClientJoinEvent e) {
//				System.out.println("ClientJoined");
//				System.out.println(e.getInvokerId());
//			}
//		};
//
//		TS3EventAdapter tester = new TS3EventAdapter() {
//		};
//		
//		Ts3Client test = new Ts3Client();
//
//		// test.startTs3Client("C:/Program Files/TeamSpeak
//		// 3Client/ts3client_win64.exe");
//
//		String uId = "giwLzOcXMB1fi+DVtV6uSDzCqGo=";
//		TS3Config config = new TS3Config();
//		TS3Query query = new TS3Query(config);
//
//		ExtendedTS3Api api = new ExtendedTS3Api(query);
//
//		// TS3Api api = new TS3Api(query);
//		// TS3Api api = query.getApi();
//		// TsConection Settings
//		config.setHost("127.0.0.1");
//		config.setDebugLevel(Level.ALL);
//
//		
//		
//		query.connect();
//
//		api.login("QueryTester", "IRvo65OQ");
//		api.selectVirtualServerByPort(Integer.valueOf(9987));
//		api.setNickname("QueryTester");
//		api.registerAllEvents();
//		api.sendServerMessage("QueryTester is now online!");
//		// api.add(tester);
//		// api.addTS3Listeners(tester);
//		api.addTS3Listeners(extendedTS3EventAdapter);
//		System.out.println(api.getAllTS3Listeners().get(0).getName());
//		
//		api.removeTS3Listeners(AllExistingEventAdapter.GREETING_MESSAGE);
//		System.out.println(api.getAllTS3Listeners().size() + " current size");
//		
//		//System.out.println(api.getClientsByName("Zephira").get(0).getChannelId());
//		//api.removeTS3Listeners(AllExistingEventAdapter.GREETING_MESSAGE);
//
//		System.out.println(api.getClientByUId(uId));

	}
}
