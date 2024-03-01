package com.keko.lunastellar;

import com.keko.lunastellar.entities.ModEntities;
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
	}
}
