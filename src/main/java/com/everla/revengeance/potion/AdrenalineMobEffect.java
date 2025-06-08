
package com.everla.revengeance.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import com.everla.revengeance.procedures.AdrenalineAttributeAddProcedure;

public class AdrenalineMobEffect extends MobEffect {
	public AdrenalineMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -16711783);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		AdrenalineAttributeAddProcedure.execute(entity);
	}
}
