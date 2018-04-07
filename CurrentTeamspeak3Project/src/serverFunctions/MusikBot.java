package serverFunctions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;

public class MusikBot {
	public void startMusikBot(ExtendedTS3Api api, String vlcPath) {
		api.addTS3Listeners(getMusikBot(api, vlcPath));
	}

	private ExtendedTS3EventAdapter getMusikBot(ExtendedTS3Api api, String vlcPath) {
		ExtendedTS3EventAdapter musikBot = new ExtendedTS3EventAdapter(AllExistingEventAdapter.MUSIK_BOT) {
			private Process process;
			private String vlcPathInternal = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe"; // vlcPath;

			@Override
			public void  onClientJoin(ClientJoinEvent e) {
				api.sendPrivateMessage(e.getClientId(), " \n Musik Bot is currently active \n type !commands or !help for further information");
			}
			
			// @Override
			// public void onClientJoin(ClientJoinEvent e) {
			// System.out.println("ClientJoined");
			// System.out.println(e.getInvokerId());
			
			
			@Override
			public void onTextMessage(TextMessageEvent e) {
				if (e.getMessage().startsWith("!")) {
					String messageWithoutPrefix = e.getMessage().substring(1);
					if (messageWithoutPrefix.equals("commands") || messageWithoutPrefix.equals("help")) {
						api.sendPrivateMessage(e.getInvokerId(), " \n !commands \n !\"insert youtube link here\" \n !DasDing \n !killMusic");
						//api.sendServerMessage(");
					} else if (messageWithoutPrefix.contains("www.youtube.com")) {
						this.playAudioLink(messageWithoutPrefix);
					} else if (messageWithoutPrefix.equals("DasDing")) {
						this.playAudioLink("http://swr-dasding-live.cast.addradio.de/swr/dasding/live/mp3/128/stream.mp3?ar-distributor=f0a0");
					} else if(messageWithoutPrefix.equals("killMusic")){
						if(process != null && process.isAlive()) {
							process.destroy();
						}
					} else {
						api.sendServerMessage("Command could not be interpreted - Syntaxerror");
					}
				}
			}

	private void playAudioLink(String messageWithoutPrefix) {
		try {
			// if a previous file is playing then
			if (process != null && process.isAlive()) {
				process.destroy();
			}

			System.out.println("process  NOT alive");
			process = new ProcessBuilder(vlcPathInternal, messageWithoutPrefix).start();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void print() {
		System.out.println("print was done");
	}

};return musikBot;

}}
