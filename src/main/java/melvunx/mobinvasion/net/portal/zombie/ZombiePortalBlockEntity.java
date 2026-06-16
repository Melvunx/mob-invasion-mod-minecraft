package melvunx.mobinvasion.net.portal.zombie;

import melvunx.mobinvasion.net.portal.base.PortalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;


public class ZombiePortalBlockEntity extends PortalBlockEntity {

    public ZombiePortalBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected String getControllerName() { return "zombie_portal_controller"; }

    @Override
    protected String getOpenAnimationName() { return "open"; }

    @Override
    protected String getIdleAnimationName() { return "idle"; }

    @Override
    protected String getCloseAnimationName() { return "close"; }

    @Override
    public void onPortalTick() {
        // TODO : spawn zombies, effets de particules, sons...
    }
}
