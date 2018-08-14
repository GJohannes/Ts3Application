package serverFunctions.riotApi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;

import miscellaneous.ExtendedTS3Api;

public class RiotApiNotification implements Runnable{
	private ExtendedTS3Api api;
	private boolean threadRunningFlag = true;
	ArrayList<RiotApiUser> userList = new ArrayList<>();
	RiotApiInterface riotInterface = new RiotApiInterface();
	String apiKey = "";
	public RiotApiNotification(ExtendedTS3Api api) {
		this.api = api;
	}
	
	public void setApiKey(String key) {
		this.apiKey=key;
	}
	
	public boolean addUser (String nickName) {
		
		try {
					
			long accountId=riotInterface.getIdByNickName(nickName, apiKey);
			long lastGameId = riotInterface.getLastGameIdByAccId(accountId, apiKey);
			RiotApiUser newUser = new RiotApiUser(accountId, nickName,lastGameId);
			userList.add(newUser);
			
		}catch(FileNotFoundException w){
			System.out.println("userNotFound");
			w.printStackTrace();
			return false;
		}catch (IOException e) {
		    System.out.println("KeyExpired");
			e.printStackTrace();
			return false;
		}
		catch (ParseException e) {
			System.out.println("parseError");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public void exit() {
		this.threadRunningFlag = false;
	}
	
	@Override
	public void run() {
		while(threadRunningFlag) {
			for(RiotApiUser user:userList) {
				try {
					long newGameId = riotInterface.getLastGameIdByAccId(user.getAccountId(), apiKey);
					if(newGameId!=user.getLastGameId()) {
						user.setLastGameId(newGameId);
						boolean win = riotInterface.getWinFromGameId(user.getLastGameId(), apiKey, user.getAccountName());
					
					    if(win) {
					    	api.sendServerMessage(user.getAccountName()+" won his last game");
					    }else {
					    	api.sendServerMessage(user.getAccountName()+" lost his last game");
					    }					
					}
					Thread.sleep(1000);
				} catch (IOException | ParseException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			api.sendServerMessage("Penis");
			try {
				Thread.sleep(60000-userList.size()*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
