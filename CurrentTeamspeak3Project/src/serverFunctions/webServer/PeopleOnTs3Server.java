package serverFunctions.webServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	private String loadValue = "";
	private ExtendedTS3Api api;
	private int peopleOnTS3ServerCounter;
	private JSONArray jsonArrayOfAllEventStamps;

	public PeopleOnTs3Server(String load, ExtendedTS3Api api) {
		super();
		this.loadValue = load;
		this.api = api;
		peopleOnTS3ServerCounter = api.getClients().size();
		this.jsonArrayOfAllEventStamps = new JSONArray();
		JSONArray legendGraph = new JSONArray();
		legendGraph.add("Time");
		legendGraph.add("Number of People on Server");
		jsonArrayOfAllEventStamps.add(legendGraph);

		JSONArray eventStamp = new JSONArray();
		eventStamp.add(System.currentTimeMillis());
		eventStamp.add(peopleOnTS3ServerCounter);
		jsonArrayOfAllEventStamps.add(eventStamp);

		api.addTS3Listeners(getEventsForLoggingNumberOfPeopleOnTheServer());
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println("Update get was called");
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
		} else if (whichMethod.equals("getLineChartData")) {
			this.getLineChartData(response);
		}
	}

	private void getLineChartData(HttpServletResponse response) throws IOException {
//		JSONArray outerArray = new JSONArray();
//		JSONArray legendGraph = new JSONArray();
//		legendGraph.add("Time");
//		legendGraph.add("Number of People on Server");
//		outerArray.add(legendGraph);
//
//		for (int i = 0; i < 100; i++) {
//			JSONArray responseDataPoint = new JSONArray();
//			responseDataPoint.add("sdfdssdf");
//			responseDataPoint.add(Math.random() * 10);
//			outerArray.add(responseDataPoint);
//		}

		response.getWriter().append(this.jsonArrayOfAllEventStamps.toString());
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

	private ExtendedTS3EventAdapter getEventsForLoggingNumberOfPeopleOnTheServer() {
		ExtendedTS3EventAdapter eventsForLoggingNumberOfPeopleOnTheServer = new ExtendedTS3EventAdapter(
				AllExistingEventAdapter.LOG_NUMBER_OF_PEOPLE_FOR_WEBPAGE) {

			@Override
			public void onClientJoin(ClientJoinEvent e) {
			
				peopleOnTS3ServerCounter++;
				long currentMiliSeconds = System.currentTimeMillis();
				removeEventStamps(currentMiliSeconds);
				JSONArray eventStamp = new JSONArray();
				eventStamp.add(currentMiliSeconds);
				eventStamp.add(peopleOnTS3ServerCounter);
				jsonArrayOfAllEventStamps.add(eventStamp);
			}

			@Override
			public void onClientLeave(ClientLeaveEvent e) {
					peopleOnTS3ServerCounter--;
					long currentMiliSeconds = System.currentTimeMillis();
					removeEventStamps(currentMiliSeconds);
					JSONArray eventStamp = new JSONArray();
					eventStamp.add(currentMiliSeconds);
					eventStamp.add(peopleOnTS3ServerCounter);
					jsonArrayOfAllEventStamps.add(eventStamp);
			}

		};
		return eventsForLoggingNumberOfPeopleOnTheServer;
	}

	private void removeEventStamps(Long currentMiliSeconds) {
		int isDisntanceToRemove = 86400000; // 24 hours in miliseconds
		for (int i = jsonArrayOfAllEventStamps.size() - 1; i >= 0; i--) { // go through list high to low so that removed items do not change index of i to remove
			if((((JSONArray) jsonArrayOfAllEventStamps.get(i)).get(0)) instanceof Long){ // if the first entry is a long it is a eventStamp
				long miliSecondOfEventStamp = (long)(((JSONArray) jsonArrayOfAllEventStamps.get(i)).get(0)); // get long of eventstemp which is Unix Timestamp for date
				if (currentMiliSeconds - miliSecondOfEventStamp > isDisntanceToRemove) { // if the checked is longer than 24h away then remove
					jsonArrayOfAllEventStamps.remove(i);
				}
			}
		}
	}
}
