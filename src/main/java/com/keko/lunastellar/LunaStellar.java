package com.keko.lunastellar;

import com.keko.lunastellar.item.ModItems;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LunaStellar implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("luna");
	public static final String MOD_ID = "lunastellar";

	@Override
	public void onInitialize(ModContainer mod) {
		LOGGER.info("Bow before the stars (Cringe af log ngl)", mod.metadata().name());
		ModItems.registerModitems();
	}
}
