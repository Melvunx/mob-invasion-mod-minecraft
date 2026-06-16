package melvunx.mobinvasion.net.portal.base;


import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;


public abstract class PortalModel<T extends PortalBlockEntity> extends GeoModel<T> {

    // Chaque portail définit ses propres ressources
    @Override
    public abstract ResourceLocation getModelResource(T animatable);

    @Override
    public abstract ResourceLocation getTextureResource(T animatable);

    @Override
    public abstract ResourceLocation getAnimationResource(T animatable);
}
