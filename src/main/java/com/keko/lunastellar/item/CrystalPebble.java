package com.keko.lunastellar.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class CrystalPebble extends ThrownItemEntity {
	public CrystalPebble(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	protected Item getDefaultItem() {
		return null;
	}
}
