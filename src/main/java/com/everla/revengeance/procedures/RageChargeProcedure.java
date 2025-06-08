package com.everla.revengeance.procedures;

import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.Event;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.tags.TagKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.Registries;

import javax.annotation.Nullable;

import java.util.Comparator;

import com.everla.revengeance.init.RevengeanceModAttributes;

@EventBusSubscriber
public class RageChargeProcedure {
	@SubscribeEvent
	public static void onEntityTick(EntityTickEvent.Pre event) {
		execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
	}

	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		execute(null, world, x, y, z, entity);
	}

	private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (!entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse("revengeance:not_trigger_rage")))) {
			{
				final Vec3 _center = new Vec3(x, y, z);
				for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(16 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
					if ((entityiterator instanceof LivingEntity _livingEntity1 && _livingEntity1.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL)
							? _livingEntity1.getAttribute(RevengeanceModAttributes.RAGE_LEVEL).getBaseValue()
							: 0) < 100) {
						if (entityiterator instanceof LivingEntity _livingEntity3 && _livingEntity3.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL))
							_livingEntity3.getAttribute(RevengeanceModAttributes.RAGE_LEVEL)
									.setBaseValue(((entityiterator instanceof LivingEntity _livingEntity2 && _livingEntity2.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL)
											? _livingEntity2.getAttribute(RevengeanceModAttributes.RAGE_LEVEL).getBaseValue()
											: 0) + 0.08));
					}
				}
			}
		}
	}
}
