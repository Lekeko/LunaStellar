package com.keko.lunastellar.mixin;

import com.keko.lunastellar.customParticles.StarExplosion;
import com.keko.lunastellar.enchantments.CrystalLeap;
import com.keko.lunastellar.enchantments.ModEnch;
import com.keko.lunastellar.particles.ModParticles;
import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.setup.LodestoneScreenParticles;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import com.sammy.lodestone.systems.rendering.particle.screen.ScreenParticleEffect;
import io.netty.buffer.Unpooled;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(ClientPlayerEntity.class)
public abstract class JumpMixin {
	private int jumpCount = 0;
	private boolean jumpedLastTick = false;
	int jump = 0;
	int powerDivider = 6;

	@Inject(method = "tickMovement", at = @At("HEAD"))
	private void onJump(CallbackInfo info) {
		ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
		if (player.input.jumping) {
			jump++;
		} else jump = 0;

		if (player.isOnGround() || player.isClimbing()) {
			jumpCount = EnchantmentHelper.getEquipmentLevel(ModEnch.CRYSTAL_LEAP, player);
		} else if (jumpedLastTick && jumpCount > 0) {
			if (player.input.jumping && !player.getAbilities().flying) {
				if (!player.isOnGround() && player.getVelocity().y < -0.2) {
					jumpCount--;
					player.jump();
					player.setVelocity(player.getVelocity().x + player.getRotationVec(1).x / powerDivider, 0.8, player.getVelocity().z + player.getRotationVec(1).z / powerDivider);
					PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
					passedData.writeUuid(player.getUuid());
					double centerx = player.getX();
					double centery = player.getY();
					double centerz = player.getZ();
					Color startingColor = new Color(220, 0, 255);
					Color endingColor = new Color(251, 235, 255);
					player.world.playSound((PlayerEntity)player, centerx, centery, centerz, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK , SoundCategory.NEUTRAL, 2.5F, 2.4F / (player.world.getRandom().nextFloat() * 0.4F + 0.8F));
					player.world.playSound((PlayerEntity)player, centerx, centery, centerz, SoundEvents.BLOCK_AMETHYST_BLOCK_BREAK	 , SoundCategory.NEUTRAL, 2.5F, 2.4F / (player.world.getRandom().nextFloat() * 0.4F + 0.8F));


					ParticleBuilders.create(LodestoneParticles.TWINKLE_PARTICLE)
						.disableForcedMotion()
						.setColor(startingColor, endingColor)
						.setScale(0.4f, 0)
						.setLifetime(20)
						.enableNoClip()
						.spawn(player.world, centerx, centery + 0.5, centerz);
				}
			}
		}

		jumpedLastTick = player.input.jumping;
	}

}
