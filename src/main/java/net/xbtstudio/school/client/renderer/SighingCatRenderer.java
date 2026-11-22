package net.xbtstudio.school.client.renderer;

import net.xbtstudio.school.entity.SighingCatEntity;

import net.minecraft.client.model.animal.feline.AdultOcelotModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.FelineRenderState;
import net.minecraft.resources.Identifier;

public class SighingCatRenderer extends MobRenderer<SighingCatEntity, FelineRenderState, AdultOcelotModel> {
	private static final Identifier TEXTURE = Identifier.parse("school:textures/entities/red_cat_texture.png");

	public SighingCatRenderer(EntityRendererProvider.Context context) {
		super(context, new AdultOcelotModel(context.bakeLayer(ModelLayers.OCELOT)), 0.5f);
	}

	@Override
	public FelineRenderState createRenderState() {
		return new FelineRenderState();
	}

	@Override
	public Identifier getTextureLocation(FelineRenderState state) {
		return TEXTURE;
	}
}
