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

    private static PortalSpawnManager instance;

    // Every 1200 ticks (60 seconds) try to spawn a portal
    private static final int SPAWN_INTERVAL = 1200;
    private static final Random RANDOM = new Random();
    private static final List<PortalEntity> activePortals = new ArrayList<>();

    private static int spawnCooldown = 0;

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(PortalSpawnManager::onServerTick);
        MobInvasion.LOGGER.info("PortalSpawnManager registered !");
    }

    private static void onServerTick(MinecraftServer server) {
        ServerLevel level = server.getLevel(Level.OVERWORLD);
        if (level == null) return;

        // Tick every active portal
        List<PortalEntity> toRemove = new ArrayList<>();
        for (PortalEntity portal : activePortals) {
            if (portal.shouldClose(level)) {
                portal.close(level);
                toRemove.add(portal);
                MobInvasion.LOGGER.info("Portal closed at {}", portal.getPosition());
            } else {
                portal.tick(level);
            }
        }
        activePortals.removeAll(toRemove);

        // Cooldown before the next spawn
        if (spawnCooldown > 0) {
            spawnCooldown--;
            return;
        }

        long dayTime = level.getDayTime() % 24000;
        if (dayTime < 13000 || dayTime > 23000) return;

        // Spawn the portal near a random player
        List<? extends Player> players = level.players();
        if (players.isEmpty()) return;

        Player target = players.get(RANDOM.nextInt(players.size()));
        BlockPos portalPos = findSafePosition(level, target);

        if (portalPos != null) {
            ZombiePortalEntity portal = new ZombiePortalEntity(portalPos);
            portal.open(level);
            activePortals.add(portal);
            spawnCooldown = SPAWN_INTERVAL;
            MobInvasion.LOGGER.info("Zombie portal spawned at {} !", portalPos);
        } else {
            MobInvasion.LOGGER.info("No valid position found for portal spawn.");
        }
    }

    private static BlockPos findSafePosition(ServerLevel level, Player player) {
        BlockPos playerPos = player.blockPosition();

        for (int attempt = 0; attempt < 10; attempt++) {
            int offsetX = RANDOM.nextInt(60) - 30;
            int offsetZ = RANDOM.nextInt(60) - 30;

            BlockPos candidate = playerPos.offset(offsetX, 0, offsetZ);
            candidate = level.getHeightmapPos(
                    net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    candidate
            );

            double dist = Math.sqrt(candidate.distSqr(playerPos));
            if (dist >= 20) {
                return candidate;
            }
        }
        return null;
    }

    public static PortalSpawnManager getInstance() {
        if (instance == null) instance = new PortalSpawnManager();
        return instance;
    }

    public void forceSpawnPortal(ServerLevel level, BlockPos pos) {
        ZombiePortalEntity portal = new ZombiePortalEntity(pos);
        portal.open(level);
        activePortals.add(portal);
        MobInvasion.LOGGER.info("Portal force spawned at {} !", pos);
    }

    public int closeAllPortals(ServerLevel level) {
        int count = activePortals.size();
        for (PortalEntity portal : activePortals) portal.close(level);
        activePortals.clear();
        return count;
    }
}
