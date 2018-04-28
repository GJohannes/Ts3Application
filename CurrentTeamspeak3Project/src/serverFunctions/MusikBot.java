package serverFunctions;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

import com.github.theholywaffle.teamspeak3.EventManager;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.BaseEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientMovedEvent;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;

public class MusikBot {
	public void startMusikBot(ExtendedTS3Api api, String vlcPath) {
		api.addTS3Listeners(getMusikBot(api, vlcPath));
	}
	
	public void stopMusicBot(ExtendedTS3Api api) {
		api.removeTS3Listeners(AllExistingEventAdapter.MUSIK_BOT);
	}

	private ExtendedTS3EventAdapter getMusikBot(ExtendedTS3Api api, String vlcPath) {
		ExtendedTS3EventAdapter musikBot = new ExtendedTS3EventAdapter(AllExistingEventAdapter.MUSIK_BOT) {
			private Process process;
			private String vlcPathInternal = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe"; // vlcPath;
			private double soundHighValue = 1.00;

			@Override
			public void onClientJoin(ClientJoinEvent e) {
				api.sendPrivateMessage(e.getClientId(),
						" \n Musik Bot is currently active \n type !commands or !help for further information");
				if(process == null) {
					ClientInfo botInfo = api.getClientByUId(api.getConnectedConfigValues().getUniqueClientID());

					int oldChannelID = botInfo.getChannelId();
					int musicBotIDOnServer = botInfo.getId();
					api.moveClient(musicBotIDOnServer, 1);
					System.out.println(api.getConnectedConfigValues().getclientName());
					this.playAudioLink("C:\\Users\\Johannes\\Desktop\\Teamspeak3Project\\greeting sounds\\CakeHelloAudio.mp3");

					while(process.isAlive()) {
						//wait e.g. do nothing
					}
					this.stopMusic();
					api.moveClient(musicBotIDOnServer, oldChannelID);
				}
			}

			@Override
			public void onTextMessage(TextMessageEvent messageToBotEvent) {

				// only accept message that has been send from a client, otherwise SERVER
				// messages would also be interpreted
				if (messageToBotEvent.getTargetMode().equals(TextMessageTargetMode.CLIENT)) {
					// only if message was directed as a command
					if (messageToBotEvent.getMessage().startsWith("!")) {
						
						// get message without "!"
						String messageWithoutPrefix = messageToBotEvent.getMessage().substring(1);
						System.out.println(messageWithoutPrefix);
						if (messageWithoutPrefix.equals("commands") || messageWithoutPrefix.equals("help")) {
							this.sendCommandsToClient(messageToBotEvent);
						} else if (messageWithoutPrefix.contains("www.youtube.com")) {
							this.playYoutubeLink(messageWithoutPrefix);
						} else if (messageWithoutPrefix.equals("DasDing")) {
							this.playDasDing();
						} else if (messageWithoutPrefix.toLowerCase().equals("killmusic")
								|| messageWithoutPrefix.toLowerCase().equals("killMusik")
								|| messageWithoutPrefix.toLowerCase().equals("kill")) {
							this.stopMusic();
						} else if (messageWithoutPrefix.startsWith("Volume")) {
							this.adjustVolume(messageWithoutPrefix,messageToBotEvent);

						} else {
							this.sendSyntaxErrorMessage(messageToBotEvent);
						}
					}
				}
			}
			
			private void sendSyntaxErrorMessage(TextMessageEvent messageToBotEvent) {
				api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
						"Command could not be interpreted - Syntaxerror");
			}
			
			private void sendCommandsToClient(TextMessageEvent e) {
				api.sendPrivateMessage(e.getInvokerId(),
						" \n !commands \n !\"insert youtube link here\" \n !DasDing \n !killMusic \n !Volume XXX (Number between 0 and 200))");
			}

			private void playYoutubeLink(String youtubeLocation) {
				this.playAudioLink(youtubeLocation);
			}

			private void playDasDing() {
				this.playAudioLink(
						"http://swr-dasding-live.cast.addradio.de/swr/dasding/live/mp3/128/stream.mp3?ar-distributor=f0a0");
			}

			private void stopMusic() {
				if (process != null && process.isAlive()) {
					process.destroy();
					System.out.println("Destroyed old Process");
				}
				process = null;
			}

			private void adjustVolume(String messageWithoutPrefix, TextMessageEvent messageToBotEvent) {
				
				try {
					String argumentAfterVolumeCommand = messageWithoutPrefix.split(" ")[1];
					System.out.println(argumentAfterVolumeCommand + "arg after coluimehn commands");
					double doubleValueOfArgument = Double.parseDouble(argumentAfterVolumeCommand);
					
					if(doubleValueOfArgument <= 200 && doubleValueOfArgument >= 0) {
						this.soundHighValue = doubleValueOfArgument/100;
						System.out.println(soundHighValue + " new voulme high");
						api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Volume for future music is now: " + doubleValueOfArgument);
					} else {
						api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Volume needs to be between 0 and 200");
					}
				//in case the parse to double false because the argumnet contains letters
				} catch  (NumberFormatException e) {
					api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Only numbers between 0 and 200 are allowed");
				} catch (ArrayIndexOutOfBoundsException e2) {
					api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Insert number as argument between 0 and 200 after !Volume command");
				}
			}

			private void playAudioLink(String audioLocation) {
				String vlcPathInternal = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe"; // vlcPath;
				String soundHighCommand = "--directx-volume";
				String endAgrumentAfterPlay = "vlc://quit";
				String prefferedResolutionCommand = "--preferred-resolution";
				String prefferedResolutionValue = "144";
				this.stopMusic();
				
				// set playing TRUE HERE
				try {
					// if a previous file is playing then
					
					process = new ProcessBuilder(vlcPathInternal, audioLocation, soundHighCommand, Double.toString(soundHighValue),
							prefferedResolutionCommand, prefferedResolutionValue, endAgrumentAfterPlay).start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		};
		return musikBot;

	}
}
