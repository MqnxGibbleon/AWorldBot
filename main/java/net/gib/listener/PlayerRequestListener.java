package net.gib.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.gib.events.ActionMethod;
import net.gib.events.Listener;
import net.gib.events.types.PlayerRequestInfoEvent;

import java.awt.*;

public class PlayerRequestListener implements Listener {



    @ActionMethod
    public void onPlayerRequestInfo(PlayerRequestInfoEvent event) {
        event.getManager().load();
        var player = event.getManager().get();
        var user = event.getUser();
        event.reply(new EmbedBuilder()
                .setColor(Color.GREEN)
                .setTitle(">>Player Data<<")
                .addField("Level","<<" + player.getLevel() + ">>",true)
                .addField("Silver"," " + player.getSilver(),true)
                .addField("Gold"," " + player.getGold(),true)
                .addField("Shards of Eleftheria"," " + player.getShardsOfEleftheria(),true)
                .addField("Knowledge Points"," " + player.getKnowledge(),true)
                .setThumbnail(user.getAvatarUrl())
                .build()).queue();
    }
}
