package melvunx.mobinvasion.net.portal.base;

import net.minecraft.server.level.ServerLevel;

public interface IPortal {
    // Get the unique portal name
    String getPortalId();

    // Start the mob spawn wave
    void startWaves(ServerLevel level);


    // Called every ticks
    void tick(ServerLevel level);

    //Open a portal
    void open(ServerLevel level);


    // Close and delete the portal
    void close(ServerLevel level);

    boolean shouldClose(ServerLevel level);

    // Get portal lifetime
    int getLifetimeTicks();

    int getWaveCount();

    int getMobsPerWave();
}
