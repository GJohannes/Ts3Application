package login;

import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;

import javafx.concurrent.Task;
import miscellaneous.ExtendedTS3Api;


public class ConnectToServer extends Task<ExtendedTS3Api> {
	private String ipAdress;
	private String serverQueryName;
	private String serverQueryPassword;
	private int serverPort;
	private Logger logger = Logger.getLogger("connectToServerLogger");
	
	
	private static ConnectToServer instance = null;
	
	/*
	 * returns a serverConectionClass if none already exists
	 * can start a new thread that connects to teamspeakServer
	 */
	public static ConnectToServer getInstance(String ipAdress, String serverQueryName, String serverQueryPassword, int serverPort) {
		if (instance == null) {
			instance = new ConnectToServer(ipAdress , serverQueryName , serverQueryPassword , serverPort);
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
	public ExtendedTS3Api call() throws Exception {
		logger.log(Level.INFO, "Started building connection to server");
		TS3Config config = new TS3Config();
		TS3Query query = new TS3Query(config);
		
		ExtendedTS3Api api = new ExtendedTS3Api(query);
		//OLD TS3Api api = query.getApi();

		config.setHost(this.ipAdress);
		config.setDebugLevel(Level.ALL);
		try{
			// throws exception if no connection could be established 
			query.connect();
			//is true if connect was successful 
			if(api.login(this.serverQueryName, this.serverQueryPassword)){
				api.selectVirtualServerByPort(this.serverPort);
				api.setNickname(this.serverQueryName);
				api.registerAllEvents();
				api.sendServerMessage("QueryTester is now online!");
				// exception is thrown if no connection could be established and
				//therefore a null pointer gets to this information
				api.getConnectionInfo();
				
				return api;
			//connect was not successful
			} else {
				throw new Exception();
			}
		} catch (Exception e){
			throw new Exception();
		} finally {
			this.instance = null;
		}
	} 
}
