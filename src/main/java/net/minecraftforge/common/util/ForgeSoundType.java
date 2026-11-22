package net.minecraftforge.common.util;

import java.util.function.Supplier;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

/**
 * A {@link SoundType} whose sounds are resolved lazily through {@link Supplier}s, so blocks can
 * reference not-yet-registered sound events at construction time. Mirrors Forge's
 * {@code net.minecraftforge.common.util.ForgeSoundType}.
 */
public class ForgeSoundType extends SoundType {
	private final Supplier<SoundEvent> breakSound;
	private final Supplier<SoundEvent> stepSound;
	private final Supplier<SoundEvent> placeSound;
	private final Supplier<SoundEvent> hitSound;
	private final Supplier<SoundEvent> fallSound;

	public ForgeSoundType(float volume, float pitch, Supplier<SoundEvent> breakSound, Supplier<SoundEvent> stepSound,
			Supplier<SoundEvent> placeSound, Supplier<SoundEvent> hitSound, Supplier<SoundEvent> fallSound) {
		super(volume, pitch, null, null, null, null, null);
		this.breakSound = breakSound;
		this.stepSound = stepSound;
		this.placeSound = placeSound;
		this.hitSound = hitSound;
		this.fallSound = fallSound;
	}

	@Override
	public SoundEvent getBreakSound() {
		return this.breakSound.get();
	}

	@Override
	public SoundEvent getStepSound() {
		return this.stepSound.get();
	}

	@Override
	public SoundEvent getPlaceSound() {
		return this.placeSound.get();
	}

	@Override
	public SoundEvent getHitSound() {
		return this.hitSound.get();
	}

	@Override
	public SoundEvent getFallSound() {
		return this.fallSound.get();
	}
}
