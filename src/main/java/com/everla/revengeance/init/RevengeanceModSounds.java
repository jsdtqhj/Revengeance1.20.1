
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.everla.revengeance.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import com.everla.revengeance.RevengeanceMod;

public class RevengeanceModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(Registries.SOUND_EVENT, RevengeanceMod.MODID);
	public static final DeferredHolder<SoundEvent, SoundEvent> RAGE_ACTIVITE = REGISTRY.register("rage_activite", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("revengeance", "rage_activite")));
	public static final DeferredHolder<SoundEvent, SoundEvent> RAGE_END = REGISTRY.register("rage_end", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("revengeance", "rage_end")));
	public static final DeferredHolder<SoundEvent, SoundEvent> RAGE_FULL = REGISTRY.register("rage_full", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("revengeance", "rage_full")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ADRENALINE_ACTIVITE = REGISTRY.register("adrenaline_activite", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("revengeance", "adrenaline_activite")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ADRENALINE_FULL = REGISTRY.register("adrenaline_full", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("revengeance", "adrenaline_full")));
	public static final DeferredHolder<SoundEvent, SoundEvent> ADRENALINE_CHARGE_BREAK = REGISTRY.register("adrenaline_charge_break",
			() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("revengeance", "adrenaline_charge_break")));
}
