package com.jsdtqhj.revengeance.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class RageMobEffect extends MobEffect {
	private static final String RAGE_MELEE_UUID = "8394164b-693a-58b2-94f9-d51644cec666";
	public RageMobEffect() {

		super(MobEffectCategory.BENEFICIAL, -3407872);
		this.addAttributeModifier(
				Attributes.ATTACK_DAMAGE,
				RAGE_MELEE_UUID,
				0.35, AttributeModifier.Operation.MULTIPLY_TOTAL
		);
	}
}

