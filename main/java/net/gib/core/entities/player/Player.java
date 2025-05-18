package net.gib.core.entities.player;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.gib.storage.cloud.mySql.CloudStorage;
import net.gib.storage.cloud.mySql.SaveAs;
import net.gib.util.RConsumer;

import java.awt.*;


public class Player implements CloudStorage {

    @SaveAs(mySql = "VARCHAR(24) UNIQUE KEY")
    private final String id;

    @SaveAs(mySql = "int default 0")
    private int level;
    @SaveAs(mySql = "double default 0")
    private double exp;
    @SaveAs(mySql = "double default 500")
    private double silver;
    @SaveAs(mySql = "double default 10")
    private double gold;
    @SaveAs(mySql = "double default 2.5")
    private double shardsOfEleftheria;
    @SaveAs(mySql = "int default 0")
    private int knowledge;
    @SaveAs(mySql = "int default 0")
    private int trophies;

    private PlayerMissions mission;


    public Player(String id) {
        this.id = id;
    }



    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getSilver() {
        return silver;
    }

    public void setSilver(double silver) {
        this.silver = silver;
    }

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public double getShardsOfEleftheria() {
        return shardsOfEleftheria;
    }

    public void setShardsOfEleftheria(double shardsOfEleftheria) {
        this.shardsOfEleftheria = shardsOfEleftheria;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(int knowledge) {
        this.knowledge = knowledge;
    }

    public void addGold(double value) {
        setGold(this.gold + value);
    }
    public void addSilver(double value) {
        setSilver(this.silver + value);
    }
    public void addExp(double value) {
        setExp(exp + value);

    }

    public void updateLevel(RConsumer<MessageEmbed,MessageCreateAction> consumer) {
        int levelRequirement = (int) (5000 * Math.pow(level + 1,0.25));
        boolean canLevelUp = levelRequirement <= exp;
        var levelBefore = level;
        while (canLevelUp) {
            level = level + 1;
            exp = exp - levelRequirement;
            levelRequirement = (int) (5000 * Math.pow(level,0.25));
            canLevelUp = levelRequirement <= exp;
            System.out.println(levelRequirement);
        }
        if (level <= levelBefore) return;
        consumer.accept(new EmbedBuilder().setTitle("Level Up!").setDescription("You went from level <<" + levelBefore + ">> to level <<" + level + ">>").setColor(Color.CYAN).build()).queue();
    }


    @Override
    public String getTable() {
        return "players";
    }



}
