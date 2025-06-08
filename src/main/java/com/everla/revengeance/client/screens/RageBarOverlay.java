
package com.everla.revengeance.client.screens;

import org.checkerframework.checker.units.qual.h;

import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Minecraft;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.platform.GlStateManager;

import com.everla.revengeance.procedures.RageDisplaylxProcedure;
import com.everla.revengeance.procedures.RageDisplayl9Procedure;
import com.everla.revengeance.procedures.RageDisplayl8Procedure;
import com.everla.revengeance.procedures.RageDisplayl7Procedure;
import com.everla.revengeance.procedures.RageDisplayl6Procedure;
import com.everla.revengeance.procedures.RageDisplayl5Procedure;
import com.everla.revengeance.procedures.RageDisplayl4Procedure;
import com.everla.revengeance.procedures.RageDisplayl3Procedure;
import com.everla.revengeance.procedures.RageDisplayl2Procedure;
import com.everla.revengeance.procedures.RageDisplayl1Procedure;
import com.everla.revengeance.procedures.RageDisplayProcedure;
import com.everla.revengeance.procedures.RageDisplayFullProcedure;
import com.everla.revengeance.procedures.AdrenalineDisplayProcedure;
import com.everla.revengeance.procedures.AdrenalineDisplayLxiiiProcedure;
import com.everla.revengeance.procedures.AdrenalineDisplayLxiiProcedure;
import com.everla.revengeance.procedures.AdrenalineDisplayLxiProcedure;
import com.everla.revengeance.procedures.AdrenalineDisplayLxProcedure;
import com.everla.revengeance.procedures.AdrenalineDisplayLvProcedure;
import com.everla.revengeance.procedures.AdrenalineDisplayLivProcedure;
import com.everla.revengeance.procedures.AdrenalineDisplayL9Procedure;
import com.everla.revengeance.procedures.AdrenalineDisplayL8Procedure;
import com.everla.revengeance.procedures.AdrenalineDisplayL7Procedure;
import com.everla.revengeance.procedures.AdrenalineDisplayL6Procedure;
import com.everla.revengeance.procedures.AdrenalineDisplayL5Procedure;
import com.everla.revengeance.procedures.AdrenalineDisplayL4Procedure;
import com.everla.revengeance.procedures.AdrenalineDisplayL3Procedure;
import com.everla.revengeance.procedures.AdrenalineDisplayL2Procedure;
import com.everla.revengeance.procedures.AdrenalineDisplayL1Procedure;
import com.everla.revengeance.procedures.AdrenalineDisplayFullProcedure;

@EventBusSubscriber({Dist.CLIENT})
public class RageBarOverlay {
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void eventHandler(RenderGuiEvent.Pre event) {
		int w = event.getGuiGraphics().guiWidth();
		int h = event.getGuiGraphics().guiHeight();
		Level world = null;
		double x = 0;
		double y = 0;
		double z = 0;
		Player entity = Minecraft.getInstance().player;
		if (entity != null) {
			world = entity.level();
			x = entity.getX();
			y = entity.getY();
			z = entity.getZ();
		}
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		RenderSystem.setShaderColor(1, 1, 1, 1);
		if (true) {
			if (RageDisplayProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplayl1Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_l1_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplayl2Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_l2_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplayl3Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_l3_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplayl4Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_l4_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplayl5Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_l5_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplayl6Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_l6_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplayl7Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_l7_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplayl8Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_l8_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplayl9Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_l9_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplaylxProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_lx_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (RageDisplayFullProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/rage_bar_full_overlay.png"), -6, h - 21, 0, 0, 76, 19, 76, 19);
			}
			if (AdrenalineDisplayProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayL1Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_l1_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayL2Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_l2_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayL3Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_l3_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayL4Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_l4_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayL5Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_l5_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayL6Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_l6_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayL7Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_l7_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayL8Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_l8_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayL9Procedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_l9_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayLxProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_lx_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayLxiProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_lxi_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayLxiiProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_lxii_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayLxiiiProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_lxiii_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayLivProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_lxiv_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayLvProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_lxv_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
			if (AdrenalineDisplayFullProcedure.execute(entity)) {
				event.getGuiGraphics().blit(ResourceLocation.parse("revengeance:textures/screens/adrenaline_bar_full_overlay.png"), 44, h - 27, 0, 0, 77, 34, 77, 34);
			}
		}
		RenderSystem.depthMask(true);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableDepthTest();
		RenderSystem.disableBlend();
		RenderSystem.setShaderColor(1, 1, 1, 1);
	}
}
