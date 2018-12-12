package serverFunctions.webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import miscellaneous.ExtendedTS3Api;

@WebServlet("/privateMessage")
public class PrivateMessageChatServlet extends HttpServlet {
	ExtendedTS3Api api;
	HashMap<String, ArrayList<SingleMessage>> allMessages;

	public PrivateMessageChatServlet(ExtendedTS3Api api, HashMap<String, ArrayList<SingleMessage>> allMessages) {
		this.api = api;
		this.allMessages = allMessages;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 12L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BufferedReader br = request.getReader();
		JSONObject jsonFromWebPage = new JSONObject();

		JSONParser parser = new JSONParser();
		try {
			jsonFromWebPage = (JSONObject) parser.parse(br);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String teamspeakUser = jsonFromWebPage.get("teamspeakUser").toString();
		String messageFromWebPage = "Message From WebServer: " + jsonFromWebPage.get("message").toString();
		// maybe problematic if two perosns have the same ID
		

		List<Client> allClientsWithGivenName = api.getClientsByName(teamspeakUser);
		//case that parameter name is wrong
		if(allClientsWithGivenName == null) {
			System.out.println("returned because of wrong parameter name");
			JSONObject jsonReturnPersonNotExisting = new JSONObject();
			jsonReturnPersonNotExisting.put("personExisting", false);
			response.getWriter().append(jsonReturnPersonNotExisting.toString());
			return;
		}
		int clientId = allClientsWithGivenName.get(0).getId();
		
		synchronized (allMessages) {
			// case that a message was sent
			if (jsonFromWebPage.get("message").toString().length() > 0) {
				api.sendPrivateMessage(clientId, messageFromWebPage);
				ArrayList<SingleMessage> messagesOfThisCommunication;
				if (!allMessages.containsKey(teamspeakUser)) {
					messagesOfThisCommunication = new ArrayList<SingleMessage>(); 
					allMessages.put(teamspeakUser, messagesOfThisCommunication);
				} else {
					messagesOfThisCommunication = allMessages.get(teamspeakUser);
				}
				SingleMessage newSingleMessage = new SingleMessage(messageFromWebPage);
				messagesOfThisCommunication.add(newSingleMessage);
			}

			UpdatePrivateChatBoxesServlet update = new UpdatePrivateChatBoxesServlet(allMessages);
			update.doUpdateFromOtherServlet(teamspeakUser, response);

//			//UPDATE ONLY (could be set to different servlet exclusively for update)
//			//if not there is no message history that can be sent to the server
//			if(allMessages.containsKey(teamspeakUser)) {
//				JSONObject jsonToWebPage = new JSONObject();
//				JSONArray lastTenMessages = new JSONArray();
//				ArrayList<String> messageHistory = allMessages.get(teamspeakUser);
//				
//				if(messageHistory.size() > 10) {
//					for(int i = messageHistory.size()-10 ; i < messageHistory.size(); i++) {
//						lastTenMessages.add(messageHistory.get(i));
//					}
//				} else {
//					for(int i = 0; i < messageHistory.size(); i++) {
//						lastTenMessages.add(messageHistory.get(i));
//					}
//				}
//				
//				System.out.println("Last 10 Messages " + lastTenMessages);
//				
//				jsonToWebPage.put("chatContent", lastTenMessages.toString());
//				response.getWriter().append(jsonToWebPage.toString());
//			}
//		}
//
//		response.getWriter().flush();
//		response.getWriter().close();

		}
	}

}
