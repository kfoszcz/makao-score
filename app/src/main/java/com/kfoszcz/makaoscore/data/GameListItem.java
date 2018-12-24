package com.kfoszcz.makaoscore.data;

import com.kfoszcz.makaoscore.logic.PlayerWithWinner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameListItem {
    public Game game;
    public int deals;
    public List<PlayerWithWinner> players;

    public GameListItem(Game game, int deals) {
        this.game = game;
        this.deals = deals;
        players = new ArrayList<>();
    }
}
