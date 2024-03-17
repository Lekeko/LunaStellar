package com.keko.lunastellar.enchantments;

import com.keko.lunastellar.LunaStellar;
import com.keko.lunastellar.item.InfusedCrystal;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.item.setting.api.QuiltItemSettings;

public class ModEnch {
	public static final Enchantment CRYSTAL_LEAP = registerEnchantment("crystal_leap", new CrystalLeap());

	private static Enchantment registerEnchantment(String name , Enchantment item){
		return Registry.register(Registry.ENCHANTMENT, new Identifier(LunaStellar.MOD_ID, name), item);
	}

	public static void registerModEnchantment(){

	}

}
