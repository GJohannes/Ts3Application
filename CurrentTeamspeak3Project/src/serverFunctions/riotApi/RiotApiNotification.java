package serverFunctions.riotApi;

import miscellaneous.ExtendedTS3Api;

public class RiotApiNotification implements Runnable{
	private ExtendedTS3Api api;
	private boolean threadRunningFlag = true;
	
	public RiotApiNotification(ExtendedTS3Api api) {
		this.api = api;
	}
	
	public void exit() {
		this.threadRunningFlag = false;
	}
	
	@Override
	public void run() {
		while(threadRunningFlag) {
			api.sendServerMessage("Penis");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
