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

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import java.util.logging.Level;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import login.LoginController;
import login.Ts3Client;

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
		launch(args);
		

//		 Ts3Client test = new Ts3Client();
//		
//		// test.startTs3Client("C:/Program Files/TeamSpeak 3Client/ts3client_win64.exe");
//
//		 String uId = "giwLzOcXMB1fi+DVtV6uSDzCqGo=";
//		 TS3Config config = new TS3Config();
//		 TS3Query query = new TS3Query(config);
//		 TS3Api api = query.getApi();
//		 // TsConection Settings
//		 config.setHost("127.0.0.1");
//		 config.setDebugLevel(Level.ALL);
//		
//		 query.connect();
//		
//		 api.login("QueryTester", "cdgT5HY9");
//		 api.selectVirtualServerByPort(Integer.valueOf(9987));
//		 api.setNickname("QueryTester");
//		 api.registerAllEvents();
//		 api.sendServerMessage("QueryTester is now online!");
//		
//		 System.out.println(api.getClientsByName("Zephira").get(0).getChannelId());
//		
//		
//		 System.out.println(api.getClientByUId(uId));
		
		
	}
}
