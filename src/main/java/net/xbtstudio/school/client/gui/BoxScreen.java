package net.xbtstudio.school.client.gui;

import net.xbtstudio.school.world.inventory.BoxMenu;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class BoxScreen extends AbstractContainerScreen<BoxMenu> {
	private final static HashMap<String, Object> guistate = BoxMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;

	public BoxScreen(BoxMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		// 26.3 imageWidth/Height are final; sizes applied via menu/screen defaults 176 x 166
	}

	private static final Identifier texture = Identifier.parse("school:textures/screens/box.png");

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		this.extractTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractContents(guiGraphics, mouseX, mouseY, partialTicks);
		renderBgCompat(guiGraphics); }
	private void renderBgCompat(GuiGraphicsExtractor guiGraphics) {
		guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, texture, this.leftPos, this.topPos, (float)0, (float)0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}

	@Override
	public boolean keyPressed(net.minecraft.client.input.KeyEvent event) {
		if (event.key() == 256) {
			this.onClose();
			return true;
		}
		return super.keyPressed(event);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		guiGraphics.text(this.font, Component.translatable("gui.school.box.label_laboratory_cabinet"), 5, 8, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
	}
}
