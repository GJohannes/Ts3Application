package login;

public class ConnectedConfigValues {
	private String serverQueryName;
	private String serverQueryPassword;
	
	private String serverIpAdress;
	private int serverPort;
	private String usedNickName;
	
	public ConnectedConfigValues(String serverIpAdress, String serverQueryName, String serverQueryPassword, int serverPort){
		this.serverIpAdress = serverIpAdress;
		this.serverQueryName = serverQueryName;
		this.serverQueryPassword = serverQueryPassword;
		this.serverPort = serverPort;
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
