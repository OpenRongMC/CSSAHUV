package net.xbtstudio.school.client.renderer;

import net.xbtstudio.school.entity.ChalkHairSprayerProjectileEntity;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChalkHairSprayerProjectileRenderer extends ThrownItemRenderer<ChalkHairSprayerProjectileEntity> {
	public ChalkHairSprayerProjectileRenderer(EntityRendererProvider.Context context) {
		super(context, 1.0f, true);
	}
}
