package serverFunctions.riotApi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;

public class RiotApiNotification implements Runnable {
	private ExtendedTS3Api api;
	private boolean threadRunningFlag = true;
	ArrayList<RiotApiUser> userList = new ArrayList<>();
	RiotApiInterface riotInterface = new RiotApiInterface();
	String apiKey = "RGAPI-4c46e0b2-8cce-4a8c-821d-fe4c9abc41cd";

	public RiotApiNotification(ExtendedTS3Api api) {
		this.api = api;
	}

	public void setApiKey(String key) {
		this.apiKey = key;
	}

	// should get message of add and nickname to add
	private void splittStringAndAddUser(String message) throws IOException, ParseException {
		String nickName = message.split(" ")[1];
		this.addUser(nickName);
	}

	private boolean splittStringAndRemoveNickName(String message) {
		String nickName = message.split(" ")[1];
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getAccountName().toLowerCase().equals(nickName.toLowerCase())) {
				userList.remove(i);
				return true;
			}
		}
		// case could not find user
		return false;
	}

	
	private void splittStringAndUpdateKey(String message) {
		String key = message.split(" ")[1];
		this.apiKey = key;
	}
	
	private void showAllRegisteredToClient(ExtendedTS3Api api, TextMessageEvent e) {
		String allUsers = "";
		if (userList.size() == 0) {
			allUsers = "No users currently added";
		}
		for (int i = 0; i < userList.size(); i++) {
			allUsers = allUsers + userList.get(i).getAccountName() + " ;";
		}
		api.sendPrivateMessage(e.getInvokerId(), allUsers);
	}

	private void showHelp(ExtendedTS3Api api, TextMessageEvent e) {
		api.sendPrivateMessage(e.getInvokerId(), " "
				+ "\n ?help" 
				+ "\n ?add \"League_Of_Legends_Summoner_Name\""
				+ "\n ?remove \"League_Of_Legends_Summoner_Name\"" 
				+ "\n ?show");
	}

	private void addUser(String nickName) throws IOException, ParseException {
		long accountId = riotInterface.getIdByNickName(nickName, apiKey);
		long lastGameId = riotInterface.getLastGameIdByAccId(accountId, apiKey);
		RiotApiUser newUser = new RiotApiUser(accountId, nickName, lastGameId);
		userList.add(newUser);
	}

	private ExtendedTS3EventAdapter getRiotApi(ExtendedTS3Api api) {
		ExtendedTS3EventAdapter riotApiAdapter = new ExtendedTS3EventAdapter(AllExistingEventAdapter.RIOT_API) {
			@Override
			public void onTextMessage(TextMessageEvent messageToBotEvent) {
				String message = messageToBotEvent.getMessage();

				if (message.startsWith("?")) {
					message = message.substring(1);
					if (message.toLowerCase().startsWith("add")) {
						try {
							splittStringAndAddUser(message);
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Successfully added");
						} catch (FileNotFoundException w) {
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
									"Could not find User, check spelling");
						} catch (IOException e) {
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
									"Could not add user because API Key expired");
						} catch (ParseException e) {
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
									"Internal Error - Should not happen");
						}
					} else if (message.toLowerCase().startsWith("remove")) {
						if (splittStringAndRemoveNickName(message)) {
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Successful removed user");
						} else {
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
									"Could not remove user, check spelling ");
						}
					} else if (message.toLowerCase().startsWith("show")) {
						showAllRegisteredToClient(api, messageToBotEvent);
					} else if (message.toLowerCase().startsWith("help")) {
						showHelp(api, messageToBotEvent);
					} else if (message.toLowerCase().startsWith("key")) {
						splittStringAndUpdateKey(message);
						api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
								"Successful updated Key");
					}
				}
			}

			@Override
			public void onClientJoin(ClientJoinEvent e) {
				api.sendPrivateMessage(e.getClientId(),
						"League of Legends interface is Online - Type ?help for information");
			}
		};
		return riotApiAdapter;
	}

	public void exit() {
		api.removeTS3Listeners(AllExistingEventAdapter.RIOT_API);
		this.threadRunningFlag = false;
	}

	@Override
	public void run() {
		api.addTS3Listeners(this.getRiotApi(api));
		this.activeLogic();
	}

	private void activeLogic() {
		while (threadRunningFlag) {
			for (RiotApiUser user : userList) {
				try {
					long newGameId = riotInterface.getLastGameIdByAccId(user.getAccountId(), apiKey);
					if (newGameId != user.getLastGameId()) {
						user.setLastGameId(newGameId);
						boolean win = riotInterface.getWinFromGameId(user.getLastGameId(), apiKey,
								user.getAccountName());

						if (win) {
							api.sendServerMessage(user.getAccountName() + " won his last game");
						} else {
							api.sendServerMessage(user.getAccountName() + " lost his last game");
						}
					}
					Thread.sleep(1000);
				} catch (IOException | ParseException | InterruptedException e) {
					api.logToCommandline("Internal Error - Check Key expiration date");
				}

			}
			api.logToCommandline("Thread for Riot api is still running");

			System.out.println(userList.size());

			try {
				Thread.sleep(60000 - userList.size() * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
