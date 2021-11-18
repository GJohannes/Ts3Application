package serverFunctions.riotApi;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import serverFunctions.riotApi.DataObjects.EncryptedAccountIdAndCaseCorrectNickNameHolder;
import serverFunctions.riotApi.DataObjects.WinKdaMostDamageHolder;

public class RiotApiInterface {
	public EncryptedAccountIdAndCaseCorrectNickNameHolder getIdAndCaseCorrectNickNameByNickName(String nickName, String ApiKey) throws IOException, ParseException {
		//URL can not contain spaces which are possible in nicknames
		if(nickName.contains(" ")) {
			nickName = nickName.replace(" ", "%20");
		}
		// deprecated v3 call returned: {"id":84941,"accountId":90255,"name":"XZephiraX","profileIconId":10,"revisionDate":1545243549000,"summonerLevel":45}
		URL url = new URL("https://euw1.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nickName + "?api_key=" + ApiKey);
		JSONObject summenorData  = getJSONFromUrl(url);	
		String encryptedAccountId =  (String) summenorData.get("accountId");
		String caseCorrectNickName =  (String) summenorData.get("name");
		String playerUuid = (String) summenorData.get("puuid");
		EncryptedAccountIdAndCaseCorrectNickNameHolder holder = new EncryptedAccountIdAndCaseCorrectNickNameHolder(encryptedAccountId, caseCorrectNickName, playerUuid);
		return holder;		
	}
		
	public String getLastGameIdByEncryptedAccId(String accId, String playerUuid, String ApiKey) throws IOException, ParseException {
		URL url = new URL("https://europe.api.riotgames.com/lol/match/v5/matches/by-puuid/" + playerUuid + "/ids?start=0&count=20&api_key=" + ApiKey);
		String matchId = getlastMatchIdFromJsonArray(url);
		return matchId;
	}
	
	/*
	 * returns true if last game was won; returns false if last game was lost
	 */
	public WinKdaMostDamageHolder getWinAndKdaFromGameId(String gameId, String ApiKey, String nickName, String playerUuid) throws IOException, ParseException {		
		URL url = new URL("https://europe.api.riotgames.com/lol/match/v5/matches/" + gameId + "?api_key=" + ApiKey);
		JSONObject match = getJSONFromUrl(url); 
		System.out.println("Match: " + match);
		JSONArray participants =(JSONArray) ((JSONObject) match.get("info")).get("participants");
		
		double kda = -1;
		boolean win = false;
		
		WinKdaMostDamageHolder winAndKdaHolder = new WinKdaMostDamageHolder(win, kda);
		
		for (int i = 0; i < participants .size(); i++ ) {
			if(((JSONObject) participants.get(i)).get("summonerName").toString().equalsIgnoreCase(nickName)) {
				JSONObject relevantParticipant = (JSONObject) participants.get(i);
				
				long kills = (long) relevantParticipant.get("kills");
				long deaths =  (long) relevantParticipant.get("deaths");
				long assists = (long) relevantParticipant.get("assists");
				
				if(deaths == 0) {
					deaths = 1; // to prevent math error while calculating kda
				}
				kda = (double) (kills + assists) / deaths;
				win = (boolean) relevantParticipant.get("win");
				winAndKdaHolder = new WinKdaMostDamageHolder(win, kda);
			}
		}
		
		return winAndKdaHolder;
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
	
	public String getlastMatchIdFromJsonArray(URL url) throws IOException, ParseException {
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		System.out.println("url " + url);
		// Input-Stream from HTTP-Request
		InputStreamReader in = new InputStreamReader(conn.getInputStream());
		JSONParser parser = new JSONParser();
		JSONArray resultingJSONArray = (JSONArray) parser.parse(in);
		System.out.println("resultung json array " + resultingJSONArray);
		return (String) resultingJSONArray.get(0);
	}
	
}
