package com.keko.lunastellar;

import com.keko.lunastellar.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LunaStellar implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("luna");
	public static final String MOD_ID = "lunastellar";
	private static final Identifier AMETHYST_CLUSTER_LOOT_TABLE_ID = Blocks.AMETHYST_CLUSTER.getLootTableId();
	@Override
	public void onInitialize(ModContainer mod) {
		LootNumberProvider lootNumberProvider = new LootNumberProvider() {
			@Override
			public float nextFloat(LootContext context) {
				return 3;
			}

			@Override
			public LootNumberProviderType getType() {
				return null;
			}
		};


		LootTableEvents.MODIFY.register(((resourceManager, lootManager, id, tableBuilder, source) -> {
			if (source.isBuiltin() && AMETHYST_CLUSTER_LOOT_TABLE_ID.equals(id)){
				LootPool.Builder poolBuilder = LootPool.builder().rolls(lootNumberProvider).
					with(ItemEntry.builder(ModItems.CRYSTALPEBBLE));

				tableBuilder.pool(poolBuilder);
			}
		}));

		LOGGER.info("Bow before the stars (Cringe af log ngl)", mod.metadata().name());
		ModItems.registerModitems();
	}
}
