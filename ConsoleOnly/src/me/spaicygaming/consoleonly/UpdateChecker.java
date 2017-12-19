package me.spaicygaming.consoleonly;

import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class UpdateChecker{	
	
	private static ConsoleOnly main = ConsoleOnly.getInstance();
	
	final static String VERSION_URL = "https://api.spiget.org/v2/resources/40873/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs";
	final static String DESCRIPTION_URL = "https://api.spiget.org/v2/resources/40873/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs";
	
	private double currentVersion = Double.parseDouble(main.getVer()); 
	private double latestVersion = currentVersion;
	private String updateTitle = "";
	
	
	public UpdateChecker(){
		getLastUpdate();
	}
	
	private void getLastUpdate(){
		try {
			JSONArray versionsArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(VERSION_URL))));
			latestVersion = Double.parseDouble(((JSONObject) versionsArray.get(versionsArray.size() - 1)).get("name").toString());

			if(availableUpdate()){
				JSONArray updatesArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(DESCRIPTION_URL))));
				updateTitle = ((JSONObject) updatesArray.get(updatesArray.size() - 1)).get("title").toString();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	public boolean availableUpdate(){
		if (latestVersion > currentVersion){
			return true;
		}
		return false;
	}
	
	public double getLatestVersion(){
		return latestVersion;
	}
	
	public String getUpdateTitle(){
		return updateTitle;
	}
	
	
	
}