package clientFunctions;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;

import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;

public class MusicOnMove {

	public void startMusicOnMove(ExtendedTS3Api api, String userName, int audioLenght) {
		api.addTS3Listeners(this.playMusicOnClientMove(api, userName, audioLenght));
	}
	
	public void stopMusicOnMove(ExtendedTS3Api api) {
		api.removeTS3Listeners(AllExistingEventAdapter.MUSIC_ON_MOVE);
	}
	
	private ExtendedTS3EventAdapter playMusicOnClientMove(ExtendedTS3Api api, String userName, int audioLenght) {
		ExtendedTS3EventAdapter listenToMusikOnMove = new ExtendedTS3EventAdapter(AllExistingEventAdapter.MUSIC_ON_MOVE) {
			private boolean soundPlaying = false;
			private Logger logger = Logger.getLogger("musicOnMoveLogger");
			
			@Override
			public void onClientMoved(ClientMovedEvent e) {
				logger.log(Level.INFO, "MusikOnMove is alive");
				
				if ((e.getClientId() == api.getClientsByName(userName).get(0).getId()) && !soundPlaying) {
					soundPlaying = true;
					try {
						logger.log(Level.INFO,"TS input: VLC");
						
						Robot robot = new Robot();
						robot.keyPress(KeyEvent.VK_F3);
						Thread.sleep(50);
						robot.keyRelease(KeyEvent.VK_F3);

						Runtime.getRuntime().exec("StartAudio.bat");

						Thread.sleep(audioLenght); // Sleeptime of mp3
						robot.keyPress(KeyEvent.VK_F4);
						Thread.sleep(50);
						robot.keyRelease(KeyEvent.VK_F4);
						logger.log(Level.INFO,"TS input : Normal");

					} catch (InterruptedException | AWTException | IOException e2) {
						e2.printStackTrace();
					}
					soundPlaying = false;
				} else {
					logger.log(Level.INFO,"No Switch of Target Person someone else moved OR sound is already playing");
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

//	@Override
//	public void onTextMessage(TextMessageEvent e) {}
//
//	@Override
//	public void onClientJoin(ClientJoinEvent e) {}
//
//	@Override
//	public void onClientLeave(ClientLeaveEvent e) {}
//
//	@Override
//	public void onServerEdit(ServerEditedEvent e) {}
//
//	@Override
//	public void onChannelEdit(ChannelEditedEvent e) {}
//
//	@Override
//	public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {}
//
//	@Override
//	public void onClientMoved(ClientMovedEvent e) {}
//
//	@Override
//	public void onChannelCreate(ChannelCreateEvent e) {}
//
//	@Override
//	public void onChannelDeleted(ChannelDeletedEvent e) {}
//
//	@Override
//	public void onChannelMoved(ChannelMovedEvent e) {}
//
//	@Override
//	public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {}
//
//	@Override
//	public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {}
	
}
