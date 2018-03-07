package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.Game;
import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.view.GameViewInterface;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-07.
 */

public class GameListController {

    private GameViewInterface view;
    private MakaoDao dataSource;

    public GameListController(GameViewInterface view, MakaoDao dataSource) {
        this.view = view;
        this.dataSource = dataSource;
    }

    public void getGameList() {
        (new LoadGamesTask()).execute();
    }

    public void gameRowClicked(Game game) {
        view.startScoreListActivity(game.getId());
    }

    private class LoadGamesTask extends AsyncTask<Void, Void, List<Game>> {

        @Override
        protected List<Game> doInBackground(Void... voids) {
            return dataSource.getAllGames();
        }

        @Override
        protected void onPostExecute(List<Game> games) {
            view.setUpGameList(games);
        }

    }

}
