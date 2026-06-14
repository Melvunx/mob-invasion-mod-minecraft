package melvunx.mobinvasion.net.portal.zombie;

import melvunx.mobinvasion.net.portal.PortalEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

public class ZombiePortalEntity extends PortalEntity {
    // lifetime : 600 ticks = 30 seconds
    private static final int LIFETIME = 600;
    private static final int WAVES = 3;
    private static final int MOBS_PER_WAVE = 4;

    public ZombiePortalEntity(BlockPos position) {
        super(position);
    }

    @Override
    public String getPortalId() {
        return "zombie_portal";
    }

    @Override
    public void startWaves(ServerLevel level) {
        for (int i = 0; i < getMobsPerWave(); i++) {
            // Spawn a zombie near to the portal
            EntityType.ZOMBIE.spawn(level, position.offset(i % 3, 0, i / 3), MobSpawnType.MOB_SUMMONED);
        }
    }

    @Override
    public int getLifetimeTicks() {
        return LIFETIME;
    }

    @Override
    public int getWaveCount() {
        return WAVES;
    }

    @Override
    public int getMobsPerWave() {
        return MOBS_PER_WAVE;
    }
}
