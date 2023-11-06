package win.oreo.autonpc.npc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import win.oreo.autonpc.AutoNPC;
import win.oreo.autonpc.util.NPCUtil;
import win.oreo.autonpc.util.Paper_v12R1;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.UUID;

public class NPC {
    private UUID uuid;
    private String name;
    private EntityPlayer entityPlayer;
    private String value;
    private String signature;
    private String story;
    private

    public NPC(UUID uuid, String name, String value, String signature) {
        this.uuid = uuid;
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public static boolean summon(String name, double x, double y, double z, UUID uuid, String value, String signature) {
        return new NPC(uuid, name, value, signature).spawn(x, y, z);
    }

    public boolean spawn(double x, double y, double z) {
        if (name == null) {
            return false;
        }
        if (name.length() >= 16) {
            return false;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }

        entityPlayer = spawn(this, x, y, z);
        NPCUtil.npcSet.add(this);
        return true;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }

    public void removePlayer() {
        removePlayer(this);
        NPCUtil.npcSet.remove(this);
    }

    private static EntityPlayer spawn(NPC npc, double x, double y, double z) {
        WorldServer worldServer = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
        MinecraftServer mcServer = ((CraftServer) Bukkit.getServer()).getServer();
        EntityPlayer entityPlayer = createEntityPlayer(npc.getUUID(), npc.getName(), worldServer, npc.getValue(), npc.getSignature());
        CraftPlayer bukkitPlayer = entityPlayer.getBukkitEntity();

        try {
            PlayerPreLoginEvent playerPreLoginEvent = new PlayerPreLoginEvent(npc.getName(), InetAddress.getByName("127.0.0.1"), npc.getUUID());
            AsyncPlayerPreLoginEvent asyncPlayerPreLoginEvent = new AsyncPlayerPreLoginEvent(npc.getName(), InetAddress.getByName("127.0.0.1"), npc.getUUID());
            new Thread(() -> Bukkit.getPluginManager().callEvent(asyncPlayerPreLoginEvent)).start();
            Bukkit.getPluginManager().callEvent(playerPreLoginEvent);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mcServer.getPlayerList().a(entityPlayer);
        Location loc = bukkitPlayer.getLocation();
        entityPlayer.setPositionRotation(x, y, z, loc.getYaw(), loc.getPitch());

        DataWatcher data = entityPlayer.getDataWatcher();
        data.set(DataWatcherRegistry.a.a(13), (byte)127);

        String joinMessage = "";

        if (JavaPlugin.getPlugin(AutoNPC.class).usesPaper()) {
            Paper_v12R1.playerInitialSpawnEvent(bukkitPlayer);
        }

        entityPlayer.spawnIn(worldServer);
        entityPlayer.playerInteractManager.a((WorldServer) entityPlayer.world);
        entityPlayer.playerInteractManager.b(EnumGamemode.CREATIVE);
        entityPlayer.playerConnection = new PlayerConnection(mcServer, new NetworkManager(EnumProtocolDirection.SERVERBOUND), entityPlayer);
        entityPlayer.playerConnection.networkManager.channel = new EmbeddedChannel(new ChannelInboundHandlerAdapter());
        entityPlayer.playerConnection.networkManager.channel.close();

        worldServer.getPlayerChunkMap().addPlayer(entityPlayer);
        mcServer.getPlayerList().players.add(entityPlayer);

        try {
            Field j = PlayerList.class.getDeclaredField("j");
            j.setAccessible(true);
            Object valJ = j.get(mcServer.getPlayerList());

            Method jPut = valJ.getClass().getDeclaredMethod("put", Object.class, Object.class);
            jPut.invoke(valJ, bukkitPlayer.getUniqueId(), entityPlayer);

            Field playersByName = PlayerList.class.getDeclaredField("playersByName");
            playersByName.setAccessible(true);
            Object valPlayerByName = playersByName.get(mcServer.getPlayerList());

            Method playersByNamePut = Map.class.getDeclaredMethod("put", Object.class, Object.class);
            playersByNamePut.invoke(valPlayerByName, entityPlayer.getName(), entityPlayer);

        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        PlayerJoinEvent playerJoinEvent = new PlayerJoinEvent(((CraftServer)Bukkit.getServer()).getPlayer(entityPlayer), joinMessage);
        Bukkit.getPluginManager().callEvent(playerJoinEvent);

        String finalJoinMessage = playerJoinEvent.getJoinMessage();

        if (finalJoinMessage != null && !finalJoinMessage.equals("")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(finalJoinMessage);
            }
        }

        PlayerResourcePackStatusEvent resourcePackStatusEventAccepted = new PlayerResourcePackStatusEvent(bukkitPlayer, PlayerResourcePackStatusEvent.Status.ACCEPTED);
        PlayerResourcePackStatusEvent resourcePackStatusEventSuccessFullyLoaded = new PlayerResourcePackStatusEvent(bukkitPlayer, PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED);


        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(AutoNPC.class), () -> Bukkit.getPluginManager().callEvent(resourcePackStatusEventAccepted), 20);
        Bukkit.getScheduler().scheduleSyncDelayedTask(JavaPlugin.getPlugin(AutoNPC.class), () -> Bukkit.getPluginManager().callEvent(resourcePackStatusEventSuccessFullyLoaded), 40);


        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
            connection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));
            connection.sendPacket(new PacketPlayOutEntityMetadata(entityPlayer.getId(), entityPlayer.getDataWatcher(), true));
        }

        worldServer.addEntity(entityPlayer);

        entityPlayer.getBukkitEntity().setMetadata("npc", new FixedMetadataValue(JavaPlugin.getPlugin(AutoNPC.class), npc.getName()));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(JavaPlugin.getPlugin(AutoNPC.class), entityPlayer::playerTick, 1, 1);

        entityPlayer.getBukkitEntity().spigot().setCollidesWithEntities(false);

        return entityPlayer;
    }

    @Deprecated
    private static String[] getSkinData(String skinOwner) {
        String[] str = new String[2];

        if (getUUID(skinOwner) == null || skinOwner.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
            str[0] = "ewogICJ0aW1lc3RhbXAiIDogMTY3MTUzMDk2MzU1NCwKICAicHJvZmlsZUlkIiA6ICI4YmU1NzgzOGY0YzY0ODU2Yjc5OTcwNTFkYjU1ODBjZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJJc1NoZV8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I1N2Y5YThjMTNhMzY0ODg4N2YwYjdhZTA4MWRiNDZhZDRkNGNmZmQ0MmRmZTVkN2JkZTRjMmUxYWQ4N2ZkYiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9";
            str[1] = "ml1kKrlT3r+cnj8V7Y1LhtKJkrFmrp/GQ0VLvaB3xOulbsVbVo5xMXJ5xbj4s/me8glfdeVN+3BYFyfBAXsPGUoFvr3oynh/6oJKqrNys3bDiTynynCtOWjH5FEtb5uOm6qRkBlqPIGmvniWiUS2z2Do0nnORRtO9J4nBbzAEjg+jqRzpAM5hjELZJDlN/YMAmjXW87wO1qQ7Lt8w2wCdjLQykZY+dHFp3qBZUHkgCa365n5kSCo2cCCL78mgxqUB3hhxK3jeK1+wONaQQPDygXbWer+JYQd1iAa5iK79CC+ksj+I2CUCCnoEte2Ba1Jbrm/LMOpnuuVp9bCSUagisYYONXH6gYni/yBolhTtbbsEqStkm/lNZTy8a0U7GnfAeS9rLJxJEhtMMVxI1BaAPplWJSX18PkHEl+nxmcexoLyb7c8p96Kw/5oV6yWGktUUWO7Gr1GfLbTmZz6n52gyPrvyqU5r93pBmjeeF/3MHU9W5ptH2LWVERdvM5dE1eU3p0EUXtC0Y1zvofmAGH12OyFhgs/e9utd4Kq3dV6BXgK7LDVFv83KnIbpvYoYRP29vT68oqUGDk0ltimOgXNhz91HXAhm/r8B+gYOw6eoj41igIuzHEw5z+ucB7omIpi/94mGGqKpyXP6KBVrYHGbdjgJWBYYv7cartgyY8LGw=";
        }

        else try {
            URL profile = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + getUUID(skinOwner) + "?unsigned=false");
            InputStreamReader reader = new InputStreamReader(profile.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            str[0] = textureProperty.get("value").getAsString();
            str[1] = textureProperty.get("signature").getAsString();
        } catch (IOException ignored) {

        }
        return str;
    }

    private static String getUUID(String name) {
        try {
            URL profile = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            InputStreamReader reader = new InputStreamReader(profile.openStream());
            JsonElement element = new JsonParser().parse(reader);
            if (!(element instanceof JsonObject)) {
                return null;
            }

            JsonObject object = element.getAsJsonObject();
            return object.get("id").getAsString();
        } catch (IOException ignored) {}
        return null;
    }

    private static EntityPlayer createEntityPlayer(UUID uuid, String name, WorldServer worldServer, String value, String signature) {
        MinecraftServer mcServer = ((CraftServer) Bukkit.getServer()).getServer();
        GameProfile gameProfile = new GameProfile(uuid, name);
        gameProfile.getProperties().put("textures", new Property("textures", value, signature));

        return new EntityPlayer(mcServer, worldServer, gameProfile, new PlayerInteractManager(worldServer));
    }

    private static void removePlayer(NPC player) {
        MinecraftServer mcServer = ((CraftServer) Bukkit.getServer()).getServer();
        CraftServer craftServer = (CraftServer) Bukkit.getServer();
        EntityPlayer entityPlayer = player.getEntityPlayer();
        WorldServer worldServer = entityPlayer.getWorld().getWorld().getHandle();

        if (entityPlayer.activeContainer != entityPlayer.defaultContainer) {
            entityPlayer.closeInventory();
        }

        PlayerQuitEvent playerQuitEvent = new PlayerQuitEvent(craftServer.getPlayer(entityPlayer), "");

        Bukkit.getPluginManager().callEvent(playerQuitEvent);

        worldServer.getPlayerChunkMap().removePlayer(entityPlayer);
        worldServer.removeEntity(entityPlayer);

        if (mcServer.isMainThread()) {
            entityPlayer.playerTick();
        }

        if (!entityPlayer.inventory.getCarried().isEmpty()) {
            ItemStack carried = entityPlayer.inventory.getCarried();
            entityPlayer.drop(carried, false);
        }

        entityPlayer.getBukkitEntity().disconnect(playerQuitEvent.getQuitMessage());
        entityPlayer.getAdvancementData().a();
        mcServer.getPlayerList().players.remove(entityPlayer);

        try {
            Field j = PlayerList.class.getDeclaredField("j");
            j.setAccessible(true);
            Object valJ = j.get(mcServer.getPlayerList());

            Method jRemove = valJ.getClass().getDeclaredMethod("remove", Object.class);
            jRemove.invoke(valJ, entityPlayer.getUniqueID());
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        String finalQuitMessage = playerQuitEvent.getQuitMessage();

        if (finalQuitMessage != null && !finalQuitMessage.equals("")) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
                connection.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));
                connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
                p.sendMessage(playerQuitEvent.getQuitMessage());
            }
        }

        try {
            Method savePlayerFile = PlayerList.class.getDeclaredMethod("savePlayerFile", EntityPlayer.class);
            savePlayerFile.setAccessible(true);
            savePlayerFile.invoke(mcServer.getPlayerList(), entityPlayer);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
