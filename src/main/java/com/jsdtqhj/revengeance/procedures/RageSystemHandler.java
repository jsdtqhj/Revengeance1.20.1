package com.jsdtqhj.revengeance.procedures;

import com.jsdtqhj.revengeance.Config;
import com.jsdtqhj.revengeance.RevengeanceMod;
import com.jsdtqhj.revengeance.attributes.RevengeanceModAttributes;
import com.jsdtqhj.revengeance.potion.RevengeanceModMobEffects;
import com.jsdtqhj.revengeance.sounds.RevengeanceModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public class RageSystemHandler {

    // === Performance Caching ===
    
    // 缓存每个玩家的怒气充能速度
    private static final Map<UUID, CachedRageData> rageCache = new HashMap<>();
    
    // 缓存数据结构
    private static class CachedRageData {
        double totalChargeRate;
        long cacheTime;
        long lastUpdateTime;
        
        CachedRageData(double totalChargeRate, long cacheTime, long lastUpdateTime) {
            this.totalChargeRate = totalChargeRate;
            this.cacheTime = cacheTime;
            this.lastUpdateTime = lastUpdateTime;
        }
        
        boolean isExpired(long currentTime, int cacheDuration) {
            return currentTime - cacheTime >= cacheDuration;
        }
        
        boolean shouldUpdate(long currentTime, int updateInterval) {
            return currentTime - lastUpdateTime >= updateInterval;
        }
    }

    // === Event Listeners ===

    @SubscribeEvent
    public static void
    onPlayerRespawn(PlayerEvent.PlayerRespawnEvent
                            event) {
        resetAttributes(event.getEntity());
        // 清除缓存
        rageCache.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void
    onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null) {
            chargeRageForPlayer(event.player);
            handleRageAnimation(event.player);
            handleRageConsumption(event.player);
        }
    }

    /**
     * 处理怒气效果过期事件
     * 当怒气效果过期时播放音效
     */
    @SubscribeEvent
    public static void onMobEffectExpired(MobEffectEvent.Expired event) {
        // 检查是否为怒气效果过期
        if (event.getEffectInstance().getEffect() == RevengeanceModMobEffects.RAGE_EFFECT.get()) {
            LivingEntity entity = event.getEntity();
            
            // 播放怒气结束音效
            if (!entity.level().isClientSide() && entity.level() instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null,
                        entity.getX(), entity.getY(), entity.getZ(),
                        RevengeanceModSounds.RAGE_END.get(),
                        SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }


    /**
     * 重置实体的怒气和肾上腺素属性
     * 在玩家重生时调用此方法
     */
    public static void resetAttributes(Entity
                                               entity) {
        // 只处理LivingEntity
        if (!(entity instanceof LivingEntity
                living)) {
            return;
        }

        // 重置怒气值
        if (living.getAttributes().hasAttribute(
                RevengeanceModAttributes.RAGE_LEVEL.get())) {
            living.getAttribute(RevengeanceModAttributes.RAGE_LEVEL.get()).setBaseValue(0);
            // 同时清除相关标记
            living.getPersistentData().putBoolean("rageFullSoundPlayed", false);
            living.getPersistentData().putBoolean("enabledRage", false);
        }

        // 重置肾上腺素值
        if
        (living.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get())) {

            living.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).setBaseValue(0);
        }
    }

    // 创建一个静态的TagKey，避免重复创建
    private static final ResourceLocation
            RAGE_TAG_LOCATION =
            ResourceLocation.tryParse(RevengeanceMod.MODID +
                    ":not_trigger_rage");
    private static final TagKey<EntityType<?>>
            NOT_TRIGGER_RAGE = TagKey.create(ForgeRegistries
                    .ENTITY_TYPES.getRegistryKey(),
            RAGE_TAG_LOCATION);

    // 怒气充能和流失的常量
    private static final double MAX_RAGE = 100.0;
    private static final double MAX_CHARGE_RATE = MAX_RAGE / 45.0; // 每秒充能满值的1/45
    private static final double DECAY_RATE = MAX_RAGE / 30.0; // 每秒流失满值的1/30
    private static final long DECAY_DELAY_TICKS = 20 * 20; // 20秒 = 400 ticks

    /**
     * 为单个玩家处理怒气充能逻辑 (优化版本)
     * 使用缓存机制减少实体搜索频率，同时保持效果一致
     */
    public static void chargeRageForPlayer(Player player) {
        // 检查玩家是否有怒气属性
        if (!player.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL.get())) {
            return;
        }

        // 如果怒气效果正在激活，不进行充能
        if (player.hasEffect(RevengeanceModMobEffects.RAGE_EFFECT.get())) {
            return;
        }

        LevelAccessor world = player.level();
        long currentTime = world.getLevelData().getGameTime();
        UUID playerId = player.getUUID();
        
        // 获取缓存的充能速度
        double totalChargeRate = getCachedTotalChargeRate(player, world, currentTime);
        
        double current = player.getAttribute(RevengeanceModAttributes.RAGE_LEVEL.get()).getValue();

        if (totalChargeRate > 0) {
            // 有敌对生物在范围内，进行充能
            double newValue = Math.min(MAX_RAGE, current + totalChargeRate / 20.0); // 除以20因为每tick调用
            
            player.getAttribute(RevengeanceModAttributes.RAGE_LEVEL.get()).setBaseValue(newValue);
            
            // 重置无敌对生物计时器
            player.getPersistentData().putLong("lastHostileTime", currentTime);
        } else {
            // 没有敌对生物在范围内，检查是否需要开始流失
            long lastHostileTime = player.getPersistentData().getLong("lastHostileTime");

            if (lastHostileTime == 0) {
                // 首次记录时间
                player.getPersistentData().putLong("lastHostileTime", currentTime);
            } else if (currentTime - lastHostileTime >= DECAY_DELAY_TICKS) {
                // 超过20秒没有敌对生物，开始流失
                if (current > 0) {
                    double newValue = Math.max(0, current - DECAY_RATE / 20.0); // 除以20因为每tick调用
                    player.getAttribute(RevengeanceModAttributes.RAGE_LEVEL.get()).setBaseValue(newValue);
                }
            }
        }
    }

    /**
     * 获取缓存的总充能速度，如果缓存过期或需要更新则重新计算
     */
    private static double getCachedTotalChargeRate(Player player, LevelAccessor world, long currentTime) {
        UUID playerId = player.getUUID();
        CachedRageData cached = rageCache.get(playerId);
        
        // 如果没有缓存或缓存过期，直接重新计算
        if (cached == null || cached.isExpired(currentTime, Config.rageCacheDuration)) {
            double totalChargeRate = calculateTotalChargeRate(player, world);
            rageCache.put(playerId, new CachedRageData(totalChargeRate, currentTime, currentTime));
            return totalChargeRate;
        }
        
        // 如果需要更新缓存
        if (cached.shouldUpdate(currentTime, Config.rageUpdateInterval)) {
            double totalChargeRate = calculateTotalChargeRate(player, world);
            cached.totalChargeRate = totalChargeRate;
            cached.lastUpdateTime = currentTime;
            return totalChargeRate;
        }
        
        // 使用缓存的数据
        return cached.totalChargeRate;
    }

    /**
     * 计算所有敌对生物的总充能速度 (使用配置项)
     * @param player 玩家实体
     * @param world 世界
     * @return 所有敌对生物提供的总充能速度
     */
    private static double calculateTotalChargeRate(LivingEntity player, LevelAccessor world) {
        Vec3 playerPos = player.position();
        double detectionRadius = Config.rageDetectionRadius;
        
        AABB searchArea = new AABB(
                playerPos.x - detectionRadius,
                playerPos.y - detectionRadius, playerPos.z - detectionRadius,
                playerPos.x + detectionRadius,
                playerPos.y + detectionRadius, playerPos.z + detectionRadius
        );

        double totalChargeRate = 0.0;

        // 搜索所有LivingEntity
        for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, searchArea)) {
            // 跳过玩家自己和有not_trigger_rage标签的实体
            if (entity == player || entity.getType().is(NOT_TRIGGER_RAGE)) {
                continue;
            }

            // 检查是否为敌对生物（对玩家有敌意）
            if (isHostileToPlayer(entity, player)) {
                double distance = playerPos.distanceTo(entity.position());
                if (distance <= detectionRadius) {
                    // 计算这个敌对生物提供的充能速度并叠加
                    double chargeRate = calculateChargeRate(distance);
                    totalChargeRate += chargeRate;
                }
            }
        }

        return totalChargeRate;
    }

    /**
     * 检查实体是否对玩家敌对
     * @param entity 要检查的实体
     * @param player 玩家
     * @return 如果敌对则返回true
     */
    private static boolean
    isHostileToPlayer(LivingEntity entity,
                      LivingEntity player) {
        // 检查实体的目标是否为玩家
        if (entity.getLastHurtByMob() == player
                || entity.getLastHurtMob() == player) {
            return true;
        }

        //检查实体是否为怪物类型（通常对玩家敌对）
        return
                !entity.getType().getCategory().isFriendly();
    }

    /**
     * 根据距离计算充能速度 (使用配置项)
     * @param distance 到最近敌对生物的距离
     * @return 每秒的充能速度
     */
    private static double calculateChargeRate(double distance) {
        double innerRadius = Config.rageInnerRadius;
        double outerRadius = Config.rageDetectionRadius;
        
        if (distance <= innerRadius) {
            // 内圈：最大充能速度
            return MAX_CHARGE_RATE;
        } else if (distance <= outerRadius) {
            // 内圈到外圈：线性递减充能速度
            // 从内圈的100%递减到外圈的0%
            double ratio = 1.0 - ((distance - innerRadius) / (outerRadius - innerRadius));
            return MAX_CHARGE_RATE * ratio;
        } else {
            // 超出范围：无充能
            return 0.0;
        }
    }

    /**
     * 激活怒气效果
     * 当怒气值达到100时，添加怒气效果并标记状态
     */
    public static void activateRage(Entity entity) {
        // 快速类型检查
        if (!(entity instanceof LivingEntity
                living)) {
            return;
        }

        // 检查是否有怒气属性
        if (!living.getAttributes().hasAttribute
                (RevengeanceModAttributes.RAGE_LEVEL.get())) {
            return;
        }

        // 检查怒气值是否达到100且没有激活怒气效果
        if (living.getAttribute(RevengeanceModAttributes.RAGE_LEVEL.get()).getValue() == 100 
                && !living.hasEffect(RevengeanceModMobEffects.RAGE_EFFECT.get())) {
            // 仅在服务器端添加效果
            if (!entity.level().isClientSide())
            {
                // 添加怒气效果
                living.addEffect(new
                        MobEffectInstance(RevengeanceModMobEffects.RAGE_EFFECT.get(), 180, 0, true, true));

                // 播放音效
                if (living.level() instanceof
                        ServerLevel serverLevel) {
                    serverLevel.playSound(null,
                            living.getX(), living.getY(), living.getZ(),

                            RevengeanceModSounds.RAGE_ACTIVITE.get(),
                            SoundSource.PLAYERS, 1.0F, 1.0F);
                }

                // 生成粒子效果
                if (living.level() instanceof
                        ServerLevel serverLevel) {
                    // 愤怒村民粒子
                    //(angry_villager particles)
                    serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                            living.getX(),
                            living.getY(), living.getZ(),
                            20, 1.0, 1.0, 1.0,
                            1.0);

                    // 深红色孢子粒子
                    //(crimson_spore_particles)
                    serverLevel.sendParticles(ParticleTypes.CRIMSON_SPORE,
                            living.getX(),
                            living.getY() + 1, living.getZ(),
                            100, 0.0, 0.0, 0.0,
                            1.0);
                }
            }
            entity.getPersistentData().putBoolean("enabledRage", true);
        }
    }

    /**
     * 处理怒气动画和音效
     * 当怒气值达到100时播放音效
     */
    public static void
    handleRageAnimation(Entity entity) {
        //快速检查：必须是LivingEntity且在服务器端
        if (!(entity instanceof LivingEntity
                living) || living.level().isClientSide()) {
            return;
        }

        // 获取怒气值，如果没有怒气属性则默认为0
        double rageValue =
                living.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL.get()) ?
                        living.getAttribute(RevengeanceModAttributes.RAGE_LEVEL.get()).getValue() : 0;

        // 处理音效播放逻辑
        boolean soundPlayed = living.getPersistentData().getBoolean("rageFullSoundPlayed");

        if (rageValue >= 100 && !soundPlayed) {
            // 播放怒气满值音效
            if (living.level() instanceof
                    ServerLevel serverLevel) {
                serverLevel.playSound(null,
                        living.getX(), living.getY(), living.getZ(),

                        RevengeanceModSounds.RAGE_FULL.get(),
                        living.getSoundSource(), 1.0F, 1.0F);
            }
            living.getPersistentData().putBoolean("rageFullSoundPlayed", true);
        } else if (rageValue < 100 &&
                soundPlayed) {
            // 重置音效状态
            living.getPersistentData().putBoolean("rageFullSoundPlayed", false);
        }
    }

    /**
     * 处理怒气消耗逻辑
     * 当玩家有怒气效果时，逐渐消耗rage_level
     */
    public static void handleRageConsumption(Player player) {
        // 检查玩家是否有怒气属性
        if (!player.getAttributes().hasAttribute(RevengeanceModAttributes.RAGE_LEVEL.get())) {
            return;
        }

        // 检查玩家是否有怒气效果
        if (player.hasEffect(RevengeanceModMobEffects.RAGE_EFFECT.get())) {
            double currentRage = player.getAttribute(RevengeanceModAttributes.RAGE_LEVEL.get()).getValue();
            
            if (currentRage > 0) {
                // 计算每tick消耗的怒气值 (100怒气在180刻内消耗完毕)
                double consumptionRate = 100.0 / 180.0; // 每tick消耗的怒气值
                double newRage = Math.max(0, currentRage - consumptionRate);
                
                player.getAttribute(RevengeanceModAttributes.RAGE_LEVEL.get()).setBaseValue(newRage);
            }
        }
    }
}