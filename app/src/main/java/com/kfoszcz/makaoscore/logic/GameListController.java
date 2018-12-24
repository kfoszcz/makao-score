package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.Game;
import com.kfoszcz.makaoscore.data.GameListItem;
import com.kfoszcz.makaoscore.data.GameWithPlayers;
import com.kfoszcz.makaoscore.data.GameWithWinners;
import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.view.GameViewInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

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
        new LoadGamesWinnersTask().execute();
    }

    public void gameRowClicked(Game game) {
        view.startScoreListActivity(game.getId());
    }

    public void deleteGame(Game game) {
        (new DeleteGameTask()).execute(game);
    }

    public void loadGamesWithWinners() {
        new LoadGamesWinnersTask().execute();
    }

    private class DeleteGameTask extends AsyncTask<Game, Void, Void> {

        @Override
        protected Void doInBackground(Game... games) {
            dataSource.deleteGame(games[0]);
            return null;
        }
    }

    private class LoadGamesWinnersTask extends AsyncTask<Void, Void, List<GameListItem>> {

        @Override
        protected List<GameListItem> doInBackground(Void... voids) {
            return prepareGameList(dataSource.getGamesWithWinners());
        }

        @Override
        protected void onPostExecute(List<GameListItem> games) {
            view.setUpGameList(games);
        }

    }

    private static List<GameListItem> prepareGameList(List<GameWithWinners> queryResult) {
        List<GameListItem> result = new ArrayList<>();
        ListIterator<GameWithWinners> iterator = queryResult.listIterator();

        GameListItem current = null;
        int currentId = 0;
        while (iterator.hasNext()) {
            GameWithWinners item = iterator.next();
            if (currentId != item.game.getId()) {
                if (current != null) {
                    result.add(current);
                }
                currentId = item.game.getId();
                current = new GameListItem(item.game, item.deals);
            }
            current.players.add(new PlayerWithWinner(item.initial, item.winner));
        }
        if (current != null) {
            result.add(current);
        }
        return result;
    }

}
