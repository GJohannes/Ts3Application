package serverFunctions;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.json.simple.JSONObject;

import miscellaneous.FileInputOutput;

public class UserLoggedInEntity {

	private int id;
	private String uId;
	private String nickname;

	
	
	public void logUser(LoggedServerEvents event){
		GregorianCalendar calendar = new GregorianCalendar();
		Date date = calendar.getTime();
		JSONObject json = new JSONObject();
		
		json.put("Date", date);
		json.put("Nickname", this.nickname);
		json.put("UID", this.uId);
		json.put("Event", event);
		
		FileInputOutput inOut = new FileInputOutput();
		try {
			inOut.writeServerLog(json);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getuId() {
		return uId;
	}
	public void setuId(String uId) {
		this.uId = uId;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
}
