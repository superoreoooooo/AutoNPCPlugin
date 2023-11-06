package win.oreo.autonpc.listener;

import net.minecraft.server.v1_12_R1.PacketPlayOutEntity;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import win.oreo.autonpc.util.NPCUtil;

public class PlayerMovementListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        NPCUtil.npcSet.forEach(npc -> {
            Location playerLoc = e.getPlayer().getLocation();
            Location location = npc.getEntityPlayer().getBukkitEntity().getLocation();
            double distance = playerLoc.distance(location);
            if (distance >= 10) return;
            location.setDirection(e.getPlayer().getLocation().subtract(location).toVector());
            float yaw = location.getYaw();
            float pitch = location.getPitch();
            PlayerConnection connection = ((CraftPlayer)e.getPlayer()).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc.getEntityPlayer(), (byte)((yaw%360)*256/360)));
            connection.sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(npc.getEntityPlayer().getBukkitEntity().getEntityId(), (byte)((yaw%360.)*256/360), (byte) ((pitch%360.)*256/360), false));
        });
    }
}