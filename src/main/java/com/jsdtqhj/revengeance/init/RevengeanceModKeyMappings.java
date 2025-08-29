/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.jsdtqhj.revengeance.init;

import com.jsdtqhj.revengeance.RevengeanceMod;
import com.jsdtqhj.revengeance.network.AdrenalineButtonMessage;
import com.jsdtqhj.revengeance.network.RageButtonMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class RevengeanceModKeyMappings {
	public static final KeyMapping RAGE_BUTTON = new KeyMapping("key.revengeance.rage_button", GLFW.GLFW_KEY_V, "key.categories.gameplay");
	public static final KeyMapping ADRENALINE_BUTTON = new KeyMapping("key.revengeance.adrenaline_button", GLFW.GLFW_KEY_B, "key.categories.gameplay");

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(RAGE_BUTTON);
		event.register(ADRENALINE_BUTTON);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		private static boolean rageKeyWasPressed = false;
		private static boolean adrenalineKeyWasPressed = false;

		@SubscribeEvent
		public static void onKeyInput(InputEvent.Key event) {
			if (Minecraft.getInstance().screen == null) {
				// 处理怒气按键
				boolean rageKeyIsPressed = RAGE_BUTTON.isDown();
				if (rageKeyIsPressed && !rageKeyWasPressed) {
					RevengeanceMod.PACKET_HANDLER.sendToServer(new RageButtonMessage(0, 0));
					RageButtonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				}
				rageKeyWasPressed = rageKeyIsPressed;

				// 处理肾上腺素按键
				boolean adrenalineKeyIsPressed = ADRENALINE_BUTTON.isDown();
				if (adrenalineKeyIsPressed && !adrenalineKeyWasPressed) {
					RevengeanceMod.PACKET_HANDLER.sendToServer(new AdrenalineButtonMessage(0, 0));
					AdrenalineButtonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
				}
				adrenalineKeyWasPressed = adrenalineKeyIsPressed;
			}
		}
	}
}