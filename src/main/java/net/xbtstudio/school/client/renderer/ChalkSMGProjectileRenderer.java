package net.xbtstudio.school.client.renderer;

import net.xbtstudio.school.entity.ChalkSMGProjectileEntity;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class ChalkSMGProjectileRenderer extends ThrownItemRenderer<ChalkSMGProjectileEntity> {
	public ChalkSMGProjectileRenderer(EntityRendererProvider.Context context) {
		super(context, 1.0f, true);
	}
}
