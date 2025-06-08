package com.everla.revengeance.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.CommandSource;

import javax.annotation.Nullable;

import com.everla.revengeance.init.RevengeanceModMobEffects;
import com.everla.revengeance.init.RevengeanceModAttributes;

@EventBusSubscriber
public class RageAnimationProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity());
	}

	public static void execute(Entity entity) {
		execute(null, entity);
	}

	private static void execute(@Nullable Event event, Entity entity) {
		if (entity == null)
			return;
		if (entity.getPersistentData().getBoolean("enabledRage") == true) {
			if ((entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL) ? _livingEntity1.getAttribute(RevengeanceModAttributes.RAGE_LEVEL).getValue() : 0) >= 1) {
				if (entity instanceof LivingEntity _livingEntity3 && _livingEntity3.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL))
					_livingEntity3.getAttribute(RevengeanceModAttributes.RAGE_LEVEL)
							.setBaseValue(((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(RevengeanceModMobEffects.RAGE) ? _livEnt.getEffect(RevengeanceModMobEffects.RAGE).getDuration() : 0) / 1.8));
			} else if ((entity instanceof LivingEntity _livingEntity4 && _livingEntity4.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL) ? _livingEntity4.getAttribute(RevengeanceModAttributes.RAGE_LEVEL).getValue() : 0) < 1) {
				if (entity instanceof LivingEntity _livingEntity5 && _livingEntity5.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL))
					_livingEntity5.getAttribute(RevengeanceModAttributes.RAGE_LEVEL).setBaseValue(0);
				entity.getPersistentData().putBoolean("enabledRage", false);
				entity.getPersistentData().putBoolean("fullRage", false);
			}
		}
		if ((entity instanceof LivingEntity _livingEntity8 && _livingEntity8.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL) ? _livingEntity8.getAttribute(RevengeanceModAttributes.RAGE_LEVEL).getValue() : 0) == 100) {
			if (!(entity.getPersistentData().getBoolean("fullRage") == true)) {
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound revengeance:rage_full player @s");
					}
				}
				entity.getPersistentData().putBoolean("fullRage", true);
			}
		}
	}
}
