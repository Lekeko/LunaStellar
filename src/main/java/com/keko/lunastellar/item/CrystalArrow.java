package com.keko.lunastellar.item;

import com.keko.lunastellar.entities.CrystalArrowProjectile;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CrystalArrow extends ArrowItem {
	public CrystalArrow(Settings settings) {
		super(settings);
	}

	public PersistentProjectileEntity createArrow(World world, ItemStack stack, EntityType shooter) {
		return new CrystalArrowProjectile(shooter, world);
	}


}
