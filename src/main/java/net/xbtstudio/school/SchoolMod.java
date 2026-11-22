package net.xbtstudio.school;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.xbtstudio.school.init.SchoolModBlockEntities;
import net.xbtstudio.school.init.SchoolModBlocks;
import net.xbtstudio.school.init.SchoolModEntities;
import net.xbtstudio.school.init.SchoolModItems;
import net.xbtstudio.school.init.SchoolModMenus;
import net.xbtstudio.school.init.SchoolModSounds;
import net.xbtstudio.school.init.SchoolModTabs;

public class SchoolMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger(SchoolMod.class);
	public static final String MODID = "school";

	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(
			Identifier.fromNamespaceAndPath(MODID, MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
	private static int messageID = 0;
	private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();
	private static volatile net.minecraft.server.MinecraftServer currentServer;

	@Override
	public void onInitialize() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		SchoolModSounds.REGISTRY.register(bus);
		SchoolModBlocks.REGISTRY.register(bus);
		SchoolModBlockEntities.REGISTRY.register(bus);
		SchoolModItems.REGISTRY.register(bus);
		SchoolModEntities.REGISTRY.register(bus);
		SchoolModTabs.REGISTRY.register(bus);
		SchoolModMenus.REGISTRY.register(bus);

		LOGGER.info("school registries: blocks={} items={} entities={} menus={} blockEntities={} sounds={} tabs={}",
				SchoolModBlocks.REGISTRY.getEntries().size(),
				SchoolModItems.REGISTRY.getEntries().size(),
				SchoolModEntities.REGISTRY.getEntries().size(),
				SchoolModMenus.REGISTRY.getEntries().size(),
				SchoolModBlockEntities.REGISTRY.getEntries().size(),
				SchoolModSounds.REGISTRY.getEntries().size(),
				SchoolModTabs.REGISTRY.getEntries().size());

		net.minecraftforge.ForgeBootstrap.bootstrap();
		bus.post(new FMLCommonSetupEvent());

		ServerTickEvents.END_SERVER_TICK.register(server -> {
			currentServer = server;
			MinecraftForge.EVENT_BUS.post(new TickEvent.ServerTickEvent(TickEvent.Phase.END));
			List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
			workQueue.forEach(work -> {
				work.setValue(work.getValue() - 1);
				if (work.getValue() == 0)
					actions.add(work);
			});
			actions.forEach(e -> e.getKey().run());
			workQueue.removeAll(actions);
		});
	}

	public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder,
			BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
		PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
		messageID++;
	}

	public static net.minecraft.server.MinecraftServer getServer() {
		return currentServer;
	}

	public static void queueServerWork(int tick, Runnable action) {
		workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
	}
}
