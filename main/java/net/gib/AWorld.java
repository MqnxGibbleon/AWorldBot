package net.gib;

// Minispiel in Discord via Discord API
// Leander Höft

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.gib.events.EventHandler;
import net.gib.events.EventListener;
import net.gib.listener.KillEventListener;
import net.gib.listener.MultiplayerListener;
import net.gib.listener.PlayerRequestListener;
import net.gib.multiplayer.QueManager;
import net.gib.storage.cloud.mySql.MySQL;
import net.gib.util.ProjectData;

public class AWorld {

    private static final ProjectData PROJECT_DATA;
    private static EventHandler EVENT_HANDLER;
    private static QueManager MULTIPLAYER;

    private static MySQL mySQL;

    public static MySQL getMySQL() {
        return mySQL;
    }

    public static EventHandler getEventHandler() {
        return EVENT_HANDLER;
    }

    public static QueManager getMultiplayer() {
        return MULTIPLAYER;
    }

    static {
        try {
            PROJECT_DATA = ProjectData.get("settings","ps");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        mySQL = PROJECT_DATA.getMySql(MySQL.DataBaseType.MYSQL);
        launchBot();
        EVENT_HANDLER = new EventHandler(
                new KillEventListener(),
                new PlayerRequestListener(),
                new MultiplayerListener()
        );
        MULTIPLAYER = new QueManager(2,1);

    }

    public static void launchBot() {

        var builder = JDABuilder.createDefault(PROJECT_DATA.token());
        builder.setActivity(Activity.watching("/kill"));
        builder.addEventListeners(
                new EventListener()
        );

        var JDA = builder.build();

        JDA.updateCommands().addCommands(
                Commands.slash("kill","Select an enemy and try to kill it!")
                        .setGuildOnly(true)
                        .addOptions(new OptionData(OptionType.STRING,"enemy", "Select your enemy",true)
                                        .addChoice("Zombie","zombie")
                                        .addChoice("Witcher","witcher")
                                        .addChoice("Werwolf","werwolf")
                                        .addChoice("Amethyst Troll","amethyst_troll")
                                        .addChoice("Dark Fairy","dark_fairy")
                                        .addChoice("Dragon","dragon")
                                ),
                Commands.slash("collect","Collects the current loot!")
                        .setGuildOnly(true),
                Commands.slash("info","Information about the player!")
                        .setGuildOnly(true),
                Commands.slash("que","Queues you into the multiplayer fights!")
                        .setGuildOnly(true),
                Commands.slash("que1","Queues you into the multiplayer fights!")
                        .setGuildOnly(true)


        ).queue();
    }




}