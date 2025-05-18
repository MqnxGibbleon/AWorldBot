package net.gib.events;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.gib.storage.cloud.mySql.CloudStorage;
import net.gib.storage.cloud.mySql.DBManager;
import net.gib.util.RConsumer;

public class Event<E extends CloudStorage> {

    private final String name;
    private final RConsumer<MessageEmbed, ReplyCallbackAction> consumer;
    private final DBManager<E> manager;

    public Event(String name, RConsumer<MessageEmbed, ReplyCallbackAction> consumer, DBManager<E> manager) {
        this.name = name;
        this.consumer = consumer;
        this.manager = manager;
    }

    public String getName() {
        return name;
    }

    public ReplyCallbackAction reply(MessageEmbed embed) {
       return consumer.accept(embed);
    }

    public DBManager<E> getManager() {
        return manager;
    }


}
