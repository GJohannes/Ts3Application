package serverFunctions.musicBot;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.json.simple.JSONObject;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
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
			private Process musicProcess;
			// private String vlcPathInternal = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe";
			// // vlcPath;
			private double soundHighValue = 1.00;
			private ArrayList<String> allComands = new ArrayList<>();
			private ProcessDestoryer processDestoryer = new ProcessDestoryer();

			@Override
			public void onClientJoin(ClientJoinEvent e) {
				api.sendPrivateMessage(e.getClientId(), ">>>><<<<----Music Bot " + api.getConnectedConfigValues().getclientName() + "---->>>><<<< \r\n"
						+ "                                                                  type !help or !commands for further information");
				// if (process == null) {
				// ClientInfo botInfo =
				// api.getClientByUId(api.getConnectedConfigValues().getUniqueClientID());
				//
				// int oldChannelID = botInfo.getChannelId();
				// int musicBotIDOnServer = botInfo.getId();
				// api.moveClient(musicBotIDOnServer, 1);
				// System.out.println(api.getConnectedConfigValues().getclientName());
				//
				// this.playAudioLink(
				// "C:\\Users\\Johannes\\Desktop\\Teamspeak3Project\\greeting
				// sounds\\CakeHelloAudio.mp3");
				//
				// while (process.isAlive()) {
				// // wait e.g. do nothing
				// }
				//// try {
				//// Thread.sleep(2000);
				//// } catch (InterruptedException e1) {
				//// }
				// this.stopMusic();
				// // move back to previous location
				// api.moveClient(musicBotIDOnServer, oldChannelID);
				// }
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
						api.logToCommandline("Command -- " + messageWithoutPrefix);

						// list for command history
						allComands.add(messageWithoutPrefix + " -- " + messageToBotEvent.getInvokerName() + " -- "
								+ "Client Command Nr: " + allComands.size());

						if (messageWithoutPrefix.equals("commands") || messageWithoutPrefix.equals("help")) {
							this.sendCommandsToClient(messageToBotEvent);
						} else if (messageWithoutPrefix.contains("www.youtube.com")) {
							this.playYoutubeLink(messageWithoutPrefix, messageToBotEvent);
						} else if (messageWithoutPrefix.toLowerCase().equals("dasding")) {
							this.playDasDing(messageToBotEvent);
						} else if (messageWithoutPrefix.toLowerCase().equals("1077")) {
							this.playDieNeue1077(messageToBotEvent);
						} else if (messageWithoutPrefix.toLowerCase().equals("antenne1")) {
							this.playAntenne1(messageToBotEvent);
						} else if (messageWithoutPrefix.toLowerCase().equals("killmusic")
								|| messageWithoutPrefix.toLowerCase().equals("killMusik")
								|| messageWithoutPrefix.toLowerCase().equals("kill")) {
							this.stopMusicInSeconds(0);
						} else if (messageWithoutPrefix.toLowerCase().startsWith("volume")) {
							this.adjustVolume(messageWithoutPrefix, messageToBotEvent);
						} else if (messageWithoutPrefix.equals("pull")) {
							this.pullAllClinetsIntoInvokerChannel(messageToBotEvent);
						} else if (messageWithoutPrefix.equals("version")) {
							// this.sendAsciArt(messageToBotEvent);
						} else if (messageWithoutPrefix.toLowerCase().equals("history")) {
							this.showHistory(messageToBotEvent);
						} else if (messageWithoutPrefix.toLowerCase().equals("changelog")) {
							this.sendChangeLog(messageToBotEvent);
						} else {
							this.sendSyntaxErrorMessage(messageToBotEvent);
						}
					}
				}
			}

			private void pullAllClinetsIntoInvokerChannel(TextMessageEvent messageToBotEvent) {
				List<Client> allClientsOnServer = api.getClients();
				int targetChannelId = 1;
				ArrayList<Client> temp = new ArrayList<Client>();
				// get the target channel from all Clinets on Server
				for (int i = 0; i < allClientsOnServer.size(); i++) {
					if (allClientsOnServer.get(i).getId() == messageToBotEvent.getInvokerId()) {
						targetChannelId = allClientsOnServer.get(i).getChannelId();
					}
				}

				// add all clients that are not in the target channel to temp list
				for (int i = 0; i < allClientsOnServer.size(); i++) {
					if (allClientsOnServer.get(i).getChannelId() == targetChannelId) {
						// do nothing if already in correct channel
					} else {
						temp.add(allClientsOnServer.get(i));
					}
				}

				// list to array conversion since api requires array
				int[] clientIds = new int[temp.size()];
				for (int i = 0; i < temp.size(); i++) {
					clientIds[i] = temp.get(i).getId();
				}

				api.moveClients(clientIds, targetChannelId);
			
			}

			private void showHistory(TextMessageEvent messageToBotEvent) {
				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("\n");
				// if it should ever be parameterized so that any number of last commands can be
				// read --> this integer
				int numberOfLastCommendsShown = 10;
				// i >= 0 secures that there are only positions accessed that are existing
				// inside the list
				for (int i = allComands.size() - 1; (i > allComands.size() - 1 - numberOfLastCommendsShown)
						&& (i >= 0); i--) {
					stringBuilder.append(allComands.get(i));
					stringBuilder.append("\n");
					// api.sendPrivateMessage(messageToBotEvent.getInvokerId(), allComands.get(i));
				}
				api.sendPrivateMessage(messageToBotEvent.getInvokerId(), stringBuilder.toString());
			}

			private void sendSyntaxErrorMessage(TextMessageEvent messageToBotEvent) {
				api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
						"Command could not be interpreted - Syntaxerror");
			}

			private void sendCommandsToClient(TextMessageEvent e) {
				api.sendPrivateMessage(e.getInvokerId(),
						" \n !commands"
						+ " \n !\"insert youtube link here\""
						+ " \n !DasDing"
						+ " \n !1077"
						+ " \n !Antenne1"
						+ " \n !killMusic or killMusik or kill"
						+ " \n !Volume XXX (Number between 50 and 150)"
						+ " \n !history to get the last 10 commands"
						+ " \n !changelog displays features of versions");
			}
			
			private void sendChangeLog(TextMessageEvent e) {
				api.sendPrivateMessage(e.getInvokerId(), 
						"  \n -->>Lilith<<-- Features:"
						+ "\n -Additional webradio streams"
						+ "\n -Volume implementation (!Volume XXX)"
						+ "\n -Server side performance increases (autostop after 4h and reduced network load)"
						+ "\n -Bot switches to person who requested music"
						+ "\n -ignor of case sensitivity for commands"
						+ "\n -added history of commands (!history)"
						+ "\n -added changelog command (!changelog)"
						+ "\n -->>Rudolf<<-- Features:"
						+ "\n -Youtube Links playable"
						+ "\n -webradio DasDing"
						+ "\n -stop playing music (!killMusic) command"
						+ "\n -Repository: www.github.com/GJohannes/Ts3Application/");
			}

			private void playYoutubeLink(String youtubeLocation, TextMessageEvent messageToBotEvent) {
				this.playAudioLink(youtubeLocation, messageToBotEvent);
			}

			private void playDasDing(TextMessageEvent messageToBotEvent) {
				this.playAudioLink(
						"http://swr-dasding-live.cast.addradio.de/swr/dasding/live/mp3/128/stream.mp3?ar-distributor=f0a0",
						messageToBotEvent);
			}

			private void playDieNeue1077(TextMessageEvent messageToBotEvent) {
				this.playAudioLink(
						"https://dieneue1077.cast.addradio.de/dieneue1077/simulcast/high/stream.mp3?ar-purpose=web&ar-distributor=f0b7",
						messageToBotEvent);
			}

			private void playAntenne1(TextMessageEvent messageToBotEvent) {
				this.playAudioLink("https://stream.antenne1.de/a1stg/livestream2.mp3", messageToBotEvent);
			}

			

			private void adjustVolume(String messageWithoutPrefix, TextMessageEvent messageToBotEvent) {
				try {
					String argumentAfterVolumeCommand = messageWithoutPrefix.split(" ")[1];
					double doubleValueOfArgument = Double.parseDouble(argumentAfterVolumeCommand);

					if (doubleValueOfArgument <= 150 && doubleValueOfArgument >= 50) {
						this.soundHighValue = doubleValueOfArgument / 100;
						api.logToCommandline("New volume high : " + soundHighValue);
						api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
								"Volume for future music is now: " + doubleValueOfArgument);
					} else {
						api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
								"Volume needs to be between 50 and 150");
					}
					// in case the parse to double false because the argumnet contains letters
				} catch (NumberFormatException e) {
					api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
							"Only numbers between 50 and 150 are allowed");
				} catch (ArrayIndexOutOfBoundsException e2) {
					api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
							"Insert number as argument between 50 and 150 after !Volume command");
				}
			}

			/*
			 * get all clinets and search those to reduce amohunts of times connection to
			 * server has to be build to get a
			 */
			private void moveBotToMusicRequestingUser(TextMessageEvent messageToBotEvent) {
				List<Client> allClients = api.getClients();
				int botClientId = 0;
				int targetChannelId = 0;

				for (int i = 0; i < allClients.size(); i++) {

					// if unique id and name match the ones that where stored inside api (in login
					// screen) then get his id to get current bot id
					if (allClients.get(i).getUniqueIdentifier()
							.equals(api.getConnectedConfigValues().getUniqueClientID())
							&& allClients.get(i).getNickname().equals(api.getConnectedConfigValues().getclientName())) {
						botClientId = allClients.get(i).getId();
					}

					// if client matches name and Uid to invoker then get his channel to move bot
					// there
					if (allClients.get(i).getUniqueIdentifier().equals(messageToBotEvent.getInvokerUniqueId())
							&& allClients.get(i).getNickname().equals(messageToBotEvent.getInvokerName())) {
						{
							targetChannelId = allClients.get(i).getChannelId();
						}

					}
				}

				api.moveClient(botClientId, targetChannelId);

				// to many api requests that slowed the interpretation of the command
				// ClientInfo botInfo =
				// api.getClientByUId(api.getConnectedConfigValues().getUniqueClientID());
				// api.moveClient(api.getClientsByName("Zephira").get(0).getId(),
				// api.getClientByUId(messageToBotEvent.getInvokerUniqueId()).getChannelId());

			}

			private void playAudioLink(String audioLocation, TextMessageEvent messageToBotEvent) {
				String soundHighCommand = "--directx-volume";
				String endAgrumentAfterPlay = "vlc://quit";
				String prefferedResolutionCommand = "--preferred-resolution";
				String prefferedResolutionValue = "144";
				this.stopMusicInSeconds(0);
				this.moveBotToMusicRequestingUser(messageToBotEvent);

				// set playing TRUE HERE
				try {
					ProcessBuilder processBuilder = new ProcessBuilder(vlcPath, audioLocation, soundHighCommand,
							Double.toString(soundHighValue), prefferedResolutionCommand, prefferedResolutionValue,
							endAgrumentAfterPlay);
					processBuilder.redirectErrorStream(true); // !! important !!
					
					musicProcess = processBuilder.start();
					
					//prints error stream to command line instead of just closing. bad for production since it is spamming the shell but better for debugging
					BufferedReader reader = new BufferedReader(new InputStreamReader(musicProcess.getInputStream()));
//					pb.redirectOutput(Redirect.INHERIT); // alternative to closing the stream. will use command line output from regular application 
//					
					String line = "";
					
					line = reader.readLine();
					while(line != null) {
						System.out.println(line);
						// case that lua was outdated results offten in a http 403 from youtube
						if(line.contains("403")) {
							stopMusicInSeconds(0);
							api.sendPrivateMessage(messageToBotEvent.getInvokerId(), "Could not play Music - Outdated yutube.lua - please notify an Admin");
							return; // remove for debugging! return results in less command line output. 
						}
						line = reader.readLine();
					}
					
					
					reader.close();
					
					api.logToCommandline("Started playing music");

				//14400 secconds are 4 hours. After 4 hours audio is stopped to ensure performance on server
				this.stopMusicInSeconds(14400);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			private void stopMusicInSeconds(int seconds) {
				Timer timer = new Timer();
				BotTimeout timeout = new BotTimeout(timer, musicProcess);
				timer.schedule(timeout, seconds * 1000);
			}
		};
		return musikBot;
	}
}
