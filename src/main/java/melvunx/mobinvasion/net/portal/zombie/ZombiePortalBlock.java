package melvunx.mobinvasion.net.portal.zombie;

import com.mojang.serialization.MapCodec;
import melvunx.mobinvasion.net.MobInvasion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ZombiePortalBlock extends BaseEntityBlock {

    public static final MapCodec<ZombiePortalBlock> CODEC = simpleCodec(ZombiePortalBlock::new);

    public ZombiePortalBlock() {
        super(BlockBehaviour.Properties.of()
                .noCollission()
                .lightLevel(state -> 10)
                .strength(-1.0f, 3600000.0f) // indestructible
        );
    }

    @Override
    public MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    // -----------------------------------------------
    // BlockEntity
    // -----------------------------------------------
    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ZombiePortalBlockEntity(pos, state);
    }

    // Rendu custom via GeckoLib
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    // -----------------------------------------------
    // Tick pour gérer les transitions d'états
    // -----------------------------------------------
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {

        if (level.isClientSide()) return null;

        return createTickerHelper(type, MobInvasion.ZOMBIE_PORTAL_BLOCK_ENTITY,
                (w, pos, s, blockEntity) -> blockEntity.tick(w, pos, s)
        );
    }
}
