
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.xbtstudio.school.init;

import net.xbtstudio.school.SchoolMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.Identifier;

public class SchoolModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SchoolMod.MODID);
	public static final RegistryObject<SoundEvent> SHOOT = REGISTRY.register("shoot", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("school", "shoot")));
	public static final RegistryObject<SoundEvent> SMGSHOOT = REGISTRY.register("smgshoot", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("school", "smgshoot")));
	public static final RegistryObject<SoundEvent> ABLETENNISTABL = REGISTRY.register("abletennistabl", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("school", "abletennistabl")));
	public static final RegistryObject<SoundEvent> AAIO = REGISTRY.register("aaio", () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath("school", "aaio")));
}
