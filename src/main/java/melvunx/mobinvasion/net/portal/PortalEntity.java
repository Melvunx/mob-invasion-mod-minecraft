package melvunx.mobinvasion.net.portal;

import melvunx.mobinvasion.net.MobInvasion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

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

        // Every tick
        spawnParticles(level);
        playAmbientSound(level);

        // Première vague immédiatement à l'ouverture
        if (currentWave == 0) {
            currentWave++;
            ticksSinceLastWave = 0;
            MobInvasion.LOGGER.info("[{}] Wave {} / {} on the portal in {}",
                    getPortalId(), currentWave, getWaveCount(), position);
            startWaves(level);
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

    //  Portal particles
    protected void spawnParticles(ServerLevel level) {
        double cx = position.getX() + 0.5;
        double cy = position.getY();
        double cz = position.getZ() + 0.5;

        // Elliptic portal form  (debut, axe Y)
        for (int i = 0; i < 20; i++) {
            double angle = (2 * Math.PI / 20) * i;
            double x = cx + Math.cos(angle) * 1.5;
            double y = cy + Math.sin(angle) * 2.5;
            double z = cz;

            level.sendParticles(
                    getPortalParticle(),
                    x, y, z,
                    1,      // count
                    0, 0, 0, // offset XYZ
                    0.05    // speed
            );
        }

        // Interns particles
        for (int i = 0; i < 5; i++) {
            double x = cx + (Math.random() - 0.5) * 2;
            double y = cy + Math.random() * 4;
            double z = cz + (Math.random() - 0.5) * 0.2;

            level.sendParticles(
                    getPortalParticle(),
                    x, y, z,
                    1,
                    0, 0.05, 0,
                    0.02
            );
        }
    }

    // Each portal can défine its own particles
    protected net.minecraft.core.particles.ParticleOptions getPortalParticle() {
        return ParticleTypes.PORTAL;
    }

    public void open(ServerLevel level) {
        level.playSound(
                null,
                position,
                SoundEvents.PORTAL_TRIGGER,
                SoundSource.AMBIENT,
                1.0f,  // volume
                0.5f   // pitch
        );
        MobInvasion.LOGGER.info("[{}] Portal open in {}", getPortalId(), position);
    }

    protected void playAmbientSound(ServerLevel level) {
        if (ticksAlive % 60 == 0) {
            level.playSound(
                    null,
                    position,
                    SoundEvents.PORTAL_AMBIENT,
                    SoundSource.AMBIENT,
                    0.5f,  // volume discrete
                    1.0f
            );
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
        // Little explosion at the end
        double cx = position.getX() + 0.5;
        double cy = position.getY() + 2;
        double cz = position.getZ() + 0.5;

        level.sendParticles(
                ParticleTypes.EXPLOSION,
                cx, cy, cz,
                5,
                1, 1, 1,
                0.1
        );

        // Son de fermeture
        level.playSound(
                null,
                position,
                SoundEvents.PORTAL_TRAVEL,
                SoundSource.AMBIENT,
                1.0f,
                0.8f
        );

        MobInvasion.LOGGER.info("[{}] Portal close in {}", getPortalId(), position);
    }

    public BlockPos getPosition() {
        return position;
    }
}
