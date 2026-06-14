package melvunx.mobinvasion.net.portal;

import melvunx.mobinvasion.net.MobInvasion;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public abstract class PortalEntity implements IPortal {
    protected BlockPos position;
    protected int ticksAlive = 0;
    protected int currentWave = 0;
    protected int ticksSinceLastWave = 0;

    // Interval between each wave (200 ticks = 10 seconds)
    protected static final int WAVE_INTERVAL = 200;

    public PortalEntity(BlockPos position) {
        this.position = position;
    }

    @Override
    public void tick(ServerLevel level) {
        ticksAlive++;
        ticksSinceLastWave++;

        if (shouldClose(level)) {
            close(level);
            return;
        }

        // Launch a new wave on the right time
        if (ticksSinceLastWave >= WAVE_INTERVAL && currentWave < getWaveCount()) {
            currentWave++;
            ticksSinceLastWave = 0;
            MobInvasion.LOGGER.info("[{}] Wave {} / {} on the portal in {}",
                    getPortalId(), currentWave, getWaveCount(), position);
            startWaves(level);
        }
    }

    @Override
    public boolean shouldClose(ServerLevel level) {
        // Close if the lifetime portal is over
        if (ticksAlive >= getLifetimeTicks()) return true;

        // Close the portal during the day
        long dayTime = level.getDayTime() % 24000;
        if (dayTime >= 0 && dayTime < 13000) return true;

        return false;
    }

    @Override
    public void close(ServerLevel level) {
        MobInvasion.LOGGER.info("[{}] Portal close in {}", getPortalId(), position);
    }

    public BlockPos getPosition() {
        return position;
    }
}
