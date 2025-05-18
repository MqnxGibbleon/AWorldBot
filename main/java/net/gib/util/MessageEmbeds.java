package net.gib.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.gib.multiplayer.MPPlayer;

import java.awt.*;

public class MessageEmbeds {


    public static MessageEmbed getMatchFoundEmbed(MPPlayer p1, MPPlayer p2){
        return new EmbedBuilder().setTitle("MATCH FOUND!").setColor(Color.RED).addField(">>ARENA<<","<@" + p1.getId() + "> " + "found a Match with <@" + p2.getId() + "> !",false).build();
    }


}
