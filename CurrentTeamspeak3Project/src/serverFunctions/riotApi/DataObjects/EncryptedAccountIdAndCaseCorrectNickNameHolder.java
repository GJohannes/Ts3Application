package serverFunctions.riotApi.DataObjects;

/**
 * Simple holder to have method return two values.
 * Two method call would make additional network calls
 */
public class EncryptedAccountIdAndCaseCorrectNickNameHolder {

	private String encryptedAccountId;
	private String caseCorrectNickName;
	private String playerUuid;
	
	public EncryptedAccountIdAndCaseCorrectNickNameHolder(String encryptedAccountId, String caseCorrectNickName, String playerUuid) {
		this.encryptedAccountId = encryptedAccountId;
		this.caseCorrectNickName = caseCorrectNickName;
		this.playerUuid = playerUuid;
	}

	public String getPlayerUuid() {
		return playerUuid;
	}
	
	public String getEncryptedAccountId() {
		return encryptedAccountId;
	}

	public String getCaseCorrectNickName() {
		return caseCorrectNickName;
	}
	

	
}
