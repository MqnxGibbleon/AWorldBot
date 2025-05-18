package net.gib.core.entities.enemy;

import net.gib.core.loot.ILootDroppable;
import net.gib.util.ResourceDirectory;

public abstract class Enemy implements ILootDroppable {
    private final int levelRequirement;
    private final String id;
    private final String name;
    private final long killTime;
    private final String picUrl;

    public Enemy(int levelRequirement, String id, String name, long killTime, String picUrl) {
        this.levelRequirement = levelRequirement;
        this.id = id;
        this.name = name;
        this.killTime = killTime;
        this.picUrl = picUrl;
    }

    public int getLevelRequirement() {
        return levelRequirement;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getKillTime() {
        return killTime;
    }

    public String getPicUrl() {
        return ResourceDirectory.getPic("pictures/enemy",picUrl).getPath();
    }
}
