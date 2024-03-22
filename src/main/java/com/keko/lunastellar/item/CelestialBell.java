package com.keko.lunastellar.item;

import com.keko.lunastellar.entities.TrackingStar;
import com.keko.lunastellar.helpers.Directional;
import com.keko.lunastellar.helpers.InvSearch;
import com.sun.source.tree.LiteralTree;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.ScreenTexts;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.awt.*;

public class CelestialBell extends Item {
	int distance = 200;
	double velocityForLeftX = 0.13;
	double velocityForRightX = 0.13;
	private boolean dmg = false;

	public CelestialBell(Settings settings) {
		super(settings.maxCount(1));
	}


	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		Color c1 = new Color(237, 0, 255, 255);
		Color c2 = new Color(255, 255, 255, 255);
		((PlayerEntity)user).getItemCooldownManager().set(this, 10);

		ItemStack stack = InvSearch.hasItemInInv(user, ModItems.INFUSED_CRYSTAL);

		if (stack != null){
			if (!user.isCreative()){
				stack.decrement(1);
				dmg = false;
			}
		}

		if (user.getOffHandStack().getItem() instanceof InfusedCrystal ){
			if (!user.isCreative()){
				user.getOffHandStack().decrement(1);
				dmg = false;
			}
		}

		if (stack == null && !(user.getOffHandStack().getItem() instanceof InfusedCrystal )) {
			user.damage(DamageSource.MAGIC, 2);
			dmg = true;
		}

		ItemStack itemStack = user.getStackInHand(hand);
		world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL , SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK , SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!world.isClient){
			BlockPos pos = Directional.rayChastHitBlock(world, user, user.getRotationVec(1f), distance);
			assert pos != null;
			pos = new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ());
			BlockState blockState = world.getBlockState(pos);
			doTheOtherThing(world, user, pos ,c1 ,c2);
		}


		return TypedActionResult.success(itemStack, world.isClient());
	}

	private void doTheOtherThing(World world, PlayerEntity user, BlockPos pos, Color c1, Color c2) {
		TrackingStar trackingStarLeft = new TrackingStar(user, world, pos, c1, c2);
		TrackingStar trackingStarRight = new TrackingStar(user, world, pos, c1, c2);

			double xOffset = 3.0;
			double zOffset = 3.0;

			Vec3d playerPos = user.getPos();
			float playerYaw = user.getYaw();

			double leftX = playerPos.x + xOffset * Math.cos(Math.toRadians(playerYaw)) - zOffset * Math.sin(Math.toRadians(playerYaw));
			double leftZ = playerPos.z + xOffset * Math.sin(Math.toRadians(playerYaw)) + zOffset * Math.cos(Math.toRadians(playerYaw));

			double rightX = playerPos.x - xOffset * Math.cos(Math.toRadians(playerYaw)) - zOffset * Math.sin(Math.toRadians(playerYaw));
			double rightZ = playerPos.z - xOffset * Math.sin(Math.toRadians(playerYaw)) + zOffset * Math.cos(Math.toRadians(playerYaw));

			double motionLeftX = leftX - playerPos.x;
			double motionLeftZ = leftZ - playerPos.z;

			double motionRightX = rightX - playerPos.x;
			double motionRightZ = rightZ - playerPos.z;

			trackingStarLeft.setPos(leftX, user.getY(), leftZ);
			trackingStarRight.setPos(rightX, user.getY(), rightZ);

			trackingStarLeft.setNoGravity(true);
			trackingStarRight.setNoGravity(true);

			BlockPos pos1 = Directional.rayChastHitBlock(world, trackingStarLeft, new Vec3d(motionLeftX, user.getRotationVec(1).y, motionLeftZ), 6);
			BlockPos pos2 = Directional.rayChastHitBlock(world, trackingStarRight, new Vec3d(motionRightX, user.getRotationVec(1).y, motionRightZ), 6);

			BlockState state1 = world.getBlockState(pos1);
			BlockState state2 = world.getBlockState(pos2);

			if (state1.isOf(Blocks.AIR) && state2.isOf(Blocks.AIR)){
				trackingStarLeft.setVelocity(motionLeftX, user.getRotationVec(2).y + 2, motionLeftZ);
				trackingStarRight.setVelocity(motionRightX, user.getRotationVec(2).y + 2, motionRightZ);
			}else {
				trackingStarLeft.setPos(leftX, user.getY() + 2, leftZ);
				trackingStarRight.setPos(rightX, user.getY() + 2, rightZ);
			}

			world.spawnEntity(trackingStarLeft);
			world.spawnEntity(trackingStarRight);


	}


}
