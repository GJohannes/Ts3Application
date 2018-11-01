package serverFunctions.webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import miscellaneous.ExtendedTS3Api;

/**
 * Servlet implementation class Update
 */
@WebServlet("/Update")
public class PeopleOnTs3Server extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private String loadValue = "";
	private ExtendedTS3Api api;

	public PeopleOnTs3Server(String load, ExtendedTS3Api api) {
		super();
		this.loadValue = load;
		this.api = api;
	}

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
		String whichMethod = request.getParameter("method");
		if (whichMethod.equals("refreshUsers")) {
			this.updateOnlinePeople(response);
		} else if (whichMethod.equals("sendServerMessage")) {
			this.sendServerMessage(request);
		}
	}

	private void updateOnlinePeople(HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();

		List<Client> allClients = api.getClients();

		
		JSONObject returnData = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		for (int i = 0; i < allClients.size(); i++) {
			jsonArray.add(allClients.get(i).getNickname().toString());
		}

		json.put("allClientNicknames", jsonArray);
		
		response.getWriter().append(json.toString());
	}

	private void sendServerMessage(HttpServletRequest request) throws IOException {
		api.sendServerMessage(request.getParameter("serverMessage"));
	}

}
