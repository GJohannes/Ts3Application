package InputOutput;

public enum DefinedStringsRiotApiIO {
	riotApiPersistencDataFolderName("riotApiPersistence"),
	riotApiPersistencDataFileName("kdaData.txt"),
	riotApiJSONKeyNickName("caseCorrectNickName"),
	riotApiJSONKeyAverageKDA("averageKDA"),
	riotApiJSONKeyNumberOfGamesPlayed("numberOfGamesPlayed"),
	riotApiJSONKeyIsPartOfRepeatedApiCheck("isPartOfRepeatedApiCheck"),
	riotApiJSONKeyEncryptedAccoutID("encryptedAccountID"),
	riotApiJSONKeyUserFirstCreated("userCreatedOn");
	
	private final String keyValue;
	
	private DefinedStringsRiotApiIO(String s) {
		keyValue = s;
	}
	
	public String getValue() {
		return keyValue;
	}
}
