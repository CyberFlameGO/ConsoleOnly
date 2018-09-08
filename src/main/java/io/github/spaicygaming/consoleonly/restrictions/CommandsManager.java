package io.github.spaicygaming.consoleonly.restrictions;

import io.github.spaicygaming.consoleonly.ConsoleOnly;

import java.util.List;
import java.util.stream.Collectors;

public class CommandsManager {

    private ConsoleOnly main;

    private List<String> consoleOnlyCmds, blockedCmds, worldEditCmds, antiTabCmds;

    public CommandsManager(ConsoleOnly main) {
        this.main = main;

        initializeLists();
    }

    public void initializeLists() {
        consoleOnlyCmds = main.getConfig().getStringList("Settings.ConsoleOnly.commands");
        blockedCmds = main.getConfig().getStringList("Settings.BlockedCommands.commands");
        worldEditCmds = main.getConfig().getStringList("Settings.WorldEditCrashFix.Commands");

        antiTabCmds = main.getConfig().getStringList("Settings.WorldEditCrashFix.Commands").stream().map(cmd -> "/" + cmd).collect(Collectors.toList());
    }

    public List<String> getConsoleOnlyCmds() {
        return consoleOnlyCmds;
    }

    public List<String> getBlockedCmds() {
        return blockedCmds;
    }

    List<String> getWorldEditCmds() {
        return worldEditCmds;
    }

    public List<String> getAntiTabCmds() {
        return antiTabCmds;
    }
}
