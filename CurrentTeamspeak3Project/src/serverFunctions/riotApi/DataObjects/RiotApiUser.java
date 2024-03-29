package serverFunctions.riotApi.DataObjects;

import org.json.simple.JSONObject;

import InputOutput.DefinedStringsRiotApiIO;

public class RiotApiUser {


	private String encryptedAccountId; //stored on hdd
	private String caseCorrectNickName; //stored on hdd
	private String lastGameId;
	private String playerUuid;
	
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
	private final String playerUuidKey = DefinedStringsRiotApiIO.riotApiJSONKeyPlayerUuid.getValue();
	private final String lastGameIdKey = DefinedStringsRiotApiIO.riotApiJSONKeyLastGameId.getValue();

	public RiotApiUser(String encryptedAccountId, String caseCorrectNickName, String lastGameId, double averageKDA, long numberOfGamesPlayed, boolean isPartOfRepeatedApiCheck, long userFirstCreated, String playerUuid) {
		this.encryptedAccountId = encryptedAccountId;
		this.caseCorrectNickName = caseCorrectNickName;
		this.lastGameId = lastGameId;
		this.averageKDA = averageKDA;
		this.numberOfGamesPlayed = numberOfGamesPlayed;
		this.isPartOfRepeatedApiCheck = isPartOfRepeatedApiCheck;
		this.userFirstCreated = userFirstCreated;
		this.playerUuid = playerUuid;
	}

	public void setLastGameId(String gameId) {
		this.lastGameId = gameId;
	}

	public String getLastGameId() {
		return this.lastGameId;
	}

	public String getPlayerUuid() {
		return this.playerUuid;
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
		object.put(this.playerUuidKey, this.playerUuid);
		object.put(this.lastGameIdKey, this.lastGameId);
		return object;		
	}
}
