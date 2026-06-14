package melvunx.mobinvasion.net.portal;

import net.minecraft.server.level.ServerLevel;

public interface IPortal {
    // Get the unique portal name
    String getPortalId();

    // Start the mob spawn wave
    void startWaves(ServerLevel level);

    // Called every ticks
    void tick(ServerLevel level);

    // Close and delete the portal
    void close(ServerLevel level);

    boolean shouldClose(ServerLevel level);

    // Get portal life time
    int getLifetimeTicks();

    int getWaveCount();

    int getMobsPerWave();
}
