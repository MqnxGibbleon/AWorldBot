package net.gib.util;

public interface RConsumer<T,R> {

    R accept(T t);

}
