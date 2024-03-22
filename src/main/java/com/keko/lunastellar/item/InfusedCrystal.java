package com.keko.lunastellar.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InfusedCrystal extends Item {
	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}

	public InfusedCrystal(Settings settings) {
		super(settings);
	}
}
