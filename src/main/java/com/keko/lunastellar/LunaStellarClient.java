package com.keko.lunastellar;

import com.keko.lunastellar.customParticles.StarExplosion;
import com.keko.lunastellar.entities.ModEntities;
import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.systems.rendering.particle.type.LodestoneParticleType;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

import javax.swing.text.html.parser.Entity;

public class LunaStellarClient implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		EntityRendererRegistry.register(ModEntities.CRYSTAL_PEBBLE_PROJECTILE, FlyingItemEntityRenderer::new);
		EntityRendererRegistry.register(ModEntities.FALLING_STAR, FlyingItemEntityRenderer::new);
		StarExplosion.registerFactories();
		ParticleFactoryRegistry.getInstance().register(StarExplosion.STAR_EXPLOSION, LodestoneParticleType.Factory::new);
	}
}
