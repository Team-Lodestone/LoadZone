package me.dexrn.loadzone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;

public class LoadZone implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("LoadZone");
	public static final String VERSION = "1.1.0";

	@Override
	public void onInitialize() {
		LOGGER.info("Loading the Zone™...");
	}
}
