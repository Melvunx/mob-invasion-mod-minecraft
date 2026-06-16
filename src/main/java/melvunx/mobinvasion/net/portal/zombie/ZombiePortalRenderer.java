package melvunx.mobinvasion.net.portal.zombie;

import melvunx.mobinvasion.net.MobInvasion;
import melvunx.mobinvasion.net.portal.base.PortalRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;


public class ZombiePortalRenderer extends PortalRenderer<ZombiePortalBlockEntity> {

    public ZombiePortalRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new ZombiePortalModel());
    }

    @Override
    public ResourceLocation getTextureLocation(ZombiePortalBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(MobInvasion.MOD_ID, "textures/block/portal_blue.png");
    }
}
