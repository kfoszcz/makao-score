package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.view.PlayerViewInterface;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-01.
 */

public class PlayerController {

    private PlayerViewInterface view;
    private MakaoDao dataSource;

    private static final Player samplePlayers[] = {
            new Player("K", "Krzycho"),
            new Player("M", "Mama"),
            new Player("T", "Tata"),
            new Player("W", "Wujek"),
            new Player("D", "Dziadziu"),
            new Player("S", "Kasia"),
    };

    private int samplePlayersIndex = 0;

    public PlayerController(PlayerViewInterface view, MakaoDao dataSource) {
        this.view = view;
        this.dataSource = dataSource;
    }

    public void getPlayerList() {

        LoadPlayersTask loadPlayersTask = new LoadPlayersTask();
        loadPlayersTask.execute();

    }

    public void addButtonClicked() {

        if (samplePlayersIndex < samplePlayers.length) {
            (new AddPlayerTask()).execute(samplePlayers[samplePlayersIndex]);
            samplePlayersIndex++;
        }

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

    private class AddPlayerTask extends AsyncTask<Player, Void, Player> {

        @Override
        protected Player doInBackground(Player... players) {
            long rowId = dataSource.insertPlayers(players[0]);
            Player inserted = new Player(players[0].getInitial(), players[0].getName());
            inserted.setId((int) rowId);
            return inserted;
        }

        @Override
        protected void onPostExecute(Player player) {
            view.playerAdded(player);
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
