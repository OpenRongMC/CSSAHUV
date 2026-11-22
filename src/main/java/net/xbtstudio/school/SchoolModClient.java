package net.xbtstudio.school;

import net.fabricmc.api.ClientModInitializer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.xbtstudio.school.init.SchoolModEntityRenderers;
import net.xbtstudio.school.init.SchoolModModels;
import net.xbtstudio.school.init.SchoolModScreens;

public final class SchoolModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();

		// Client @Mod.EventBusSubscriber classes must be registered before events fire.
		net.minecraftforge.ForgeBootstrap.registerSubscriber(SchoolModScreens.class);
		net.minecraftforge.ForgeBootstrap.registerSubscriber(SchoolModEntityRenderers.class);
		net.minecraftforge.ForgeBootstrap.registerSubscriber(SchoolModModels.class);
		net.minecraftforge.ForgeBootstrap.registerSubscriber(net.xbtstudio.school.init.SchoolModBlocks.BlocksClientSideHandler.class);

		bus.post(new EntityRenderersEvent.RegisterRenderers());
		bus.post(new EntityRenderersEvent.RegisterLayerDefinitions());
		bus.post(new RegisterColorHandlersEvent.Block());
		bus.post(new FMLClientSetupEvent());

		// S2C handlers for messages registered during common setup.
		net.minecraftforge.network.simple.SimpleChannel.registerClientReceivers();
	}
}
