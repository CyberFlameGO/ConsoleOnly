package me.spaicygaming.consoleonly;

import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UpdateChecker{	
	
	private static CmdsBlocker main = CmdsBlocker.getInstance();
	
	//Something went wrong while downloading an update
	final static String VERSION_URL = "https://api.spiget.org/v2/resources/40873/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs";
	final static String DESCRIPTION_URL = "https://api.spiget.org/v2/resources/40873/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs";
	
	public static Object[] getLastUpdate(){
		try {
			JSONArray versionsArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(VERSION_URL))));
			Double lastVersion = Double.parseDouble(((JSONObject) versionsArray.get(versionsArray.size() - 1)).get("name").toString());
			Double currVersion = Double.parseDouble(main.getDescription().getVersion());

			if(lastVersion > currVersion){
				
				JSONArray updatesArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(DESCRIPTION_URL))));
				String updateName = ((JSONObject) updatesArray.get(updatesArray.size() - 1)).get("title").toString();
				
				Object[] update = {lastVersion, updateName};
				return update;
			}
		} catch (Exception e){
			e.printStackTrace();
			return new String[0];
		}
		return new String[0];
	}
	
}