package com.everla.revengeance.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import com.everla.revengeance.init.RevengeanceModAttributes;

public class RageDisplayl6Procedure {
	public static boolean execute(Entity entity) {
		if (entity == null)
			return false;
		if ((entity instanceof LivingEntity _livingEntity0 && _livingEntity0.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL) ? _livingEntity0.getAttribute(RevengeanceModAttributes.RAGE_LEVEL).getValue() : 0) >= 54) {
			return true;
		}
		return false;
	}
}
