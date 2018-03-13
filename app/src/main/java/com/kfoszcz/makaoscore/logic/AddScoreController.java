package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.view.AddScoreInterface;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Krzysztof on 2018-03-11.
 */

public class AddScoreController {

    private AddScoreInterface view;
    private MakaoDao dataSource;

    public AddScoreController(AddScoreInterface view, MakaoDao dataSource) {
        this.view = view;
        this.dataSource = dataSource;
    }

    public void getPlayerList(int gameId) {
        (new LoadPlayersTask()).execute(gameId);
    }

    private class LoadPlayersTask extends AsyncTask<Integer, Void, Player[]> {

        @Override
        protected Player[] doInBackground(Integer... integers) {
            Player[] players = dataSource.getPlayersInGame(integers[0]);
            return players;
        }

        @Override
        protected void onPostExecute(Player[] players) {
            view.setUpInputList(new ArrayList<>(Arrays.asList(players)));
        }
    }

}
