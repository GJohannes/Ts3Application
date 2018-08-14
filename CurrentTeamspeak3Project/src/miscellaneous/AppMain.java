package miscellaneous;

import java.io.IOException;
import java.net.URI;
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

import org.json.simple.parser.ParseException;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;

import java.util.logging.Level;



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
import serverFunctions.riotApi.riotApiData;

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
		//launch(args);
		
		
		riotApiData riotApi = new riotApiData();
		riotApi.getWinFromGameId(3730321220L, "", "");
		
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
