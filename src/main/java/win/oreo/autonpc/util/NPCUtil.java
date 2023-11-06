package win.oreo.autonpc.util;

import win.oreo.autonpc.npc.NPC;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class NPCUtil {
    public static Set<NPC> npcSet;

    public NPCUtil() {
        npcSet = new HashSet<>();
    }

    public static NPC getNPC(String name) {
        for (NPC npc : npcSet) {
            if (npc.getName().equals(name)) {
                return npc;
            }
        }
        return null;
    }

    public static NPC getNPC(UUID id) {
        for (NPC npc : npcSet) {
            if (npc.getUUID().equals(id)) {
                return npc;
            }
        }
        return null;
    }
}
