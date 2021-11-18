package serverFunctions.riotApi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.jasper.tagplugins.jstl.core.ForEach;
import org.json.simple.parser.ParseException;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;
import miscellaneous.InsultGenerator;
import serverFunctions.riotApi.DataObjects.EncryptedAccountIdAndCaseCorrectNickNameHolder;
import serverFunctions.riotApi.DataObjects.RiotApiUser;
import serverFunctions.riotApi.DataObjects.UserAddedInformation;
import serverFunctions.riotApi.DataObjects.WinKdaMostDamageHolder;

public class RiotApiNotification implements Runnable {
	private ExtendedTS3Api api;
	private boolean threadRunningFlag = true;
	// thread save list in case that add/remove is done while the lookup thread is
	// using the list to to api requests
	private CopyOnWriteArrayList<RiotApiUser> userList = new CopyOnWriteArrayList<RiotApiUser>();
	private RiotApiInterface riotInterface = new RiotApiInterface();
	private String apiKey = "";
	private RiotApiPersistentDataLogic riotApiPersistentData;
	private int timeBetweenRiotApiQueries = 500;
	private int averageLatencyOfOneRiotApiCall = 250;
	private InsultGenerator insultGenerator = new InsultGenerator();

	public RiotApiNotification(ExtendedTS3Api api) {
		this.api = api;
		this.riotApiPersistentData = new RiotApiPersistentDataLogic();
	}

	public void setApiKey(String key) {
		this.apiKey = key;
	}

	// should get message of add and nickname to add
	private UserAddedInformation splittStringAndAddUser(String message) throws IOException, ParseException {
		String nickName = message.split(" ", 2)[1];
		return this.addUser(nickName);
	}

	private boolean splittStringAndRemoveNickName(String message) {
		String nickName = message.split(" ", 2)[1];
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getCaseCorrectNickName().equalsIgnoreCase(nickName)) {
				userList.get(i).setPartOfRepeatedApiCheck(false);
				riotApiPersistentData.updateUserPersistantInformationOnHDD(userList.get(i));
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
			allUsers = allUsers + userList.get(i).getCaseCorrectNickName() + " -- ";
		}
		api.sendPrivateMessage(e.getInvokerId(), allUsers);
	}

	private void showHelp(ExtendedTS3Api api, TextMessageEvent e) {
		api.sendPrivateMessage(e.getInvokerId(), " " + "\n ?help" + "\n ?add \"League_Of_Legends_Summoner_Name\"" + "\n ?remove \"League_Of_Legends_Summoner_Name\"" + "\n ?show");
	}

	/**
	 * 
	 * 
	 * @param nickName
	 * @return true if was added; false if user nickname was already in the list
	 * @throws IOException
	 * @throws ParseException
	 */

	public UserAddedInformation addUser(String nickName) throws IOException, ParseException {
		for (int i = 0; i < userList.size(); i++) {
			if (userList.get(i).getCaseCorrectNickName().equalsIgnoreCase(nickName)) {
				// case that user already added to the list
				return new UserAddedInformation(false);
			}
		}
		EncryptedAccountIdAndCaseCorrectNickNameHolder holder = riotInterface.getIdAndCaseCorrectNickNameByNickName(nickName, apiKey);
		String encryptedAccountId = holder.getEncryptedAccountId();
		String caseCorrectNickName = holder.getCaseCorrectNickName();
		String playerUuid = holder.getPlayerUuid();
		String lastGameId = riotInterface.getLastGameIdByEncryptedAccId(encryptedAccountId,playerUuid, apiKey);

		RiotApiUser newUser;
		UserAddedInformation information;
		if (riotApiPersistentData.userAlreadyStoredOnHDD(encryptedAccountId)) {
			newUser = riotApiPersistentData.getRiotApiUserFromHDD(encryptedAccountId, caseCorrectNickName, playerUuid);
			newUser.setLastGameId(lastGameId);
			newUser.setPartOfRepeatedApiCheck(true);
			information = new UserAddedInformation(true, true);
		} else {
			newUser = new RiotApiUser(encryptedAccountId, caseCorrectNickName, lastGameId, 0.0, 0, true, System.currentTimeMillis(), holder.getPlayerUuid());
			information = new UserAddedInformation(true, false);
		}
		riotApiPersistentData.updateUserPersistantInformationOnHDD(newUser);
		userList.add(newUser);
		return information;
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
							UserAddedInformation information = splittStringAndAddUser(message);
							if (information.isUserWasSuccessfullyAdded()) {
								if (information.isUserWasAddedInThePast()) {
									api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Successfully added, loaded average kda data from the past");
								} else {
									api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Successfully added");
								}
							} else {
								api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "User already registered. Duplicates not allowed");
							}
						} catch (FileNotFoundException w) {
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Could not find User, check spelling");
						} catch (IOException e) {
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Could not add user because API Key expired");
						} catch (ParseException e) {
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Internal Error - Should not happen");
						} catch (Exception e) {
							System.out.println(e);
						}
					} else if (message.toLowerCase().startsWith("remove")) {
						if (splittStringAndRemoveNickName(message)) {
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Successful removed user");
						} else {
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Could not remove user, check spelling ");
						}
					} else if (message.toLowerCase().startsWith("show")) {
						showAllRegisteredToClient(api, messageToBotEvent);
					} else if (message.toLowerCase().startsWith("help")) {
						showHelp(api, messageToBotEvent);
					} else if (message.toLowerCase().startsWith("key")) {
						// if only key then check validity
						if (message.equalsIgnoreCase("key")) {
							try {
								riotInterface.getIdAndCaseCorrectNickNameByNickName("XZephiraX", apiKey);
								api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Key is still valid");
							} catch (IOException | ParseException e) {
								api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Invalid Key - Please Update to proceed");
							}
							// else update the key
						} else {
							splittStringAndUpdateKey(message);
							ArrayList<RiotApiUser> allAddedUsersFromHDD = riotApiPersistentData.getAllRepeatedApiCheckAddedUsersFromHDD();

							api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Successful updated Key");

							try {
								riotInterface.getIdAndCaseCorrectNickNameByNickName("XZephiraX", apiKey);
								api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Key is valid");
							} catch (IOException | ParseException e) {
								api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Key is NOT valid");
								return;
							}

							for (RiotApiUser oneUserToAdd : allAddedUsersFromHDD) {
								try {
									UserAddedInformation userAddedInformation = addUser(oneUserToAdd.getCaseCorrectNickName());
									if (userAddedInformation.isUserWasSuccessfullyAdded()) {
										api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "To the api check was addded: " + oneUserToAdd.getCaseCorrectNickName());
									}
								} catch (IOException | ParseException e) {
									// shuld not happen
									api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Invalid Api Key while trying to add " + oneUserToAdd.getCaseCorrectNickName());
								}

								try {
									Thread.sleep(timeBetweenRiotApiQueries);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					} else {
						api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Syntaxerror - could not read command");
					}
				}
			}

			@Override
			public void onClientJoin(ClientJoinEvent e) {
				api.sendPrivateMessage(e.getClientId(), "League of Legends interface is Online - Type ?help for information");
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
		// TODO Change request. Players save to hdd
		// when added/removed and calculate an average kda and save it to the harddrive
		api.addTS3Listeners(this.getRiotApi(api));
		this.activeLogic();
	}

	private void activeLogic() {
		while (threadRunningFlag) {
			for (RiotApiUser user : userList) {
				try {
					System.out.println("now getting last game id");
					String newGameId = riotInterface.getLastGameIdByEncryptedAccId(user.getEncryptedAccountId(),user.getPlayerUuid(), apiKey);
					System.out.println("last game id " + newGameId);
					System.out.println("!!!" + user.getLastGameId());
					System.out.println("!!!" + newGameId);
					if (!newGameId.equals(user.getLastGameId())) {
						String message = "";

						user.setLastGameId(newGameId);
						WinKdaMostDamageHolder winKdaMostDamageHolder = riotInterface.getWinAndKdaFromGameId(user.getLastGameId(), apiKey, user.getCaseCorrectNickName(),user.getPlayerUuid());
						System.out.println(winKdaMostDamageHolder + "wind and kda holder data ");
						double averageKda = riotApiPersistentData.updateAverageKdaAndGamesPlayedOnHDD(user, winKdaMostDamageHolder);
						System.out.println("done updaze on hdd");
						String averageKdaVisual = String.format("%.2f", averageKda);

						if (winKdaMostDamageHolder.isWin()) {
							message = user.getCaseCorrectNickName() + " just WON in League of Legends ( KDA: " + winKdaMostDamageHolder.getKdaVisual() + "; Ø: " + averageKdaVisual
									+ " )";
						} else {
							message = user.getCaseCorrectNickName() + " just LOST in League of Legends. What a " + insultGenerator.getRandomInsult().toLowerCase() + " ( KDA: "
									+ winKdaMostDamageHolder.getKdaVisual() + "; Ø: " + averageKdaVisual + " )";
						}
						if (winKdaMostDamageHolder.isHighestDamageDealer()) {
							message = message + " ( Most damage )";
						}

						api.sendServerMessage(message);
					}
				} catch (IOException | ParseException e) {
					api.logToCommandline("Internal Error - Check Key expiration date");
				}
				try {
					Thread.sleep(this.timeBetweenRiotApiQueries);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			api.logToCommandline("Thread for Riot api is still running");

			try {
				// check all 60 sec - sleep time - approximated time for all server query
				// requests
				int threadSleepTime = 20000 - (userList.size() * this.timeBetweenRiotApiQueries) - (averageLatencyOfOneRiotApiCall * userList.size());
				// preventing negative thread sleep time
				if (threadSleepTime < 0) {
					threadSleepTime = 0;
				}
				Thread.sleep(threadSleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
