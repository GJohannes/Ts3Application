package InputOutput;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import serverFunctions.riotApi.DataObjects.RiotApiUser;

public class RiotApiIO {

	private static RiotApiIO instance = null;
	private final String persistentFolderName = DefinedStringsRiotApiIO.riotApiPersistencDataFolderName.getValue();
	private final String persistentFileName = DefinedStringsRiotApiIO.riotApiPersistencDataFileName.getValue();
	private final String nickNameKey = DefinedStringsRiotApiIO.riotApiJSONKeyNickName.getValue();
	private final String averageKDAKey = DefinedStringsRiotApiIO.riotApiJSONKeyAverageKDA.getValue();
	private final String numberOfGamesPlayedKey = DefinedStringsRiotApiIO.riotApiJSONKeyNumberOfGamesPlayed.getValue();
	private final String repeatetCheckKey = DefinedStringsRiotApiIO.riotApiJSONKeyIsPartOfRepeatedApiCheck.getValue();
	private final String encryptedAccountIdKey = DefinedStringsRiotApiIO.riotApiJSONKeyEncryptedAccoutID.getValue();
	private final String userFirstCreatedKey = DefinedStringsRiotApiIO.riotApiJSONKeyUserFirstCreated.getValue();
	private final String playerUuidKey = DefinedStringsRiotApiIO.riotApiJSONKeyPlayerUuid.getValue();
	private final String lastGameIdKey = DefinedStringsRiotApiIO.riotApiJSONKeyLastGameId.getValue();
	
	private RiotApiIO() {
	}

	public static RiotApiIO getInstance() {
		if (instance == null) {
			instance = new RiotApiIO();
		}
		return instance;
	}
	
	/**
	 * 
	 * @param user
	 * @return true if user was already stored before, false if a new user was set on the HDD
	 */
	public boolean updateUserPersistance(RiotApiUser user) {
		ArrayList<RiotApiUser> readRiotApiPersitantUsers = readAllRiotApiPersitantUsers();
		boolean riotApiUserWasStoredOnHDDAlready;
		if(this.isRiotApiUserAlreadyStoredPersistantlyLogic(user)) {
			riotApiUserWasStoredOnHDDAlready = true;
			Iterator<RiotApiUser> iterator = readRiotApiPersitantUsers.iterator();
			while (iterator.hasNext()) {
				RiotApiUser riotApiPersitentObject = iterator.next();
				if (riotApiPersitentObject.getEncryptedAccountId().equals(user.getEncryptedAccountId())) {
					iterator.remove();
					readRiotApiPersitantUsers.add(user);
					break;
				}
			}
		} else {
			riotApiUserWasStoredOnHDDAlready = false;
			readRiotApiPersitantUsers.add(user);			
		}
		
		this.writeRiotApiPersistanceToHardDrive(readRiotApiPersitantUsers);
		return riotApiUserWasStoredOnHDDAlready;
	}
	
	private boolean isRiotApiUserAlreadyStoredPersistantlyLogic(RiotApiUser user) {
		ArrayList<RiotApiUser> readRiotApiPersitance = readAllRiotApiPersitantUsers();
		Iterator<RiotApiUser> iterator = readRiotApiPersitance.iterator();
		while (iterator.hasNext()) {
			RiotApiUser riotApiPersitentObject = iterator.next();
			if (riotApiPersitentObject.getEncryptedAccountId().equals(user.getEncryptedAccountId())) {
				return true;
			}
		}
		return false;
	}
	
	

	public void checkAndCreateRiotApiPersistenceFileSystemStructure() {
		File dirctory = new File(this.persistentFolderName);
		if (!dirctory.exists()) {
			dirctory.mkdirs();
		}

		File persistentDataFile = new File(this.persistentFolderName +"/"+this.persistentFileName);
		if (!persistentDataFile.exists()) {
			try {
				FileWriter writer = new FileWriter(persistentDataFile);
				JSONArray jsonArray = new JSONArray();
				writer.write(jsonArray.toJSONString());
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * !Overwrites existing information. be careful with this method!
	 * 
	 * @param allPersistentUsers are going to be stored on the HDD
	 */
	private synchronized void writeRiotApiPersistanceToHardDrive(ArrayList<RiotApiUser> allPersistentUsers) {
		this.checkAndCreateRiotApiPersistenceFileSystemStructure();
		File persistentDataFile = new File(this.persistentFolderName +"/"+this.persistentFileName);
		JSONArray jsonArray = new JSONArray();
		for (RiotApiUser riotApiPersitentObject : allPersistentUsers) {
			jsonArray.add(riotApiPersitentObject.toJSONObject());
		}

		try {
			FileWriter writer = new FileWriter(persistentDataFile);
			writer.write(jsonArray.toJSONString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<RiotApiUser> readAllRiotApiPersitantUsers() {
		this.checkAndCreateRiotApiPersistenceFileSystemStructure();
		ArrayList<RiotApiUser> allRiotApiPersistenObjects = new ArrayList<>();
		try {
			File persistentDataFile = new File(this.persistentFolderName +"/"+this.persistentFileName);
			FileReader reader = new FileReader(persistentDataFile);
			JSONParser parser = new JSONParser();
			JSONArray allUsers = (JSONArray) parser.parse(reader);
			for (int i = 0; i < allUsers.size(); i++) {
				JSONObject singleUser = (JSONObject) allUsers.get(i);
				
				String caseCorrectNickName = (String) singleUser.get(this.nickNameKey);
				double averageKDA = (double) singleUser.get(this.averageKDAKey);
				long numberOfGamesPlayed = (long) singleUser.get(this.numberOfGamesPlayedKey);
				boolean isPartOfRepeatedApiCheck = (boolean) singleUser.get(this.repeatetCheckKey);
				String encryptedAccountID = (String) singleUser.get(this.encryptedAccountIdKey);
				long  userFirstCreated = (long) singleUser.get(this.userFirstCreatedKey);
				String playerUuid = (String) singleUser.get(this.playerUuidKey);
				String lastGameId = (String) singleUser.get(this.lastGameIdKey);
				
				RiotApiUser user = new RiotApiUser(encryptedAccountID, caseCorrectNickName, lastGameId, averageKDA, numberOfGamesPlayed, isPartOfRepeatedApiCheck, userFirstCreated, playerUuid);
				allRiotApiPersistenObjects.add(user);
			}
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return allRiotApiPersistenObjects;
	}
	
}
