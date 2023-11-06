package win.oreo.autonpc;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.autonpc.command.npcCommand;
import win.oreo.autonpc.listener.PlayerMovementListener;
import win.oreo.autonpc.listener.npcListener;
import win.oreo.autonpc.manager.YmlManager;
import win.oreo.autonpc.npc.NPC;
import win.oreo.autonpc.npc.quest.QuestUtil;
import win.oreo.autonpc.util.NPCUtil;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class AutoNPC extends JavaPlugin {
    public YmlManager ymlManager;
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
        this.ymlManager = new YmlManager(this);
        NPCUtil util = new NPCUtil();
        checkForClasses();
        run();

        Bukkit.getPluginManager().registerEvents(new PlayerMovementListener(), this);
        Bukkit.getPluginManager().registerEvents(new npcListener(), this);

        QuestUtil util1 = new QuestUtil();
        util1.timer();
    }

    @Override
    public void onDisable() {
        if (NPCUtil.npcSet != null || NPCUtil.npcSet.size() != 0) {
            Set<NPC> cp = new HashSet<>(NPCUtil.npcSet);
            for (NPC npc : cp) {
                npc.removePlayer();
            }
        }
    }

    public void run() {
        Bukkit.getScheduler().runTaskTimer(JavaPlugin.getPlugin(this.getClass()), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.spigot().setCollidesWithEntities(false);
            }
        }, 0, 20);
    }
}
