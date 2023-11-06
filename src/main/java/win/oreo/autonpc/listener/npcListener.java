package win.oreo.autonpc.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.autonpc.AutoNPC;
import win.oreo.autonpc.npc.NPC;
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

        if (!e.getPlayer().isSneaking()) {
            if (e.getRightClicked().getType().equals(EntityType.PLAYER)) {
                if (e.getRightClicked().hasMetadata("npc")) {
                    String npcMetadata = e.getRightClicked().getMetadata("npc").get(0).asString();
                }
            }
        }
        else {
            if (e.getRightClicked().getType().equals(EntityType.PLAYER)) {
                if (e.getRightClicked().hasMetadata("npc")) {
                    String npcMetadata = e.getRightClicked().getMetadata("npc").get(0).asString();

                    NPC npc = NPCUtil.getNPC(npcMetadata);
                    if (npc != null) {
                        if (!e.getPlayer().isSneaking()) {
                            player.sendMessage("NPC's story : " + npc.getStory());
                        } else {
                            player.sendMessage("asdf");
                        }
                    }
                }
            }
        }
    }
}
