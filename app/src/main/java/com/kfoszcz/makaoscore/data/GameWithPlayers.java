package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Embedded;

/**
 * Created by Krzysztof on 2018-03-10.
 */

public class GameWithPlayers {

    @Embedded
    public Game game;
    public int playerCount;

}
