package com.keko.lunastellar.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CrystalArrowProjectile extends PersistentProjectileEntity {
	public CrystalArrowProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected ItemStack asItemStack() {
		return null;
	}
}
