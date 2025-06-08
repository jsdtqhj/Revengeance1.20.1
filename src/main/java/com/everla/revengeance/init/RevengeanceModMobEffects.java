
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.everla.revengeance.init;

import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.core.registries.Registries;

import com.everla.revengeance.procedures.RageAttributeRemoveProcedure;
import com.everla.revengeance.procedures.AdrenalineAttributeRemoveProcedure;
import com.everla.revengeance.potion.RageMobEffect;
import com.everla.revengeance.potion.AdrenalineMobEffect;
import com.everla.revengeance.RevengeanceMod;

@EventBusSubscriber
public class RevengeanceModMobEffects {
	public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, RevengeanceMod.MODID);
	public static final DeferredHolder<MobEffect, MobEffect> RAGE = REGISTRY.register("rage", () -> new RageMobEffect());
	public static final DeferredHolder<MobEffect, MobEffect> ADRENALINE = REGISTRY.register("adrenaline", () -> new AdrenalineMobEffect());

	@SubscribeEvent
	public static void onEffectRemoved(MobEffectEvent.Remove event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			expireEffects(event.getEntity(), effectInstance);
		}
	}

	@SubscribeEvent
	public static void onEffectExpired(MobEffectEvent.Expired event) {
		MobEffectInstance effectInstance = event.getEffectInstance();
		if (effectInstance != null) {
			expireEffects(event.getEntity(), effectInstance);
		}
	}

	private static void expireEffects(Entity entity, MobEffectInstance effectInstance) {
		if (effectInstance.getEffect().is(RAGE)) {
			RageAttributeRemoveProcedure.execute(entity);
		} else if (effectInstance.getEffect().is(ADRENALINE)) {
			AdrenalineAttributeRemoveProcedure.execute(entity);
		}
	}
}
