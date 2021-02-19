package com.davinci.Games;

/**
 * Created by macintoshhd on 3.02.18.
 */

import java.util.Comparator;

/**
 * @author Comp
 */
public class RatingComparator implements Comparator {

    @Override
    public int compare(Object p1, Object p2) { //compares two objects.
        int rating1 = ((Gamers) p1).getRating();
        int rating2 = ((Gamers) p2).getRating();

        if (rating1 > rating2) {
            return -1;
        } else if (rating1 < rating2) {
            return 1;
        } else {
            return 0;
        }

    }
}
