/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package com.jsdtqhj.revengeance.attributes;

import com.jsdtqhj.revengeance.RevengeanceMod;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = RevengeanceMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public class RevengeanceModAttributes {
	public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, RevengeanceMod.MODID);
	public static final RegistryObject<Attribute> RAGE_LEVEL = REGISTRY.register("rage_level", () -> new RangedAttribute("attribute.revengeance.rage_level", 0, 0, 100).setSyncable(true));
	public static final RegistryObject<Attribute> ADRENALINE_LEVEL = REGISTRY.register("adrenaline_level", () -> new RangedAttribute("attribute.revengeance.adrenaline_level", 0, 0, 10000).setSyncable(true));

	@SubscribeEvent
	public static void addAttributes(EntityAttributeModificationEvent event) {
		event.add(EntityType.PLAYER, RAGE_LEVEL.get());
		event.add(EntityType.PLAYER, ADRENALINE_LEVEL.get());
	}
}