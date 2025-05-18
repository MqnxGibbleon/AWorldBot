package net.gib.events.types;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.gib.core.entities.player.Player;
import net.gib.core.entities.player.PlayerMissions;
import net.gib.events.BiManagerEvent;
import net.gib.storage.cloud.mySql.DBManager;
import net.gib.util.RConsumer;

public class PlayerCollectLootEvent extends BiManagerEvent<PlayerMissions,Player> {
    private final TextChannel channel;

    public PlayerCollectLootEvent(RConsumer<MessageEmbed, ReplyCallbackAction> consumer, DBManager<Player> storage, DBManager<PlayerMissions> manager, TextChannel channel) {
        super("playerCollectLoot", consumer, storage, manager);
        this.channel = channel;
    }



    public TextChannel getChannel() {
        return channel;
    }
}
