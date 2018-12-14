package serverFunctions.webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
	HashMap<String, CopyOnWriteArrayList<SingleMessage>> allMessages;

	public PrivateMessageChatServlet(ExtendedTS3Api api, HashMap<String, CopyOnWriteArrayList<SingleMessage>> allMessages) {
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");

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
		String webPageUserName = jsonFromWebPage.get("webPageUserName").toString();
		String messageFromWebPage = "Message From " + webPageUserName + ": " + jsonFromWebPage.get("message");
		// maybe problematic if two persons have the same ID

		List<Client> allClientsWithGivenName = api.getClientsByName(teamspeakUser);
		// case that parameter name is wrong
		if (allClientsWithGivenName == null) {
			System.out.println("returned because of wrong parameter name");
			JSONObject jsonReturnPersonNotExisting = new JSONObject();
			jsonReturnPersonNotExisting.put("personExisting", false);
			response.getWriter().append(jsonReturnPersonNotExisting.toString());
			return;
		}
		
		int clientId = allClientsWithGivenName.get(0).getId();
		CopyOnWriteArrayList<SingleMessage> messagesOfThisCommunication;
		
		// sending message from another thread so that there is now time wasted for waiting for a response
		MessageToTS3ServerThread messageSender = new MessageToTS3ServerThread(api, messageFromWebPage, clientId);
		Thread messageSenderThread = new Thread(messageSender);
		messageSenderThread.start();

		synchronized (allMessages) {
			if (allMessages.containsKey(teamspeakUser)) {
				messagesOfThisCommunication = allMessages.get(teamspeakUser);
			} else {
				messagesOfThisCommunication = new CopyOnWriteArrayList<SingleMessage>();
				allMessages.put(teamspeakUser, messagesOfThisCommunication);
			}
		}
		SingleMessage newSingleMessage = new SingleMessage(messageFromWebPage);
		messagesOfThisCommunication.add(newSingleMessage);
		
		UpdatePrivateChatBoxesServlet update = new UpdatePrivateChatBoxesServlet(allMessages);
		update.doUpdateFromOtherServlet(teamspeakUser, response);
	}
}
