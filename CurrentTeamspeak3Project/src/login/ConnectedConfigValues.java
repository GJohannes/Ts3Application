package login;

public class ConnectedConfigValues {
	private String serverQueryName;
	private String serverQueryPassword;
	
	private String serverIpAdress;
	private int serverPort;
	private String usedNickName;
	
	private String uniqueClientID;
	private String clientName;
	
	public ConnectedConfigValues(String serverIpAdress, String serverQueryName, String serverQueryPassword, int serverPort, String uniqueID, String clientName){
		this.serverIpAdress = serverIpAdress;
		this.serverQueryName = serverQueryName;
		this.serverQueryPassword = serverQueryPassword;
		this.serverPort = serverPort;
		this.uniqueClientID = uniqueID;
		this.clientName = clientName;
	}

	public String getclientName() {
		return this.clientName;
	}
	
	public String getUniqueClientID() {
		return this.uniqueClientID;
	}
	
	public String getUsedNickName() {
		return usedNickName;
	}

	public void setUsedNickName(String usedNickName) {
		this.usedNickName = usedNickName;
	}

	public String getServerQueryName() {
		return serverQueryName;
	}

	public String getServerQueryPassword() {
		return serverQueryPassword;
	}

	public String getServerIpAdress() {
		return serverIpAdress;
	}

	public int getServerPort() {
		return serverPort;
	}
}
