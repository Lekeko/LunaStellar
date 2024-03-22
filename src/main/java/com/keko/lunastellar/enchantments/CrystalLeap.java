package com.keko.lunastellar.enchantments;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Arm;


public class CrystalLeap extends Enchantment {

	protected CrystalLeap(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
		super(weight, type, slotTypes);
	}


	public CrystalLeap(){
		super(Rarity.COMMON, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
	}

	@Override
	public boolean isAcceptableItem(ItemStack stack) {
		if (stack.getItem() instanceof ArmorItem && stack.getItem().asItem().getName().toString().contains("Boots")){
			return true;
		} return false;
	}

	@Override
	public boolean isAvailableForEnchantedBookOffer() {
		return true;
	}

	@Override
	public boolean isAvailableForRandomSelection() {
		return true;
	}


	@Override
	public int getMinLevel() {
		return 1;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public int getMaxPower(int level) {
		return 2;
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return true;
	}
}
