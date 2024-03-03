package com.keko.lunastellar.entities;

import com.keko.lunastellar.customParticles.StarExplosion;
import com.keko.lunastellar.item.ModItems;
import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.awt.*;
import java.util.Random;

public class FallingStar extends ThrownItemEntity {
	private int speed = 1;
	int i  = 1;

	public FallingStar(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
	}

	public FallingStar(LivingEntity livingEntity, World world){
		super(ModEntities.FALLING_STAR, livingEntity, world);
	}

	@Override
	public void tick() {
		//this.setPos(this.getX(), this.getY() - speed, this.getZ());
		if (i == 1){
			createStartEffect(); i--;
		}
		createStarEffect();
		super.tick();
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		world.createExplosion(null, getX(), getY(), getZ(), 2, Explosion.DestructionType.NONE);
		this.discard();
		super.onCollision(hitResult);
	}

	@Override
	public void onRemoved() {
		Random random1 = new Random();
		Color startingColor = new Color(255, 85, 159, 50);
		Color endingColor = new Color(255, 255, 255, 60);
		BlockPos pos = new BlockPos(getX(), getY(), getZ());
		for (int i = 0; i < 27; i++){
			pos = new BlockPos(getX() + random1.nextInt(2) - 1 , getY() + random1.nextInt(2) - 1 , getZ() + random1.nextInt(2) - 1 );
			ParticleBuilders.create(StarExplosion.STAR_EXPLOSION)
				.setScale(2, 6, (float)(10 - i * 0.5))
				.setColor(startingColor, endingColor)
				.setLifetime(25)
				.setSpin(i % 2 == 0 ? 1 : -1)
				.setMotion(0, 0.1f , 0)
				.enableNoClip()
				.evenlySpawnAtAlignedEdges(world, pos , world.getBlockState(pos), 2)
				.spawnAtEdges(this.getWorld(), pos);}

		super.onRemoved();
	}

	private void createStartEffect() {
		Color startingColor = new Color(255, 85, 159, 255);
		Color endingColor = new Color(255, 255, 255, 255);
		ParticleBuilders.create(LodestoneParticles.STAR_PARTICLE)
			.setScale(3)
			.setColor(startingColor, endingColor)
			.setLifetime(101)
			.setMotion(this.getVelocity().getX() * 1.1, this.getVelocity().getY() * 1.3, this.getVelocity().getZ() * 1.1)
			.enableNoClip()
			.spawn(this.getWorld(),this.getX(), this.getY(), this.getZ());
		createCircleEffect(this.getX(), this.getY(), this.getZ(), world);

	}

	public static void createCircleEffect(double centerx, double centery, double centerz, World world) {
		float radius = 4;
		for (float x = -radius; x <= radius; x+=0.1) {
			for (float z = -radius; z <= radius; z+=0.1) {
				float checker = x*x + z*z;
				if (Math.ceil(checker) == radius * radius) {
					Color endingColor = new Color(220, 0, 255);
					Color startingColor = new Color(251, 235, 255);
					ParticleBuilders.create(LodestoneParticles.WISP_PARTICLE)
						.disableForcedMotion()
						.setColor(startingColor, endingColor)
						.setScale(1, 0)
						.setLifetime(20)
						.addMotion(x/50*centery/50, 0, z/50*centery/50)
						.enableNoClip()
						.spawn(world, x + centerx, centery, centerz+ z);
				}
			}
		}
	}

	private void createStarEffect() {
		Color startingColor = new Color(229, 85, 255, 255);
		Color endingColor = new Color(240, 215, 255, 255);
		ParticleBuilders.create(LodestoneParticles.STAR_PARTICLE)
			.setScale(2)
			.setColor(startingColor, endingColor)
			.setLifetime(9)
			.enableNoClip()
			.spawn(this.getWorld(),this.getX(), this.getY(), this.getZ());

	}

	@Override
	protected Item getDefaultItem() {
		return null;
	}
	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}
}
