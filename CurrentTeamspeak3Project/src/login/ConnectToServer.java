package login;

import java.util.concurrent.TimeoutException;
import java.util.logging.Level;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

import javafx.concurrent.Task;


public class ConnectToServer extends Task<TS3Api> {
	private String ipAdress;
	private String serverQueryName;
	private String serverQueryPassword;
	private int serverPort;
	
	private static ConnectToServer instance = null;
	
	public static ConnectToServer getInstance(String ipAdress, String serverQueryName, String serverQueryPassword, int serverPort) {
		if (instance == null) {
			instance = new ConnectToServer(ipAdress , serverQueryName , serverQueryPassword , serverPort);
			System.out.println("Started to connect to server");
		}
		else {
			return null;
		}
		return instance;
	}

	private ConnectToServer(String ipAdress, String serverQueryName, String serverQueryPassword, int serverPort) {
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
		System.out.println("LOG: call started");
		TS3Config config = new TS3Config();
		TS3Query query = new TS3Query(config);
		TS3Api api = query.getApi();

		config.setHost(this.ipAdress);
		config.setDebugLevel(Level.ALL);
		try{
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
		} catch (Exception e){
			throw new Exception();
		} finally {
			this.instance = null;
		}
	} 
}
