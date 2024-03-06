package com.keko.lunastellar.entities;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BigFallingStar  extends ThrownItemEntity {
	public BigFallingStar(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	public BigFallingStar(LivingEntity livingEntity, World world){
		super(ModEntities.BIG_FALLING_STAR, livingEntity, world);
	}

	@Override
	protected Item getDefaultItem() {
		return null;
	}
}
