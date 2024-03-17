package com.keko.lunastellar.block;

import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.systems.rendering.particle.ParticleBuilders;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.random.RandomGenerator;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.awt.*;
import java.util.Random;

public class CrystalCampfire extends Block {
	private static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 8, 16);
	Color color1 = new Color(237, 0, 255, 255);
	Color color2 = Color.WHITE;

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, RandomGenerator random) {
		double radiusd = 14;
		Random random1 = new Random();
		for (int i = 0 ; i < 6; i++){
			double x = random1.nextDouble(radiusd) - 7;
			double y = random1.nextDouble(radiusd - 7);
			double z = random1.nextDouble(radiusd) - 7;
			ParticleBuilders.create(LodestoneParticles.SPARKLE_PARTICLE)
				.setScale(0.2f)
				.setColor(color1, color2)
				.disableForcedMotion()
				.setLifetime(40)
				.enableNoClip()
				.spawn(world, pos.getX() + x, pos.getY() + y, pos.getZ() + z);
		}

		ParticleBuilders.create(LodestoneParticles.SPARKLE_PARTICLE)
			.setScale(0.65f)
			.setColor(color1, color2)
			.disableForcedMotion()
			.enableNoClip()
			.spawn(world, pos.getX() + 0.5, pos.getY() + 0.7, pos.getZ() + 0.5);


		super.randomDisplayTick(state, world, pos, random);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		ItemStack itemStack = new ItemStack(this);
		dropStack(world, pos, itemStack);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	public CrystalCampfire(Settings settings) {
		super(settings);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}
}
