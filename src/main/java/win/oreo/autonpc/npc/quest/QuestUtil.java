package win.oreo.autonpc.npc.quest;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import win.oreo.autonpc.AutoNPC;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class QuestUtil {
    private AutoNPC plugin;
    public static HashMap<Player, Quest> playerQuestHashMap;
    public static Set<Player> timerPlayers;

    public QuestUtil() {
        this.plugin = JavaPlugin.getPlugin(AutoNPC.class);
        playerQuestHashMap = new HashMap<>();
        timerPlayers = new HashSet<>();
    }

    public void create(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = scoreboard.registerNewObjective("PlayerBoard", "[AutoNPC]");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        player.setScoreboard(scoreboard);
    }

    public void scoreBoardList(Player player) {
        create(player);
        if (playerQuestHashMap.get(player) == null) return;
        Quest quest = playerQuestHashMap.get(player);
        Score questName = player.getScoreboard().getObjective("PlayerBoard").getScore(ChatColor.DARK_BLUE + "퀘스트 : " + ChatColor.WHITE + quest.getName());
        Score questGoal = player.getScoreboard().getObjective("PlayerBoard").getScore(ChatColor.DARK_BLUE + "목표 : " + ChatColor.WHITE + quest.getGoal());
        Score questReward = player.getScoreboard().getObjective("PlayerBoard").getScore(ChatColor.DARK_BLUE + "보상 : " + ChatColor.WHITE + quest.getReward());

        questName.setScore(11);
        questGoal.setScore(10);
        questReward.setScore(9);
    }

    public void timer() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                Bukkit.getOnlinePlayers().forEach(player -> scoreBoardList(player));
            }
        }, 0, 10);
    }
}
