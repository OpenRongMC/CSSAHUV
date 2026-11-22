package net.minecraftforge;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class ForgeBootstrap {
	public static final IEventBus MOD_BUS = FMLJavaModLoadingContext.get().getModEventBus();
	public static final IEventBus FORGE_BUS = MinecraftForge.EVENT_BUS;

	private static boolean bootstrapped;

	private ForgeBootstrap() {
	}

	public static synchronized void bootstrap() {
		if (bootstrapped) {
			return;
		}
		bootstrapped = true;
		forceLoadSubscribers();
		CommandRegistrationCallback.EVENT.register((dispatcher, registry, environment) -> FORGE_BUS.post(new RegisterCommandsEvent(dispatcher)));
		EntityAttributeCreationEvent attributes = new EntityAttributeCreationEvent();
		MOD_BUS.post(attributes);
		attributes.getCreatedAttributes().forEach((type, supplier) -> FabricDefaultAttributeRegistry.register(type, supplier));
	}

	public static void registerSubscriber(Class<?> subscriber) {
		Mod.EventBusSubscriber annotation = subscriber.getAnnotation(Mod.EventBusSubscriber.class);
		IEventBus bus = (annotation != null && annotation.bus() == Mod.EventBusSubscriber.Bus.MOD) ? MOD_BUS : FORGE_BUS;
		bus.register(subscriber);
	}

	public static void registerSubscriber(String className) {
		try {
			registerSubscriber(Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Unknown @Mod.EventBusSubscriber class: " + className, e);
		}
	}

	private static void forceLoadSubscribers() {
		String[] classes = {
				"net.xbtstudio.school.command.AlcoholLampECommand",
				"net.xbtstudio.school.command.HowdoyousleepCommand",
				"net.xbtstudio.school.command.KaohaodaxueCommand",
				"net.xbtstudio.school.command.NEWACommand",
				"net.xbtstudio.school.command.OpeneaalguiCommand",
				"net.xbtstudio.school.command.ShanghaogaozhongCommand",
				"net.xbtstudio.school.init.SchoolModEntities",
				"net.xbtstudio.school.init.SchoolModBlocks",
				"net.xbtstudio.school.init.SchoolModTabs",
				// Client-only subscribers are registered from SchoolModClient to avoid
				// double FMLClientSetup / MenuScreens.register on the client.
				"net.xbtstudio.school.network.SchoolModVariables",
				"net.xbtstudio.school.network.SchoolBellControllerGUIButtonMessage",
				"net.xbtstudio.school.network.MusicselectionpageButtonMessage",
				"net.xbtstudio.school.network.PrinterGuiButtonMessage",
				"net.xbtstudio.school.network.NOCATButtonMessage",
				"net.xbtstudio.school.network.SNEButtonMessage",
				"net.xbtstudio.school.procedures.SchoolCProcedure",
				"net.xbtstudio.school.procedures.ShowDeathScreenProcedure",
		};
		for (String name : classes) {
			try {
				registerSubscriber(Class.forName(name));
			} catch (Throwable ignored) {
			}
		}
	}
}
