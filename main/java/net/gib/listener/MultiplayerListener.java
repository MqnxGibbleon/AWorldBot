package net.gib.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.gib.AWorld;
import net.gib.events.ActionMethod;
import net.gib.events.Listener;
import net.gib.events.types.PlayerQueEvent;

public class MultiplayerListener implements Listener {

    @ActionMethod
    public void onPlayerQue(PlayerQueEvent event) {
        event.getManager().load();
        event.reply(AWorld.getMultiplayer().quePlayer(event.getManager().get())).queue();

    }


}
