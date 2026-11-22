package net.xbtstudio.school.client.renderer;

import net.xbtstudio.school.entity.ChalkCannonProjectileEntity;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChalkCannonProjectileRenderer extends ThrownItemRenderer<ChalkCannonProjectileEntity> {
	public ChalkCannonProjectileRenderer(EntityRendererProvider.Context context) {
		super(context, 1.0f, true);
	}
}
