package net.gib.multiplayer;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.gib.AWorld;
import net.gib.core.entities.player.Player;
import net.gib.storage.cloud.mySql.DBManager;
import net.gib.util.MessageEmbeds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class QueManager {

    private final int maxQueCount;
    private final int maxPlayer;

    private final HashMap<String,Integer> QUE = new HashMap<>();
    private final HashMap<String,Arena> ARENA = new HashMap<>();

    public QueManager(int maxQueCount, int maxPlayer) {
        this.maxQueCount = maxQueCount;
        this.maxPlayer = maxPlayer;
    }

    public int getMaxQueCount() {
        return maxQueCount;
    }

    public int getMaxPlayer() {
        return maxPlayer;
    }

    public MessageEmbed quePlayer(Player player) {
        if (QUE.containsKey(player.getId())) return new EmbedBuilder().setTitle("ALREADY QUEUED!").build();
        if (isInArena(player)) return new EmbedBuilder().setTitle("ALREADY IN A FIGHT!").build();
        if (queFull()) return new EmbedBuilder().setTitle("QUE FULL!").build();
        if (arenaFull()) return new EmbedBuilder().setTitle("ARENA FULL!").build();

        var mplayer = MPPlayer.detectRating(player);
        var possibleMatches = new ArrayList<String>();
        QUE.forEach((id,rating) -> {
            if (mplayer.canMatch(rating,25)) possibleMatches.add(id);
        });

        if (possibleMatches.isEmpty()) {
            QUE.put(mplayer.getId(),mplayer.getRating());
            return new EmbedBuilder().setTitle("QUEUED!").build();
        }

        var manager = new DBManager<>(new Player(possibleMatches.get(0)), AWorld.getMySQL());
        manager.load();
        return putInArena(mplayer,MPPlayer.detectRating(manager.get()));
    }

    private boolean arenaFull() {
        return ARENA.size() >= maxPlayer;
    }

    private boolean queFull() {
        return QUE.size() >= maxQueCount;
    }

    private MessageEmbed putInArena(MPPlayer p1, MPPlayer p2) {
        var arena = new Arena(p1,p2);
        ARENA.put(getKey(arena),arena);
        QUE.remove(p2.getId());
        QUE.remove(p1.getId());
        return MessageEmbeds.getMatchFoundEmbed(p1,p2);
    }


    private String getKey(Arena arena) {
        return arena.getOpponents().get(0).getId() + ":" + arena.getOpponents().get(1).getId();
    }

    private boolean isInArena(Player player) {
        var is = new AtomicBoolean(false);
        ARENA.forEach((id,arena)-> {
            if (is.get()) return;
            for (MPPlayer opponent : arena.getOpponents()) {
                if (!opponent.getId().equals(player.getId())) continue;
                is.set(true);
            }
        });
        return is.get();
    }



}
