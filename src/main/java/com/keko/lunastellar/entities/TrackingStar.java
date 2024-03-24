package com.keko.lunastellar.entities;

import com.keko.lunastellar.helpers.InvSearch;
import com.keko.lunastellar.item.ModItems;
import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import com.sammy.lodestone.systems.rendering.particle.type.LodestoneParticleType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;
import java.util.Random;

public class TrackingStar extends ThrownItemEntity {
	BlockPos target;
	int i = 2;
	double speed = 3.6;
	private  Color startingColor = new Color(237, 0, 255, 255);
	private  Color endingColor = Color.WHITE;



	public TrackingStar(EntityType<? extends ThrownItemEntity> entityType, World world) {
		super(entityType, world);
		this.target = null;
	}


	public TrackingStar(LivingEntity livingEntity, World world, BlockPos target, Color c1, Color c2){
		super(ModEntities.TRACKING_STAR, livingEntity, world);
		if (c1 != null && c2 != null){
		startingColor = c1;
		endingColor = c2;}
		this.target = target;
	}

	int testTime = 60;
	@Override
	public void tick() {

		if (world.isClient)
			createParticle(LodestoneParticles.SPARKLE_PARTICLE, 1, 9, new Vec3d(0, 0, 0), this.getX(), this.getY(), this.getZ());
		if (!world.isClient){
			i--;
			if (testTime <= 0) this.discard();
			if (i < 0) goToTarget();
			testTime--;
		}
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
		if (!world.isClient) {
			if (entityHitResult.getEntity() != this.getOwner() || (Entity) entityHitResult.getEntity() instanceof TrackingStar) {
				createStarBoom(this.getX(), this.getY(), this.getZ());
			}
			this.discard();
		}

		super.onEntityHit(entityHitResult);
	}

	@Override
	protected void onBlockCollision(BlockState state) {
		if (!world.isClient) {
			if (!state.isOf(Blocks.AIR) && !state.isOf(Blocks.GRASS) && !state.isOf(Blocks.TALL_GRASS) && !state.isOf(Blocks.WATER) && !state.isOf(Blocks.KELP)) {
				createStarBoom(this.getX(), this.getY(), this.getZ());
				this.discard();
			}
		}

	}


	@Override
	public void onRemoved() {
		createStarBoom(this.getX(), this.getY(), this.getZ());
		super.onRemoved();
	}

	private void createStarBoom(double centerx, double centery, double centerz ) {
		if (world.isClient) createParticle(LodestoneParticles.STAR_PARTICLE, 1, 20 , 50, new Vec3d(0,0,0), centerx, centery, centerz);

			int radius = 7;Random random1 = new Random();
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						int checker = x * x + y * y + z * z;
						if (checker <= radius * radius) {

							int chance = random1.nextInt(100) + 1;

							BlockPos blockPos = new BlockPos(this.getPos().getX() + x,  this.getPos().getY()  + y,  this.getPos().getZ() + z);
							Box box = new Box(blockPos);
							if (!world.isClient) {
								for (Entity entity : world.getOtherEntities(null, box)) {
									if (entity instanceof EnderDragonEntity) {
										entity.damage(DamageSource.player((PlayerEntity) this.getOwner()), 19);
									}
									if (!(entity instanceof ItemEntity))
										if (world.getRegistryKey() == World.END)
											entity.damage(DamageSource.MAGIC, 19);
										else
											entity.damage(DamageSource.MAGIC, 9);
								}
							}

								if (chance > 99){
									if(random1.nextInt(100)+ 1> 50){
										startingColor = endingColor;
									}


									if (world.isClient) createParticle(LodestoneParticles.SPARKLE_PARTICLE, 1, 20, new Vec3d(x/1.5, y/1.5, z/1.5), x + centerx, y+  centery, centerz+ z);
							}
						}

					}
				}


		}this.discard();
	}

	public void createParticle(LodestoneParticleType particles, int scale, int lifeTime, Vec3d motion, double x, double y, double z){
		try {
			ParticleBuilders.create(particles)
				.disableForcedMotion()
				.setColor(startingColor, endingColor)
				.setScale(scale)
				.setLifetime(lifeTime)
				.addMotion(motion.getX(), motion.getY(), motion.getZ())
				.enableNoClip()
				.spawn(world, x, y, z);
		}catch (Exception ignored){}
	}

	public void createParticle(LodestoneParticleType particles, int scale1, int scale2, int lifeTime, Vec3d motion, double x, double y, double z){

		try {
			ParticleBuilders.create(particles)
				.disableForcedMotion()
				.setColor(startingColor, endingColor)
				.setScale(scale1, scale2)
				.setLifetime(lifeTime)
				.addMotion(motion.getX(), motion.getY(), motion.getZ())
				.enableNoClip()
				.spawn(world, x, y, z);
		}catch (Exception e){}
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return new EntitySpawnS2CPacket(this);
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
	}

	@Override
	protected void onCollision(HitResult hitResult) {
		if (!world.isClient){
			double x = this.getX();
			double y = this.getY();
			double z = this.getZ();

			world.playSound((PlayerEntity) null, x, y, z, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.NEUTRAL, 3.5F, 2.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			world.playSound((PlayerEntity) null, x, y, z, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.NEUTRAL, 3.5F, 2.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			world.playSound((PlayerEntity) null, x, y, z, SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.NEUTRAL, 3.5F, 2.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			world.playSound((PlayerEntity) null, x, y, z, SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.NEUTRAL, 2F, 0.8F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			world.playSound((PlayerEntity) null, x, y, z, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.NEUTRAL, 2F, 0.8F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			//DONT ASK IT SOUNDS COOL TRUST

		}super.onCollision(hitResult);
	}
}
