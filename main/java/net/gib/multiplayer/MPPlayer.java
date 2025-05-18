package net.gib.multiplayer;

import net.gib.core.entities.player.Player;

/*
MPPlayer = Multiplayer Player
Only for the Que System
 */
public class MPPlayer {

    private final String id;
    private final int rating;
    private int health;
    private final int maxHealth;
    private int defence;
    private final int maxDefence;
    private int shield;
    private int strength;


    public MPPlayer(String id, int rating, int maxHealth, int maxDefence) {
        this.id = id;
        this.rating = rating;
        this.maxHealth = maxHealth;
        this.maxDefence = maxDefence;
        this.health = maxHealth;
        this.defence = maxDefence;
        this.shield = 0;
        this.strength = 0;
    }

    public int getRating() {
        return rating;
    }

    public int getHealth() {
        return health;
    }

    public MPPlayer setHealth(int health) {
        this.health = health;
        return this;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getDefence() {
        return defence;
    }

    public MPPlayer setDefence(int defence) {
        this.defence = defence;
        return this;
    }

    public int getMaxDefence() {
        return maxDefence;
    }

    public int getShield() {
        return shield;
    }

    public MPPlayer setShield(int shield) {
        this.shield = shield;
        return this;
    }

    public int getStrength() {
        return strength;
    }

    public MPPlayer setStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public String getId() {
        return id;
    }

    public static MPPlayer detectRating(Player player) {

        return new MPPlayer(player.getId(),player.getLevel(),500,100);
    }

    public boolean canMatch(int rating, int radius) {
        return rating - radius <= this.rating && rating + radius >= this.rating;
    }
}
