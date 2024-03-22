package com.keko.lunastellar.helpers;

import com.keko.lunastellar.entities.TrackingStar;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class Directional {

	public static Vec3d rayCasr(World world, Entity player, Vec3d direction, int distance){
		Vec3d vec3d = new Vec3d(player.getX(), player.getY(), player.getZ());
		for (double i = 0.1; i <= distance; i+= 0.1){
			BlockState state = world.getBlockState(new BlockPos(vec3d));
			if (!state.isOf(Blocks.AIR) && !state.isOf(Blocks.WATER) && !state.isOf(Blocks.VOID_AIR)){
				return vec3d;
			}else {
				vec3d = new Vec3d(
					direction.getX() * i + player.getX(),
					direction.getY() * i + player.getY() + 1.75 ,
					direction.getZ() * i + player.getZ());
			}
		}
		return vec3d;

	}

	public static BlockPos rayChastHitBlock(World world, Entity player, Vec3d direction , int distance){
		for (int i = 1; i < distance; i++) {
			Box box = new Box(new BlockPos(
				direction.getX() * i + player.getX(),
				direction.getY() * i + player.getY() + 2 ,
				direction.getZ() * i + player.getZ()));

			BlockState blockState = world.getBlockState( new BlockPos(
				direction.getX() * i + player.getX(),
				direction.getY() * i + player.getY() + 2 ,
				direction.getZ() * i + player.getZ() ));

			for (Entity entity : world.getOtherEntities(null, box)){
				if (entity.getBoundingBox().intersects(box) && entity != player && !(entity instanceof TrackingStar)){
					return new BlockPos(
						direction.getX() * i + player.getX(),
						direction.getY() * i + player.getY() + 2 ,
						direction.getZ() * i + player.getZ());
				}
			}

			if (!blockState.isOf(Blocks.AIR) && !blockState.isOf(Blocks.GRASS) && !blockState.isOf(Blocks.TALL_GRASS) && !blockState.isOf(Blocks.WATER) && !blockState.isOf(Blocks.KELP)){
				return new BlockPos(
					direction.getX() * i + player.getX(),
					direction.getY() * i + player.getY() + 2 ,
					direction.getZ() * i + player.getZ());
			}

			if (blockState.isOf(Blocks.AIR) && i == distance - 1){
				return new BlockPos(
					direction.getX() * i + player.getX(),
					direction.getY() * i + player.getY() + 2 ,
					direction.getZ() * i + player.getZ());
			}
		}
		return null;
	}

}
