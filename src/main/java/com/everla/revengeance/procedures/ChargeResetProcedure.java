package com.everla.revengeance.procedures;

import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

import com.everla.revengeance.init.RevengeanceModAttributes;

@EventBusSubscriber
public class ChargeResetProcedure {
	@SubscribeEvent
	public static void onPlayerRespawned(PlayerEvent.PlayerRespawnEvent event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _livingEntity0 && _livingEntity0.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL))
			_livingEntity0.getAttribute(RevengeanceModAttributes.RAGE_LEVEL).setBaseValue(0);
		if (entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL))
			_livingEntity1.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).setBaseValue(0);
	}
}
