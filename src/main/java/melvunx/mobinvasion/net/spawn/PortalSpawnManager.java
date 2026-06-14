package melvunx.mobinvasion.net.spawn;

import melvunx.mobinvasion.net.MobInvasion;
import melvunx.mobinvasion.net.portal.PortalEntity;
import melvunx.mobinvasion.net.portal.zombie.ZombiePortalEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PortalSpawnManager {

    private static int tickCounter = 0;

    //Every 1200 ticks (60 seconds) try to spawn a portal
    private static final int SPAWN_INTERVAL = 1200;
    private static final Random RANDOM = new Random();

    private static final List<PortalEntity> activePortals = new ArrayList<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(PortalSpawnManager::onServerTick);
        MobInvasion.LOGGER.info("PortalSpawnManager registered !");
    }

    private static void onServerTick(MinecraftServer server) {
        tickCounter++;

        if (tickCounter < SPAWN_INTERVAL) return;
        tickCounter = 0;

        // Get only overworld dimension
        ServerLevel level = server.getLevel(Level.OVERWORLD);
        if (level == null) return;

        // Tick every active portals
        List<PortalEntity> toRemove = new ArrayList<>();
        for (PortalEntity portal : activePortals) {
            if (portal.shouldClose(level)) {
                portal.close(level);
                toRemove.add(portal);
            } else {
                portal.tick(level);
            }
        }
        activePortals.removeAll(toRemove);

        MobInvasion.LOGGER.info("The night is here ! Trying to spawn a portal ...");

        // Try to spawn a new portal
        tickCounter++;
        if (tickCounter < SPAWN_INTERVAL) return;
        tickCounter = 0;

        long dayTime = level.getDayTime() % 24000;
        if (dayTime < 13000 || dayTime > 23000) {
            MobInvasion.LOGGER.info("During the day, no portal !");
            return;
        }

        // Spawn the portal near to the player
        List<? extends Player> players = level.players();
        if (players.isEmpty()) return;

        Player target = players.get(RANDOM.nextInt(players.size()));
        BlockPos portalPos = findSafePosition(level, target);

        if (portalPos != null) {
            ZombiePortalEntity portal = new ZombiePortalEntity(portalPos);
            portal.open(level);
            activePortals.add(portal);
            MobInvasion.LOGGER.info("Zombie portal spawn in {} !", portalPos);
        }

    }

    private static BlockPos findSafePosition(ServerLevel level, Player player) {
        BlockPos playerPos = player.blockPosition();

        // Find a position between 20 and 50 blocs around the player
        for (int attempt = 0; attempt < 10; attempt++) {
            int offsetX = (RANDOM.nextInt(60) - 30);
            int offsetZ = (RANDOM.nextInt(60) - 30);

            BlockPos candidate = playerPos.offset(offsetX, 0, offsetZ);
            candidate = level.getHeightmapPos(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    candidate
            );

            // Verify minimal distance (20 blocks)
            double dist = Math.sqrt(candidate.distSqr(playerPos));
            if (dist >= 20) {
                return candidate;
            }
        }
        return null;
    }
}
