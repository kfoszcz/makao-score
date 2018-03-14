package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Embedded;

/**
 * Created by Krzysztof on 2018-03-14.
 */

public class ScoreWithPlayer {

    @Embedded
    public Player player;

    @Embedded
    public Score score;

    public int playerIndex;

}
