package com.keko.lunastellar.entities;

import com.keko.lunastellar.entities.ModEntities;
import com.keko.lunastellar.item.ModItems;
import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Random;

public class CrystalPebbleProjectile extends ThrownItemEntity {
	private final float damage = 4.5f;
	public CrystalPebbleProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	public CrystalPebbleProjectile(LivingEntity livingEntity, World world){
		super(ModEntities.CRYSTAL_PEBBLE_PROJECTILE, livingEntity, world);
	}

	@Override
	protected Item getDefaultItem() {
		return ModItems.CRYSTALPEBBLE;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		super.onEntityHit(entityHitResult);
		if (!(entityHitResult.getEntity() == this.getOwner())) {
			Entity entity = entityHitResult.getEntity();
			entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), damage);
			world.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(),
				SoundEvents.BLOCK_AMETHYST_BLOCK_FALL, SoundCategory.NEUTRAL,
				0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		}
	}


	private void createParticleEffect() {



		Color startingColor = new Color(179, 85, 255, 255);
		Color endingColor = new Color(242, 223, 255, 255);
		for (int i = 0; i < 2; i++)
			ParticleBuilders.create(LodestoneParticles.SPARKLE_PARTICLE)
			.setScale(0, 1)
			.setColor(startingColor, endingColor)
			.setLifetime(5)
			.addMotion(0, 0.01f, 0)
			.enableNoClip()
			.spawn(this.getWorld(),this.getX(), this.getY(), this.getZ());

	}
	@Override
	public void onRemoved() {
		createParticleEffect();

		super.onRemoved();
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);
		if (!this.world.isClient) {
			this.world.sendEntityStatus(this, (byte)3);
			this.discard();
		}
	}
}
