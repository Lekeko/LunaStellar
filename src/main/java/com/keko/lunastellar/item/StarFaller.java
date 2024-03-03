package com.keko.lunastellar.item;

import com.keko.lunastellar.entities.CrystalPebbleProjectile;
import com.keko.lunastellar.entities.FallingStar;
import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.awt.*;
import java.util.Random;

public class StarFaller extends Item {
	//cooldown in seconds!
	private int cooldown = 12;
	int distance = 70;


	public StarFaller(Settings settings) {
		super(settings);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		BlockPos targetedBlock ;
		BlockState blockState;
		//user.getItemCooldownManager().set(this, 20 * cooldown);
			Vec3d direction = user.getRotationVec(1.0F);
			targetedBlock = rayChastHitBlock(world, user, direction, distance);
			blockState = world.getBlockState(targetedBlock);
			if (blockState.isOf(Blocks.AIR)){
				for (int i = 1; i < 200; i++){
					assert targetedBlock != null;
					blockState = world.getBlockState(new BlockPos( targetedBlock.getX(),
						targetedBlock.getY() - i,
						targetedBlock.getZ()));
					if (!blockState.isOf(Blocks.AIR)){
						doTheThing(world, user,new BlockPos( targetedBlock.getX(), targetedBlock.getY() - i, targetedBlock.getZ()) );
						break;
					}
				}
			}else {
				doTheThing(world, user, targetedBlock);
			}


		return TypedActionResult.success(itemStack, world.isClient());
	}

	private void doTheThing(World world, PlayerEntity user, BlockPos blockPos) {
		Random random = new Random();
		int xOffset = random.nextInt(50) - 25;
		int zOffset = random.nextInt(50) - 25;
		int height = 80;
		blockPos = new BlockPos(blockPos.getX() ,blockPos.getY() + height ,blockPos.getZ());
		if (!world.isClient) {
				FallingStar fallingStar = new FallingStar(user, world);
				fallingStar.setPos(
					blockPos.getX() + xOffset,
					blockPos.getY(),
					blockPos.getZ() + zOffset);
				fallingStar.setVelocity((double) -xOffset / 15, -5, (double) -zOffset / 15);
				fallingStar.setNoGravity(true);
				System.out.println("Spawned main at : " + (int)(blockPos.getX() + xOffset) + " " + blockPos.getY() + " " + (int)(blockPos.getZ() + zOffset));
				world.spawnEntity(fallingStar);

		}
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
