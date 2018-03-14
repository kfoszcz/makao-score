package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.data.Score;
import com.kfoszcz.makaoscore.data.ScoreRow;
import com.kfoszcz.makaoscore.data.ScoreWithPlayer;
import com.kfoszcz.makaoscore.view.ScoreViewInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Krzysztof on 2018-03-07.
 */

public class ScoreListController {

    private ScoreViewInterface view;
    private MakaoDao dataSource;

    private Player[] playersToSend;
    private List<ScoreRow> scoreRowsToSend;

    public ScoreListController(ScoreViewInterface view, MakaoDao dataSource) {
        this.view = view;
        this.dataSource = dataSource;
    }

    public void getScoreList(int gameId) {
        (new LoadScoresTask()).execute(gameId);
    }

    public void menuAddPressed(int gameId) {
        (new GetLatestDealTask()).execute(gameId);
    }

    public void scoreRowClicked(ScoreRow scoreRow) {
        view.startAddScoreActivity(scoreRow.getDealId());
    }

    private class GetLatestDealTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            return dataSource.getLatestDealIdForGame(integers[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            view.startAddScoreActivity(integer + 1);
        }
    }

    private class LoadScoresTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            Player[] players = dataSource.getPlayersInGame(integers[0]);
            List<ScoreWithPlayer> scores = dataSource.getScoresForGame(integers[0]);

            // create list of score rows from list of scores
            List<ScoreRow> scoreRows = new ArrayList<>();
            ListIterator<ScoreWithPlayer> iterator = scores.listIterator();
            int previousDeal = 0;
            ScoreRow row = null;
            while (iterator.hasNext()) {
                ScoreWithPlayer currentScore = iterator.next();
                if (currentScore.score.getDealId() != previousDeal) {
                    if (row != null) {
                        scoreRows.add(row);
                    }
                    previousDeal = currentScore.score.getDealId();
                    row = new ScoreRow(previousDeal, players.length);
                }
                row.getScores()[currentScore.playerIndex] = currentScore.score;
            }
            if (row != null) {
                scoreRows.add(row);
            }

            // calculate total points
            ScoreRow previousRow = null;
            for (ScoreRow currentRow : scoreRows) {
                for (int i = 0; i < currentRow.getScores().length; i++) {
                    int previousPoints = (previousRow != null)
                            ? previousRow.getScores()[i].getTotalPoints() : 0;

                    currentRow.getScores()[i].setTotalPoints(
                            currentRow.getScores()[i].getPoints() + previousPoints
                    );
                }
                previousRow = currentRow;
            }

            playersToSend = players;
            scoreRowsToSend = scoreRows;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            view.setUpScoreListHeader(playersToSend);
            view.setUpScoreList(scoreRowsToSend);
        }
    }

}
