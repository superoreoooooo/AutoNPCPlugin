package win.oreo.autonpc.listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.autonpc.AutoNPC;
import win.oreo.autonpc.npc.NPC;
import win.oreo.autonpc.npc.quest.Quest;
import win.oreo.autonpc.util.NPCUtil;

import java.util.ArrayList;
import java.util.List;

public class npcListener implements Listener {
    private static List<Player> coolDown = new ArrayList<>();

    public void delay(Player player) {
        coolDown.add(player);
        Bukkit.getScheduler().runTaskLaterAsynchronously(JavaPlugin.getPlugin(AutoNPC.class), () -> coolDown.remove(player), 10);
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();
        if (coolDown.contains(player)) return;
        delay(player);

        if (e.getRightClicked().getType().equals(EntityType.PLAYER)) {
            if (e.getRightClicked().hasMetadata("npc")) {
                String npcMetadata = e.getRightClicked().getMetadata("npc").get(0).asString();
                NPC npc = NPCUtil.getNPC(npcMetadata);
                if (npc != null) {
                    player.sendMessage("==========[" + npc.getName() + "]==========");
                    if (e.getPlayer().isSneaking()) {
                        player.sendMessage("NPC's story : " + npc.getStory());
                    } else { //quest area
                        List<Quest> quests = npc.getQuestList();
                        for (int i = 0; i < 4; i++) {
                            TextComponent msg = new TextComponent("Quest" + (i + 1) + " : " + quests.get(i).getName());
                            msg.setColor(ChatColor.AQUA);
                            msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/npc quest start " + npc.getUUID() + " " + quests.get(i).getId().toString()));
                            msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("퀘스트 " + quests.get(i).getName() + "을(를) 시작합니다!").color(ChatColor.GOLD).create()));
                            player.spigot().sendMessage(msg);
                        }
                    }
                }
            }
        }
    }
}
