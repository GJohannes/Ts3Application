package serverFunctions.riotApi.DataObjects;

import org.json.simple.JSONObject;

import InputOutput.DefinedStringsRiotApiIO;

public class RiotApiPersitentUserInformation {
	private String caseCorrectNickName;
	private double averageKDA;
	private long numberOfGamesPlayed;
	private boolean isPartOfRepeatedApiCheck;
	private String encryptedAccountId;

	public String getEncryptedAccountId() {
		return encryptedAccountId;
	}

	private final String nickNameKey = DefinedStringsRiotApiIO.riotApiJSONKeyNickName.getValue();
	private final String averageKDAKey = DefinedStringsRiotApiIO.riotApiJSONKeyAverageKDA.getValue();
	private final String numberOfGamesPlayedKey = DefinedStringsRiotApiIO.riotApiJSONKeyNumberOfGamesPlayed.getValue();
	private final String repeatetCheckKey = DefinedStringsRiotApiIO.riotApiJSONKeyIsPartOfRepeatedApiCheck.getValue();
	private final String encryptedAccountIdKey = DefinedStringsRiotApiIO.riotApiJSONKeyEncryptedAccoutID.getValue();
	
	

	public RiotApiPersitentUserInformation(String caseCorrectNickName, double averageKDA, long numberOfGamesPlayed, boolean isPartOfRepeatedApiCheck, String encryptedAccountId) {
		this.caseCorrectNickName = caseCorrectNickName;
		this.averageKDA = averageKDA;
		this.numberOfGamesPlayed = numberOfGamesPlayed;
		this.isPartOfRepeatedApiCheck = isPartOfRepeatedApiCheck;
		this.encryptedAccountId = encryptedAccountId;
	}

	public String getCaseCorrectNickName() {
		return caseCorrectNickName;
	}

	public double getAverageKDA() {
		return averageKDA;
	}

	public long getNumberOfGamesPlayed() {
		return numberOfGamesPlayed;
	}
	
	public void setPartOfRepeatedApiCheck(boolean isPartOfRepeatedApiCheck) {
		this.isPartOfRepeatedApiCheck = isPartOfRepeatedApiCheck;
	}

	public boolean isPartOfRepeatedApiCheck() {
		return isPartOfRepeatedApiCheck;
	}

	public JSONObject toJSONObject() {
		JSONObject object = new JSONObject();
		object.put(this.nickNameKey, this.caseCorrectNickName);
		object.put(this.averageKDAKey, this.averageKDA);
		object.put(this.numberOfGamesPlayedKey, this.numberOfGamesPlayed);
		object.put(this.repeatetCheckKey, this.isPartOfRepeatedApiCheck);
		object.put(this.encryptedAccountIdKey, this.encryptedAccountId);
		return object;		
	}
	
}
