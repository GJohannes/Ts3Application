package serverFunctions.riotApi;

import org.json.simple.JSONObject;

public class RiotApiPersitentObject {
	private String caseCorrectNickName;
	private double averageKDA;
	private long numberOfGamesPlayed;
	private boolean isPartOfRepeatedApiCheck;

	public void setPartOfRepeatedApiCheck(boolean isPartOfRepeatedApiCheck) {
		this.isPartOfRepeatedApiCheck = isPartOfRepeatedApiCheck;
	}

	public boolean isPartOfRepeatedApiCheck() {
		return isPartOfRepeatedApiCheck;
	}

	public RiotApiPersitentObject(String caseCorrectNickName, double averageKDA, long numberOfGamesPlayed, boolean isPartOfRepeatedApiCheck) {
		this.caseCorrectNickName = caseCorrectNickName;
		this.averageKDA = averageKDA;
		this.numberOfGamesPlayed = numberOfGamesPlayed;
		this.isPartOfRepeatedApiCheck = isPartOfRepeatedApiCheck;
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

	public JSONObject toJSONObject() {
		JSONObject object = new JSONObject();
		object.put("caseCorrectNickName", this.caseCorrectNickName);
		object.put("averageKDA", this.averageKDA);
		object.put("numberOfGamesPlayed", this.numberOfGamesPlayed);
		object.put("isPartOfRepeatedApiCheck", this.isPartOfRepeatedApiCheck);
		return object;		
	}
	
}
