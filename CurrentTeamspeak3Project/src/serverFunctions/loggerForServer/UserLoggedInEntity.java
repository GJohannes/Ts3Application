package serverFunctions.loggerForServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import org.json.simple.JSONObject;

import InputOutput.FileInputOutput;

public class UserLoggedInEntity {

	private int id;
	private String uId;
	private String nickname;
	private LocalDateTime timeUserJoinedTheServer;
	
	
	
	public LocalDateTime getTimeUserJoinedTheServer() {
		return timeUserJoinedTheServer;
	}

	public void setTimeUserJoinedTheServer(LocalDateTime timeUserJoinedTheServer) {
		this.timeUserJoinedTheServer = timeUserJoinedTheServer;
	}

	public void logUser(LoggedServerEvents event, int numberOfPeopleOnServer) {
		LocalDateTime now = LocalDateTime.now();
		JSONObject json = new JSONObject();

		json.put("Event", event.toString());
		json.put("LocalDateTime ", now.toString());
		json.put("Nickname", this.nickname);
		json.put("UniqueId", this.uId);
		json.put("numberOfPeopleOnServer", numberOfPeopleOnServer);

		FileInputOutput inOut = FileInputOutput.getInstance();
		try {
			Files.createDirectories(Paths.get("log"));
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
