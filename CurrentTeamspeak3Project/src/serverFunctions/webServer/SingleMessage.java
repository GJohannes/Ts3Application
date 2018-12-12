package serverFunctions.webServer;

import java.time.LocalDateTime;

public class SingleMessage {
	private LocalDateTime messageCreated;
	private long messageCreatedUnixTimestamp;
	private String message;

	public SingleMessage(String message) {
		this.messageCreated = LocalDateTime.now();
		this.messageCreatedUnixTimestamp = System.currentTimeMillis();
		this.message = message;
	}

	public String getMessage() {
		return this.message;
	}

	public LocalDateTime getLocalDateTimeOfCreation(){
		return this.messageCreated;
	}
	
	public long getMessageCreatedInUnixStamp() {
		return this.messageCreatedUnixTimestamp;
	}
}