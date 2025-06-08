
package com.everla.revengeance.potion;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffect;

import com.everla.revengeance.procedures.RageAttributeAddProcedure;

public class RageMobEffect extends MobEffect {
	public RageMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -3407872);
	}

	@Override
	public void onEffectStarted(LivingEntity entity, int amplifier) {
		RageAttributeAddProcedure.execute(entity);
	}
}
