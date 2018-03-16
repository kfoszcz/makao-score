package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.Game;
import com.kfoszcz.makaoscore.data.GameWithPlayers;
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

    public void deleteGame(Game game) {
        (new DeleteGameTask()).execute(game);
    }

    private class DeleteGameTask extends AsyncTask<Game, Void, Void> {

        @Override
        protected Void doInBackground(Game... games) {
            dataSource.deletePlayerGameByGameId(games[0].getId());
            dataSource.deleteScoresByGameId(games[0].getId());
            dataSource.deleteGame(games[0]);
            return null;
        }
    }

    private class LoadGamesTask extends AsyncTask<Void, Void, List<GameWithPlayers>> {

        @Override
        protected List<GameWithPlayers> doInBackground(Void... voids) {
            return dataSource.getAllGamesWithPlayerCount();
        }

        @Override
        protected void onPostExecute(List<GameWithPlayers> games) {
            view.setUpGameList(games);
        }

    }

}
