package net.gib.multiplayer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Arena {

    private final List<MPPlayer> opponents = new ArrayList<>(2);
    private Timestamp start;

    public Arena(MPPlayer player,MPPlayer player2) {
        opponents.add(0,player);
        opponents.add(1,player2);
        this.start = new Timestamp(System.currentTimeMillis());
    }

    public List<MPPlayer> getOpponents() {
        return opponents;
    }


}
