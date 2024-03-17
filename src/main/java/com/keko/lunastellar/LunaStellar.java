package com.keko.lunastellar;

import com.keko.lunastellar.block.ModBlocks;
import com.keko.lunastellar.customParticles.StarExplosion;
import com.keko.lunastellar.enchantments.ModEnch;
import com.keko.lunastellar.item.ModItems;
import com.mojang.blaze3d.platform.InputUtil;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.include.com.google.common.collect.ImmutableMap;

import javax.swing.*;
import javax.swing.text.JTextComponent;

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
		LootNumberProvider lootNumberProvider2 = new LootNumberProvider() {
			@Override
			public float nextFloat(LootContext context) {
				return 1;
			}

			@Override
			public LootNumberProviderType getType() {
				return null;
			}
		};
		ItemStack itemStack = new ItemStack(Items.ENCHANTED_BOOK);
		itemStack.addEnchantment(ModEnch.CRYSTAL_LEAP, 1);

		LootTableLoadingCallback.EVENT.register((resourceManager, lootManager, id, supplier, setter) -> {
			if (id.equals(LootTables.ANCIENT_CITY_CHEST)) {
				supplier.withPool(LootPool.builder()
					.rolls(lootNumberProvider2) // Number of items to roll
					.conditionally(RandomChanceLootCondition.builder(70f))
					.with(ItemEntry.builder(itemStack.getItem()))
					.build());
			}
		});

		LOGGER.info("Bow before the stars (Cringe af log ngl)", mod.metadata().name());
		ModBlocks.registerModBlocks();
		ModEnch.registerModEnchantment();
		ModItems.registerModitems();
		StarExplosion.init();
		StarExplosion.registerFactories();
	}

}
