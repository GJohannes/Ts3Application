package serverFunctions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import miscellaneous.FileInputOutput;

public class ServerLogger {
	private static ServerLogger instance = null;
	private TS3Api api;
	// api only sends id of leaving persons. Therefore data that should be
	// logged must be held until leave event is triggered in this list
	private ArrayList<UserLoggedInEntity> usersLoggedIn = new ArrayList<>();
	// holding adapters to still have them around should they be removed
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
		api.addTS3Listeners(greetingMessage());
	}

	private TS3EventAdapter greetingMessage() {
		TS3EventAdapter greetinMessageEvent = new TS3EventAdapter() {
			@Override
			public void onClientJoin(ClientJoinEvent e) {
				api.sendPrivateMessage(e.getClientId(), getWelcomeMessage(e));
				api.sendPrivateMessage(e.getClientId(), "... also Fuck off");
			}
		};
		return greetinMessageEvent;
	}

	private TS3EventAdapter logOnClientJoinedServer() {
		TS3EventAdapter logOnClientJoinedAdapter = new TS3EventAdapter() {
			@Override
			public void onClientJoin(ClientJoinEvent e) {
				UserLoggedInEntity newUser = new UserLoggedInEntity();
				newUser.setId(e.getClientId());
				newUser.setNickname(e.getClientNickname());
				newUser.setuId(e.getUniqueClientIdentifier());
				newUser.setTimeUserJoinedTheServer(LocalDateTime.now());

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
						LogTimeOnServer logTimeOnServer = new LogTimeOnServer();
						logTimeOnServer.updateAbsoluteTimeOnServer(usersLoggedIn.get(i).getTimeUserJoinedTheServer(),
								usersLoggedIn.get(i).getuId());
						usersLoggedIn.get(i).logUser(LoggedServerEvents.LEFT_SERVER);

						usersLoggedIn.remove(i);
						break;
					}
				}
			}
		};
		return logOnClientLeaveAdapter;
	}
	
	private String getWelcomeMessage(ClientJoinEvent e){
		FileInputOutput inOut = new FileInputOutput();
		try {
			long timeOnServer = inOut.readTimeOnServer(e.getUniqueClientIdentifier());
			if (timeOnServer == -1) {
				return "Welcome " + e.getClientNickname() +  " this is your first time on this Server. ";
			} else {
				
				long totalSecs = timeOnServer;
				long seconds = 0;
				long minutes = 0;
				long hours = 0;
				long days = 0;
				
				days = (totalSecs / 3600) / 24;
				hours = (totalSecs / 3600) % 24;
				minutes = (totalSecs % 3600) / 60;
				seconds = totalSecs % 60;
				return  "Welcome " +  e.getClientNickname() + " you already spent \n "+ days + "-Days " + hours + "-Hours " + minutes + "-Minutes and " + seconds + "-Seconds \n of your lifetime on this Server. SAD!";
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return "Internal IO Server Error";
	}
}
