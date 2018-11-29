package serverFunctions.webServer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import miscellaneous.FileInputOutput;
import serverFunctions.loggerForServer.LoggedServerEvents;

@WebServlet("/historyDataIrrelevantString")
public class HistoryData extends HttpServlet {

	private static final long serialVersionUID = 44L;
	private JSONArray allEventsOfOneDay;

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LocalDateTime requestedDate = LocalDateTime.now();
		try {
			requestedDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(request.getParameter("dateInMilis"))), ZoneId.systemDefault());			
		} catch (Exception e) {
			return;
		}
		
		FileInputOutput inOut = FileInputOutput.getInstance();
		allEventsOfOneDay = new JSONArray();

		ArrayList<JSONObject> allEvents = inOut.getHistoryFromLocalDateTime(requestedDate);

		JSONArray legendGraph = new JSONArray();
		legendGraph.add("Time");
		legendGraph.add("Number of People on Server at: " + requestedDate.toString());
		allEventsOfOneDay.add(legendGraph);

		for (int i = 0; i < allEvents.size(); i++) {
			LocalDateTime date = LocalDateTime.parse((CharSequence) allEvents.get(i).get("LocalDateTime "));
			long milis = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

			String event = (String) allEvents.get(i).get("Event");

			long numberOfpeoleOnServer = (long) allEvents.get(i).get("numberOfPeopleOnServer");
			
			JSONArray eventStamp = new JSONArray();
			eventStamp.add(milis);
			eventStamp.add(numberOfpeoleOnServer);
			allEventsOfOneDay.add(eventStamp);
		}

		response.getWriter().append(allEventsOfOneDay.toString());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
