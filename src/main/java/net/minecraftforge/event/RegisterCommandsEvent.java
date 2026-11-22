package net.minecraftforge.event;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraftforge.eventbus.api.Event;

import net.minecraft.commands.CommandSourceStack;

/**
 * Fired on the FORGE bus to let mods register commands. Mirrors Forge's
 * {@code RegisterCommandsEvent}; exposes the vanilla {@link CommandDispatcher}.
 */
public class RegisterCommandsEvent extends Event {
	private final CommandDispatcher<CommandSourceStack> dispatcher;

	public RegisterCommandsEvent(CommandDispatcher<CommandSourceStack> dispatcher) {
		this.dispatcher = dispatcher;
	}

	public CommandDispatcher<CommandSourceStack> getDispatcher() {
		return this.dispatcher;
	}

	@Override
	public boolean isCancelable() {
		return false;
	}
}
