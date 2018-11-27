package miscellaneous;

public enum DefinedStrings {
	logFolderName("log");
	
	private final String keyValue;
	
	DefinedStrings(String s) {
		keyValue = s;
	}
	
	public String getValue() {
		return keyValue;
	}
}