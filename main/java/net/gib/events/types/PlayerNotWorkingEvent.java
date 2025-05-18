package net.gib.events.types;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.gib.core.entities.player.Player;
import net.gib.events.Event;
import net.gib.util.RConsumer;

public class PlayerNotWorkingEvent extends Event<Player> {
    public PlayerNotWorkingEvent(RConsumer<MessageEmbed, ReplyCallbackAction> consumer) {
        super("playerNotWorkingEvent", consumer,null);
    }
}
