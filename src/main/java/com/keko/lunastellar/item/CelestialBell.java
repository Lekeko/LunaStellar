package com.keko.lunastellar.item;

import com.keko.lunastellar.entities.TrackingStar;
import com.keko.lunastellar.helpers.Directional;
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

public class CelestialBell extends Item {
	int distance = 200;
	double velocityForLeftX = 0.13;
	double velocityForRightX = 0.13;

	public CelestialBell(Settings settings) {
		super(settings.maxCount(1));
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		((PlayerEntity)user).getItemCooldownManager().set(this, 10);
		boolean foundAmmo = false;
		for (ItemStack stack : user.getInventory().main){
			if (stack.getItem() instanceof InfusedCrystal){
				foundAmmo = true;
				if (!user.isCreative()){
					stack.decrement(1);
				}
			}
		}

		if (!foundAmmo){
			user.damage(DamageSource.MAGIC, 2);
		}

		ItemStack itemStack = user.getStackInHand(hand);
		world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL , SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK , SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		BlockPos pos = Directional.rayChastHitBlock(world, user, user.getRotationVec(1f), distance);
		pos = new BlockPos(pos.getX(), pos.getY()+1, pos.getZ());
		BlockState blockState = world.getBlockState(pos);
		if (!world.isClient)
			doTheOtherThing(world, user, pos);

		return TypedActionResult.success(itemStack, world.isClient());
	}

	private void doTheOtherThing(World world, PlayerEntity user, BlockPos pos) {
		TrackingStar trackingStarLeft = new TrackingStar(user, world, pos);
		TrackingStar trackingStarRight = new TrackingStar(user, world, pos);
		double xOffset = 3.0;
		double zOffset = 3.0;

		Vec3d playerPos = user.getPos();
		float playerYaw = user.getYaw();

		double leftX = playerPos.x + xOffset * Math.cos(Math.toRadians(playerYaw)) - zOffset * Math.sin(Math.toRadians(playerYaw));
		double leftZ = playerPos.z + xOffset * Math.sin(Math.toRadians(playerYaw)) + zOffset * Math.cos(Math.toRadians(playerYaw));

		double rightX = playerPos.x - xOffset * Math.cos(Math.toRadians(playerYaw)) - zOffset * Math.sin(Math.toRadians(playerYaw));
		double rightZ = playerPos.z - xOffset * Math.sin(Math.toRadians(playerYaw)) + zOffset * Math.cos(Math.toRadians(playerYaw));

		double motionLeftX = leftX - playerPos.x ;
		double motionLeftZ = leftZ - playerPos.z;

		double motionRightX = rightX - playerPos.x;
		double motionRightZ = rightZ - playerPos.z;

		trackingStarLeft.setPos(leftX, user.getY(), leftZ);
		trackingStarRight.setPos(rightX, user.getY(), rightZ);

		trackingStarLeft.setNoGravity(true);
		trackingStarRight.setNoGravity(true);


		trackingStarLeft.setVelocity(motionLeftX, user.getRotationVec(2).y + 2, motionLeftZ);
		trackingStarRight.setVelocity(motionRightX,  user.getRotationVec(2).y +  2, motionRightZ);

		world.spawnEntity(trackingStarLeft);
		world.spawnEntity(trackingStarRight);



	}


}
