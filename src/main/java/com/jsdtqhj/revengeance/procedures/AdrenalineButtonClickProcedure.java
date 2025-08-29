package com.jsdtqhj.revengeance.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import com.jsdtqhj.revengeance.potion.RevengeanceModMobEffects;
import com.jsdtqhj.revengeance.attributes.RevengeanceModAttributes;

public class AdrenalineButtonClickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity _livingEntity0 && _livingEntity0.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get())) {
			if ((entity instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get())
					? _livingEntity1.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).getValue()
					: 0) == 10000) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(RevengeanceModMobEffects.ADRENALINE_EFFECT.get(), 100, 0, true, true));
				entity.getPersistentData().putBoolean("enabledAdrenaline", true);
			}
		}
	}
}