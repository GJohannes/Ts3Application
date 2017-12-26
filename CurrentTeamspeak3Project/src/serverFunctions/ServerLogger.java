package serverFunctions;

import java.util.ArrayList;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

public class ServerLogger {
	private static ServerLogger instance = null;
	private TS3Api api;
	// api only sends id of leaving persons. Therefore data that should be
	// logged must be held until leave event is triggered in this list
	private ArrayList<UserLoggedInEntity> usersLoggedIn = new ArrayList<>();
	//holding adapters to still have them around should they be removed 
	private static TS3EventAdapter logOnJoin;
	private static TS3EventAdapter logOnLeave;

	public static ServerLogger getInstance(TS3Api api) {
		if (instance == null) {
			instance = new ServerLogger(api);
			instance.api = api;
			System.out.println("Now logging Join/Leav on server");
		}
		return instance;
	}

	// initialize so that all currently logged in people start to be logged
	private ServerLogger(TS3Api api) {
		ArrayList<Client> allClientsWhileStartingtoLogg = new ArrayList<Client>();
		allClientsWhileStartingtoLogg = (ArrayList<Client>) api.getClients();

		for (int i = 0; i < allClientsWhileStartingtoLogg.size(); i++) {
			UserLoggedInEntity userAlreadyOnline = new UserLoggedInEntity();

			userAlreadyOnline.setId(allClientsWhileStartingtoLogg.get(i).getId());
			userAlreadyOnline.setNickname(allClientsWhileStartingtoLogg.get(i).getNickname());
			userAlreadyOnline.setuId(allClientsWhileStartingtoLogg.get(i).getUniqueIdentifier());
			userAlreadyOnline.logUser(LoggedServerEvents.STARTED_LOG);

			usersLoggedIn.add(userAlreadyOnline);
		}
	}

	public void startServerLogging() {
		if (logOnJoin == null) {
			logOnJoin = logOnClientJoinedServer();
			api.addTS3Listeners(logOnJoin);
		}
		if (logOnLeave == null) {
			logOnLeave = logOnClientLeaveServer();
			api.addTS3Listeners(logOnLeave);
		}
	}

	private TS3EventAdapter logOnClientJoinedServer() {
		TS3EventAdapter logOnClientJoinedAdapter = new TS3EventAdapter() {
			@Override
			public void onClientJoin(ClientJoinEvent e) {
				UserLoggedInEntity newUser = new UserLoggedInEntity();
				newUser.setId(e.getClientId());
				newUser.setNickname(e.getClientNickname());
				newUser.setuId(e.getUniqueClientIdentifier());

				newUser.logUser(LoggedServerEvents.JOIN_SERVER);

				usersLoggedIn.add(newUser);
			}
		};
		return logOnClientJoinedAdapter;
	}

	private TS3EventAdapter logOnClientLeaveServer() {
		TS3EventAdapter logOnClientLeaveAdapter = new TS3EventAdapter() {
			@Override
			public void onClientLeave(ClientLeaveEvent e) {
				for (int i = 0; i < usersLoggedIn.size(); i++) {
					if (usersLoggedIn.get(i).getId() == e.getClientId()) {
						usersLoggedIn.get(i).logUser(LoggedServerEvents.LEFT_SERVER);
						usersLoggedIn.remove(i);
						break;
					}
				}
			}
		};
		return logOnClientLeaveAdapter;
	}
}