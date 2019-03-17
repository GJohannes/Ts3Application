package serverFunctions.riotApi.DataObjects;

import org.json.simple.JSONObject;

import InputOutput.DefinedStringsRiotApiIO;

public class RiotApiUser {


	private String encryptedAccountId; //stored on hdd
	private String caseCorrectNickName; //stored on hdd
	private long lastGameId;
	
	private double averageKDA; //stored on hdd
	private long numberOfGamesPlayed; //stored on hdd
	private boolean isPartOfRepeatedApiCheck; //stored on hdd
	private long userFirstCreated;
	
	private final String nickNameKey = DefinedStringsRiotApiIO.riotApiJSONKeyNickName.getValue();
	private final String averageKDAKey = DefinedStringsRiotApiIO.riotApiJSONKeyAverageKDA.getValue();
	private final String numberOfGamesPlayedKey = DefinedStringsRiotApiIO.riotApiJSONKeyNumberOfGamesPlayed.getValue();
	private final String repeatetCheckKey = DefinedStringsRiotApiIO.riotApiJSONKeyIsPartOfRepeatedApiCheck.getValue();
	private final String encryptedAccountIdKey = DefinedStringsRiotApiIO.riotApiJSONKeyEncryptedAccoutID.getValue();
	private final String userFirstCreatedKey = DefinedStringsRiotApiIO.riotApiJSONKeyUserFirstCreated.getValue();
	

	public RiotApiUser(String encryptedAccountId, String caseCorrectNickName, long lastGameId, double averageKDA, long numberOfGamesPlayed, boolean isPartOfRepeatedApiCheck, long userFirstCreated) {
		this.encryptedAccountId = encryptedAccountId;
		this.caseCorrectNickName = caseCorrectNickName;
		this.lastGameId = lastGameId;
		this.averageKDA = averageKDA;
		this.numberOfGamesPlayed = numberOfGamesPlayed;
		this.isPartOfRepeatedApiCheck = isPartOfRepeatedApiCheck;
		this.userFirstCreated = userFirstCreated;
	}

	public void setLastGameId(long gameId) {
		this.lastGameId = gameId;
	}

	public long getLastGameId() {
		return this.lastGameId;
	}

	public String getCaseCorrectNickName() {
		return this.caseCorrectNickName;
	}

	public String getEncryptedAccountId() {
		return this.encryptedAccountId;
	}
	
	public double getAverageKDA() {
		return averageKDA;
	}

	public void setAverageKDA(double averageKDA) {
		this.averageKDA = averageKDA;
	}

	public long getNumberOfGamesPlayed() {
		return numberOfGamesPlayed;
	}

	public void incrementNumberOfGamesPlayed() {
		this.numberOfGamesPlayed++;
	}

	public boolean isPartOfRepeatedApiCheck() {
		return isPartOfRepeatedApiCheck;
	}

	public void setPartOfRepeatedApiCheck(boolean isPartOfRepeatedApiCheck) {
		this.isPartOfRepeatedApiCheck = isPartOfRepeatedApiCheck;
	}
	
	public JSONObject toJSONObject() {
		JSONObject object = new JSONObject();
		object.put(this.nickNameKey, this.caseCorrectNickName);
		object.put(this.averageKDAKey, this.averageKDA);
		object.put(this.numberOfGamesPlayedKey, this.numberOfGamesPlayed);
		object.put(this.repeatetCheckKey, this.isPartOfRepeatedApiCheck);
		object.put(this.encryptedAccountIdKey, this.encryptedAccountId);
		object.put(this.userFirstCreatedKey, this.userFirstCreated);
		return object;		
	}
}
