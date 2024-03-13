package com.keko.lunastellar.entities;

import com.mojang.datafixers.kinds.IdF;
import com.sammy.lodestone.network.screenshake.PositionedScreenshakePacket;
import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.systems.rendering.particle.Easing;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import io.netty.buffer.Unpooled;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.quiltmc.qsl.lifecycle.api.event.ServerTickEvents;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;

import java.awt.*;
import java.util.Random;

public class TrackingStar extends ThrownItemEntity {
	BlockPos target;
	int i = 2;
	double speed = 2;


	public TrackingStar(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
		this.target = null;
	}

	public TrackingStar(LivingEntity livingEntity, World world, BlockPos target){
		super(ModEntities.TRACKING_STAR, livingEntity, world);

		this.target = target;
	}

	int testTime = 60;
	@Override
	public void tick() {
		i--;
		Color startingColor = new Color(164, 0, 255, 255);
		Color endingColor = new Color(255, 255, 255, 255);

		ParticleBuilders.create(LodestoneParticles.SPARKLE_PARTICLE)
			.setScale(1)
			.setColor(startingColor, endingColor)
			.setLifetime(9)
			.enableNoClip()
			.spawn(this.getWorld(),
				this.getX(),
				this.getY(),
				this.getZ());
		if (testTime <= 0){
			this.discard();
		}
		if (i < 0)
			goToTarget();
		testTime--;
		super.tick();
	}
	private void goToTarget() {
		if (target != null) {
			i = 1000;
			if ((int)target.getX() == (int)this.getX() && (int)target.getY() == (int)this.getY() && (int)target.getZ() == (int)this.getZ()){
				this.discard();
			}
			Vec3d currentPos = this.getPos();
			double distanceX = target.getX() - currentPos.x;
			double distanceY = target.getY() - currentPos.y;
			double distanceZ = target.getZ() - currentPos.z;
			double distanceSquared = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
			if (distanceSquared > 1.0) {

				double scale = speed / Math.sqrt(distanceSquared);
				double velocityX = distanceX * scale;
				double velocityY = distanceY * scale;
				double velocityZ = distanceZ * scale;

				this.setVelocity(velocityX, velocityY, velocityZ);
			}
		}
	}

	@Override
	protected Item getDefaultItem() {
		return null;
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		if (entityHitResult.getEntity() != this.getOwner() || (Entity)entityHitResult.getEntity() instanceof TrackingStar){
			createStarBoom(this.getX(), this.getY(), this.getZ());
		}
			this.discard();
		System.out.println(entityHitResult.getEntity());

		super.onEntityHit(entityHitResult);
	}

	@Override
	protected void onBlockCollision(BlockState state) {
		if (!state.isOf(Blocks.AIR) && !state.isOf(Blocks.GRASS) && !state.isOf(Blocks.TALL_GRASS) && !state.isOf(Blocks.WATER) && !state.isOf(Blocks.KELP)) {
			createStarBoom(this.getX(), this.getY(), this.getZ());
				this.discard();
		}

	}


	@Override
	public void onRemoved() {
		createStarBoom(this.getX(), this.getY(), this.getZ());
		super.onRemoved();
	}

	private void createSound(double x, double y, double z) {
		world.playSound((PlayerEntity)null, x, y, z, SoundEvents.BLOCK_BEACON_AMBIENT , SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		world.playSound((PlayerEntity)null,x, y, z, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK , SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
	}

	private void createStarBoom(double centerx, double centery, double centerz ) {
		ParticleBuilders.create(LodestoneParticles.STAR_PARTICLE)
			.disableForcedMotion()
			.setColor(new Color(237, 0, 255, 255), new Color(255, 255, 255, 255))
			.setScale(1, 20)
			.setLifetime(50)
			.enableNoClip()
			.spawn(world, centerx, centery, centerz);

			int radius = 7;Random random1 = new Random();
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						int checker = x * x + y * y + z * z;
						if (checker <= radius * radius) {

							int chance = random1.nextInt(100) + 1;

							BlockPos blockPos = new BlockPos(this.getPos().getX() + x,  this.getPos().getY()  + y,  this.getPos().getZ() + z);
							Box box = new Box(blockPos);
							for (Entity entity : world.getOtherEntities(null, box)){
								if (!(entity instanceof ItemEntity))
									entity.damage(DamageSource.MAGIC, 5);
							}

								Color startingColor = new Color(237, 0, 255, 255);
								Color endingColor = new Color(255, 255, 255, 255);
								if (chance > 99){
									if(random1.nextInt(100)+ 1> 50){
										startingColor = endingColor;
									}



								ParticleBuilders.create(LodestoneParticles.SPARKLE_PARTICLE)
									.disableForcedMotion()
									.setColor(startingColor, endingColor)
									.setScale(1)
									.setLifetime(20)
									.addMotion(x/1.5, y/1.5, z/1.5)
									.enableNoClip()
									.spawn(world, x + centerx, y+  centery, centerz+ z);
							}
						}

					}
				}


		}this.discard();
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		double x = this.getX();
		double y = this.getY();
		double z = this.getZ();


		world.playSound((PlayerEntity)null, x, y, z, SoundEvents.BLOCK_BEACON_ACTIVATE , SoundCategory.NEUTRAL, 2F, 0.8F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		world.playSound((PlayerEntity)null,x, y, z, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK , SoundCategory.NEUTRAL, 2F, 0.8F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		super.onCollision(hitResult);
	}
}
