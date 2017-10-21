package application;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileInputOutput {
	
	public JSONObject readFile() {
		JSONParser parser = new JSONParser();
		System.out.println("HereAmI");
		try {
			InputStreamReader read = new FileReader("Data");
			Object o = parser.parse(read);
			JSONObject json = (JSONObject) o;
			read.close();
			return json;
		} catch (IOException | ParseException e1) {
			System.out.println("Could not Read File");
			e1.printStackTrace();
		}
		return null; // TO DO Insert Error Case
	}

	public void writeFile(JSONObject o) {
		try {
			FileWriter writer = new FileWriter("Data");
			writer.write(o.toJSONString());
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not write File");
			e.printStackTrace();
		}

	}

	public void writeAudioBatch(String audioFileName) {
		try {
			FileWriter writer = new FileWriter("StartAudio.bat");
			writer.write("start %~dp0" + audioFileName); // For Same Path
			//writer.write(audioFileName);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
