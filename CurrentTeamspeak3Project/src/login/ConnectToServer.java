package login;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import org.omg.Messaging.SyncScopeHelper;
import org.omg.PortableServer.ThreadPolicyOperations;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

import clientControllers.MainWindowController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import serverControllers.*;
import serverFunctions.ServerLogger;

public class ConnectToServer extends Task<TS3Api> {
	private String ipAdress;
	private String serverQueryName;
	private String serverQueryPassword;
	private int serverPort;
	
//	private static ConnectToServer instance = null;
//	
//	public static ConnectToServer getInstance(String ipAdress, String serverQueryName, String serverQueryPassword, int serverPort) {
//		if (instance == null) {
//			instance = new ConnectToServer(ipAdress , serverQueryName , serverQueryPassword , serverPort);
//			System.out.println("Started to connect to server");
//		}
//		else {
//			return null;
//		}
//		return instance;
//	}
	
	public ConnectToServer(String ipAdress, String serverQueryName, String serverQueryPassword, int serverPort) {
		this.ipAdress = ipAdress;
		this.serverQueryName = serverQueryName;
		this.serverQueryPassword = serverQueryPassword;
		this.serverPort = serverPort;

//		// moved to controller instead 
//		this.setOnFailed(e -> {
//			System.out.println("failed");
//		});
	}

	@Override
	public TS3Api call() throws Exception {
		System.out.println("call started");
		TS3Config config = new TS3Config();
		TS3Query query = new TS3Query(config);
		TS3Api api = query.getApi();

		config.setHost(this.ipAdress);
		config.setDebugLevel(Level.ALL);

		try {
			query.connect();
		} catch (Exception e) {
			throw new TimeoutException();
		}
		
		//if true connect was successful
		if(api.login(this.serverQueryName, this.serverQueryPassword)){
			api.selectVirtualServerByPort(this.serverPort);
			api.setNickname(this.serverQueryName);
			api.registerAllEvents();
			api.sendServerMessage("QueryTester is now online!");

			try {
				// exception is thrown if no connection could be established and therefore a nullpointer gets to this information
				api.getConnectionInfo();
			} catch (Exception e) {
				System.out.println(e);
				throw new TimeoutException();
			}
			
			return api;
		//connect was not sucesfull
		} else {
			throw new TimeoutException();
		}
	}
}
