package serverFunctions.riotApi;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RiotApiInterface {
	
	public AccountIdAndCaseCorrectNickNameHolder getIdAndCaseCorrectNickNameByNickName(String nickName, String ApiKey) throws IOException, ParseException {
		//URL can not contain spaces which are possible in nicknames
		if(nickName.contains(" ")) {
			nickName = nickName.replace(" ", "%20");
		}
		URL url = new URL("https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + nickName + "?api_key=" + ApiKey);
		JSONObject summenorData  = getJSONFromUrl(url);		
		long accountId =  (long) summenorData.get("accountId");
		String caseCorrectNickName =  (String) summenorData.get("name");
		AccountIdAndCaseCorrectNickNameHolder holder = new AccountIdAndCaseCorrectNickNameHolder(accountId, caseCorrectNickName);
		return holder;		
	}
		
	public long getLastGameIdByAccId(long accId,String ApiKey) throws IOException, ParseException {
		URL url = new URL("https://euw1.api.riotgames.com/lol/match/v3/matchlists/by-account/" + accId + "?api_key=" + ApiKey);
		JSONObject matchData = getJSONFromUrl(url);
		JSONArray matchlist =   (JSONArray) matchData.get("matches");
		
		return (long) ((JSONObject) matchlist.get(0)).get("gameId");
	}
	
	/*
	 * returns true if last game was won; returns false if last game was lost
	 */
	public boolean getWinFromGameId(long gameId, String ApiKey, String nickName) throws IOException, ParseException {		
		URL url = new URL("https://euw1.api.riotgames.com/lol/match/v3/matches/" + gameId + "?api_key=" + ApiKey);
		JSONObject match = getJSONFromUrl(url); 
		JSONArray participantIdentities = (JSONArray) match.get("participantIdentities");
		long participantId = -1;
		
		//get each player that participated in the game until match is found for given nickname
		for(int i = 0; i < participantIdentities.size(); i++) {
			JSONObject player = (JSONObject)((JSONObject)participantIdentities.get(i)).get("player");
			if(player.get("summonerName").toString().equalsIgnoreCase(nickName)) {
				participantId = (long) ((JSONObject)participantIdentities.get(i)).get("participantId");
			}
		}
		
		if(participantId == -1) {
			//defensive programmed should bever be executed
			return false;
		}
		
		JSONArray participants = (JSONArray) match.get("participants");
		
		for(int i = 0; i < participants.size(); i++) {
			JSONObject oneParticipant = (JSONObject) participants.get(i);
			
			if(((long)(oneParticipant.get("participantId"))) == participantId){
				return (boolean) ((JSONObject)oneParticipant.get("stats")).get("win");

			}
		}
		return false;
	}
	
	public JSONObject getJSONFromUrl(URL url) throws IOException, ParseException {
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		
		// Input-Stream from HTTP-Request
		InputStreamReader in = new InputStreamReader(conn.getInputStream());
		
		JSONParser parser = new JSONParser();
		JSONObject resultingJSON = (JSONObject) parser.parse(in);
		return resultingJSON;
	}
}
