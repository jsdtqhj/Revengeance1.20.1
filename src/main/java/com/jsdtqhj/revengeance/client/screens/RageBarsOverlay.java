package com.jsdtqhj.revengeance.client.screens;

import com.jsdtqhj.revengeance.RevengeanceMod;
import com.jsdtqhj.revengeance.attributes.RevengeanceModAttributes;
import com.jsdtqhj.revengeance.potion.RevengeanceModMobEffects;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid = RevengeanceMod.MODID, value = Dist.CLIENT)
public class RageBarsOverlay {
	private static final ResourceLocation RAGE_BAR_BASE = ResourceLocation.tryParse(
			RevengeanceMod.MODID + ":textures/screens/rage_bar_base.png");
	private static final ResourceLocation RAGE_BAR_FILL = ResourceLocation.tryParse(
			RevengeanceMod.MODID + ":textures/screens/rage_bar_fill.png");
	
	private static final ResourceLocation[] RAGE_FULL_ANIMATION = new ResourceLocation[10];
	static {
		for (int i = 0; i < 10; i++) {
			RAGE_FULL_ANIMATION[i] = ResourceLocation.tryParse(
					RevengeanceMod.MODID + ":textures/screens/rage_full_animation_" + (i + 1) + ".png");
		}
	}

	private static final ResourceLocation ADRENALINE_BAR_BASE = ResourceLocation.tryParse(
			RevengeanceMod.MODID + ":textures/screens/adrenaline_bar_base.png");
	private static final ResourceLocation ADRENALINE_BAR_FILL = ResourceLocation.tryParse(
			RevengeanceMod.MODID + ":textures/screens/adrenaline_bar_fill.png");
	private static final ResourceLocation ADRENALINE_BAR_FULL = ResourceLocation.tryParse(
			RevengeanceMod.MODID + ":textures/screens/adrenaline_bar_full.png");

	private static boolean rageWasFull = false;
	private static boolean isPlayingRageAnimation = false;
	private static int rageAnimationFrame = 0;
	private static long rageAnimationStartTime = 0;
	private static final int RAGE_ANIMATION_FRAME_DURATION = 1;
	
	// 缓存抖动计算结果
	private static int cachedShakeX = 0;
	private static int cachedShakeY = 0;
	private static long lastShakeUpdateTime = 0;
	private static final int SHAKE_UPDATE_INTERVAL = 16; // 每25ms更新一次抖动
	
	// 缓存动画计算结果
	private static long lastAnimationUpdateTime = 0;
	private static final int ANIMATION_UPDATE_INTERVAL = 16; // 动画帧更新间隔

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onPreGui(RenderGuiEvent.Pre event) {
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		if (player == null) return;

		AttributeMap attributes = player.getAttributes();
		double worldRage = attributes.getValue(RevengeanceModAttributes.RAGE_LEVEL.get());
		double worldAdren = attributes.getValue(RevengeanceModAttributes.ADRENALINE_LEVEL.get());

		double maxRage = 100.0;
		double maxAdrenaline = 10000.0;

		double ragePct = clamp(worldRage / maxRage, 0.0, 1.0);
		double adrenPct = clamp(worldAdren / maxAdrenaline, 0.0, 1.0);

		int screenH = event.getGuiGraphics().guiHeight();

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(
				GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
				GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

		// 检查怒气是否刚刚达到满值，触发动画
		boolean rageIsFull = ragePct >= 1.0;
		if (rageIsFull && !rageWasFull && !isPlayingRageAnimation) {
			// 怒气刚刚满，开始播放动画
			isPlayingRageAnimation = true;
			rageAnimationFrame = 0;
			rageAnimationStartTime = System.currentTimeMillis();
		}
		rageWasFull = rageIsFull;

		// 更新动画帧 - 优化为只在需要时计算
		if (isPlayingRageAnimation) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastAnimationUpdateTime >= ANIMATION_UPDATE_INTERVAL) {
				long elapsedTicks = (currentTime - rageAnimationStartTime) / 50;
				int targetFrame = (int) (elapsedTicks / RAGE_ANIMATION_FRAME_DURATION);
				
				if (targetFrame >= 10) {
					isPlayingRageAnimation = false;
					rageAnimationFrame = 0;
				} else {
					rageAnimationFrame = targetFrame;
				}
				lastAnimationUpdateTime = currentTime;
			}
		}

		// —— Rage Bar ——
		int rageBarW = 76, rageBarH = 19;
		int x_rage = -7, y_rage = screenH - 20;
		
		// 检查是否有怒气效果激活，如果有则添加抖动
		boolean hasRageEffect = player.hasEffect(RevengeanceModMobEffects.RAGE_EFFECT.get());
		if (hasRageEffect) {
			// 使用缓存的抖动值，减少计算频率
			long currentTime = System.currentTimeMillis();
			if (currentTime - lastShakeUpdateTime >= SHAKE_UPDATE_INTERVAL) {
				double shakeIntensity = 1.1;
				cachedShakeX = (int) (Math.sin(currentTime * 0.05) * shakeIntensity);
				cachedShakeY = (int) (Math.cos(currentTime * 0.08) * shakeIntensity);
				lastShakeUpdateTime = currentTime;
			}
			x_rage += cachedShakeX;
			y_rage += cachedShakeY;
		}
		
		event.getGuiGraphics().blit(
				RAGE_BAR_BASE, x_rage, y_rage,
				0, 0, rageBarW, rageBarH, rageBarW, rageBarH);

		// fill 源图有效区间 [18,57)
		int rageFillStart = 18, rageFillEnd = 57;
		int rageFillWidth = rageFillEnd - rageFillStart;
		int filledRage = (int) Math.floor(ragePct * rageFillWidth);
		if (filledRage > 0) {
			event.getGuiGraphics().blit(
					RAGE_BAR_FILL,
					x_rage, y_rage,                  // 目的地起点保持与 base 一致（包含抖动偏移）
					0, 0,               // 源图裁剪起点
					rageFillStart + filledRage, rageBarH,           // 裁剪 & 绘制宽高
					rageBarW, rageBarH);            // 纹理整体宽高
		}

		// —— Rage Full Animation ——
		if (isPlayingRageAnimation && rageAnimationFrame < 10) {
			event.getGuiGraphics().blit(
					RAGE_FULL_ANIMATION[rageAnimationFrame],
					x_rage, y_rage,    // 使用相同的坐标（包含抖动偏移）
					0, 0, rageBarW, rageBarH, rageBarW, rageBarH);
		}

		// —— Adrenaline Bar ——
		int adrenBarW = 77, adrenBarH = 34;
		int x_adren = 42, y_adren = screenH - 26;
		// 始终先绘制 base
		event.getGuiGraphics().blit(
				ADRENALINE_BAR_BASE, x_adren, y_adren,
				0, 0, adrenBarW, adrenBarH, adrenBarW, adrenBarH);

		// fill 源图有效区间 [23,63)
		int adrenFillStart = 23, adrenFillEnd = 63;
		int adrenFillWidth = adrenFillEnd - adrenFillStart;
		int filledAdren = (int) Math.floor(adrenPct * adrenFillWidth);
		if (filledAdren > 0 && adrenPct < 1.0) {
			event.getGuiGraphics().blit(
					ADRENALINE_BAR_FILL,
					x_adren, y_adren,
					0, 0,
					adrenFillStart + filledAdren, adrenBarH,
					adrenBarW, adrenBarH);
		}

		// 100% 时额外叠加 full
		if (adrenPct >= 1.0) {
			event.getGuiGraphics().blit(
					ADRENALINE_BAR_FULL, x_adren, y_adren,
					0, 0, adrenBarW, adrenBarH, adrenBarW, adrenBarH);
		}

		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	}

	private static double clamp(double v, double min, double max) {
		return v < min ? min : (v > max ? max : v);
	}
}
