package com.keko.lunastellar.entities;

import com.keko.lunastellar.LunaStellar;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.quiltmc.qsl.entity.api.QuiltEntityTypeBuilder;

public class ModEntities {


	public static final EntityType<CrystalPebbleProjectile> CRYSTAL_PEBBLE_PROJECTILE = Registry.register(Registry.ENTITY_TYPE,
		new Identifier(LunaStellar.MOD_ID, "crystal_pebble"), QuiltEntityTypeBuilder.<CrystalPebbleProjectile>create(SpawnGroup.MISC, CrystalPebbleProjectile::new)
			.setDimensions(EntityDimensions.fixed(0.2f, 0.2f)).build());

}
