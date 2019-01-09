package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.data.PlayerGroup;
import com.kfoszcz.makaoscore.data.PlayerWithStats;
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

    public void loadStats(String playerIds) {
        int[] ids = null;
        if (playerIds.length() > 0) {
            String[] splitted = playerIds.split(",");
            ids = new int[splitted.length];
            for (int i = 0; i < splitted.length; i++) {
                ids[i] = Integer.parseInt(splitted[i]);
            }
        }
        new LoadStatsTask(view, dataSource, ids).execute();
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

    private static class LoadStatsTask extends AsyncTask<Void, Void, List<PlayerWithStats>> {
        private StatsViewInterface view;
        private MakaoDao dataSource;
        private int[] players;

        LoadStatsTask(StatsViewInterface view, MakaoDao dataSource, int[] players) {
            this.view = view;
            this.dataSource = dataSource;
            this.players = players;
        }

        @Override
        protected List<PlayerWithStats> doInBackground(Void... voids) {
            List<PlayerWithStats> stats;
            if (players == null) {
                stats = dataSource.getPlayersStats();
            } else {
                int[] games = dataSource.getGamesByPlayers(players);
                stats = dataSource.getPlayersStats(games);
            }
            return stats;
        }

        @Override
        protected void onPostExecute(List<PlayerWithStats> stats) {
            view.setupStatsList(stats);
        }
    }
}
