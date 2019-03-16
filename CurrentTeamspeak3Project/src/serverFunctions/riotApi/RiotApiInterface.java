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
		EncryptedAccountIdAndCaseCorrectNickNameHolder holder = new EncryptedAccountIdAndCaseCorrectNickNameHolder(encryptedAccountId, caseCorrectNickName);
		return holder;		
	}
		
	public long getLastGameIdByEncryptedAccId(String accId,String ApiKey) throws IOException, ParseException {
		URL url = new URL("https://euw1.api.riotgames.com/lol/match/v4/matchlists/by-account/" + accId + "?api_key=" + ApiKey);
		JSONObject matchData = getJSONFromUrl(url);
		JSONArray matchlist =   (JSONArray) matchData.get("matches");
		
		return (long) ((JSONObject) matchlist.get(0)).get("gameId");
	}
	
	/*
	 * returns true if last game was won; returns false if last game was lost
	 */
	public WinKdaMostDamageHolder getWinAndKdaFromGameId(long gameId, String ApiKey, String nickName) throws IOException, ParseException {		
		URL url = new URL("https://euw1.api.riotgames.com/lol/match/v4/matches/" + gameId + "?api_key=" + ApiKey);
		JSONObject match = getJSONFromUrl(url); 
		JSONArray participantIdentities = (JSONArray) match.get("participantIdentities");
		long participantId = -1;
		double kda = -1;
		boolean win = false;
		
		WinKdaMostDamageHolder winAndKdaHolder = new WinKdaMostDamageHolder(win, kda);
		
		//get each player that participated in the game until match is found for given nickname
		for(int i = 0; i < participantIdentities.size(); i++) {
			JSONObject player = (JSONObject)((JSONObject)participantIdentities.get(i)).get("player");
			if(player.get("summonerName").toString().equalsIgnoreCase(nickName)) {
				participantId = (long) ((JSONObject)participantIdentities.get(i)).get("participantId");
			}
		}
		
		if(participantId == -1) {
			//defensive programmed should never be executed because a the summoner has to be found  
			return winAndKdaHolder;
		}
		
		JSONArray participants = (JSONArray) match.get("participants");
		long mostTotalDamageDealtFromPlayer = 0;
		long playerIdOfMostDamageDealt = -1;
		
		for(int i = 0; i < participants.size(); i++) {
			JSONObject oneParticipant = (JSONObject) participants.get(i);
			
			//make sure that 
			if(mostTotalDamageDealtFromPlayer < (long)((JSONObject) oneParticipant.get("stats")).get("totalDamageDealtToChampions")) {
				mostTotalDamageDealtFromPlayer = (long)((JSONObject) oneParticipant.get("stats")).get("totalDamageDealtToChampions");
				playerIdOfMostDamageDealt = (long)((JSONObject) oneParticipant.get("stats")).get("participantId");
			}
			
			if(((long)(oneParticipant.get("participantId"))) == participantId){
				long kills = (long) ((JSONObject)oneParticipant.get("stats")).get("kills");
				long deaths =  (long) ((JSONObject)oneParticipant.get("stats")).get("deaths");
				long assists = (long) ((JSONObject)oneParticipant.get("stats")).get("assists");
				
				if(deaths == 0) {
					deaths = 1; // to prevent math error while calculating kda
				}
				kda = (double) (kills + assists) / deaths;
				win = (boolean) ((JSONObject)oneParticipant.get("stats")).get("win");
				winAndKdaHolder = new WinKdaMostDamageHolder(win, kda);
			}
		}
		
		if(participantId == playerIdOfMostDamageDealt) {
			winAndKdaHolder.setHighestDamageDealer(true);
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
}
