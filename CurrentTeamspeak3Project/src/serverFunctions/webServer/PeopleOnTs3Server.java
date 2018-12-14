package serverFunctions.webServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import miscellaneous.AllExistingEventAdapter;
import miscellaneous.ExtendedTS3Api;
import miscellaneous.ExtendedTS3EventAdapter;

/**
 * Servlet implementation class Update
 */
@WebServlet("/Update")
public class PeopleOnTs3Server extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ExtendedTS3Api api;
	private JSONArray jsonArrayOfAllEventStamps;

	public PeopleOnTs3Server(ExtendedTS3Api api) {
		super();
		this.api = api;
		int peopleOnTS3Server = api.getClients().size();
		this.jsonArrayOfAllEventStamps = new JSONArray();

		// legend of the graph that is displayed on the webpage
		ArrayList<String> legendOfGraph = new ArrayList<>();
		legendOfGraph.add("Time");
		legendOfGraph.add("Number of People on Server");

		JSONArray legendGraph = new JSONArray();
		legendGraph.add("Time");
		legendGraph.add("Number of People on Server");
		jsonArrayOfAllEventStamps.add(legendGraph);

		JSONArray eventStamp = new JSONArray();
		eventStamp.add(System.currentTimeMillis());
		eventStamp.add(peopleOnTS3Server);
		jsonArrayOfAllEventStamps.add(eventStamp);

		api.addTS3Listeners(getEventsForLoggingNumberOfPeopleOnTheServer());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		String whichMethod = request.getParameter("method");
		if (whichMethod.equals("refreshUsers")) {
			this.updateOnlinePeople(response);
		} else if (whichMethod.equals("getLineChartData")) {
			this.getLineChartData(response);
		} else {
			this.sendServeltMethodNotFound(response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		String whichMethod = request.getParameter("method");
		if (whichMethod.equals("sendServerMessage")) {
			this.sendServerMessage(request, response);
		} else if(whichMethod.equals("getPassword")) {
			this.getPassword(request, response);
		} else {
			this.sendServeltMethodNotFound(response);
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

	private void sendServeltMethodNotFound(HttpServletResponse response) throws IOException {
		response.getWriter().append("Requested Method not found");
	}

	/*
	 * Server Message is requiring the sha256 hash of the current date in iso format as a password (without zeros)
	 * (minor improvement to hardcoding the password);
	 * 
	 */
	private void sendServerMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		LocalDateTime dateTime = LocalDateTime.now();
		int year = dateTime.getYear();
		int month = dateTime.getMonthValue();
		int day = dateTime.getDayOfMonth();
		String dateInISOFormat = year + "-" + month + "-" + day;
		String password = this.calculatePassword(dateInISOFormat);
		JSONObject responseJSON = new JSONObject();
		if (request.getParameter("password").equalsIgnoreCase(password)) {
			api.sendServerMessage(request.getParameter("serverMessage"));
			responseJSON.put("passwordCorrect", true);
		} else {
			responseJSON.put("passwordCorrect", false);
		}
		response.getWriter().append(responseJSON.toJSONString());
	}

	/*
	 * calculate the password based on the string the user sent from the website
	 */
	private void getPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String passwordToConvert = request.getParameter("passwordToConvert");
		String password = this.calculatePassword(passwordToConvert);
		JSONObject responseJSON = new JSONObject();
		responseJSON.put("password", password);
		response.getWriter().append(responseJSON.toJSONString());
	}
	
	private String calculatePassword(String passwordToConvert) {
		String password = DigestUtils.sha256Hex(passwordToConvert);
		return password;
	}
	
	
	private void getLineChartData(HttpServletResponse response) throws IOException {
		response.getWriter().append(this.jsonArrayOfAllEventStamps.toString());
	}

	private ExtendedTS3EventAdapter getEventsForLoggingNumberOfPeopleOnTheServer() {
		ExtendedTS3EventAdapter eventsForLoggingNumberOfPeopleOnTheServer = new ExtendedTS3EventAdapter(AllExistingEventAdapter.LOG_NUMBER_OF_PEOPLE_FOR_WEBPAGE) {

			@Override
			public void onClientJoin(ClientJoinEvent e) {
				int peopleOnTS3Server = api.getClients().size();
				long currentMiliSeconds = System.currentTimeMillis();
				removeEventStamps(currentMiliSeconds);
				JSONArray eventStamp = new JSONArray();
				eventStamp.add(currentMiliSeconds);
				eventStamp.add(peopleOnTS3Server);
				jsonArrayOfAllEventStamps.add(eventStamp);
			}

			@Override
			public void onClientLeave(ClientLeaveEvent e) {
				int peopleOnTS3Server = api.getClients().size();
				long currentMiliSeconds = System.currentTimeMillis();
				removeEventStamps(currentMiliSeconds);
				JSONArray eventStamp = new JSONArray();
				eventStamp.add(currentMiliSeconds);
				eventStamp.add(peopleOnTS3Server);
				jsonArrayOfAllEventStamps.add(eventStamp);
			}
		};
		return eventsForLoggingNumberOfPeopleOnTheServer;
	}

	private void removeEventStamps(Long currentMiliSeconds) {
		int isDisntanceToRemove = 86400000; // 24 hours in miliseconds
		for (int i = jsonArrayOfAllEventStamps.size() - 1; i >= 0; i--) { // go through list high to low so that removed items do not change index of i to
																			// remove
			if ((((JSONArray) jsonArrayOfAllEventStamps.get(i)).get(0)) instanceof Long) { // if the first entry is a long it is a eventStamp
				long miliSecondOfEventStamp = (long) (((JSONArray) jsonArrayOfAllEventStamps.get(i)).get(0)); // get long of eventstemp which is Unix Timestamp for date
				if (currentMiliSeconds - miliSecondOfEventStamp > isDisntanceToRemove) { // if the checked is longer than 24h away then remove
					jsonArrayOfAllEventStamps.remove(i);
				}
			}
		}
	}
}
