package login;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.concurrent.Task;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3Config;
import miscellaneous.ExtendedTS3Query;

public class ConnectToServer extends Task<ExtendedTS3Api> {
	private String ipAdress;
	private String serverQueryName;
	private String serverQueryPassword;
	private String clientName;
	private String clientUniqueID;
	private int serverPort;

	private static ConnectToServer instance = null;

	/*
	 * returns a serverConectionClass if none already exists can start a new thread
	 * that connects to teamspeakServer
	 */
	public static ConnectToServer getInstance(String ipAdress, String serverQueryName, String serverQueryPassword,
			int serverPort, String clinetName, String clientUniqueID) {
		if (instance == null) {
			instance = new ConnectToServer(ipAdress, serverQueryName, serverQueryPassword, serverPort, clinetName, clientUniqueID);
		} else {
			return null;
		}
		return instance;
	}

	private ConnectToServer(String ipAdress, String serverQueryName, String serverQueryPassword, int serverPort, String clientName, String clientUniqueID) {
		this.ipAdress = ipAdress;
		this.serverQueryName = serverQueryName;
		this.serverQueryPassword = serverQueryPassword;
		this.serverPort = serverPort;
		this.clientName = clientName;
		this.clientUniqueID = clientUniqueID;
		
		// // moved to controller instead
		// this.setOnFailed(e -> {
		// System.out.println("failed");
		// });
	}

	@Override
	public ExtendedTS3Api call() throws Exception {
		ExtendedTS3Config config = new ExtendedTS3Config(this.ipAdress);
		ExtendedTS3Query query = new ExtendedTS3Query(config);
		ExtendedTS3Api api = new ExtendedTS3Api(query);
		
		config.setHost(this.ipAdress);
		config.setDebugLevel(Level.ALL);
		try {
			// throws exception if no connection could be established
			query.connect();
			api.logToCommandline("Connected to Server");
			//api.login(this.serverQueryName, this.serverQueryPassword);
			// is true if connect was successful
			if (api.login(this.serverQueryName, this.serverQueryPassword, this.clientName, this.clientUniqueID)) {
				if (api.selectVirtualServerByPort(this.serverPort)) {
					api.setNickname(this.serverQueryName);
					api.registerAllEvents();
					api.sendServerMessage(api.getConnectedConfigValues().getclientName() + "is now online!");
					api.logToCommandline("Logged in to Server Instance");
					// exception is thrown if no connection could be established and
					// therefore a null pointer gets to this information
				} else {
					throw new Exception();
				}
				
				return api;
			// connect was not successful
			} else {
				api.logToCommandline("Could not login to Server -- Serverquery name or password have to be wrong");
				throw new Exception();
			}
		} catch (Exception e) {
			api.logToCommandline("Could not connect to server -- Ip adress or port have to be wrong");
			throw new Exception();
		} finally {
			this.instance = null;
		}
	}
}
