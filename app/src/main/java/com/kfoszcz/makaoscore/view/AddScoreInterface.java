package com.kfoszcz.makaoscore.view;

import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.data.ScoreRow;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-11.
 */

public interface AddScoreInterface {

    void setUpInputList(List<Player> players, ScoreRow row);

    void finishActivity();

}
