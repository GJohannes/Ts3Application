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
	private int serverPort;
	private Logger logger = Logger.getLogger("connectToServerLogger");

	private static ConnectToServer instance = null;

	/*
	 * returns a serverConectionClass if none already exists can start a new thread
	 * that connects to teamspeakServer
	 */
	public static ConnectToServer getInstance(String ipAdress, String serverQueryName, String serverQueryPassword,
			int serverPort) {
		if (instance == null) {
			instance = new ConnectToServer(ipAdress, serverQueryName, serverQueryPassword, serverPort);
		} else {
			return null;
		}
		return instance;
	}

	private ConnectToServer(String ipAdress, String serverQueryName, String serverQueryPassword, int serverPort) {
		this.ipAdress = ipAdress;
		this.serverQueryName = serverQueryName;
		this.serverQueryPassword = serverQueryPassword;
		this.serverPort = serverPort;

		// // moved to controller instead
		// this.setOnFailed(e -> {
		// System.out.println("failed");
		// });
	}

	@Override
	public ExtendedTS3Api call() throws Exception {
		logger.log(Level.INFO, "Started building connection to server");
		ExtendedTS3Config config = new ExtendedTS3Config();
		ExtendedTS3Query query = new ExtendedTS3Query(config);
		ExtendedTS3Api api = new ExtendedTS3Api(query);
		
		config.setHost(this.ipAdress);
		config.setDebugLevel(Level.ALL);
		try {
			// throws exception if no connection could be established
			query.connect();
			api.login(this.serverQueryName, this.serverQueryPassword);
			// is true if connect was successful
			if (api.login(this.serverQueryName, this.serverQueryPassword)) {
				if (api.selectVirtualServerByPort(this.serverPort)) {
					api.setNickname(this.serverQueryName);
					api.registerAllEvents();
					api.sendServerMessage("QueryTester is now online!");
					// exception is thrown if no connection could be established and
					// therefore a null pointer gets to this information
				} else {
					throw new Exception();
				}
				
				return api;
				// connect was not successful
			} else {
				throw new Exception();
			}
		} catch (Exception e) {
			throw new Exception();
		} finally {
			this.instance = null;
		}
	}
}
