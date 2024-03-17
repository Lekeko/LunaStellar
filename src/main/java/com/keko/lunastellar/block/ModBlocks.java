package com.keko.lunastellar.block;

import com.keko.lunastellar.LunaStellar;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.block.extensions.api.QuiltBlockSettings;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;


public class ModBlocks {
	public static final Block CRYSTAL_CAMPFIRE = registerBlock("crystal_campfire",
		new CrystalCampfire(QuiltBlockSettings.copyOf(Blocks.AMETHYST_CLUSTER).breakInstantly().nonOpaque().luminance((state) -> {
			return 14;})));



	private static Block registerBlock(String name, Block block){
		registerBlockItem(name, block);
		return Registry.register(Registry.BLOCK, new Identifier(LunaStellar.MOD_ID, name), block);
	}

	private static Item registerBlockItem(String name, Block block){
		return Registry.register(Registry.ITEM, new Identifier(LunaStellar.MOD_ID, name),
			new BlockItem(block, new QuiltItemSettings()));
	}

	public static void registerModBlocks() {

	}

}
