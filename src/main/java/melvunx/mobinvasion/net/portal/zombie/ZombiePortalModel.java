package melvunx.mobinvasion.net.portal.zombie;

import melvunx.mobinvasion.net.MobInvasion;
import melvunx.mobinvasion.net.portal.base.PortalModel;
import net.minecraft.resources.ResourceLocation;



public class ZombiePortalModel extends PortalModel<ZombiePortalBlockEntity> {

    @Override
    public ResourceLocation getModelResource(ZombiePortalBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(MobInvasion.MOD_ID, "geo/portal.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ZombiePortalBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(MobInvasion.MOD_ID, "textures/block/portal_blue.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ZombiePortalBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(MobInvasion.MOD_ID, "animations/portal.animations.json");
    }
}
