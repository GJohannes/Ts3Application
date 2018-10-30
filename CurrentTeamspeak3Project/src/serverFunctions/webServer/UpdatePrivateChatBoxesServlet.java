package serverFunctions.webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UpdatePrivateChatBoxesServlet extends HttpServlet{

	HashMap<String, ArrayList<String>> allMessages;
	
	public UpdatePrivateChatBoxesServlet(HashMap<String, ArrayList<String>> allMessages) {
		this.allMessages = allMessages;
	}
	
	private static final long serialVersionUID = 13L;
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doUpdateFromServletRequest(request,response);

	}
	
	public void doUpdateFromServletRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
		doUpdate(teamspeakUser, response);
	}
	
	public void doUpdateFromOtherServlet(String teamspeakUser, HttpServletResponse response) throws IOException {
		doUpdate(teamspeakUser, response);
	}
	
	private void doUpdate(String teamspeakUser, HttpServletResponse response) throws IOException{
		synchronized (allMessages) {
			//UPDATE ONLY (could be set to different servlet exclusively for update)
			//if not there is no message history that can be sent to the server
			if(allMessages.containsKey(teamspeakUser)) {
				JSONObject jsonToWebPage = new JSONObject();
				JSONArray lastTenMessages = new JSONArray();
				ArrayList<String> messageHistory = allMessages.get(teamspeakUser);
				
				if(messageHistory.size() > 10) {
					for(int i = messageHistory.size()-10 ; i < messageHistory.size(); i++) {
						lastTenMessages.add(messageHistory.get(i));
					}
				} else {
					for(int i = 0; i < messageHistory.size(); i++) {
						lastTenMessages.add(messageHistory.get(i));
					}
				}
				
				jsonToWebPage.put("chatContent", lastTenMessages.toString());
				jsonToWebPage.put("personExisting", "true");
				response.getWriter().append(jsonToWebPage.toString());
			} else {
				JSONObject jsonToWebPage = new JSONObject();
				jsonToWebPage.put("personExisting", "false");
				response.getWriter().append(jsonToWebPage.toString());
			}
		}

		response.getWriter().flush();
		response.getWriter().close();
	}

	
}
