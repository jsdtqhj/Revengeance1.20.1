
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package com.everla.revengeance.init;

import org.lwjgl.glfw.GLFW;

import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import com.everla.revengeance.network.RageButtonMessage;
import com.everla.revengeance.network.AdrenalineButtonMessage;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class RevengeanceModKeyMappings {
	public static final KeyMapping RAGE_BUTTON = new KeyMapping("key.revengeance.rage_button", GLFW.GLFW_KEY_V, "key.categories.gameplay") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new RageButtonMessage(0, 0));
				RageButtonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping ADRENALINE_BUTTON = new KeyMapping("key.revengeance.adrenaline_button", GLFW.GLFW_KEY_B, "key.categories.gameplay") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				PacketDistributor.sendToServer(new AdrenalineButtonMessage(0, 0));
				AdrenalineButtonMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(RAGE_BUTTON);
		event.register(ADRENALINE_BUTTON);
	}

	@EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(ClientTickEvent.Post event) {
			if (Minecraft.getInstance().screen == null) {
				RAGE_BUTTON.consumeClick();
				ADRENALINE_BUTTON.consumeClick();
			}
		}
	}
}
