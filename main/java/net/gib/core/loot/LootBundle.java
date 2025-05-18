package net.gib.core.loot;

public record LootBundle(Drop[] drops) {

    public void gather() {
        for (Drop drop : drops) {
            drop.drop();
        }
    }
}
