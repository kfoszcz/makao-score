package com.kfoszcz.makaoscore.view;

import com.kfoszcz.makaoscore.data.Game;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-07.
 */

public interface GameViewInterface {

    void setUpGameList(List<Game> games);

    void startScoreListActivity(int gameId);
}
