package net.minecraftforge.fml.event.lifecycle;

import java.util.concurrent.CompletableFuture;

import net.minecraftforge.eventbus.api.Event;

/**
 * Fired on the MOD bus during common (both-sides) setup. {@link #enqueueWork(Runnable)} runs the
 * work immediately and synchronously on this shim, which is safe for the registration-style work
 * MCreator enqueues here.
 */
public class FMLCommonSetupEvent extends Event {
	public CompletableFuture<Void> enqueueWork(Runnable work) {
		work.run();
		return CompletableFuture.completedFuture(null);
	}

	@Override
	public boolean isCancelable() {
		return false;
	}
}
