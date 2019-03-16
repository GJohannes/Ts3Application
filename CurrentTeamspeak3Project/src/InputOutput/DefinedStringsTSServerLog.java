package InputOutput;

public enum DefinedStringsTSServerLog {
	logFolderName("log");
	
	private final String keyValue;
	
	DefinedStringsTSServerLog(String s) {
		keyValue = s;
	}
	
	public String getValue() {
		return keyValue;
	}
}