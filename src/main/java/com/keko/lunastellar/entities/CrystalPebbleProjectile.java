package com.keko.lunastellar.entities;

import com.keko.lunastellar.entities.ModEntities;
import com.keko.lunastellar.item.ModItems;
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

public class CrystalPebbleProjectile extends ThrownItemEntity {
	public CrystalPebbleProjectile(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	public CrystalPebbleProjectile(LivingEntity livingEntity, World world){
		super(ModEntities.CRYSTAL_PEBBLE_PROJECTILE, livingEntity, world);
	}

	private ParticleEffect getParticleParameters() {
		ItemStack itemStack = this.getItem();
		return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.ITEM : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
	}

	public void handleStatus(byte status) {
		if (status == 3) {
			ParticleEffect particleEffect = this.getParticleParameters();

			for(int i = 0; i < 8; ++i) {
				this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
			}
		}

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
		Entity entity = entityHitResult.getEntity();
		float damage = 4.5f;
		entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), damage);
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
		if (!this.getWorld().isClient){
			this.getWorld().sendEntityStatus(this, (byte) 3);
		}
		world.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(),
			SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.NEUTRAL,
			0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

		super.onBlockHit(blockHitResult);
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
