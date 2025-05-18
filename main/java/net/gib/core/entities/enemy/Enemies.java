package net.gib.core.entities.enemy;

import net.gib.core.loot.LootRatio;
import net.gib.core.entities.player.Player;
import net.gib.core.loot.Drop;
import net.gib.core.loot.LootBundle;
import net.gib.core.loot.LootBundleBuilder;
import net.gib.util.TimeSpan;

import java.util.HashMap;

public class Enemies {

    private final Player killer;
    private final HashMap<String, Enemy> ENEMIES = new HashMap<>();
    public Enemies(Player killer) {
        this.killer = killer;
        register();
    }

    public Player getKiller() {
        return killer;
    }

    public Enemy of(String id) {
        return ENEMIES.get(id);
    }


    private LootBundle playerLoot(LootRatio exp, LootRatio silver, LootRatio gold) {
        return LootBundleBuilder.create()
                .addDrop(()->new Drop(killer::addExp,exp.spin()))
                .addDrop(()->new Drop(killer::addSilver,silver.spin()))
                .addDrop(()->new Drop(killer::addGold,gold.spin()))
                .build();
    }

    public void register() {
        ENEMIES.put("zombie",new EnemyBuilder("zombie")
                .setBundle(playerLoot(new LootRatio(500,300),new LootRatio(3,1),new LootRatio(1,0)))
                .setLevelRequirement(0)
                .setKillTime(TimeSpan.secs(30))
                .build("Zombie")
        );
        ENEMIES.put("witcher",new EnemyBuilder("witcher")
                .setBundle(playerLoot(new LootRatio(2400,1800),new LootRatio(5,2),new LootRatio(3,1)))
                .setLevelRequirement(3)
                .setKillTime(TimeSpan.minutes(2))
                .build("Witcher")
        );
        ENEMIES.put("werwolf",new EnemyBuilder("werwolf")
                .setBundle(playerLoot(new LootRatio(8000,7000),new LootRatio(15,9),new LootRatio(6,3)))
                .setLevelRequirement(7)
                .setKillTime(TimeSpan.minutes(6))
                .build("Werwolf")
        );
        ENEMIES.put("amethyst_troll",new EnemyBuilder("amethyst_troll")
                .setBundle(playerLoot(new LootRatio(30000,20000),new LootRatio(80,50),new LootRatio(25,18)))
                .setLevelRequirement(20)
                .setKillTime(TimeSpan.minutes(20))
                .build("Amethyst Troll")
        );
        ENEMIES.put("dark_fairy",new EnemyBuilder("dark_fairy")
                .setBundle(playerLoot(new LootRatio(120000,105000),new LootRatio(800,600),new LootRatio(36,34)))
                .setLevelRequirement(40)
                .setKillTime(TimeSpan.minutes(60))
                .build("Dark Fairy")
        );
        ENEMIES.put("dragon",new EnemyBuilder("dragon")
                .setBundle(playerLoot(new LootRatio(450000,320000),new LootRatio(300,200),new LootRatio(200,180)))
                .setLevelRequirement(75)
                .setKillTime(TimeSpan.minutes(180))
                .build("Dragon")
        );
    }

}
