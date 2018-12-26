package serverFunctions.webServer.servlets.privateChatWindow;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;

public class SingleMessage {
	private LocalDateTime messageCreated;
	private long messageCreatedUnixTimestamp;
	private String message;
	//byte[] messageInUTF8;
	
	public SingleMessage(String message)  {
		this.messageCreated = LocalDateTime.now();
		this.messageCreatedUnixTimestamp = System.currentTimeMillis();
		this.message = message;
//		try {
//			byte[] messageInUTF8 = message.getBytes("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
	}

	public String getMessage() throws UnsupportedEncodingException {
//		try {
//			return new String(this.messageInUTF8, "UTF-8");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		return this.message;
	}

	public LocalDateTime getLocalDateTimeOfCreation(){
		return this.messageCreated;
	}
	
	public long getMessageCreatedInUnixStamp() {
		return this.messageCreatedUnixTimestamp;
	}
}