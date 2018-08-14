package serverFunctions.riotApi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import netscape.javascript.JSObject;

public class riotApiData {
	public long getIdByNickName(String nickName, String ApiKey) throws IOException, ParseException {
		ApiKey = "RGAPI-4c46e0b2-8cce-4a8c-821d-fe4c9abc41cd";
		nickName = "XZephiraX";
		
		String urlString =  "https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/";
		
		URL url = new URL("https://euw1.api.riotgames.com/lol/summoner/v3/summoners/by-name/" + nickName + "?api_key=" + ApiKey);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");

		// Input-Stream from HTTP-Request
		InputStreamReader in = new InputStreamReader(conn.getInputStream());

		JSONParser parser = new JSONParser();
		JSONObject summenorData = (JSONObject) parser.parse(in);
		
		System.out.println(summenorData.get("accountId").getClass());
		
		
		long accountId =  (long) summenorData.get("accountId");
		System.out.println(accountId);
	    return accountId;
		//JSONObject summonerByNameData = (JSONObject) parser.parse(line);
	//	System.out.println(summonerByNameData.toJSONString() + " adfshjdf");
		
		
		
		
	}
	
}
