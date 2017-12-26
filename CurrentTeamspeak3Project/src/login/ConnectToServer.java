package login;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

import clientControllers.MainWindowController;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import serverControllers.*;

public class ConnectToServer extends Task<TS3Api> {
	private String ipAdress;
	private String serverQueryName;
	private String serverQueryPassword;
	private int serverPort;
	private String userName;
	private String uniqueId;
	
	public ConnectToServer(String ipAdress, String serverQueryName, String serverQueryPassword, int serverPort,
			AnchorPane rootPane, boolean connectAsServerSide) {
		this.ipAdress = ipAdress;
		this.serverQueryName = serverQueryName;
		this.serverQueryPassword = serverQueryPassword;
		this.serverPort = serverPort;

		if (connectAsServerSide) {
			this.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
				@Override
				public void handle(WorkerStateEvent event) {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(getClass().getResource("/ServerMainWindow.fxml"));
					try {
						loader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
					ServerMainWindowController serverMainWindowController = loader.getController();

					try {
						serverMainWindowController.setApi(get());
					} catch (InterruptedException | ExecutionException e1) {
						e1.printStackTrace();
					}

					
					serverMainWindowController.setIpAdress(ipAdress);
					serverMainWindowController.setServerPort(serverPort);
					
					Parent p = loader.getRoot();
					rootPane.getChildren().setAll(p);
					rootPane.getScene().getWindow().sizeToScene();
				}
			});
		} else {
			this.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
	            @Override
	            public void handle(WorkerStateEvent event) {
	            	try {
						if(get().getConnectionInfo() == null){
//							TODO
//							infoBox.setText("Could not connect to Server");
//							serverLoginButton.setGraphic(null);
							return;
						}
					} catch (InterruptedException | ExecutionException e1) {
						e1.printStackTrace();
					} 
	            	
	            	FXMLLoader Loader = new FXMLLoader();
	        		Loader.setLocation(getClass().getResource("/MainWindow.fxml"));
	        		try {
						Loader.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
	        		MainWindowController mainWindowController = Loader.getController();
	        		
	        		// Pass Data to new Controller
	        		mainWindowController.setIpAdress(ipAdress);
	        		mainWindowController.setServerPort(Integer.toString(serverPort));
	        		mainWindowController.setServerQueryName(serverQueryName);
	        		mainWindowController.setServerQueryPassword(serverQueryPassword);
	        		mainWindowController.setInfoBoxText("Succesfully connected to server: " + ipAdress + ":" + serverPort);
	        		mainWindowController.setUserName(userName);
	        		mainWindowController.setUId(uniqueId);
	        		try {
						mainWindowController.setApi(get());
					} catch (InterruptedException | ExecutionException e1) {
						e1.printStackTrace();
					} 
	        		
	        		//Load the next FXML File in the same Window
	        		Parent p = Loader.getRoot();
	        		
	        		rootPane.getChildren().setAll(p);
	        		rootPane.getScene().getWindow().sizeToScene();
	        		
	        		// New Window
//	        		 Parent p = Loader.getRoot();
//	        		 Stage stage = new Stage();
//	        		 stage.setScene(new Scene(p));
//	        		 stage.show();
	            }
	        });
		}
	}

	@Override
	public TS3Api call() throws Exception {
		TS3Config config = new TS3Config();
		TS3Query query = new TS3Query(config);
		TS3Api api = query.getApi();

		config.setHost(this.ipAdress);
		config.setDebugLevel(Level.ALL);

		query.connect();

		api.login(this.serverQueryName, this.serverQueryPassword);
		api.selectVirtualServerByPort(this.serverPort);
		api.setNickname(this.serverQueryName);
		api.registerAllEvents();
		api.sendServerMessage("QueryTester is now online!");
		return api;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
}
