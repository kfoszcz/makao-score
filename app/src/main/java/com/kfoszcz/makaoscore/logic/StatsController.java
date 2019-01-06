package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.data.PlayerGroup;
import com.kfoszcz.makaoscore.view.StatsViewInterface;

import java.util.List;

public class StatsController {
    private StatsViewInterface view;
    private MakaoDao dataSource;

    public StatsController(StatsViewInterface view, MakaoDao dataSource) {
        this.view = view;
        this.dataSource = dataSource;
    }

    public void loadPlayerGroups() {
        new LoadPlayerGroupsTask(view, dataSource).execute();
    }

    private static class LoadPlayerGroupsTask extends AsyncTask<Void, Void, List<PlayerGroup>> {
        private StatsViewInterface view;
        private MakaoDao dataSource;

        LoadPlayerGroupsTask(StatsViewInterface view, MakaoDao dataSource) {
            this.view = view;
            this.dataSource = dataSource;
        }

        @Override
        protected List<PlayerGroup> doInBackground(Void... voids) {
            List<PlayerGroup> groups = dataSource.getPlayerGroups();
            int gameCount = dataSource.getGameCount();
            groups.add(0, new PlayerGroup("", "All games", gameCount));
            return groups;
        }

        @Override
        protected void onPostExecute(List<PlayerGroup> groups) {
            view.setupPlayerGroupSpinner(groups);
        }
    }
}
