package com.kfoszcz.makaoscore.view;

import com.kfoszcz.makaoscore.data.Player;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-01.
 */

public interface PlayerViewInterface {

    void setUpPlayerList(List<Player> playerList);

    void playerAdded(Player player);

    void startAddPlayerActivity(int playerId);

    void startScoreListActivity(int gameId);

    void showToast(String message);
}
