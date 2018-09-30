package serverFunctions.riotApi;

public class RiotApiUser {

	private long accountId;
	private String caseCorrectNickName;
	private long lastGameId;

	public RiotApiUser(long Id, String name, long lastGameId) {
		accountId = Id;
		caseCorrectNickName = name;
		this.lastGameId = lastGameId;
	}

	public void setLastGameId(long gameId) {
		this.lastGameId = gameId;
	}

	public long getLastGameId() {
		return lastGameId;
	}

	public String getCaseCorrectNickName() {
		return caseCorrectNickName;
	}

	public long getAccountId() {
		return accountId;
	}
}
