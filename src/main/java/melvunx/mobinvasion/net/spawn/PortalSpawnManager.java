package melvunx.mobinvasion.net.spawn;

import melvunx.mobinvasion.net.MobInvasion;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class PortalSpawnManager {

    private static int tickCounter = 0;

    //Every 1200 ticks (60 seconds) try to spawn a portal
    private static final int SPAWN_INTERVAL = 1200;

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

        long dayTime = level.getDayTime() % 24000;
        if (dayTime < 13000 || dayTime > 23000) {
            MobInvasion.LOGGER.info("During the day, no portal !");
            return;
        }

        MobInvasion.LOGGER.info("The night is here ! Trying to spawn a portal ...");
        // La logique de spawn arrivera ici
    }
}
