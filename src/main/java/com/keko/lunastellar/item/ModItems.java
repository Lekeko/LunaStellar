package com.keko.lunastellar.item;

import com.keko.lunastellar.LunaStellar;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class ModItems {

	public static final Item CRYSTALPEBBLE = registerItem("crystal_pebble", new CrystalPebble(new QuiltItemSettings()));
	public static final Item CRYSTAL_ARROW = registerItem("crystal_arrow", new CrystalArrow(new QuiltItemSettings()));
	public static final Item STAR_FALLER = registerItem("star_faller", new StarFaller(new QuiltItemSettings()));
	public static final Item CELESTIAL_BELL = registerItem("celestial_bell", new CelestialBell(new QuiltItemSettings()));
	private static Item registerItem(String name , Item item){
		return Registry.register(Registry.ITEM, new Identifier(LunaStellar.MOD_ID, name), item);
	}

	public static void registerModitems(){

	}
}
