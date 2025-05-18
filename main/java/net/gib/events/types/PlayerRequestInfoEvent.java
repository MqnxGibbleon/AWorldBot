package net.gib.events.types;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.gib.core.entities.player.Player;
import net.gib.events.Event;
import net.gib.storage.cloud.mySql.DBManager;
import net.gib.util.RConsumer;

public class PlayerRequestInfoEvent extends Event<Player> {
    private final User user;

    public PlayerRequestInfoEvent(RConsumer<MessageEmbed, ReplyCallbackAction> consumer, DBManager<Player> storage, User user) {
        super("playerRequestEvent", consumer, storage);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
