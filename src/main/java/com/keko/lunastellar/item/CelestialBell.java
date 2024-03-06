package com.keko.lunastellar.item;

import com.keko.lunastellar.entities.BigFallingStar;
import com.keko.lunastellar.entities.CrystalPebbleProjectile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CelestialBell extends Item {
	int distance = 200;

	public CelestialBell(Settings settings) {
		super(settings.maxCount(1));
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_NOTE_BLOCK_BELL , SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.BLOCK_AMETHYST_CLUSTER_BREAK , SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		BlockPos pos = rayChastHitBlock(world, user, user.getRotationVec(1f), distance);
		BlockState blockState = world.getBlockState(pos);
		if (blockState.isOf(Blocks.AIR)){
			for (int i = 1; i < 200; i++){
				assert pos != null;
				blockState = world.getBlockState(new BlockPos( pos.getX(),
					pos.getY() - i,
					pos.getZ()));
				if (!blockState.isOf(Blocks.AIR)){
					doTheOtherThing(world, user,new BlockPos( pos.getX(), pos.getY() - i, pos.getZ()) );
					break;
				}
			}
		}else {
			doTheOtherThing(world, user, pos);
		}

		return TypedActionResult.success(itemStack, world.isClient());
	}

	private void doTheOtherThing(World world, PlayerEntity user, BlockPos pos) {
		BigFallingStar bigFallingStar = new BigFallingStar(user, world);
		bigFallingStar.setVelocity(0f, 3f, 0f);
		bigFallingStar.setPos(pos.getX(), pos.getY() + 70, pos.getZ());
		world.spawnEntity();
	}

	private BlockPos rayChastHitBlock(World world, PlayerEntity player, Vec3d direction , int distance){
		for (int i = 1; i < distance; i++) {
			BlockState blockState = world.getBlockState( new BlockPos(
				direction.getX() * i + player.getX(),
				direction.getY() * i + player.getY() + 2 ,
				direction.getZ() * i + player.getZ() ));
			if (!blockState.isOf(Blocks.AIR)){
				return new BlockPos(direction.getX() * i + player.getX(),
					direction.getY() * i + player.getY() + 2 ,
					direction.getZ() * i + player.getZ());
			}

			if (blockState.isOf(Blocks.AIR) && i == distance - 1){
				return new BlockPos(direction.getX() * i + player.getX(),
					direction.getY() * i + player.getY() + 2 ,
					direction.getZ() * i + player.getZ());
			}
		}
		return null;
	}
}
