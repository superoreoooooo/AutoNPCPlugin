package win.oreo.autonpc.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import win.oreo.autonpc.npc.NPC;

import java.util.UUID;

public class npcCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            if (args.length > 0) {
                Location loc = player.getLocation();
                NPC.summon(args[0], loc.getX(), loc.getY(), loc.getZ(), UUID.randomUUID(),
                        "ewogICJ0aW1lc3RhbXAiIDogMTY3MTUzMDk2MzU1NCwKICAicHJvZmlsZUlkIiA6ICI4YmU1NzgzOGY0YzY0ODU2Yjc5OTcwNTFkYjU1ODBjZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJJc1NoZV8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2I1N2Y5YThjMTNhMzY0ODg4N2YwYjdhZTA4MWRiNDZhZDRkNGNmZmQ0MmRmZTVkN2JkZTRjMmUxYWQ4N2ZkYiIsCiAgICAgICJtZXRhZGF0YSIgOiB7CiAgICAgICAgIm1vZGVsIiA6ICJzbGltIgogICAgICB9CiAgICB9CiAgfQp9",
                        "ml1kKrlT3r+cnj8V7Y1LhtKJkrFmrp/GQ0VLvaB3xOulbsVbVo5xMXJ5xbj4s/me8glfdeVN+3BYFyfBAXsPGUoFvr3oynh/6oJKqrNys3bDiTynynCtOWjH5FEtb5uOm6qRkBlqPIGmvniWiUS2z2Do0nnORRtO9J4nBbzAEjg+jqRzpAM5hjELZJDlN/YMAmjXW87wO1qQ7Lt8w2wCdjLQykZY+dHFp3qBZUHkgCa365n5kSCo2cCCL78mgxqUB3hhxK3jeK1+wONaQQPDygXbWer+JYQd1iAa5iK79CC+ksj+I2CUCCnoEte2Ba1Jbrm/LMOpnuuVp9bCSUagisYYONXH6gYni/yBolhTtbbsEqStkm/lNZTy8a0U7GnfAeS9rLJxJEhtMMVxI1BaAPplWJSX18PkHEl+nxmcexoLyb7c8p96Kw/5oV6yWGktUUWO7Gr1GfLbTmZz6n52gyPrvyqU5r93pBmjeeF/3MHU9W5ptH2LWVERdvM5dE1eU3p0EUXtC0Y1zvofmAGH12OyFhgs/e9utd4Kq3dV6BXgK7LDVFv83KnIbpvYoYRP29vT68oqUGDk0ltimOgXNhz91HXAhm/r8B+gYOw6eoj41igIuzHEw5z+ucB7omIpi/94mGGqKpyXP6KBVrYHGbdjgJWBYYv7cartgyY8LGw=");
            }
        }
        return false;
    }
}
