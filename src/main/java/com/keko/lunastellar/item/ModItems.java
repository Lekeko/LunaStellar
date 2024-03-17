package com.keko.lunastellar.item;

import com.keko.lunastellar.LunaStellar;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class ModItems {

	public static final Item CRYSTALPEBBLE = registerItem("crystal_pebble", new CrystalPebble(new QuiltItemSettings().group(ItemGroup.COMBAT)));
	public static final Item STAR_FALLER = registerItem("star_faller", new StarFaller(new QuiltItemSettings().group(ItemGroup.COMBAT)));
	public static final Item CELESTIAL_BELL = registerItem("celestial_bell", new CelestialBell(new QuiltItemSettings().group(ItemGroup.COMBAT)));
	public static final Item INFUSED_CRYSTAL = registerItem("infused_crystal", new InfusedCrystal(new QuiltItemSettings().group(ItemGroup.MATERIALS)));

	private static Item registerItem(String name , Item item){
		return Registry.register(Registry.ITEM, new Identifier(LunaStellar.MOD_ID, name), item);
	}

	public static void registerModitems(){

	}
}
