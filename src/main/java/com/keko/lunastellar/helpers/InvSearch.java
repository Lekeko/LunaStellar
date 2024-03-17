package com.keko.lunastellar.helpers;

import com.keko.lunastellar.item.InfusedCrystal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InvSearch {
	public static ItemStack hasItemInInv(PlayerEntity user, Item item){
		for (ItemStack stack : user.getInventory().main){
			if (stack.isOf(item)){
				return stack;
			}
		}
		return null;
	}
}
