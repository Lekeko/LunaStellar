package com.keko.lunastellar.entities;

import com.keko.lunastellar.customParticles.StarExplosion;
import com.keko.lunastellar.item.ModItems;
import com.mojang.datafixers.kinds.IdF;
import com.sammy.lodestone.LodestoneLib;
import com.sammy.lodestone.network.screenshake.PositionedScreenshakePacket;
import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.setup.LodestoneScreenParticles;
import com.sammy.lodestone.systems.rendering.particle.Easing;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import com.sammy.lodestone.systems.rendering.particle.screen.ScreenParticleType;
import com.sammy.lodestone.systems.rendering.particle.screen.base.ScreenParticle;
import com.sammy.lodestone.systems.rendering.particle.type.LodestoneScreenParticleType;
import com.sammy.lodestone.systems.rendering.particle.world.GenericParticle;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.Packet;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.*;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import org.quiltmc.qsl.networking.api.ServerPlayNetworking;
import static net.minecraft.world.explosion.Explosion.getExposure;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

public class FallingStar extends ThrownItemEntity {
	private float speed = 0.5f;
	int i  = 1;
	private int spinSpeed = 4;//greater is slower :p
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
			if (world.isClient)  createStartEffect();
			i--;
		}
		if (world.isClient) {
			createStarEffect();
			createStarEffect();
			createStarEffect();
		}
		Color startingColor = new Color(229, 85, 255, 255);
		Color endingColor = new Color(240, 215, 255, 255);
		if (world.isClient) {
			ParticleBuilders.create(StarExplosion.STAR_EXPLOSION)
				.setScale(2)
				.setColor(startingColor, endingColor)
				.setLifetime(9)
				.enableNoClip()
				.spawn(this.getWorld(),
					this.getX(),
					this.getY(),
					this.getZ());
			ParticleBuilders.create(StarExplosion.STAR_EXPLOSION)
				.setScale(2)
				.setColor(startingColor, endingColor)
				.setLifetime(9)
				.enableNoClip()
				.spawn(this.getWorld(),
					this.getX(),
					this.getY(),
					this.getZ());
		}
		super.tick();
	}
	public static void launchPlayer(BlockPos blockPos, Entity player) {
		if(!player.getWorld().isClient){
			float intensity = (float) (Math.abs(player.getX() - blockPos.getX()) + Math.abs(player.getY() - blockPos.getY()) + Math.abs(player.getZ() - blockPos.getZ()));
			Vec3i vec3i = new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
			if (intensity <= 15) {

				double xx = player.getX() - blockPos.getX();
				double yy = player.getY() - blockPos.getY();
				double zz = player.getZ() - blockPos.getZ();
				double aa = Math.sqrt(xx * xx + yy * yy + zz * zz);
				if (aa != 0.0) {
					xx /= aa;
					yy /= aa;
					zz /= aa;
					double ab = (double) getExposure(new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()), player);
					double ac = (2.0 - 0.1) * ab;
					double ad = ac;
					player.addVelocity((xx * ad)/2, (yy * ad)/2, (zz * ad)/2);
					player.damage(DamageSource.CACTUS, 1);
				}
			}
		}
	}


	@Override
	protected void onCollision(HitResult hitResult) {
		if (!world.isClient) {
			world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.NEUTRAL, 3.5F, 2.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK, SoundCategory.NEUTRAL, 3.5F, 2.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.NEUTRAL, 3.5F, 2.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 3.5F, 7.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));

			world.playSound((PlayerEntity) null, this.getX(), this.getY(), this.getZ(), SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.NEUTRAL, 3.5F, 2.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		}
		int radius = 200;
		Box boundingBox = new Box(this.getX() + radius, this.getY() + radius, this.getZ() + radius
			,this.getX() - radius, this.getY() - radius, this.getZ() - radius);

		if (world.isClient) {
			this.getWorld().getEntitiesByClass(PlayerEntity.class, boundingBox, playerEntity -> true).forEach(player -> {
				try {
					createShake(this.getWorld().getServer(), player, this.getPos());
				} catch (Exception e) {
				}
			});
		}

		int radiusd = 7;Random random1 = new Random();
		for (int x = -radiusd; x <= radiusd; x++) {
			for (int y = -radiusd; y <= radiusd; y++) {
				for (int z = -radiusd; z <= radiusd; z++) {
					int checker = x * x + y * y + z * z;
					if (checker <= radiusd * radiusd) {


						BlockPos blockPos = new BlockPos(this.getPos().getX() + x, this.getPos().getY() + y, this.getPos().getZ() + z);
						Box box = new Box(blockPos);
						if (!world.isClient) {
							for (Entity entity : world.getOtherEntities(null, box)) {
								if (!(entity instanceof ItemEntity)) {
									double distance = this.squaredDistanceTo(entity);
									System.out.println("Distance = " + distance);
									if (entity instanceof EnderDragonEntity) {
										entity.damage(DamageSource.player((PlayerEntity) this.getOwner()), 30);
									} else {
										if (world.getRegistryKey() == World.END)
											entity.damage(DamageSource.MAGIC, (float) (30 - distance / 3));
										else entity.damage(DamageSource.MAGIC, (float) (18 - distance / 3));
										if (!world.isClient)
											launchPlayer(this.getBlockPos(), entity);
									}
								}
							}
						}
					}

				}
			}
		}

		if (!world.isClient) {
			this.discard();
		}
		super.onCollision(hitResult);
	}

	public static <Position> void createShake(MinecraftServer s, PlayerEntity player, net.minecraft.util.math.Position pos){
		float intensity = (float) (Math.abs(player.getX() - pos.getX()) + Math.abs(player.getY() - pos.getY())  + Math.abs(player.getZ() - pos.getZ()));

		if (intensity < 200){
			intensity = (float)(200 - intensity);
		}else intensity = 0f;
		float finalIntensity = intensity;
		s.getOverworld().getPlayers(players -> players.getWorld().isChunkLoaded(new ChunkPos(player.getBlockPos()).x, new ChunkPos(player.getBlockPos()).z)).forEach(players -> {
			PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
			new PositionedScreenshakePacket(70, Vec3d.ofCenter(player.getBlockPos()),20f, 1.3f, 55f, Easing.CIRC_IN).setIntensity( finalIntensity / 120, 0f).setEasing(Easing.CIRC_OUT, Easing.CIRC_IN).write(buf);
			ServerPlayNetworking.send(players, PositionedScreenshakePacket.ID, buf);

		});


	}
	@Override
	public void onRemoved() {
			Color startingColor = new Color(255, 85, 159, 50);
			Color endingColor = new Color(255, 255, 255, 3);

		Random random1 = new Random();
		BlockPos pos = new BlockPos(getX(), getY() - 1, getZ());


		int bonusSize = world.getRegistryKey() == World.END ? 8 : 0;
		for (int i = 0; i < 15; i++){

			pos = new BlockPos(getX() + random1.nextInt(2) - 1 , getY() + random1.nextInt(2) - 1 , getZ() + random1.nextInt(2) - 1 );
			ParticleBuilders.create(StarExplosion.STAR_EXPLOSION)
				.setScale(8 + bonusSize, 10 + bonusSize)
				.setColor(startingColor, endingColor)
				.setLifetime(50)
				.setSpin(i % 2 == 0 ? (float)1/spinSpeed : (float)-1/spinSpeed)
				.setMotion(0.01 * (i % 2 == 0 ? -1 : 1)
					, 0f ,
					0.01 *(i % 2 == 0 ? 1 : -1))
				.enableNoClip()
				.evenlySpawnAtAlignedEdges(world, pos , world.getBlockState(pos), 2)
				;}
		Color startingBigColor = new Color(255, 165, 204, 101);
		ParticleBuilders.create(StarExplosion.STAR_EXPLOSION)
			.setScale(50 + bonusSize * 4)
			.setColor(startingColor, endingColor)
			.setLifetime(30)
			.enableNoClip()
			.evenlySpawnAtAlignedEdges(world, pos , world.getBlockState(pos), 2);

		super.onRemoved();
	}

	private void createStartEffect() {
		Color startingColor = new Color(255, 199, 222, 255);
		Color endingColor = new Color(255, 255, 255, 255);
		for (int i = 0; i < 5; i++)
			ParticleBuilders.create(LodestoneParticles.STAR_PARTICLE)
			.setScale(3)
			.setAlpha(10)
			.setColor(startingColor, endingColor)
			.setLifetime(16)
			.setMotion(this.getVelocity().getX() * 1.1, this.getVelocity().getY() * 1.3, this.getVelocity().getZ() * 1.1)
			.enableNoClip()
			.spawn(this.getWorld(),this.getX(), this.getY() - 2, this.getZ());
		if (world.isClient) createCircleEffect(this.getX(), this.getY(), this.getZ(), world);

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
		ParticleBuilders.create(StarExplosion.STAR_EXPLOSION)
			.setScale(2)
			.setColor(startingColor, endingColor)
			.setLifetime(9)
			.enableNoClip()
			.spawn(this.getWorld(),
				this.getX(),
				this.getY(),
				this.getZ());

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
