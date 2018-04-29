package serverFunctions;

import java.util.ArrayList;
import java.util.List;

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
			private Process process;
			// private String vlcPathInternal = "C:\\Program Files\\VideoLAN\\VLC\\vlc.exe";
			// // vlcPath;
			private double soundHighValue = 1.00;
			private ArrayList<String> allComands = new ArrayList<>();

			@Override
			public void onClientJoin(ClientJoinEvent e) {
				api.sendPrivateMessage(e.getClientId(),
						">>>><<<<----Music Bot Lilith---->>>><<<< \r\n" + 
						"                                                                  type !help or !commands for further information");
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
						
						//list for command history
						allComands.add(messageWithoutPrefix + " -- " + messageToBotEvent.getInvokerName() + " -- " + "Client Command Nr: " + allComands.size());
						
						System.out.println(messageWithoutPrefix);
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
							this.stopMusic();
						} else if (messageWithoutPrefix.startsWith("Volume")) {
							this.adjustVolume(messageWithoutPrefix, messageToBotEvent);
						} else if (messageWithoutPrefix.equals("pull")) {
							this.pullAllClinetsIntoInvokerChannel(messageToBotEvent);
						} else if (messageWithoutPrefix.equals("version")) {
							// this.sendAsciArt(messageToBotEvent);
						} else if (messageWithoutPrefix.equals("history")) {
							this.showHistory(messageToBotEvent);
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
				// if it should ever be parameterized so that any number of last commands can be read --> this integer
				int numberOfLastCommendsShown = 10;
				// i >= 0 secures that there are only positions accessed that are existing inside the list
				for(int i = allComands.size()-1; (i > allComands.size()-1 - numberOfLastCommendsShown) && (i >= 0); i--) {
					System.out.println(i + " i size");
					stringBuilder.append(allComands.get(i));
					stringBuilder.append("\n");
					//api.sendPrivateMessage(messageToBotEvent.getInvokerId(),  allComands.get(i));
				}
				System.out.println(stringBuilder.toString());
				api.sendPrivateMessage(messageToBotEvent.getInvokerId(), stringBuilder.toString());
			}
			
			private void sendSyntaxErrorMessage(TextMessageEvent messageToBotEvent) {
				api.sendPrivateMessage(messageToBotEvent.getInvokerId(),
						"Command could not be interpreted - Syntaxerror");
			}

			private void sendCommandsToClient(TextMessageEvent e) {
				api.sendPrivateMessage(e.getInvokerId(),
						" \n !commands \n !\"insert youtube link here\" \n !DasDing \n !1077 \n !Antenne1 \n !killMusic \n !Volume XXX (Number between 50 and 150)");
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

					if (doubleValueOfArgument <= 150 && doubleValueOfArgument >= 50) {
						this.soundHighValue = doubleValueOfArgument / 100;
						System.out.println(soundHighValue + " new voulme high");
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
			 * get all clinets and search those to reduce amohunts of times connection to server has to be build to get a 
			 */
			private void moveBotToMusicRequestingUser(TextMessageEvent messageToBotEvent) {
				List<Client> allClients = api.getClients();
				int botClientId = 0;
				int targetChannelId = 0;
				
				for (int i = 0; i < allClients.size(); i++) {
					
					// if unique id and name match the ones that where stored inside api (in login screen) then get his id to get current bot id
					if (allClients.get(i).getUniqueIdentifier()
							.equals(api.getConnectedConfigValues().getUniqueClientID())
							&& allClients.get(i).getNickname().equals(api.getConnectedConfigValues().getclientName())) {
						botClientId = allClients.get(i).getId();
					}

					//if client matches name and Uid to invoker then get his channel to move bot there
					if (allClients.get(i).getUniqueIdentifier().equals(messageToBotEvent.getInvokerUniqueId())
							&& allClients.get(i).getNickname().equals(messageToBotEvent.getInvokerName())) {
						{
							targetChannelId = allClients.get(i).getChannelId();
						}

					}
				}

				api.moveClient(botClientId, targetChannelId);
				
				//to many api requests that slowed the interpretation of the command
				// ClientInfo botInfo =
				// api.getClientByUId(api.getConnectedConfigValues().getUniqueClientID());
				//api.moveClient(api.getClientsByName("Zephira").get(0).getId(),
				//		api.getClientByUId(messageToBotEvent.getInvokerUniqueId()).getChannelId());

			}

			private void playAudioLink(String audioLocation, TextMessageEvent messageToBotEvent) {
				String soundHighCommand = "--directx-volume";
				String endAgrumentAfterPlay = "vlc://quit";
				String prefferedResolutionCommand = "--preferred-resolution";
				String prefferedResolutionValue = "144";
				this.stopMusic();
				this.moveBotToMusicRequestingUser(messageToBotEvent);
				// set playing TRUE HERE
				try {
					// if a previous file is playing then

					process = new ProcessBuilder(vlcPath, audioLocation, soundHighCommand,
							Double.toString(soundHighValue), prefferedResolutionCommand, prefferedResolutionValue,
							endAgrumentAfterPlay).start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		};
		return musikBot;

	}
}
