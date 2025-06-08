package com.everla.revengeance.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import com.everla.revengeance.init.RevengeanceModMobEffects;
import com.everla.revengeance.init.RevengeanceModAttributes;

public class RageButtonClickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _livingEntity0 && _livingEntity0.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL)) {
			if ((entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL) ? _livingEntity1.getAttribute(RevengeanceModAttributes.RAGE_LEVEL).getValue() : 0) == 100) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(RevengeanceModMobEffects.RAGE, 180, 0, true, true));
				entity.getPersistentData().putBoolean("enabledRage", true);
			}
		}
	}
}
