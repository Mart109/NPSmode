package com.example.npcmod.npcdata;

import com.example.npcmod.NpcMod;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NpcData {

    public enum NpcType {
        MERCHANT("§6 Торговец"),
        TRANSPORTER("§b Проводник"),
        DIALOGUE("§a Мудрец"),
        GUARD("§7🛡 Стражник"),
        BANDIT("§4🗡 Разбойник"),
        CITIZEN("§f Житель");

        public final String displayName;
        NpcType(String displayName) { this.displayName = displayName; }
    }

    public static class Info {
        public final String name;
        public final NpcType type;
        public final Identifier texture;
        public final String faction;
        public final String location;

        public Info(String name, NpcType type, String texturePath, String faction, String location) {
            this.name = name;
            this.type = type;
            this.texture = new Identifier(NpcMod.MOD_ID, "textures/entity/" + texturePath);
            this.faction = faction;
            this.location = location;
        }
    }

    private static final Map<UUID, Info> NPC_INFO = new HashMap<>();

    public static final String[] NPC_NAMES = {
            "§aСерафима", "§aРейн", "§aКракен", "§aЛис", "§aБреск", "§aСтрелка", "§aМолчальник",
            "§6Бернардо", "§6Капитан Морган",
            "§bСтарый Эдмон", "§bЛука",
            "§4Морте", "§4Вульфгар", "§4Главарь Чёрных Крыс",
            "§eГуннар", "§eОлдрич", "§eЭхо", "§eФеликс",
            "§cЛюциус Вертиго",
            "§fРыбак", "§fГрузчик", "§fТорговка рыбой", "§fСтаруха", "§fРебёнок",
            "§fПьяный", "§fПутешественник", "§fБард", "§fПарень", "§fДевушка",
            "§fПаломник 1", "§fПаломник 2", "§fМонах", "§fСтарик",
            "§fБогатый торговец", "§fДевушка с собакой", "§fКлерк", "§fСтарик на лавочке",
            "§fКузнец", "§fЖена кузнеца", "§fСын кузнеца", "§fУченик кузнеца",
            "§fБомж", "§fНищий ребёнок", "§fСтаруха с грибами", "§fТорговец зельями",
            "§7Стражник порта 1", "§7Стражник порта 2", "§7Стражник таверны",
            "§6Стражник склада", "§6Стражник квартала 1", "§6Стражник квартала 2",
            "§6Стражник особняка", "§6Стражник банка",
            "§8Стражник маяка 1", "§8Стражник маяка 2", "§8Стражник наверху",
            "§cСтражник ворот 1", "§cСтражник ворот 2", "§cСтражник стены", "§cКапитан стражи",
            "§eЛучник", "§eСнайпер"
    };

    public static final NpcType[] NPC_TYPES = {
            NpcType.DIALOGUE, NpcType.DIALOGUE, NpcType.DIALOGUE, NpcType.DIALOGUE,
            NpcType.DIALOGUE, NpcType.DIALOGUE, NpcType.DIALOGUE,
            NpcType.MERCHANT, NpcType.MERCHANT,
            NpcType.TRANSPORTER, NpcType.TRANSPORTER,
            NpcType.BANDIT, NpcType.BANDIT, NpcType.BANDIT,
            NpcType.DIALOGUE, NpcType.DIALOGUE, NpcType.DIALOGUE, NpcType.DIALOGUE,
            NpcType.DIALOGUE,
            NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,
            NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,
            NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,
            NpcType.MERCHANT, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,
            NpcType.MERCHANT, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN,
            NpcType.CITIZEN, NpcType.CITIZEN, NpcType.CITIZEN, NpcType.MERCHANT,
            NpcType.GUARD, NpcType.GUARD, NpcType.GUARD,
            NpcType.GUARD, NpcType.GUARD, NpcType.GUARD, NpcType.GUARD, NpcType.GUARD,
            NpcType.GUARD, NpcType.GUARD, NpcType.GUARD,
            NpcType.GUARD, NpcType.GUARD, NpcType.GUARD, NpcType.GUARD,
            NpcType.GUARD, NpcType.GUARD
    };

    public static final String[] TEXTURE_PATHS = {
            "serafima.png", "rein.png", "kraken.png", "lis.png", "bresk.png", "strelka.png", "molchalnik.png",
            "bernardo.png", "morgan.png",
            "edmon.png", "luka.png",
            "morte.png", "vulfgar.png", "rat_boss.png",
            "gunnar.png", "oldrich.png", "echo.png", "felix.png",
            "lucius.png",
            "fisherman.png", "loader.png", "fishseller.png", "oldwoman.png", "kid.png",
            "drunkard.png", "traveler.png", "bard.png", "couple_man.png", "couple_woman.png",
            "pilgrim1.png", "pilgrim2.png", "monk.png", "oldman.png",
            "richtrader.png", "richgirl.png", "clerk.png", "benchman.png",
            "smith_father.png", "smith_mother.png", "smith_son.png", "smith_apprentice.png",
            "bum.png", "beggar_kid.png", "mushroom_woman.png", "darktrader.png",
            "guard_neutral1.png", "guard_neutral2.png", "guard_neutral3.png",
            "guard_golden1.png", "guard_golden2.png", "guard_golden3.png", "guard_golden4.png", "guard_golden5.png",
            "guard_dark1.png", "guard_dark2.png", "guard_dark3.png",
            "guard_iron1.png", "guard_iron2.png", "guard_iron3.png", "guard_iron_captain.png",
            "guard_leather.png", "guard_sniper.png"
    };

    public static final String[] FACTIONS = {
            "neutral", "neutral", "black_claw", "neutral", "fierce_horn", "eagle_eye", "midnight_blade",
            "neutral", "neutral",
            "neutral", "neutral",
            "bandits", "bandits", "bandits",
            "fierce_horn", "neutral", "midnight_blade", "golden_shell",
            "golden_shell",
            "neutral", "neutral", "neutral", "neutral", "neutral",
            "neutral", "neutral", "neutral", "neutral", "neutral",
            "neutral", "neutral", "neutral", "neutral",
            "golden_shell", "golden_shell", "golden_shell", "golden_shell",
            "fierce_horn", "fierce_horn", "fierce_horn", "fierce_horn",
            "bandits", "bandits", "bandits", "bandits",
            "neutral", "neutral", "neutral",
            "golden_shell", "golden_shell", "golden_shell", "golden_shell", "golden_shell",
            "midnight_blade", "midnight_blade", "midnight_blade",
            "fierce_horn", "fierce_horn", "fierce_horn", "fierce_horn",
            "eagle_eye", "eagle_eye"
    };

    public static final String[] LOCATIONS = {
            "Храм", "Таверна", "Порт", "Подвал таверны", "Цитадель", "Башни", "Маяк",
            "Золотой квартал", "Порт",
            "Порт/Цитадель/Пик", "Престол/Базар",
            "Трущобы", "Трущобы", "Порт",
            "Цитадель", "Порт", "Храм/Маяк", "Золотой квартал",
            "Золотой квартал",
            "Порт", "Порт", "Порт", "Порт", "Порт",
            "Таверна", "Таверна", "Таверна", "Таверна", "Таверна",
            "Храм", "Храм", "Храм", "Храм",
            "Золотой квартал", "Золотой квартал", "Золотой квартал", "Золотой квартал",
            "Цитадель", "Цитадель", "Цитадель", "Цитадель",
            "Трущобы", "Трущобы", "Трущобы", "Трущобы",
            "Порт", "Порт", "Таверна",
            "Порт", "Золотой квартал", "Золотой квартал", "Золотой квартал", "Золотой квартал",
            "Маяк", "Маяк", "Маяк",
            "Цитадель", "Цитадель", "Цитадель", "Цитадель",
            "Башни", "Башни"
    };

    public static Info getInfo(UUID uuid) {
        return NPC_INFO.get(uuid);
    }

    public static void registerNpc(UUID uuid, int index) {
        if (index >= 0 && index < 62) {
            Info info = new Info(
                    NPC_NAMES[index],
                    NPC_TYPES[index],
                    TEXTURE_PATHS[index],
                    FACTIONS[index],
                    LOCATIONS[index]
            );
            NPC_INFO.put(uuid, info);
        }
    }

    public static int getTotalNpcCount() {
        return 62;
    }
}