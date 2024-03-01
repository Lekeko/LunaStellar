package com.keko.lunastellar.item;

import com.keko.lunastellar.LunaStellar;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModItems {

	private static Item registerItem(String name , Item item){
		return Registry.register(Registry.ITEM, new Identifier(LunaStellar.MOD_ID, name), item);
	}

	public static void registerModitems(){

	}
}
