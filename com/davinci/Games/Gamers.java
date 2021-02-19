package com.davinci.Games;

/**
 * Created by macintoshhd on 3.02.18.
 */
public class Gamers implements Comparable<Gamers> {

    private String nick;
    private int rating;

    public Gamers(String nick, int rating) {
        this.nick = nick;
        this.rating = rating; //set the 'rating' field of this Player to the parameter given (rating).
    }

    public int getRating() {
        return rating;
    }

    public String getNick() {
        return nick;
    }

    @Override
    public int compareTo(Gamers anotherPlayer) {
        if (this.rating < anotherPlayer.getRating()) {
            return -1; //smaller
        } else if (this.rating > anotherPlayer.getRating()) {
            return 1; // bigger
        }
        return 0; //equal
    }

}
