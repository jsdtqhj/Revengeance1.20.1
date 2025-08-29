package com.jsdtqhj.revengeance.procedures;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffectInstance;

import com.jsdtqhj.revengeance.potion.RevengeanceModMobEffects;
import com.jsdtqhj.revengeance.attributes.RevengeanceModAttributes;

public class RageButtonClickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		RageSystemHandler.activateRage(entity);
	}
}