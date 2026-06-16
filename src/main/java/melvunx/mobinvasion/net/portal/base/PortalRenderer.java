package melvunx.mobinvasion.net.portal.base;


import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public abstract class PortalRenderer<T extends PortalBlockEntity> extends GeoBlockRenderer<T> {

    public PortalRenderer(BlockEntityRendererProvider.Context context, PortalModel<T> model) {
        super(model);
    }

    @Override
    public abstract ResourceLocation getTextureLocation(T animatable);
}

