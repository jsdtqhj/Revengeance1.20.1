package com.jsdtqhj.revengeance.procedures;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class AdrenalineButtonClickProcedure {
	public static void execute(Entity entity) {
		if (entity == null)
			return;
		
		// 调用新的肾上腺素系统处理程序
		if (entity instanceof Player player) {
			AdrenalineSystemHandler.activateAdrenaline(player);
		}
	}
}