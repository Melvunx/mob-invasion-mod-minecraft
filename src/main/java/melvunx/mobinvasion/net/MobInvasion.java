package melvunx.mobinvasion.net;

import melvunx.mobinvasion.net.command.PortalCommand;
import melvunx.mobinvasion.net.portal.base.spawn.PortalSpawnManager;
import melvunx.mobinvasion.net.portal.zombie.ZombiePortalBlock;
import melvunx.mobinvasion.net.portal.zombie.ZombiePortalBlockEntity;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobInvasion implements ModInitializer {
	public static final String MOD_ID = "mob-invasion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ZombiePortalBlock ZOMBIE_PORTAL_BLOCK;
	public static BlockEntityType<ZombiePortalBlockEntity> ZOMBIE_PORTAL_BLOCK_ENTITY;

	@Override
	public void onInitialize() {

		LOGGER.info("Mob Invasion ! - Loading mod ...");
		PortalSpawnManager.register();

		// Enregistrement du block
		ZOMBIE_PORTAL_BLOCK = Registry.register(
				BuiltInRegistries.BLOCK,
				ResourceLocation.fromNamespaceAndPath(MOD_ID, "zombie_portal"),
				new ZombiePortalBlock()
		);

		// Enregistrement du BlockEntity
		ZOMBIE_PORTAL_BLOCK_ENTITY = Registry.register(
				BuiltInRegistries.BLOCK_ENTITY_TYPE,
				ResourceLocation.fromNamespaceAndPath(MOD_ID, "zombie_portal"),
				BlockEntityType.Builder.of(ZombiePortalBlockEntity::new, ZOMBIE_PORTAL_BLOCK).build()
		);


		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			PortalCommand.register(dispatcher);
		});
	}
}