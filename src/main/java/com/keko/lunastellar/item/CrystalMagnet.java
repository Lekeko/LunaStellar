package com.keko.lunastellar.item;

import com.keko.lunastellar.helpers.Directional;
import com.keko.lunastellar.helpers.InvSearch;
import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;

public class CrystalMagnet extends Item {
	private int maxDistance = 18;
	private int amplifierReducer = 10;
	static boolean used = false;

	public CrystalMagnet(Settings settings) {
		super(settings.maxCount(1));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

		BlockState ground = world.getBlockState(new BlockPos(entity.getX(), entity.getY() - 0.2, entity.getZ()));
		if (!ground.isOf(Blocks.AIR) && !ground.isOf(Blocks.WATER)){
			used = false;
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = InvSearch.hasItemInInv(user, ModItems.INFUSED_CRYSTAL);
		ItemStack itemStack = user.getStackInHand(hand);
		if (!used) {
			used = true;
			Vec3d pos = Directional.rayCasr(world, user, user.getRotationVec(1), maxDistance);
			BlockState state = world.getBlockState(new BlockPos(pos));
			if (!state.isOf(Blocks.AIR)  && !state.isOf(Blocks.VOID_AIR)) {
				user.fallDistance = 1;
				double deltaX = pos.getX() - user.getX();
				double deltaY = pos.getY() - user.getY() + 1.8;
				double deltaZ = pos.getZ() - user.getZ();
				double centerx = user.getX();
				double centery = user.getY();
				double centerz = user.getZ();
				user.jump();
				user.addVelocity(deltaX / amplifierReducer, deltaY / amplifierReducer, deltaZ / amplifierReducer);
				user.world.playSound((PlayerEntity) user, centerx, centery, centerz, SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK, SoundCategory.NEUTRAL, 2.5F, 2.4F / (user.world.getRandom().nextFloat() * 0.4F + 0.8F));
				user.world.playSound((PlayerEntity) user, centerx, centery, centerz, SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.NEUTRAL, 2.5F, 8.4F / (user.world.getRandom().nextFloat() * 0.4F + 0.8F));

				Color c1 = new Color(237, 0, 89, 255);
				Color c2 = Color.WHITE;
				for (int i = 1; i < 5; i++)
					ParticleBuilders.create(LodestoneParticles.SPARKLE_PARTICLE)
						.setLifetime(10)
						.enableNoClip()
						.setScale(2)
						.setColor(c1, c2)
						.spawn(world, pos.getX(), pos.getY(), pos.getZ());

			}
		}

		return TypedActionResult.success(itemStack, world.isClient());
	}
}
