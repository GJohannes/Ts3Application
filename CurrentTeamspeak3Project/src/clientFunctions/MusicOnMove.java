package clientFunctions;

import java.io.IOException;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;

public class MusicOnMove {
	
//	private static playMusikOnMove instance = null;
//	
//	private playMusikOnMove(){}
//	
//	public static playMusikOnMove getInstance(){
//		if(instance == null){
//			instance = new playMusikOnMove();
//		}
//		return instance;
//	}
//	
	private static TS3EventAdapter thisEventAdapter = new TS3EventAdapter() {};
	private static boolean soundPlaying = false;
	
	public TS3Api activateMusikOnMove(TS3Api api, String userName, int audioLenght, boolean activate){
		if(activate){
			api.removeTS3Listeners(thisEventAdapter);
			thisEventAdapter = playMusicOnClientMove(api, userName, audioLenght);
			api.addTS3Listeners(thisEventAdapter);
		} else {
			api.removeTS3Listeners(thisEventAdapter);
		}
		return api;
	}

	
	private TS3EventAdapter playMusicOnClientMove(TS3Api api, String userName, int audioLenght) {
		TS3EventAdapter listenToMusikOnMove = new TS3EventAdapter() {
			@Override
			public void onClientMoved(ClientMovedEvent e) {
				System.out.println("MusikOnMove is alive");
				if ((e.getClientId() == api.getClientsByName(userName).get(0).getId()) && !soundPlaying) {
					System.out.println(e.toString());
					soundPlaying = true;
					try {
						System.out.println("TS input: VLC");
						
						Robot robot = new Robot();
						robot.keyPress(KeyEvent.VK_F3);
						Thread.sleep(50);
						robot.keyRelease(KeyEvent.VK_F3);

						Runtime.getRuntime().exec("StartAudio.bat");

						Thread.sleep(audioLenght); // Sleeptime of mp3
						robot.keyPress(KeyEvent.VK_F4);
						Thread.sleep(50);
						robot.keyRelease(KeyEvent.VK_F4);
						System.out.println("TS input : Normal");

					} catch (InterruptedException | AWTException | IOException e2) {
						System.out.println("Internal Java Error");
						e2.printStackTrace();
					}
					soundPlaying = false;
				} else {
					System.out.println("No Switch of Target Person someone else moved");
				}
			}
		};

		return listenToMusikOnMove;
	}
	// @Override
	// public void onClientJoin(ClientJoinEvent e) {
	// System.out.println("ClientJoined");
	// System.out.println(e.getInvokerId());
	// }
	//
	// @Override
	// public void onClientLeave(ClientLeaveEvent e) {
	// System.out.println("ClientLeaved");
	// System.out.println(e.getInvokerId());
	// }
	//
	// @Override
	// public void onServerEdit(ServerEditedEvent e) {
	// System.out.println("Text on server");
	// System.out.println(e.getInvokerId());
	//
	// }
	//
	// @Override
	// public void onChannelEdit(ChannelEditedEvent e) {
	// }
	//
	// @Override
	// public void onTextMessage(TextMessageEvent e) {
	// System.out.println("Text on server");
	// System.out.println(e.getInvokerId());
	// e.getMessage();
	// }

}
