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

    private static final int[][] ZOMBIES_PER_WAVE = {
            {2, 5}, // First wave
            {4, 7}, // Second wave
            {7, 10} // Third wave
    };

    public ZombiePortalEntity(BlockPos position) {
        super(position);
    }


    @Override
    public void startWaves(ServerLevel level) {
        int waveIndex = currentWave - 1;
        if (waveIndex < 0 || waveIndex >= ZOMBIES_PER_WAVE.length) return;

        int min = ZOMBIES_PER_WAVE[waveIndex][0];
        int max = ZOMBIES_PER_WAVE[waveIndex][1];
        int count = min + level.random.nextInt(max - min + 1);

        for (int i = 0; i < count; i++) {
            int offsetX = (level.random.nextInt(5)) - 2; // -2 à +2
            int offsetZ = (level.random.nextInt(5)) - 2;

            EntityType.ZOMBIE.spawn(
                    level,
                    position.offset(offsetX, 0, offsetZ),
                    MobSpawnType.MOB_SUMMONED
            );
        }
    }

    @Override
    protected net.minecraft.core.particles.ParticleOptions getPortalParticle() {
        // Green particles
        return net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER;
    }

    @Override
    public String getPortalId() {
        return "zombie_portal";
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
        return ZOMBIES_PER_WAVE[Math.min(currentWave, WAVES - 1)][1];
    }
}
