package net.gib.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.FileUpload;
import net.gib.AWorld;
import net.gib.core.entities.enemy.Enemies;
import net.gib.core.entities.enemy.Enemy;
import net.gib.core.entities.player.PlayerMissions;
import net.gib.events.ActionMethod;
import net.gib.events.Listener;
import net.gib.events.types.EnemyKilledByPlayerEvent;
import net.gib.events.types.PlayerCollectLootEvent;
import net.gib.events.types.PlayerNotWorkingEvent;
import net.gib.storage.cloud.mySql.DBManager;
import net.gib.util.TimeSpan;

import java.awt.*;
import java.io.File;
import java.sql.Timestamp;

public class KillEventListener implements Listener {

    @ActionMethod
    public void onEnemyKilledByPlayerEvent(EnemyKilledByPlayerEvent event) {
        var manager = event.getManager();
        var enemy = new Enemies(manager.get()).of(event.getEnemyId());
        var mission = new PlayerMissions(manager.get().getId());
        DBManager<PlayerMissions> mManager = new DBManager<>(mission, AWorld.getMySQL());
        if (mManager.isAlreadyStored()) {
            mManager.load();
            event.reply(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("FIGHTING ALREADY")
                            .addField("Your player already fights against " + new Enemies(manager.get()).of(mission.getEnemy()).getName() + "!",
                                    mission.getEndTime() < 1 ? "Your player won. Use /collect to claim your rewards!" : "Your Player is ready in " + mission.getEndTime() + " seconds",false)
                            .build()
            ).queue();
            return;
        }
        manager.loadSingle("level");
        var player = manager.get();
        if (enemy.getLevelRequirement() > player.getLevel()){
            event.reply(
                    new EmbedBuilder()
                            .setColor(Color.RED)
                            .setTitle("QUEST FAILED")
                            .addField("The monster was stronger than you!", "Reach level " + enemy.getLevelRequirement() + " and try it again!",false)
                            .build()
            ).queue();
            return;
        }

        mManager.load();
        mission.setStartTime(new Timestamp(System.currentTimeMillis()));
        mission.setEndTime(enemy.getKillTime());
        mission.setEnemy(enemy.getId());
        mManager.insert();
        var file = new File(enemy.getPicUrl());
        event.reply(new EmbedBuilder()
                .setTitle("FIGHT STARTED")
                .setColor(Color.GREEN)
                .setThumbnail("attachment://enemy.png")
                .addField("Your player will now fight against " + enemy.getName(),"Ends in " + enemy.getKillTime() / TimeSpan.secs(1),false)
                .build()).addFiles(FileUpload.fromData(file,"enemy.png")).queue();
    }

    @ActionMethod
    public void onCollect(PlayerCollectLootEvent event) {
        var manager = event.getManager();
        var mManager = event.getSecondManager();

        if (!mManager.isAlreadyStored()) {
            AWorld.getEventHandler().fireEvent(new PlayerNotWorkingEvent(event::reply));
            return;
        }

        mManager.load();
        manager.load();
        var player = manager.get();
        var mMission = mManager.get();
        Enemy enemy = new Enemies(player).of(mMission.getEnemy());


        if (mMission.getEndTime() > 0) {
            event.reply(new EmbedBuilder()
                    .setColor(Color.ORANGE)
                    .setTitle("The player is still fighting!")
                    .setDescription("Wait " + mMission.getEndTime() + " seconds")
                    .build()).queue();
            return;
        }


        enemy.getLoot().gather();
        player.updateLevel(event.getChannel()::sendMessageEmbeds);
        manager.insert();
        event.reply(new EmbedBuilder()
                .setColor(Color.BLUE)
                .setTitle("You received from " + enemy.getName() + ": ")
                .addField("EXP : ", String.valueOf(enemy.getLoot().drops()[0].getValue()),true)
                .addField("SILVER : ", String.valueOf(enemy.getLoot().drops()[1].getValue()),true)
                .addField("GOLD : ", String.valueOf(enemy.getLoot().drops()[2].getValue()),true)
                .build()).queue();
        mManager.drop();
    }

    @ActionMethod
    public void onPlayerNotWorking(PlayerNotWorkingEvent event) {
        event.reply(new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Your player isn't working!")
                .build()).queue();
    }

}
