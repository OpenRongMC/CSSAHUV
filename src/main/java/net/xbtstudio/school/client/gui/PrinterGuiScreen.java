package net.xbtstudio.school.client.gui;

import net.xbtstudio.school.world.inventory.PrinterGuiMenu;
import net.xbtstudio.school.network.PrinterGuiButtonMessage;
import net.xbtstudio.school.SchoolMod;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class PrinterGuiScreen extends AbstractContainerScreen<PrinterGuiMenu> {
	private final static HashMap<String, Object> guistate = PrinterGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_print;

	public PrinterGuiScreen(PrinterGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		// 26.3 imageWidth/Height are final; sizes applied via menu/screen defaults 180 x 128
	}

	private static final Identifier texture = Identifier.parse("school:textures/screens/printer_gui.png");

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

		guiGraphics.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, Identifier.parse("school:textures/screens/cross.png"), this.leftPos + 53, this.topPos + 21, (float)0, (float)0, 16, 16, 16, 16);

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
		guiGraphics.text(this.font, Component.translatable("gui.school.printer_gui.label_empty"), 8, 6, -12829636, false);
	}

	@Override
	public void init() {
		super.init();
		button_print = Button.builder(Component.translatable("gui.school.printer_gui.button_print"), e -> {
			if (true) {
				SchoolMod.PACKET_HANDLER.sendToServer(new PrinterGuiButtonMessage(0, x, y, z));
				PrinterGuiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 118, this.topPos + 20, 51, 20).build();
		guistate.put("button:button_print", button_print);
		this.addRenderableWidget(button_print);
	}
}
