/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.jsdtqhj.revengeance.sounds;

import com.jsdtqhj.revengeance.RevengeanceMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RevengeanceModSounds {
	public static final DeferredRegister<SoundEvent> REGISTRY = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, RevengeanceMod.MODID);
	public static final RegistryObject<SoundEvent> RAGE_ACTIVITE = REGISTRY.register("rage_activite", 
		() -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryParse(RevengeanceMod.MODID + ":rage_activite")));
	public static final RegistryObject<SoundEvent> RAGE_END = REGISTRY.register("rage_end", 
		() -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryParse(RevengeanceMod.MODID + ":rage_end")));
	public static final RegistryObject<SoundEvent> RAGE_FULL = REGISTRY.register("rage_full",
		() -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryParse(RevengeanceMod.MODID + ":rage_full")));
	public static final RegistryObject<SoundEvent> ADRENALINE_ACTIVITE = REGISTRY.register("adrenaline_activite",
		() -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryParse(RevengeanceMod.MODID + ":adrenaline_activite")));
	public static final RegistryObject<SoundEvent> ADRENALINE_FULL = REGISTRY.register("adrenaline_full",
		() -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryParse(RevengeanceMod.MODID + ":adrenaline_full")));
	public static final RegistryObject<SoundEvent> ADRENALINE_CHARGE_BREAK = REGISTRY.register("adrenaline_charge_break",
		() -> SoundEvent.createVariableRangeEvent(ResourceLocation.tryParse(RevengeanceMod.MODID + ":adrenaline_charge_break")));

	public static void register(IEventBus eventBus) {
		REGISTRY.register(eventBus);
	}
}