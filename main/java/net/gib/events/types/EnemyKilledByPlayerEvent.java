package net.gib.events.types;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import net.gib.core.entities.player.Player;
import net.gib.events.Event;
import net.gib.storage.cloud.mySql.DBManager;
import net.gib.util.RConsumer;

public class EnemyKilledByPlayerEvent extends Event<Player> {

    private final String enemyId;
    public EnemyKilledByPlayerEvent(RConsumer<MessageEmbed, ReplyCallbackAction> consumer, DBManager<Player> manager, String enemyId) {
        super("enemyKilledByPlayerEvent", consumer, manager);
        this.enemyId = enemyId;
    }

    public String getEnemyId() {
        return enemyId;
    }
}
