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
			.setDimensions(EntityDimensions.fixed(0.4f, 0.4f)).build());

	public static final EntityType<TrackingStar> TRACKING_STAR = Registry.register(Registry.ENTITY_TYPE,
		new Identifier(LunaStellar.MOD_ID, "tracking_star"), QuiltEntityTypeBuilder.<TrackingStar>create(SpawnGroup.MISC, TrackingStar::new)
			.setDimensions(EntityDimensions.fixed(0.1f, 0.1f)).build());


	public static final EntityType<FallingStar> FALLING_STAR = Registry.register(Registry.ENTITY_TYPE,
		new Identifier(LunaStellar.MOD_ID, "falling_star"), QuiltEntityTypeBuilder.<FallingStar>create(SpawnGroup.MISC, FallingStar::new)
			.setDimensions(EntityDimensions.fixed(0.1f, 0.1f)).build());
}
