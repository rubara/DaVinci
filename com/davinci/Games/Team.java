package com.davinci.Games;

/**
 * Created by macintoshhd on 3.02.18.
 */
public class Team {
    private static final int MAX_PLAYERS = 5; //max number of players in each team
    Gamers[] teamPlayers;
    private String teamName;
    private int numPlayers;

    public Team(String teamName) {
        this.teamName = teamName;
        teamPlayers = new Gamers[MAX_PLAYERS];
        numPlayers = 0;
    }

    public void addPlayer(Gamers newPlayer) {
        if (numPlayers < 5) {
            teamPlayers[numPlayers] = newPlayer; //insert the new player into the team
            numPlayers++;
        }
    }

    public float getAverageRating() {
        return getTotalRating() / numPlayers;
    }

    public int getPlayerRating(int playerNumber) {
        return teamPlayers[playerNumber].getRating();
    }

    public String getPlayerNick(int playerNumber) {
        return teamPlayers[playerNumber].getNick();
    }

    public String getName() {
        return teamName;
    }

    public float getTotalRating() {
        float total = 0;
        for (int i = 0; i < numPlayers; i++) {
            total += teamPlayers[i].getRating();
        }
        return total;
    }
}
