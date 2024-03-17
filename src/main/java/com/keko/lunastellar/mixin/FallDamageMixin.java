package com.keko.lunastellar.mixin;

import com.keko.lunastellar.enchantments.ModEnch;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class FallDamageMixin {
	@Inject(method = "handleFallDamage", at = @At("HEAD"), cancellable = true)
	private void onFall(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir){
		PlayerEntity player = (PlayerEntity) (Object)this;
		if (EnchantmentHelper.getLevel(ModEnch.CRYSTAL_LEAP, player.getEquippedStack(EquipmentSlot.FEET)) > 0){
			if (fallDistance <= 8){
				cir.setReturnValue(false);
			}else {
				cir.setReturnValue(false);
				player.damage(DamageSource.FALL, fallDistance - 8);
			}
		}
	}
}
