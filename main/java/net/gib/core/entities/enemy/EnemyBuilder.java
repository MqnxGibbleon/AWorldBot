package net.gib.core.entities.enemy;

import net.gib.core.loot.LootBundle;
import net.gib.util.TimeSpan;

public class EnemyBuilder {

    private final String id;
    private LootBundle bundle;
    private int levelRequirement;
    private int killTime;

    public EnemyBuilder(String id) {
        this.id = id;
        this.killTime = TimeSpan.secs(1);
    }


    public EnemyBuilder setBundle(LootBundle bundle) {
        this.bundle = bundle;
        return this;
    }


    public EnemyBuilder setLevelRequirement(int levelRequirement) {
        this.levelRequirement = levelRequirement;
        return this;
    }

    public EnemyBuilder setKillTime(int killTime) {
        this.killTime = killTime;
        return this;
    }


    public Enemy build(String name) {
        return new Enemy(levelRequirement,id, name, killTime, id) {
            @Override
            public LootBundle getLoot() {
                return bundle;
            }
        };
    }
}
