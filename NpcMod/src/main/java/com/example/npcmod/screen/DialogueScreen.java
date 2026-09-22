package com.example.npcmod.screen;

import com.example.npcmod.NpcMod;
import com.example.npcmod.network.ModPackets;
import com.example.npcmod.quest.QuestManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class DialogueScreen extends HandledScreen<DialogueScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(NpcMod.MOD_ID, "textures/gui/dialogue_bg.png");

    private String[] dialogueOptions = {"", "", ""};
    private String npcDialogueText = "";
    private String npcName = "";

    public DialogueScreen(DialogueScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 400;
        this.backgroundHeight = 180;
        this.playerInventoryTitleY = 1000;
        setupDialogue();
    }

    private void setupDialogue() {
        var npc = handler.getNpc();
        if (npc == null || npc.getNpcInfo() == null) {
            npcDialogueText = "Привет, путник.";
            npcName = "NPC";
            dialogueOptions = new String[]{"Поговорить", "Уйти", ""};
            return;
        }

        npcName = npc.getNpcInfo().name;
        var player = MinecraftClient.getInstance().player;

        // СЕРАФИМА
        if (npcName.contains("Серафима")) {
            if (!QuestManager.hasQuest(player, "healing") && !QuestManager.isQuestCompleted(player, "healing")) {
                npcDialogueText = "Ты очнулся... Принеси мне 5 целебных трав, они растут у реки за храмом.";
                dialogueOptions = new String[]{"§aХорошо, я принесу", "§7Позже", ""};
            } else if (QuestManager.hasQuest(player, "healing") && QuestManager.getPlayerQuest(player, "healing").isCompleted()) {
                npcDialogueText = "Ты принёс травы! Молодец.";
                dialogueOptions = new String[]{"§aСдать квест", "§7Позже", ""};
            } else {
                npcDialogueText = "Да благословит тебя Чёрный Коготь, дитя.";
                dialogueOptions = new String[]{"§aСпасибо", "§7Уйти", ""};
            }
        }
        // РЕЙН
        else if (npcName.contains("Рейн")) {
            if (!QuestManager.hasQuest(player, "rein_debt") && !QuestManager.isQuestCompleted(player, "rein_debt")) {
                npcDialogueText = "Ищешь информацию? Сначала верни долг Олдрича. 5 монет.";
                dialogueOptions = new String[]{"§aЯ верну долг", "§eКто такой Олдрич?", "§7Уйти"};
            } else if (QuestManager.hasQuest(player, "rein_debt") && QuestManager.getPlayerQuest(player, "rein_debt").isCompleted()) {
                npcDialogueText = "Долг вернул? Молодец. Лис прячется в подвале таверны.";
                dialogueOptions = new String[]{"§aСдать квест", "§7Позже", ""};
            } else {
                npcDialogueText = "Чего желаешь? Выпить или поговорить?";
                dialogueOptions = new String[]{"§6Купить эль (2💰)", "§eЧто слышно?", "§7Ничего"};
            }
        }
        // КРАКЕН
        else if (npcName.contains("Кракен")) {
            if (!QuestManager.hasQuest(player, "kraken_box") && !QuestManager.isQuestCompleted(player, "kraken_box")) {
                npcDialogueText = "Аррр... В храме спрятан ящик с оружием. Принеси — расскажу про тайный ход.";
                dialogueOptions = new String[]{"§aЯ принесу", "§eЧто в ящике?", "§7Уйти"};
            } else {
                npcDialogueText = "Чёрный Коготь пал, но мы ещё живы.";
                dialogueOptions = new String[]{"§5Расскажи об отце", "§eКак возродить Коготь?", "§7Прощай"};
            }
        }
        // ЛИС
        else if (npcName.contains("Лис")) {
            if (!QuestManager.hasQuest(player, "steal_debts") && !QuestManager.isQuestCompleted(player, "steal_debts")) {
                npcDialogueText = "Тсс... Нужно украсть расписки из банка Панциря. Поможешь — получишь клинок.";
                dialogueOptions = new String[]{"§aЯ помогу", "§eЧто за клинок?", "§7Уйти"};
            } else {
                npcDialogueText = "Есть работа? Или мимо проходишь?";
                dialogueOptions = new String[]{"§6Работа", "§eИнформация", "§7Уйти"};
            }
        }
        // БРЕСК
        else if (npcName.contains("Бреск")) {
            if (!QuestManager.hasQuest(player, "smith_debt") && !QuestManager.isQuestCompleted(player, "smith_debt")) {
                npcDialogueText = "Союз? Докажи делом. Выбей долг с кузнеца Торгрима.";
                dialogueOptions = new String[]{"§aЯ выбью долг", "§eСколько он должен?", "§7Уйти"};
            } else {
                npcDialogueText = "Ярый Рог помнит союз с твоим отцом.";
                dialogueOptions = new String[]{"§cДа, помоги мне", "§eЧто можешь предложить?", "§7Подумаю"};
            }
        }
        // СТРЕЛКА
        else if (npcName.contains("Стрелка")) {
            if (!QuestManager.hasQuest(player, "fallen_crossbow") && !QuestManager.isQuestCompleted(player, "fallen_crossbow")) {
                npcDialogueText = "Мой арбалет упал с башни в порт. Найди его.";
                dialogueOptions = new String[]{"§aЯ найду", "§eГде искать?", "§7Уйти"};
            } else {
                npcDialogueText = "Орлиное Око видит всё. Что тебе нужно?";
                dialogueOptions = new String[]{"§bИнформация", "§eЗадание", "§7Уйти"};
            }
        }
        // ЭХО
        else if (npcName.contains("Эхо")) {
            if (!QuestManager.hasQuest(player, "lost_messenger") && !QuestManager.isQuestCompleted(player, "lost_messenger")) {
                npcDialogueText = "Моя подруга Лира пропала в трущобах. Помоги найти её.";
                dialogueOptions = new String[]{"§aЯ помогу", "§eГде искать?", "§7Уйти"};
            } else {
                npcDialogueText = "Молчальник ждёт тебя на маяке.";
                dialogueOptions = new String[]{"§5Я готов", "§8Кто такой Молчальник?", "§7Уйти"};
            }
        }
        // МОЛЧАЛЬНИК
        else if (npcName.contains("Молчальник")) {
            npcDialogueText = "...";
            dialogueOptions = new String[]{"§8Ты Молчальник?", "§5Я ищу Клинок", "§7Уйти"};
        }
        // ЛЮЦИУС
        else if (npcName.contains("Люциус")) {
            npcDialogueText = "А, наследник Чёрного Когтя. Я ждал тебя.";
            dialogueOptions = new String[]{"§4Это ты заказал резню!", "§eЧего ты хочешь?", "§7Уйти"};
        }
        // МОРТЕ
        else if (npcName.contains("Морте")) {
            npcDialogueText = "Ты ещё жив? Молчальник велел не трогать тебя... пока.";
            dialogueOptions = new String[]{"§4Это ты сбросил меня с крыши!", "§8Чего хочет Молчальник?", "§7Уйти"};
        }
        // БЕРНАРДО
        else if (npcName.contains("Бернардо")) {
            if (!QuestManager.hasQuest(player, "fake_docs") && !QuestManager.isQuestCompleted(player, "fake_docs")) {
                npcDialogueText = "Пропуск в особняк Люциуса? 50 монет — и он твой.";
                dialogueOptions = new String[]{"§aЯ заплачу", "§eУкрасть бланк", "§7Уйти"};
            } else {
                npcDialogueText = "Документы, подписи, печати... Всё для клиента.";
                dialogueOptions = new String[]{"§6Купить", "§eИнформация", "§7Уйти"};
            }
        }
        // МОРГАН
        else if (npcName.contains("Морган")) {
            npcDialogueText = "Моя 'Морская дева' требует ремонта. 16 железных слитков — и я отвезу тебя на Плавучий рынок.";
            dialogueOptions = new String[]{"§aЯ принесу железо", "§eЧто за рынок?", "§7Уйти"};
        }
        // ЭДМОН
        else if (npcName.contains("Эдмон")) {
            npcDialogueText = "Куда идёшь, путник? Проведу куда скажешь.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        // ЛУКА
        else if (npcName.contains("Лука")) {
            npcDialogueText = "Я Лука. Вожу в цитадель и на башни. Дорога безопасная.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        // ВУЛЬФГАР
        else if (npcName.contains("Вульфгар")) {
            npcDialogueText = "Ха! Гость в моём логове! Денег хочешь или голову сложить?";
            dialogueOptions = new String[]{"§6Работа", "§eИнформация", "§4Убить тебя"};
        }
        // ГЛАВАРЬ КРЫС
        else if (npcName.contains("Крыс") || npcName.contains("Главарь")) {
            npcDialogueText = "Крысы любят сыр! Принеси 5 кусков — и копай сколько хочешь!";
            dialogueOptions = new String[]{"§aПринесу сыр", "§eГде взять?", "§7Уйти"};
        }
        // ГУННАР
        else if (npcName.contains("Гуннар")) {
            npcDialogueText = "Новенький? Тренироваться или по делу?";
            dialogueOptions = new String[]{"§aСпарринг", "§eИщу Бреска", "§7Уйти"};
        }
        // ОЛДРИЧ
        else if (npcName.contains("Олдрич")) {
            if (!QuestManager.hasQuest(player, "help_loader") && !QuestManager.isQuestCompleted(player, "help_loader")) {
                npcDialogueText = "Ох... Работа тяжёлая. Поможешь разгрузить ящики?";
                dialogueOptions = new String[]{"§aПомогу", "§eТы должен Рейну?", "§7Уйти"};
            } else {
                npcDialogueText = "Спасибо за помощь.";
                dialogueOptions = new String[]{"§aПоговорить", "§7Уйти", ""};
            }
        }
        // ФЕЛИКС
        else if (npcName.contains("Феликс")) {
            npcDialogueText = "Кто вы?! Я ничего не знаю! Если от Люциуса — я всё сделал!";
            dialogueOptions = new String[]{"§eЯ от Люциуса", "§6Выпьем?", "§4Отдай книги!"};
        }
        // ЖИТЕЛИ ПОРТА
        else if (npcName.contains("Рыбак")) {
            npcDialogueText = "Клюёт сегодня плохо. Проклятие, не иначе.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Грузчик")) {
            npcDialogueText = "Работаем, работаем... Спину бы не сорвать.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Торговка рыбой")) {
            npcDialogueText = "Рыба! Свежая рыба! Всего две монеты!";
            dialogueOptions = new String[]{"§6Купить (2💰)", "§7Уйти", ""};
        }
        else if (npcName.contains("Старуха")) {
            npcDialogueText = "Ох, молодёжь... В моё время Чёрный Коготь порядок держал.";
            dialogueOptions = new String[]{"§eРасскажи о Когте", "§7Уйти", ""};
        }
        else if (npcName.contains("Ребёнок")) {
            npcDialogueText = "Дяденька/тётенька, дай монетку! Секрет скажу!";
            dialogueOptions = new String[]{"§6Дать монету (1💰)", "§7Уйти", ""};
        }
        // ЖИТЕЛИ ТАВЕРНЫ
        else if (npcName.contains("Пьяный")) {
            npcDialogueText = "Ик... Ты это... уважаешь меня? Нет? А зря...";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Путешественник")) {
            npcDialogueText = "В этом городе странная атмосфера... Будто что-то давит.";
            dialogueOptions = new String[]{"§eЧто ты видел?", "§7Уйти", ""};
        }
        else if (npcName.contains("Бард")) {
            npcDialogueText = "Баллада о падении Чёрного Когтя. Послушать? Одна монета.";
            dialogueOptions = new String[]{"§6Послушать (1💰)", "§7Уйти", ""};
        }
        else if (npcName.contains("Парень") && npcName.contains("таверна")) {
            npcDialogueText = "Отец говорит, времена тяжёлые. А я что? Пиво есть — и ладно.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Девушка") && npcName.contains("таверна")) {
            npcDialogueText = "Не смотри на меня так. Я не должна здесь быть.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        // ЖИТЕЛИ ХРАМА
        else if (npcName.contains("Паломник")) {
            npcDialogueText = "Серафима — святая женщина. Она спасла моего сына.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Монах")) {
            npcDialogueText = "Серафима учит нас милосердию. Но много зла в этом городе...";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Старик") && npcName.contains("храм")) {
            npcDialogueText = "Я помню Альдо... Хороший был господин. Справедливый.";
            dialogueOptions = new String[]{"§eРасскажи об Альдо", "§7Уйти", ""};
        }
        // ЖИТЕЛИ ЗОЛОТОГО КВАРТАЛА
        else if (npcName.contains("Богатый торговец")) {
            npcDialogueText = "Прочь с дороги, оборванец! Здесь ходят приличные люди.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Девушка с собакой")) {
            npcDialogueText = "Какой милый пёсик... Ой, простите, я не с вами.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Клерк")) {
            npcDialogueText = "Извините, я очень спешу! Господин Вертиго ждёт отчёты.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("лавочке")) {
            npcDialogueText = "Раньше я тоже бегал в золотой броне. А теперь пенсия.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        // ЖИТЕЛИ ЦИТАДЕЛИ
        else if (npcName.contains("Кузнец") && !npcName.contains("Жена") && !npcName.contains("Сын") && !npcName.contains("Ученик")) {
            npcDialogueText = "Уголь кончился... Принесёшь 48 штук из шахты?";
            dialogueOptions = new String[]{"§aПринесу", "§7Позже", ""};
        }
        else if (npcName.contains("Жена кузнеца")) {
            npcDialogueText = "Мой Торгрим — хороший муж. Просто попал в беду с долгом.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Сын кузнеца")) {
            npcDialogueText = "Я буду как Бреск! Сильным и смелым!";
            dialogueOptions = new String[]{"§aПодбодрить", "§7Уйти", ""};
        }
        else if (npcName.contains("Ученик кузнеца")) {
            npcDialogueText = "Тяжело... Но я научусь. Может, выкую меч для Бреска.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        // ЖИТЕЛИ ТРУЩОБ
        else if (npcName.contains("Бомж")) {
            npcDialogueText = "Эй, есть монетка? Расскажу, где Вульфгар прячется.";
            dialogueOptions = new String[]{"§6Дать монету (1💰)", "§7Уйти", ""};
        }
        else if (npcName.contains("Нищий ребёнок")) {
            npcDialogueText = "Подайте, добрый господин... Хотя бы корку хлеба.";
            dialogueOptions = new String[]{"§6Дать хлеб", "§7Уйти", ""};
        }
        else if (npcName.contains("Старуха с грибами")) {
            npcDialogueText = "Грибочки, грибочки... Съешь — увидишь то, чего нет.";
            dialogueOptions = new String[]{"§2Купить грибы (5💰)", "§7Уйти", ""};
        }
        else if (npcName.contains("Торговец зельями")) {
            npcDialogueText = "Зелья! Настоящие! Ну, почти. Покупай!";
            dialogueOptions = new String[]{"§6Купить зелье (10💰)", "§7Уйти", ""};
        }
        // СТРАЖНИКИ
        else if (npcName.contains("Стражник порта")) {
            npcDialogueText = "Проходи, не задерживайся. Без глупостей.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Стражник таверны")) {
            npcDialogueText = "Без драк. Без оружия на виду. Понял?";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Стражник") && npcName.contains("склад")) {
            npcDialogueText = "Склад Панциря. Посторонним вход запрещён.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Стражник") && npcName.contains("квартала")) {
            npcDialogueText = "В Золотом квартале порядок. Нищих не пускаем.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Стражник") && npcName.contains("особняка")) {
            npcDialogueText = "Особняк Вертиго. Приказ стрелять без предупреждения.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Стражник") && npcName.contains("банка")) {
            npcDialogueText = "Хранилище закрыто. Даже не пытайся.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Стражник") && npcName.contains("маяка")) {
            npcDialogueText = "Маяк Клинка. К Молчальнику пускаем только проверенных.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Стражник") && npcName.contains("ворот")) {
            npcDialogueText = "Цитадель Ярого Рога. Назовись.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Стражник") && npcName.contains("стены")) {
            npcDialogueText = "Отсюда весь город видно. Скоро буря.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Капитан стражи")) {
            npcDialogueText = "Не отвлекай моих людей. Хочешь в Рог — иди к Гуннару.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Лучник")) {
            npcDialogueText = "Башня Орлиного Ока. К Стрелке? Она занята.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        else if (npcName.contains("Снайпер")) {
            npcDialogueText = "Тсс... Я на посту. Вижу всё. И тебя вижу.";
            dialogueOptions = new String[]{"§7Уйти", "", ""};
        }
        // ПО УМОЛЧАНИЮ
        else {
            npcDialogueText = "Привет, путник. Чего желаешь?";
            dialogueOptions = new String[]{"§aПоговорить", "§eЗадать вопрос", "§7Уйти"};
        }
    }

    @Override
    protected void init() {
        super.init();

        int x = (width - backgroundWidth) / 2;
        int y = height - backgroundHeight - 10;

        for (int i = 0; i < 3; i++) {
            if (dialogueOptions[i] != null && !dialogueOptions[i].isEmpty()) {
                final int index = i;
                this.addDrawableChild(ButtonWidget.builder(
                                Text.literal(dialogueOptions[i]),
                                btn -> handleDialogueChoice(index))
                        .dimensions(x + 20, y + backgroundHeight - 35 - (2 - i) * 25, 360, 20)
                        .build());
            }
        }
    }

    private void handleDialogueChoice(int index) {
        var player = MinecraftClient.getInstance().player;
        if (player == null) return;

        String name = npcName;

        if (name.contains("Серафима")) {
            if (!QuestManager.hasQuest(player, "healing") && index == 0) sendQuestAction("start", "healing");
            else if (QuestManager.hasQuest(player, "healing") && QuestManager.getPlayerQuest(player, "healing").isCompleted() && index == 0) sendQuestAction("complete", "healing");
        }
        else if (name.contains("Рейн")) {
            if (!QuestManager.hasQuest(player, "rein_debt") && index == 0) sendQuestAction("start", "rein_debt");
            else if (QuestManager.hasQuest(player, "rein_debt") && QuestManager.getPlayerQuest(player, "rein_debt").isCompleted() && index == 0) sendQuestAction("complete", "rein_debt");
        }
        else if (name.contains("Кракен")) {
            if (!QuestManager.hasQuest(player, "kraken_box") && index == 0) sendQuestAction("start", "kraken_box");
        }
        else if (name.contains("Лис")) {
            if (!QuestManager.hasQuest(player, "steal_debts") && index == 0) sendQuestAction("start", "steal_debts");
        }
        else if (name.contains("Бреск")) {
            if (!QuestManager.hasQuest(player, "smith_debt") && index == 0) sendQuestAction("start", "smith_debt");
        }
        else if (name.contains("Стрелка")) {
            if (!QuestManager.hasQuest(player, "fallen_crossbow") && index == 0) sendQuestAction("start", "fallen_crossbow");
        }
        else if (name.contains("Эхо")) {
            if (!QuestManager.hasQuest(player, "lost_messenger") && index == 0) sendQuestAction("start", "lost_messenger");
        }
        else if (name.contains("Бернардо")) {
            if (!QuestManager.hasQuest(player, "fake_docs") && index == 0) sendQuestAction("start", "fake_docs");
        }
        else if (name.contains("Олдрич")) {
            if (!QuestManager.hasQuest(player, "help_loader") && index == 0) sendQuestAction("start", "help_loader");
        }
        else if (name.contains("Кузнец") && !name.contains("Жена") && !name.contains("Сын") && !name.contains("Ученик")) {
            if (!QuestManager.hasQuest(player, "coal_for_smith") && index == 0) sendQuestAction("start", "coal_for_smith");
        }

        close();
    }

    private void sendQuestAction(String action, String questId) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeString(action);
        buf.writeString(questId);
        ClientPlayNetworking.send(ModPackets.QUEST_ACTION, buf);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        int x = (width - backgroundWidth) / 2;
        int y = height - backgroundHeight - 10;
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.x = (width - backgroundWidth) / 2;
        this.y = height - backgroundHeight - 10;

        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);

        // ИМЯ NPC — СЛЕВА
        context.drawText(textRenderer, Text.literal("§6" + npcName), x + 15, y + 15, 0xFFD700, false);

        // ТЕКСТ ДИАЛОГА — СЛЕВА
        List<net.minecraft.text.OrderedText> wrappedLines = textRenderer.wrapLines(Text.literal(npcDialogueText), 280);
        for (int i = 0; i < wrappedLines.size() && i < 3; i++) {
            context.drawText(textRenderer, wrappedLines.get(i), x + 15, y + 35 + i * 12, 0xFFFFFF, false);
        }

        // ПОРТРЕТ — СПРАВА (64x64, не растянутый)
        var info = handler.getNpc().getNpcInfo();
        if (info != null && info.texture != null) {
            context.drawTexture(info.texture, x + backgroundWidth - 74, y + 15, 0, 0, 64, 64, 64, 64);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}