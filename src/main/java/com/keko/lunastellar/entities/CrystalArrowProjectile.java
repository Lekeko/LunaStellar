package com.keko.lunastellar.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;

public class CrystalArrowProjectile extends ArrowEntity {
	public CrystalArrowProjectile(EntityType<? extends ArrowEntity> entityType, World world) {
		super(entityType, world);
	}
}
