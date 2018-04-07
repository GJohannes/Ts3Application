package serverFunctions;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;
import miscellaneous.FileInputOutput;

public class ServerLogger {

	private ExtendedTS3Api api;
	// api only sends id of leaving persons. Therefore data that should be
	// logged must be held until leave event is triggered in this list
	private ArrayList<UserLoggedInEntity> usersLoggedIn = new ArrayList<>();
	

	// initialize so that all currently logged in people start to be logged
	public ServerLogger(ExtendedTS3Api api) {
		this.api = api;
		ArrayList<Client> allClientsWhileStartingtoLogg = new ArrayList<Client>();
		allClientsWhileStartingtoLogg = (ArrayList<Client>) api.getClients();

		for (int i = 0; i < allClientsWhileStartingtoLogg.size(); i++) {
			UserLoggedInEntity userAlreadyOnline = new UserLoggedInEntity();

			userAlreadyOnline.setId(allClientsWhileStartingtoLogg.get(i).getId());
			userAlreadyOnline.setNickname(allClientsWhileStartingtoLogg.get(i).getNickname());
			userAlreadyOnline.setuId(allClientsWhileStartingtoLogg.get(i).getUniqueIdentifier());
			userAlreadyOnline.logUser(LoggedServerEvents.STARTED_LOG);
			userAlreadyOnline.setTimeUserJoinedTheServer(LocalDateTime.now());

			usersLoggedIn.add(userAlreadyOnline);
		}
	}

	public void startServerLogging() {
		api.addTS3Listeners(logOnClientJoinedServer());
		api.addTS3Listeners(logOnClientLeaveServer());
		api.addTS3Listeners(greetingMessage());
	}
	
	public void stopServerLogging() {
		api.removeTS3Listeners(AllExistingEventAdapter.LOG_ON_JOIN);
		api.removeTS3Listeners(AllExistingEventAdapter.LOG_ON_LEAVE);
		api.removeTS3Listeners(AllExistingEventAdapter.GREETING_MESSAGE);
	}

	private ExtendedTS3EventAdapter greetingMessage() {
		ExtendedTS3EventAdapter greetinMessageEvent = new ExtendedTS3EventAdapter(AllExistingEventAdapter.GREETING_MESSAGE) {
			@Override
			public void onClientJoin(ClientJoinEvent e) {
				api.sendPrivateMessage(e.getClientId(), getWelcomeMessage(e));
				//api.sendPrivateMessage(e.getClientId(), "... also Fuck off");
			}
		};
		return greetinMessageEvent;
	}

	private ExtendedTS3EventAdapter logOnClientJoinedServer() {		
		ExtendedTS3EventAdapter logOnClientJoinedAdapter = new ExtendedTS3EventAdapter(AllExistingEventAdapter.LOG_ON_JOIN) {
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

	private ExtendedTS3EventAdapter logOnClientLeaveServer() {
		ExtendedTS3EventAdapter logOnClientLeaveAdapter = new ExtendedTS3EventAdapter(AllExistingEventAdapter.LOG_ON_LEAVE) {
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
				return  " \n Welcome " +  e.getClientNickname() + " you already spent \n "+ days + "-Days " + hours + "-Hours " + minutes + "-Minutes and " + seconds + "-Seconds \n of your lifetime on this Server. SAD!";
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		return "Internal IO Server Error";
	}
}
