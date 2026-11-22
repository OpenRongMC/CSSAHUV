package net.xbtstudio.school.client.renderer;

import net.xbtstudio.school.entity.ChalkRFProjectileEntity;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChalkRFProjectileRenderer extends ThrownItemRenderer<ChalkRFProjectileEntity> {
	public ChalkRFProjectileRenderer(EntityRendererProvider.Context context) {
		super(context, 1.0f, true);
	}
}
