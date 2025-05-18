package net.gib.events;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.gib.storage.cloud.mySql.CloudStorage;
import net.gib.storage.cloud.mySql.DBManager;
import net.gib.util.RConsumer;

public class BiManagerEvent<E extends CloudStorage,T extends CloudStorage> extends Event<T> {

    private final DBManager<E> manager;

    public BiManagerEvent(String name, RConsumer<MessageEmbed, ReplyCallbackAction> consumer, DBManager<T> storage, DBManager<E> manager) {
        super(name, consumer, storage);
        this.manager = manager;
    }

    public DBManager<E> getSecondManager() {
        return manager;
    }
}
