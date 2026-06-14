package melvunx.mobinvasion.net;

import melvunx.mobinvasion.net.command.PortalCommand;
import melvunx.mobinvasion.net.spawn.PortalSpawnManager;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobInvasion implements ModInitializer {
	public static final String MOD_ID = "mob-invasion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Mob Invasion ! - Loading mod ...");

		PortalSpawnManager.register();

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			PortalCommand.register(dispatcher);
		});
	}
}