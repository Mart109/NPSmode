package com.example.npcmod.screen;

import com.example.npcmod.NpcMod;
import com.example.npcmod.quest.Quest;
import com.example.npcmod.quest.QuestManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuestScreen extends Screen {
    private static final Identifier TEXTURE = new Identifier(NpcMod.MOD_ID, "textures/gui/quest_bg.png");
    private static final int WIDTH = 300;
    private static final int HEIGHT = 200;

    private int selectedQuest = -1;
    private List<Quest> questList = new ArrayList<>();

    public QuestScreen() {
        super(Text.literal("ЖУРНАЛ КВЕСТОВ"));
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - WIDTH) / 2;
        int y = (height - HEIGHT) / 2;

        var player = MinecraftClient.getInstance().player;
        if (player == null) return;

        Collection<Quest> activeQuests = QuestManager.getActiveQuests(player);
        questList = new ArrayList<>(activeQuests);

        for (int i = 0; i < questList.size(); i++) {
            final int idx = i;
            Quest quest = questList.get(i);

            String displayName = quest.getName();
            if (displayName.length() > 15) {
                displayName = displayName.substring(0, 13) + "..";
            }

            this.addDrawableChild(ButtonWidget.builder(
                    Text.literal("§7" + displayName),
                    btn -> selectedQuest = idx
            ).dimensions(x + 10, y + 35 + i * 25, 120, 20).build());
        }

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("§7✖"),
                btn -> close()
        ).dimensions(x + WIDTH - 25, y + 5, 20, 15).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        int x = (width - WIDTH) / 2;
        int y = (height - HEIGHT) / 2;

        context.drawTexture(TEXTURE, x, y, 0, 0, WIDTH, HEIGHT);
        context.drawText(textRenderer, title, x + 10, y + 8, 0xB42828, false);
        context.drawHorizontalLine(x + 10, x + WIDTH - 10, y + 25, 0x3C283C);
        context.drawText(textRenderer, Text.literal("§7◆ Активные квесты:"), x + 10, y + 24, 0xB4AABE, false);

        if (selectedQuest >= 0 && selectedQuest < questList.size()) {
            Quest quest = questList.get(selectedQuest);

            context.drawText(textRenderer, Text.literal("§6§l" + quest.getName()), x + 140, y + 35, 0xFFD700, false);

            List<net.minecraft.text.OrderedText> lines = textRenderer.wrapLines(
                    Text.literal("§7" + quest.getDescription()), 140);
            int lineY = y + 55;
            for (net.minecraft.text.OrderedText line : lines) {
                context.drawText(textRenderer, line, x + 140, lineY, 0xB4AABE, false);
                lineY += 12;
            }

            context.drawText(textRenderer, Text.literal("§eПрогресс: " +
                            quest.getCurrentAmount() + "/" + quest.getTargetAmount()),
                    x + 140, lineY + 10, 0xA07828, false);

            if (quest.isCompleted()) {
                context.drawText(textRenderer, Text.literal("§a✅ ГОТОВО К СДАЧЕ"),
                        x + 140, lineY + 25, 0x55AA55, false);
            }

            context.drawText(textRenderer, Text.literal("§8От: " + quest.getGiverNpc()),
                    x + 140, y + HEIGHT - 20, 0x646464, false);
        } else {
            context.drawText(textRenderer, Text.literal("§8Выберите квест"),
                    x + 150, y + 80, 0x646464, false);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}