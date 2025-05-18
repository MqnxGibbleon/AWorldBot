package net.gib.core.entities.player;


import net.gib.storage.cloud.mySql.CloudStorage;
import net.gib.storage.cloud.mySql.SaveAs;

import java.sql.Timestamp;

public class PlayerMissions implements CloudStorage {
    @SaveAs(mySql = "VARCHAR(22) UNIQUE KEY")
    private final String id;
    @SaveAs(mySql = "TIMESTAMP")
    private Timestamp startTime;
    @SaveAs(mySql = "VARCHAR(25)")
    private String enemy;
    @SaveAs(mySql = "TIMESTAMP")
    private Timestamp endTime;

    public PlayerMissions(String id) {
        this.id = id;
    }

    public String getEnemy() {
        return enemy;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return (int) (endTime.getTime() - System.currentTimeMillis())/1000;
    }

    public void setEndTime(long millis) {
        this.endTime = new Timestamp(startTime.getTime() + millis);
    }

    @Override
    public String getTable() {
        return "player_missions";
    }


    public String getId() {
        return id;
    }

    public void setEnemy(String enemy) {
        this.enemy = enemy;
    }
}
