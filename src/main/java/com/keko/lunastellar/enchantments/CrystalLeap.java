package com.keko.lunastellar.enchantments;


import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Arm;


public class CrystalLeap extends Enchantment {

	protected CrystalLeap(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
		super(weight, EnchantmentTarget.ARMOR_FEET, slotTypes);
	}
	public CrystalLeap() {
		super(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[] {EquipmentSlot.FEET, EquipmentSlot.MAINHAND});
	}
	@Override
	public int getMaxLevel() {
		return 1;
	}

	@Override
	public boolean isTreasure() {
		return  false;
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
	public boolean isAcceptableItem(ItemStack stack) {
		return true;
	}


	@Override
	public int getMinPower(int level) {
		return 12;
	}

	@Override
	protected boolean canAccept(Enchantment other) {
		return super.canAccept(other);
	}
}
