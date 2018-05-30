package com.olivercatling.hasamishogi.Database;

/**
 * Created by Oliver on 23/04/2018.
 */

public class Player {
    private String username;
    private String bio;
    private int gamesPlayed;
    private int gamesWon;
    private int player_reference;

    public Player(String username, String bio){
        this.username = username;
        this.bio = bio;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
    }

    public Player(String username, String bio, int played, int won, int player_reference){
        this.username = username;
        this.bio = bio;
        this.gamesPlayed = played;
        this.gamesWon = won;
        this.player_reference = player_reference;
    }

    public String getUsername(){ return this.username; }

    public String getBio(){ return this.bio; }

    public int getGamesPlayed(){ return this.gamesPlayed; }

    public int getGamesWon(){ return this.gamesWon; }

    public int getPlayer_reference(){ return this.player_reference; }

    public void incrementGamesPlayed(){
        this.gamesPlayed += 1;
    }

    public void incrementGamesWon(){
        this.gamesWon += 1;
    }

    @Override
    public String toString(){
        return this.username;
    }
}
