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

import serverFunctions.riotApi.DataObjects.RiotApiPersitentUserInformation;

public class RiotApiIO {

	private static RiotApiIO instance = null;
	private final String persistentFolderName = DefinedStringsRiotApiIO.riotApiPersistencDataFolderName.getValue();
	private final String persistentFileName = DefinedStringsRiotApiIO.riotApiPersistencDataFileName.getValue();
	private final String nickNameKey = DefinedStringsRiotApiIO.riotApiJSONKeyNickName.getValue();
	private final String averageKDAKey = DefinedStringsRiotApiIO.riotApiJSONKeyAverageKDA.getValue();
	private final String numberOfGamesPlayedKey = DefinedStringsRiotApiIO.riotApiJSONKeyNumberOfGamesPlayed.getValue();
	private final String repeatetCheckKey = DefinedStringsRiotApiIO.riotApiJSONKeyIsPartOfRepeatedApiCheck.getValue();
	private final String encryptedAccountIdKey = DefinedStringsRiotApiIO.riotApiJSONKeyEncryptedAccoutID.getValue();
	
	
	private RiotApiIO() {
	}

	public static RiotApiIO getInstance() {
		if (instance == null) {
			instance = new RiotApiIO();
		}
		return instance;
	}
	
	
	public void updateRiotApiPersistance(RiotApiPersitentUserInformation persistenObject) {
		ArrayList<RiotApiPersitentUserInformation> readRiotApiPersitance = readRiotApiPersitance();
		boolean creatNewPersistentUserInformation = true;

		// case that user already is logged
		Iterator<RiotApiPersitentUserInformation> iterator = readRiotApiPersitance.iterator();
		while (iterator.hasNext()) {
			RiotApiPersitentUserInformation riotApiPersitentObject = iterator.next();
			if (riotApiPersitentObject.getEncryptedAccountId().equals(persistenObject.getEncryptedAccountId())) {
				iterator.remove();
				readRiotApiPersitance.add(persistenObject);
				creatNewPersistentUserInformation = false;
				break;
			}
		}

		if (creatNewPersistentUserInformation) {
			readRiotApiPersitance.add(persistenObject);
		}
		this.writeRiotApiPersistanceToHardDrive(readRiotApiPersitance);
	}

	public void checkAndCreateRiotApiPersistence() {
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

	private void writeRiotApiPersistanceToHardDrive(ArrayList<RiotApiPersitentUserInformation> allPersistentObjects) {
		this.checkAndCreateRiotApiPersistence();
		File persistentDataFile = new File(this.persistentFolderName +"/"+this.persistentFileName);
		JSONArray jsonArray = new JSONArray();
		for (RiotApiPersitentUserInformation riotApiPersitentObject : allPersistentObjects) {
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

	public ArrayList<RiotApiPersitentUserInformation> readRiotApiPersitance() {
		this.checkAndCreateRiotApiPersistence();
		ArrayList<RiotApiPersitentUserInformation> allRiotApiPersistenObjects = new ArrayList<>();
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
				
				RiotApiPersitentUserInformation singleUserAsPersitenObject = new RiotApiPersitentUserInformation(caseCorrectNickName, averageKDA, numberOfGamesPlayed, isPartOfRepeatedApiCheck,encryptedAccountID);
				allRiotApiPersistenObjects.add(singleUserAsPersitenObject);
			}
			reader.close();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return allRiotApiPersistenObjects;
	}
	
}
