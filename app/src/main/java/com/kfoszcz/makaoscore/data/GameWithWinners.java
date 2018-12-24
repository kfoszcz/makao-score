package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Embedded;

public class GameWithWinners {
    @Embedded
    public Game game;
    public String initial;
    public int deals;
    public boolean winner;
}
