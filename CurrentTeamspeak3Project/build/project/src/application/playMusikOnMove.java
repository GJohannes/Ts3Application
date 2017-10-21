package application;

import java.io.IOException;
import java.util.logging.Level;
import org.json.simple.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.github.theholywaffle.teamspeak3.*;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.VirtualServer;
import com.github.theholywaffle.teamspeak3.api.event.ChannelEditedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.ServerEditedEvent;

public class playMusikOnMove {
	
	public static boolean soundPlaying = false;
	
	public TS3EventAdapter playMusicOnClientMove(TS3Api api, String userName, int audioLenght) {
		System.out.println("Entered");
		System.out.println(userName);
		
		TS3EventAdapter listenToMusikOnMove = new TS3EventAdapter() {
			@Override
			public void onClientMoved(ClientMovedEvent e) {
				System.out.println("I am Alive");
				if ((e.getClientId() == api.getClientsByName(userName).get(0).getId()) && !soundPlaying) {
					soundPlaying = true;
					try {
						System.out.println("Starting Button Press now");
						
						Robot robot = new Robot();
						robot.keyPress(KeyEvent.VK_F3);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_F3);

						Runtime.getRuntime().exec("StartAudio.bat");

						Thread.sleep(audioLenght); // Sleeptime of mp3
						robot.keyPress(KeyEvent.VK_F4);
						Thread.sleep(10);
						robot.keyRelease(KeyEvent.VK_F4);
						System.out.println("Buttons where pressed");

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
