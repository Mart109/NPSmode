package com.example.npcmod.network;

import com.example.npcmod.NpcMod;
import com.example.npcmod.quest.QuestManager;
import com.example.npcmod.reputation.ReputationManager;
import com.example.npcmod.screen.MerchantScreenHandler;
import com.example.npcmod.screen.TransporterScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

public class ModPackets {
    public static final Identifier DIALOGUE_CHOICE = NpcMod.id("dialogue_choice");
    public static final Identifier TELEPORT_TO = NpcMod.id("teleport_to");
    public static final Identifier MERCHANT_BUY = NpcMod.id("merchant_buy");
    public static final Identifier QUEST_ACTION = NpcMod.id("quest_action");

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(DIALOGUE_CHOICE, (server, player, handler, buf, responseSender) -> {
            int delta = buf.readInt();
            String faction = buf.readString();
            server.execute(() -> ReputationManager.changeReputation(server, player.getUuid(), faction, delta));
        });

        ServerPlayNetworking.registerGlobalReceiver(TELEPORT_TO, (server, player, handler, buf, responseSender) -> {
            int destIndex = buf.readInt();
            server.execute(() -> {
                ScreenHandler sh = player.currentScreenHandler;
                if (sh instanceof TransporterScreenHandler transporter) transporter.teleport(player, destIndex);
                player.closeHandledScreen();
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(MERCHANT_BUY, (server, player, handler, buf, responseSender) -> {
            int offerIndex = buf.readInt();
            server.execute(() -> {
                ScreenHandler sh = player.currentScreenHandler;
                if (sh instanceof MerchantScreenHandler merchant) merchant.buyItem(player, offerIndex);
            });
        });

        ServerPlayNetworking.registerGlobalReceiver(QUEST_ACTION, (server, player, handler, buf, responseSender) -> {
            String action = buf.readString();
            String questId = buf.readString();
            server.execute(() -> {
                if (action.equals("start")) QuestManager.startQuest(player, questId);
                else if (action.equals("complete")) QuestManager.completeQuest(player, questId);
            });
        });
    }

    public static void registerClient() {}
}