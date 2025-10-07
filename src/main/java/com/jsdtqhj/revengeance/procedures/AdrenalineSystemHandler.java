package com.jsdtqhj.revengeance.procedures;

import com.jsdtqhj.revengeance.attributes.RevengeanceModAttributes;
import com.jsdtqhj.revengeance.potion.RevengeanceModMobEffects;
import com.jsdtqhj.revengeance.sounds.RevengeanceModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@EventBusSubscriber
public class AdrenalineSystemHandler {

    // 肾上腺素常量
    private static final double MAX_ADRENALINE = 10000.0;
    private static final double CHARGE_RATE = MAX_ADRENALINE / (30.0 * 20.0); // 30秒充满，每tick充能速度
    private static final double DECAY_RATE = MAX_ADRENALINE / 20.0; // 1秒损失完，每tick损失速度
    private static final double BOSS_DETECTION_RADIUS = 128.0; // boss检测范围128格
    private static final int ADRENALINE_EFFECT_DURATION = 100; // 肾上腺素效果持续时间100刻
    
    // boss标签
    private static final TagKey<EntityType<?>> FORGE_BOSSES = TagKey.create(
        ForgeRegistries.ENTITY_TYPES.getRegistryKey(),
        ResourceLocation.tryParse("forge:bosses")
    );
    
    // 存储玩家受伤暂停充能的时间
    private static final Map<UUID, Long> hurtPauseTime = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player != null) {
            handleAdrenalineCharging(event.player);
            handleAdrenalineDecay(event.player);
            handleAdrenalineConsumption(event.player);
            handleAdrenalineFullEffect(event.player);
            handleAdrenalineFullSound(event.player);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        // 清除暂停充能时间
        hurtPauseTime.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof Player player) {
            handleAdrenalineLossOnHurt(player, event.getAmount());
        }
    }


    /**
     * 处理肾上腺素充能逻辑
     * 当128格内有boss实体时开始充能
     */
    public static void handleAdrenalineCharging(Player player) {
        if (!player.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get())) {
            return;
        }

        // 如果肾上腺素效果正在激活，不进行充能
        if (player.hasEffect(RevengeanceModMobEffects.ADRENALINE_EFFECT.get())) {
            return;
        }

        // 检查是否在受伤暂停期间
        long currentTime = player.level().getLevelData().getGameTime();
        UUID playerId = player.getUUID();
        
        if (hurtPauseTime.containsKey(playerId)) {
            long pauseEndTime = hurtPauseTime.get(playerId);
            if (currentTime < pauseEndTime) {
                return; // 还在暂停期间，不充能
            } else {
                hurtPauseTime.remove(playerId); // 暂停结束，移除记录
            }
        }

        // 检查是否有boss在128格范围内
        if (hasBossNearby(player)) {
            double currentAdrenaline = player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).getValue();
            
            if (currentAdrenaline < MAX_ADRENALINE) {
                // 充能
                double newValue = Math.min(MAX_ADRENALINE, currentAdrenaline + CHARGE_RATE);
                player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).setBaseValue(newValue);
            } else {
                // 已充满，持续给予抗性提升2效果
                giveResistanceEffect(player);
            }
        }
    }

    /**
     * 处理肾上腺素自然损失
     * 当没有boss在附近时持续损失
     */
    public static void handleAdrenalineDecay(Player player) {
        if (!player.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get())) {
            return;
        }

        // 如果有肾上腺素效果激活，不进行自然损失
        if (player.hasEffect(RevengeanceModMobEffects.ADRENALINE_EFFECT.get())) {
            return;
        }

        // 如果有boss在附近，不损失
        if (hasBossNearby(player)) {
            return;
        }

        double currentAdrenaline = player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).getValue();
        
        if (currentAdrenaline > 0) {
            double newValue = Math.max(0, currentAdrenaline - DECAY_RATE);
            player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).setBaseValue(newValue);
            
            // 重置充满音效标记
            if (newValue < MAX_ADRENALINE) {
                player.getPersistentData().putBoolean("adrenalineFullSoundPlayed", false);
            }
        }
    }

    /**
     * 处理肾上腺素效果激活时的消耗
     * 100刻内从满值逐渐减少至0
     */
    public static void handleAdrenalineConsumption(Player player) {
        if (!player.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get())) {
            return;
        }

        if (player.hasEffect(RevengeanceModMobEffects.ADRENALINE_EFFECT.get())) {
            double currentAdrenaline = player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).getValue();
            
            if (currentAdrenaline > 0) {
                // 100刻内均匀消耗完所有肾上腺素
                double consumptionRate = MAX_ADRENALINE / ADRENALINE_EFFECT_DURATION;
                double newValue = Math.max(0, currentAdrenaline - consumptionRate);
                player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).setBaseValue(newValue);
            }
        }
    }

    /**
     * 处理肾上腺素满值时的效果
     */
    public static void handleAdrenalineFullEffect(Player player) {
        if (!player.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get())) {
            return;
        }

        double currentAdrenaline = player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).getValue();
        
        // 如果肾上腺素已满且有boss在附近
        if (currentAdrenaline >= MAX_ADRENALINE && hasBossNearby(player)) {
            giveResistanceEffect(player);
        }
    }

    /**
     * 处理肾上腺素充满音效
     */
    public static void handleAdrenalineFullSound(Player player) {
        if (!player.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get())) {
            return;
        }

        double currentAdrenaline = player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).getValue();
        boolean soundPlayed = player.getPersistentData().getBoolean("adrenalineFullSoundPlayed");

        if (currentAdrenaline >= MAX_ADRENALINE && !soundPlayed) {
            // 播放肾上腺素充满音效
            playAdrenalineFullSound(player);
            player.getPersistentData().putBoolean("adrenalineFullSoundPlayed", true);
        } else if (currentAdrenaline < MAX_ADRENALINE && soundPlayed) {
            // 重置音效状态
            player.getPersistentData().putBoolean("adrenalineFullSoundPlayed", false);
        }
    }

    /**
     * 激活肾上腺素效果
     */
    public static void activateAdrenaline(Player player) {
        if (!player.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get())) {
            return;
        }

        double currentAdrenaline = player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).getValue();
        
        // 检查是否已满且没有激活效果
        if (currentAdrenaline >= MAX_ADRENALINE && !player.hasEffect(RevengeanceModMobEffects.ADRENALINE_EFFECT.get())) {
            if (!player.level().isClientSide()) {
                // 给予肾上腺素效果
                player.addEffect(new MobEffectInstance(RevengeanceModMobEffects.ADRENALINE_EFFECT.get(), 
                    ADRENALINE_EFFECT_DURATION, 0, true, true));
                
                // 播放激活音效
                playAdrenalineActivateSound(player);
                
                // 生成粒子效果
                generateAdrenalineParticles(player);
            }
        }
    }

    /**
     * 处理受伤时的肾上腺素损失
     */
    private static void handleAdrenalineLossOnHurt(Player player, float damage) {
        if (!player.getAttributes().hasAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get())) {
            return;
        }

        // 暂停充能1秒
        long currentTime = player.level().getLevelData().getGameTime();
        hurtPauseTime.put(player.getUUID(), currentTime + 20); // 20刻 = 1秒

        double currentAdrenaline = player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).getValue();
        if (currentAdrenaline <= 0) {
            return;
        }

        // 检查肾上腺素是否已满，如果满了则损失全部
        if (currentAdrenaline >= MAX_ADRENALINE) {
            player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).setBaseValue(0);
            
            // 重置充满音效标记
            player.getPersistentData().putBoolean("adrenalineFullSoundPlayed", false);
            
            // 播放充能中断音效
            if (!player.level().isClientSide() && player.level() instanceof ServerLevel serverLevel) {
                serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    RevengeanceModSounds.ADRENALINE_CHARGE_BREAK.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
            }
            return;
        }

        float maxHealth = player.getMaxHealth();
        int damageInt = (int) Math.floor(damage);
        double lossAmount = 0;

        if (maxHealth < 20) {
            if (damageInt == 1) {
                lossAmount = MAX_ADRENALINE * 0.1; // 损失10%
            } else if (damageInt > 1) {
                lossAmount = MAX_ADRENALINE; // 损失全部
            }
        } else {
            if (damageInt == 1) {
                lossAmount = MAX_ADRENALINE * 0.05; // 损失5%
            } else if (damageInt > 1 && damage < maxHealth * 0.1) {
                lossAmount = MAX_ADRENALINE * 0.1; // 损失10%
            } else if (damage >= maxHealth * 0.1) {
                lossAmount = MAX_ADRENALINE; // 损失全部
            }
        }

        if (lossAmount > 0) {
            double newValue = Math.max(0, currentAdrenaline - lossAmount);
            player.getAttribute(RevengeanceModAttributes.ADRENALINE_LEVEL.get()).setBaseValue(newValue);
            
            // 重置充满音效标记
            player.getPersistentData().putBoolean("adrenalineFullSoundPlayed", false);
        }
    }

    /**
     * 检查128格范围内是否有boss
     */
    private static boolean hasBossNearby(Player player) {
        Vec3 playerPos = player.position();
        AABB searchArea = new AABB(
            playerPos.x - BOSS_DETECTION_RADIUS, playerPos.y - BOSS_DETECTION_RADIUS, playerPos.z - BOSS_DETECTION_RADIUS,
            playerPos.x + BOSS_DETECTION_RADIUS, playerPos.y + BOSS_DETECTION_RADIUS, playerPos.z + BOSS_DETECTION_RADIUS
        );

        for (LivingEntity entity : player.level().getEntitiesOfClass(LivingEntity.class, searchArea)) {
            if (entity != player && entity.getType().is(FORGE_BOSSES)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * 给予抗性提升2效果（不显示粒子）
     */
    private static void giveResistanceEffect(Player player) {
        if (!player.hasEffect(MobEffects.DAMAGE_RESISTANCE) || 
            player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() < 1) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 1, false, false));
        }
    }

    /**
     * 播放肾上腺素充满音效
     */
    private static void playAdrenalineFullSound(Player player) {
        if (!player.level().isClientSide() && player.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                RevengeanceModSounds.ADRENALINE_FULL.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    /**
     * 播放肾上腺素激活音效
     */
    private static void playAdrenalineActivateSound(Player player) {
        if (!player.level().isClientSide() && player.level() instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                RevengeanceModSounds.ADRENALINE_ACTIVITE.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    /**
     * 生成肾上腺素激活粒子效果
     */
    private static void generateAdrenalineParticles(Player player) {
        if (!player.level().isClientSide() && player.level() instanceof ServerLevel serverLevel) {
            // 电火花粒子
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                player.getX(), player.getY() + 1, player.getZ(),
                20, 0, 0, 0, 3.0);
            
            // 附魔击中粒子
            serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT,
                player.getX(), player.getY() + 1, player.getZ(),
                30, 0, 0, 0, 1.0);
        }
    }
}