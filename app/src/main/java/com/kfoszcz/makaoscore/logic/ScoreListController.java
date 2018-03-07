package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.view.ScoreViewInterface;

/**
 * Created by Krzysztof on 2018-03-07.
 */

public class ScoreListController {

    private ScoreViewInterface view;
    private MakaoDao dataSource;

    public ScoreListController(ScoreViewInterface view, MakaoDao dataSource) {
        this.view = view;
        this.dataSource = dataSource;
    }

    public void getScoreList(int gameId) {
        (new LoadScoresTask()).execute(gameId);
    }

    private class LoadScoresTask extends AsyncTask<Integer, Void, Player[]> {

        @Override
        protected Player[] doInBackground(Integer... integers) {
            Player[] players = dataSource.getPlayersInGame(integers[0]);
            return players;
        }

        @Override
        protected void onPostExecute(Player[] players) {
            view.setUpScoreList(players);
        }
    }

}
