package com.kfoszcz.makaoscore.view;

import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.data.ScoreRow;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-07.
 */

public interface ScoreViewInterface {

    void setUpScoreListHeader(Player[] players);

    void setUpScoreList(List<ScoreRow> scores);

    void startAddScoreActivity(int dealId);

}
