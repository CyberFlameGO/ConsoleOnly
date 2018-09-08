package io.github.spaicygaming.consoleonly.updater;

import org.apache.commons.io.IOUtils;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;

public class UpdateChecker {

    private final String VERSION_URL = "https://api.spiget.org/v2/resources/40873/versions?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs";
    private final String DESCRIPTION_URL = "https://api.spiget.org/v2/resources/40873/updates?size=" + Integer.MAX_VALUE + "&spiget__ua=SpigetDocs";

    private String currentVersion, latestVersion, updateTitle;


    public UpdateChecker(JavaPlugin main, String currentVersion) {
        this.currentVersion = currentVersion;

        // Check for updates
        check();

        // If there is an update to notify register PlayerJoinEvent listener
        if (availableUpdate())
            main.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), main);
    }

    private void check() {
        try {
            JSONArray versionsArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(VERSION_URL))));
            latestVersion = ((JSONObject) versionsArray.get(versionsArray.size() - 1)).get("name").toString();

            if (availableUpdate()) {
                JSONArray updatesArray = (JSONArray) JSONValue.parseWithException(IOUtils.toString(new URL(String.valueOf(DESCRIPTION_URL))));
                updateTitle = ((JSONObject) updatesArray.get(updatesArray.size() - 1)).get("title").toString();
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean availableUpdate() {
        return latestVersion != currentVersion;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public String getLatestUpdateTitle() {
        return updateTitle;
    }


}