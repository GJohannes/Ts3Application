package serverFunctions.riotApi;

public class RiotApiUser {

	private long accountId;
	private String nickName;
	private long lastGameId;

	public RiotApiUser(long Id, String name, long lastGameId) {
		accountId = Id;
		nickName = name;
		this.lastGameId = lastGameId;
	}

	public void setLastGameId(long gameId) {
		this.lastGameId = gameId;
	}

	public long getLastGameId() {
		return lastGameId;
	}

	public String getAccountName() {
		return nickName;
	}

	public long getAccountId() {
		return accountId;
	}
}
