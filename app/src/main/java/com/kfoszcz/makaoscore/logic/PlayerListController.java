package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.view.PlayerViewInterface;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-01.
 */

public class PlayerListController {

    private PlayerViewInterface view;
    private MakaoDao dataSource;

    public PlayerListController(PlayerViewInterface view, MakaoDao dataSource) {
        this.view = view;
        this.dataSource = dataSource;
    }

    public void getPlayerList() {
        (new LoadPlayersTask()).execute();
    }

    public void addButtonClicked() {
        view.startAddPlayerActivity(0);
    }

    public void playerRowClicked(Player player) {
        view.startAddPlayerActivity(player.getId());
    }

    public void deletePlayer(Player player) {
        (new DeletePlayerTask()).execute(player);
    }

    private class LoadPlayersTask extends AsyncTask<Void, Void, List<Player>> {

        @Override
        protected List<Player> doInBackground(Void... voids) {
            return dataSource.getAllPlayers();
        }

        @Override
        protected void onPostExecute(List<Player> players) {
            view.setUpPlayerList(players);
        }

    }

    private class  DeletePlayerTask extends AsyncTask<Player, Void, Void> {

        @Override
        protected Void doInBackground(Player... players) {
            dataSource.deletePlayer(players[0]);
            return null;
        }

    }

}
