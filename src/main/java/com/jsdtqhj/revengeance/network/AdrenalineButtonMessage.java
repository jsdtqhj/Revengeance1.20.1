package com.jsdtqhj.revengeance.network;

import com.jsdtqhj.revengeance.RevengeanceMod;
import com.jsdtqhj.revengeance.procedures.AdrenalineButtonClickProcedure;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkDirection;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class AdrenalineButtonMessage {

    private final int eventType;
    private final int pressedms;

    public AdrenalineButtonMessage(int eventType, int pressedms) {
        this.eventType = eventType;
        this.pressedms = pressedms;
    }

    public AdrenalineButtonMessage(FriendlyByteBuf buffer) {
        this.eventType = buffer.readInt();
        this.pressedms = buffer.readInt();
    }

    public static void buffer(AdrenalineButtonMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.eventType);
        buffer.writeInt(message.pressedms);
    }

    public static void handler(AdrenalineButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                pressAction(player, message.eventType, message.pressedms);
            }
        });
        context.setPacketHandled(true);
    }

    public static void pressAction(Player entity, int type, int pressedms) {
        Level world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        // 安全措施，防止任意区块生成
        if (!world.hasChunkAt(entity.blockPosition()))
            return;
        if (type == 0) {
            AdrenalineButtonClickProcedure.execute(entity);
        }
    }

    @SubscribeEvent
    public static void registerMessage(FMLCommonSetupEvent event) {
        RevengeanceMod.addNetworkMessage(AdrenalineButtonMessage.class, AdrenalineButtonMessage::buffer, AdrenalineButtonMessage::new, AdrenalineButtonMessage::handler);
    }
}