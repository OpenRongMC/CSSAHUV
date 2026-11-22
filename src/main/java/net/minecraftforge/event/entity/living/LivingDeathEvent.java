package net.minecraftforge.event.entity.living;

import net.minecraftforge.eventbus.api.Event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

/**
 * Fired on the FORGE bus when a living entity dies. Mirrors Forge's {@code LivingDeathEvent}
 * (cancellable).
 */
public class LivingDeathEvent extends Event {
	private final LivingEntity entity;
	private final DamageSource source;

	public LivingDeathEvent(LivingEntity entity, DamageSource source) {
		this.entity = entity;
		this.source = source;
	}

	public LivingEntity getEntity() {
		return this.entity;
	}

	public DamageSource getSource() {
		return this.source;
	}
}
