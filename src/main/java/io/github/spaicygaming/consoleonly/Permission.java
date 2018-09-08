package io.github.spaicygaming.consoleonly;

import org.bukkit.permissions.Permissible;

public enum Permission {

    // Commands
    CMD_LIST,
    CMD_RELOAD,
    // Bypass
    BYPASS_CMDS,
    BYPASS_ANTITAB,
    BYPASS_WEFIX,
    // General
    NOTIFY_UPDATES;

    /**
     * Check whether the user has the permission
     *
     * @param user The user whose permissions check
     * @return true if he has the permission
     */
    public boolean has(Permissible user) {
        return user.hasPermission(this.toString());
    }

    @Override
    public String toString() {
        return "consoleonly." + this.name().toLowerCase().replace("_", ".");
    }

}
