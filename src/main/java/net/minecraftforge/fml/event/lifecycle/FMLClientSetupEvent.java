package net.minecraftforge.fml.event.lifecycle;

import java.util.concurrent.CompletableFuture;

import net.minecraftforge.eventbus.api.Event;

/**
 * Fired on the MOD bus during client-only setup. See {@link FMLCommonSetupEvent}.
 */
public class FMLClientSetupEvent extends Event {
	public CompletableFuture<Void> enqueueWork(Runnable work) {
		work.run();
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public boolean isCancelable() {
		return false;
	}
}
