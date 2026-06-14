package melvunx.mobinvasion.net;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MobInvasion implements ModInitializer {
	public static final String MOD_ID = "mob-invasion";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Mob Invasion ! - Loading mod ...");
	}
}