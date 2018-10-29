package serverFunctions.webServer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;

@WebServlet("/privateMessage")
public class PrivateMessageChatServlet extends HttpServlet {
	ExtendedTS3Api api;

	public PrivateMessageChatServlet(ExtendedTS3Api api) {
		this.api = api;
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

		String messageToServer = "";
		String tempString = "";

		BufferedReader br = request.getReader();
		JSONObject json = new JSONObject();

		JSONParser test = new JSONParser();
		try {
			json = (JSONObject) test.parse(br);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(json);

		json.put("chatContent", json.get("message"));
		response.getWriter().append(json.toString());
		response.getWriter().flush();
		response.getWriter().close();

	}


}
