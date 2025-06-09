package com.everla.revengeance.procedures;

import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import javax.annotation.Nullable;

import java.util.Comparator;

import com.everla.revengeance.init.RevengeanceModAttributes;

@EventBusSubscriber
public class AdrenalineChargeProcedure {
	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(128 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entityiterator.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("c:bosses")))) {
					entity.getPersistentData().putDouble("AdrenalineTicker", 10);
				}
			}
		}
		if (entity.getPersistentData().getDouble("AdrenalineTicker") >= 1) {
			entity.getPersistentData().putDouble("AdrenalineTicker", (entity.getPersistentData().getDouble("AdrenalineTicker") - 1));
			if ((entity instanceof LivingEntity _livingEntity6 && _livingEntity6.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
					? _livingEntity6.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).getBaseValue()
					: 0) < 10000) {
				if (entity instanceof LivingEntity _livingEntity8 && _livingEntity8.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL))
					_livingEntity8.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
							.setBaseValue(((entity instanceof LivingEntity _livingEntity7 && _livingEntity7.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
									? _livingEntity7.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).getBaseValue()
									: 0) + 16.67));
			}
			if ((entity instanceof LivingEntity _livingEntity9 && _livingEntity9.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
					? _livingEntity9.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).getBaseValue()
					: 0) >= 10000) {
				if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
					_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 5, 2, true, false));
			}
		}
		if (entity.getPersistentData().getDouble("AdrenalineTicker") == 0) {
			if ((entity instanceof LivingEntity _livingEntity12 && _livingEntity12.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
					? _livingEntity12.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).getBaseValue()
					: 0) >= 500) {
				if (entity instanceof LivingEntity _livingEntity14 && _livingEntity14.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL))
					_livingEntity14.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
							.setBaseValue(((entity instanceof LivingEntity _livingEntity13 && _livingEntity13.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
									? _livingEntity13.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).getBaseValue()
									: 0) - 500));
			}
			if ((entity instanceof LivingEntity _livingEntity15 && _livingEntity15.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL)
					? _livingEntity15.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).getBaseValue()
					: 0) < 500) {
				if (entity instanceof LivingEntity _livingEntity16 && _livingEntity16.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL))
					_livingEntity16.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL).setBaseValue(0);
			}
		}
	}
}
