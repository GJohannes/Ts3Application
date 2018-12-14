package serverFunctions.webServer;

import miscellaneous.ExtendedTS3Api;

public class MessageToTS3ServerThread implements Runnable{

	private ExtendedTS3Api api;
	private String message;
	private int clientId;
	
	public MessageToTS3ServerThread(ExtendedTS3Api api, String message, int clientId) {
		this.api = api;
		this.message = message;
		this.clientId = clientId;
	}
	
	@Override
	public void run() {
		api.sendPrivateMessage(clientId, message);
	}

}
