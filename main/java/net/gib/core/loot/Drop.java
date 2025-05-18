package net.gib.core.loot;

import java.util.function.Consumer;

public class Drop {

    private final Consumer<Integer> consumer;
    private int value;

    public Drop(Consumer<Integer> consumer, int value) {
        this.consumer = consumer;
        this.value = value;
    }


    public Consumer<Integer> getConsumer() {
        return consumer;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void drop() {
        consumer.accept(value);
    }

}
