package net.gib.core.loot;

import java.util.Locale;
import java.util.Random;

public record LootRatio(int max, int min) {

    public int spin() {
        var min = this.min + 1;
        var max = this.max + 1;
        if (max < min) {
            return new Random().nextInt(max,min);
        }

        return new Random().nextInt(min,max);
    }

}
