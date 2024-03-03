package com.keko.lunastellar.customParticles;

import com.sammy.lodestone.helpers.DataHelper;
import com.sammy.lodestone.setup.LodestoneParticles;
import com.sammy.lodestone.systems.rendering.particle.type.LodestoneParticleType;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.function.BiConsumer;

public class StarExplosion extends LodestoneParticles {
	public  static final LodestoneParticleType STAR_EXPLOSION = new LodestoneParticleType();

	public static void init(){
		initParticles(bind(Registry.PARTICLE_TYPE));
	}

	public static void registerFactories(){
		ParticleFactoryRegistry.getInstance().register(STAR_EXPLOSION, LodestoneParticleType.Factory::new);
	}

	private static void initParticles(BiConsumer<ParticleType<?>, Identifier> registry){
		registry.accept(STAR_EXPLOSION, DataHelper.prefix("custom_particle_star_explosion"));
	}

	private static <T> BiConsumer<T, Identifier> bind(Registry<? super T> registry) {
		return (t, id) -> Registry.register(registry, id, t);
	}
}
