package win.oreo.autonpc.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.autonpc.AutoNPC;
import win.oreo.autonpc.npc.NPC;
import win.oreo.autonpc.npc.quest.Quest;
import win.oreo.autonpc.util.NPCUtil;

import java.io.File;
import java.rmi.MarshalException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class npcCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                switch (args[0]) {
                    case "load" -> {
                        File dir = JavaPlugin.getPlugin(AutoNPC.class).getDataFolder();
                        File[] files = dir.listFiles();
                        if (files != null) {
                            for (File file : files) {
                                if (file.isFile()) {
                                    if (!file.getName().contains("data") && file.getName().contains(".yml")) {
                                        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

                                        String name = yaml.getString("name");
                                        String npcstory = yaml.getString("npcstory");
                                        List<Quest> quests = new ArrayList<>();

                                        for (String num : yaml.getConfigurationSection("quests.").getKeys(false)) {
                                            String goal = yaml.getString("quests." + num + ".goal");
                                            String qname = yaml.getString("quests." + num + ".name");
                                            String reward = yaml.getString("quests." + num + ".reward");
                                            String story = yaml.getString("quests." + num + ".story");

                                            quests.add(new Quest(qname, goal, reward, story));
                                        }

                                        //player.sendMessage(name + " // " + npcstory);
                                        //quests.forEach(quest -> player.sendMessage(quest.getName()));
                                        double x = new Random().nextDouble(-10, 10);
                                        double z = new Random().nextDouble(-10, 10);

                                        NPC npc = new NPC(UUID.randomUUID(), name);

                                        npc.setStory(npcstory);
                                        npc.setQuestList(quests);

                                        if (npc.spawn(x, player.getLocation().getY() + 1, z)) {
                                            player.sendMessage("SUCCESS!");
                                        }
                                    }
                                }
                            }
                        }
                    } case "quest" -> {
                        if (args.length >= 4) {
                            switch (args[1]) {
                                case "start" -> {
                                    NPC npc = NPCUtil.getNPC(UUID.fromString(args[2]));
                                    if (npc != null) {
                                        UUID questID = UUID.fromString(args[3]);
                                        Quest quest = null;
                                        for (Quest q : npc.getQuestList()) {
                                            if (q.getId().equals(questID)) {
                                                quest = q;
                                            }
                                            if (quest != null) {
                                                player.sendMessage("퀘스트 이름 : " + quest.getName());
                                                player.sendMessage("퀘스트 스토리 : " + quest.getStory());
                                                player.sendMessage("퀘스트 목표 : " + quest.getGoal());
                                                player.sendMessage("퀘스트 보상 : " + quest.getReward());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return false;
    }
}
