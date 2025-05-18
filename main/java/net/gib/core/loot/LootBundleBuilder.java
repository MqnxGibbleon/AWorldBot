package net.gib.core.loot;

import java.util.ArrayList;

public class LootBundleBuilder<E> {

    private final ArrayList<Drop> DROPS;


    private LootBundleBuilder(ArrayList<Drop> drops) {
        DROPS = drops;
    }

    public ArrayList<Drop> getDrops() {
        return DROPS;
    }


    public static <E> LootBundleBuilder<E> create() {
        return new LootBundleBuilder<>(new ArrayList<>());
    }

    public  LootBundleBuilder<E> addDrop(RConsumer<Drop> consumer) {
        DROPS.add(consumer.accept());
        return this;
    }

    public LootBundle build() {
        return new LootBundle(DROPS.toArray(new Drop[0]));
    }

    public interface RConsumer<R>{
        R accept();
    }
}
