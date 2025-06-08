package com.everla.revengeance.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import com.everla.revengeance.init.RevengeanceModMobEffects;
import com.everla.revengeance.init.RevengeanceModAttributes;

public class AdrenalineButtonClickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _livingEntity0 && _livingEntity0.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)) {
			if ((entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
					? _livingEntity1.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).getValue()
					: 0) == 10000) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(RevengeanceModMobEffects.ADRENALINE, 100, 0, true, true));
				entity.getPersistentData().putBoolean("enabledAdrenaline", true);
			}
		}
	}
}
