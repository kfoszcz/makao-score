package com.kfoszcz.makaoscore.view;

import com.kfoszcz.makaoscore.data.GameListItem;
import com.kfoszcz.makaoscore.data.GameWithPlayers;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-07.
 */

public interface GameViewInterface {

    void setUpGameList(List<GameListItem> games);

    void startScoreListActivity(int gameId);
}
