package win.oreo.autonpc.util;

import com.destroystokyo.paper.event.player.PlayerInitialSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class Paper_v12R1 {
    public static void playerInitialSpawnEvent(Player p) {
        PlayerInitialSpawnEvent ev = new PlayerInitialSpawnEvent(p, p.getLocation());
        PlayerSpawnLocationEvent ev2 = new PlayerSpawnLocationEvent(p, p.getLocation());

        Bukkit.getPluginManager().callEvent(ev);
        Bukkit.getPluginManager().callEvent(ev2);
    }
}
