package com.kfoszcz.makaoscore.view;

import com.kfoszcz.makaoscore.data.PlayerGroup;
import com.kfoszcz.makaoscore.data.PlayerWithStats;

import java.util.List;

public interface StatsViewInterface {
    void setupPlayerGroupSpinner(List<PlayerGroup> groups);
    void setupStatsList(List<PlayerWithStats> stats);
}
