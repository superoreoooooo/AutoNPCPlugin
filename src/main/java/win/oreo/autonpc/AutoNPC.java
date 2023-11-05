package win.oreo.autonpc;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.autonpc.command.npcCommand;

public final class AutoNPC extends JavaPlugin {
    private boolean usesPaper = false;
    private boolean updatedPaper = false;

    public boolean usesPaper() {
        return usesPaper;
    }

    public void checkForClasses() {
        try {
            usesPaper = Class.forName("com.destroystokyo.paper.VersionHistoryManager$VersionData") != null;
            if (usesPaper) {
                Bukkit.getLogger().info("Paper detected.");
            }
        } catch (ClassNotFoundException ignored) {

        }
        try {
            updatedPaper = Class.forName("net.kyori.adventure.text.ComponentLike") != null;
        } catch (ClassNotFoundException ignored) {

        }
    }

    @Override
    public void onEnable() {
        getCommand("npc").setExecutor(new npcCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
