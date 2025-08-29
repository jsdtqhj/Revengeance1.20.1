package com.jsdtqhj.revengeance.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AdrenalineMobEffect extends MobEffect {
	private static final String ADRENALINE_MELEE_UUID = "1347de40-9b39-50bb-bdac-f05bc35d0d20";
	public AdrenalineMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -16711783);
		this.addAttributeModifier(
				Attributes.ATTACK_DAMAGE,
				ADRENALINE_MELEE_UUID,
				1.1,
				AttributeModifier.Operation.MULTIPLY_BASE
		);
	}

}