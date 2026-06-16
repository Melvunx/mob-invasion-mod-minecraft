package melvunx.mobinvasion.net.portal.base;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class PortalBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public enum PortalState {
        CLOSED, OPENING, IDLE, CLOSING
    }

    private PortalState state = PortalState.CLOSED;

    public PortalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    // -----------------------------------------------
    // Méthodes abstraites à implémenter par les portails customs
    // -----------------------------------------------

    /** Nom du controller geckolib ex: "zombie_portal_controller" */
    protected abstract String getControllerName();

    /** Nom de l'animation d'ouverture dans le JSON */
    protected abstract String getOpenAnimationName();

    /** Nom de l'animation idle dans le JSON */
    protected abstract String getIdleAnimationName();

    /** Nom de l'animation de fermeture dans le JSON */
    protected abstract String getCloseAnimationName();

    /** Logique custom au tick (spawn de mobs, effets...) */
    public abstract void onPortalTick();

    // -----------------------------------------------
    // Gestion des états
    // -----------------------------------------------

    public void open() {
        this.state = PortalState.OPENING;
        setChanged();
    }

    public void setIdle() {
        this.state = PortalState.IDLE;
        setChanged();
    }

    public void close() {
        this.state = PortalState.CLOSING;
        setChanged();
    }

    public PortalState getState() {
        return state;
    }

    public boolean isActive() {
        return state == PortalState.IDLE || state == PortalState.OPENING;
    }

    // -----------------------------------------------
    // GeckoLib
    // -----------------------------------------------

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, getControllerName(), 0, this::animationHandler));
    }

    private PlayState animationHandler(AnimationState<PortalBlockEntity> event) {
        AnimationController<?> controller = event.getController();

        switch (state) {
            case OPENING -> controller.setAnimation(
                    RawAnimation.begin().then(getOpenAnimationName(), Animation.LoopType.PLAY_ONCE)
            );
            case IDLE -> controller.setAnimation(
                    RawAnimation.begin().then(getIdleAnimationName(), Animation.LoopType.LOOP)
            );
            case CLOSING -> controller.setAnimation(
                    RawAnimation.begin().then(getCloseAnimationName(), Animation.LoopType.PLAY_ONCE)
            );
            default -> { return PlayState.STOP; }
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
