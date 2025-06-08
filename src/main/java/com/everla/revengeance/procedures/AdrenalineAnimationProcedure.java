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
public class AdrenalineAnimationProcedure {
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
		if (entity.getPersistentData().getBoolean("enabledAdrenaline") == true) {
			if ((entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
					? _livingEntity1.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).getValue()
					: 0) >= 101) {
				if (entity instanceof LivingEntity _livingEntity3 && _livingEntity3.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL))
					_livingEntity3.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
							.setBaseValue(((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(RevengeanceModMobEffects.ADRENALINE) ? _livEnt.getEffect(RevengeanceModMobEffects.ADRENALINE).getDuration() : 0) * 100));
			} else if ((entity instanceof LivingEntity _livingEntity4 && _livingEntity4.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
					? _livingEntity4.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).getValue()
					: 0) < 101) {
				if (entity instanceof LivingEntity _livingEntity5 && _livingEntity5.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL))
					_livingEntity5.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).setBaseValue(0);
				entity.getPersistentData().putBoolean("enabledAdrenaline", false);
				entity.getPersistentData().putBoolean("fullAdrenaline", false);
			}
		}
		if ((entity instanceof LivingEntity _livingEntity8 && _livingEntity8.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL) ? _livingEntity8.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).getValue() : 0) == 10000) {
			if (!(entity.getPersistentData().getBoolean("fullAdrenaline") == true)) {
				{
					Entity _ent = entity;
					if (!_ent.level().isClientSide() && _ent.getServer() != null) {
						_ent.getServer().getCommands().performPrefixedCommand(new CommandSourceStack(CommandSource.NULL, _ent.position(), _ent.getRotationVector(), _ent.level() instanceof ServerLevel ? (ServerLevel) _ent.level() : null, 4,
								_ent.getName().getString(), _ent.getDisplayName(), _ent.level().getServer(), _ent), "playsound revengeance:adrenaline_full player @s");
					}
				}
				entity.getPersistentData().putBoolean("fullAdrenaline", true);
			}
		}
	}
}
