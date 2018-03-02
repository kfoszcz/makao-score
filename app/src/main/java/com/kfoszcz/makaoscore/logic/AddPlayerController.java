package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.view.AddPlayerInterface;

/**
 * Created by Krzysztof on 2018-03-02.
 */

public class AddPlayerController {

    private AddPlayerInterface view;
    private MakaoDao dataSource;

    public AddPlayerController(AddPlayerInterface view, MakaoDao dataSource) {
        this.view = view;
        this.dataSource = dataSource;
    }

    public void getPlayerById(int id) {
        (new GetPlayerById()).execute(id);
    }

    public void insertPlayer(Player player) {

        if (player.getInitial().length() != 1) {
            view.showErrorToast("Player initial must be 1 character long");
        }

        else if (player.getName().isEmpty()) {
            view.showErrorToast("Player name cannot be empty");
        }

        else {
            (new AddPlayerTask()).execute(player);
        }

    }

    private class GetPlayerById extends AsyncTask<Integer, Void, Player> {

        @Override
        protected Player doInBackground(Integer... ints) {
            return dataSource.getPlayerById(ints[0]);
        }

        @Override
        protected void onPostExecute(Player player) {
            view.setFormFields(player);
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
            view.finishActivity();
        }

    }

}
