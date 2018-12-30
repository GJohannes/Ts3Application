package serverFunctions.riotApi;

public class RiotApiUser {

	private String encryptedAccountId;
	private String caseCorrectNickName;
	private long lastGameId;

	public RiotApiUser(String encryptedAccountId, String caseCorrectNickName, long lastGameId) {
		this.encryptedAccountId = encryptedAccountId;
		this.caseCorrectNickName = caseCorrectNickName;
		this.lastGameId = lastGameId;
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
}
