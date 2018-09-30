package serverFunctions.riotApi;

/**
 * Simple holder to have method return two values.
 * Two method call would make additional network calls
 */
public class AccountIdAndCaseCorrectNickNameHolder {

	private long accountId;
	private String caseCorrectNickName;
	
	public AccountIdAndCaseCorrectNickNameHolder(long accountId, String caseCorrectNickName) {
		this.accountId = accountId;
		this.caseCorrectNickName = caseCorrectNickName;
	}

	public long getAccountId() {
		return accountId;
	}

	public String getCaseCorrectNickName() {
		return caseCorrectNickName;
	}
	

	
}
