package serverFunctions.riotApi.DataObjects;

/**
 * Simple holder to have method return two values.
 * Two method call would make additional network calls
 */
public class EncryptedAccountIdAndCaseCorrectNickNameHolder {

	private String encryptedAccountId;
	private String caseCorrectNickName;
	
	public EncryptedAccountIdAndCaseCorrectNickNameHolder(String encryptedAccountId, String caseCorrectNickName) {
		this.encryptedAccountId = encryptedAccountId;
		this.caseCorrectNickName = caseCorrectNickName;
	}

	public String getEncryptedAccountId() {
		return encryptedAccountId;
	}

	public String getCaseCorrectNickName() {
		return caseCorrectNickName;
	}
	

	
}
