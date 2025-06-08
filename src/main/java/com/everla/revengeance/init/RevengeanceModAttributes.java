
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.everla.revengeance.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.registries.BuiltInRegistries;

import com.everla.revengeance.RevengeanceMod;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class RevengeanceModAttributes {
	public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(BuiltInRegistries.ATTRIBUTE, RevengeanceMod.MODID);
	public static final DeferredHolder<Attribute, Attribute> RAGE_LEVEL = REGISTRY.register("rage_level", () -> new RangedAttribute("attribute.revengeance.rage_level", 0, 0, 100).setSyncable(true));
	public static final DeferredHolder<Attribute, Attribute> ADRENALINE_LEVEL = REGISTRY.register("adrenaline_level", () -> new RangedAttribute("attribute.revengeance.adrenaline_level", 0, 0, 10000).setSyncable(true));

	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, RAGE_LEVEL);
		event.add(EntityType.PLAYER, ADRENALINE_LEVEL);
	}
}
