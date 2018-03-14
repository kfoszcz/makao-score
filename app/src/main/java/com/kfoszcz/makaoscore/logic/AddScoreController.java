package com.kfoszcz.makaoscore.logic;

import android.os.AsyncTask;

import com.kfoszcz.makaoscore.data.MakaoDao;
import com.kfoszcz.makaoscore.data.Player;
import com.kfoszcz.makaoscore.data.PlayerIndexWithSum;
import com.kfoszcz.makaoscore.data.Score;
import com.kfoszcz.makaoscore.data.ScoreRow;
import com.kfoszcz.makaoscore.data.ScoreWithPlayer;
import com.kfoszcz.makaoscore.view.AddScoreInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Krzysztof on 2018-03-11.
 */

public class AddScoreController {

    private AddScoreInterface view;
    private MakaoDao dataSource;

    private List<Player> playersToSend;
    private ScoreRow rowToSend;

    public AddScoreController(AddScoreInterface view, MakaoDao dataSource) {
        this.view = view;
        this.dataSource = dataSource;
    }

    public void getPlayerList(int gameId, int dealId) {
        (new LoadPlayersTask()).execute(gameId, dealId);
    }

    public void menuSavePressed(ScoreRow scoreRow) {
        (new SaveScoresTask(false)).execute(scoreRow);
    }

    public void menuListPressed(ScoreRow scoreRow) {
        (new SaveScoresTask(true)).execute(scoreRow);
    }

    private class SaveScoresTask extends AsyncTask<ScoreRow, Void, Void> {
        private boolean exitActivity;

        public SaveScoresTask(boolean exitActivity) {
            this.exitActivity = exitActivity;
        }

        @Override
        protected Void doInBackground(ScoreRow... scoreRows) {
            dataSource.inesrtScores(scoreRows[0].getScores());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (exitActivity)
                view.finishActivity();
            else
                view.showSaveConfirmation();
        }
    }

    private class LoadPlayersTask extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            Player[] players = dataSource.getPlayersInGame(integers[0]);
            ScoreWithPlayer[] scores = dataSource.getScoresForDeal(integers[0], integers[1]);
            PlayerIndexWithSum[] totals = dataSource.getTotalPointsForPlayers(integers[0]);
            ScoreRow row = new ScoreRow(integers[1], players.length);

            for (ScoreWithPlayer score : scores) {
                row.getScores()[score.playerIndex] = score.score;
            }

            for (int i = 0; i < row.getScores().length; i++) {
                if (row.getScores()[i] == null)
                    row.getScores()[i] = new Score(
                            integers[0],
                            players[i].getId(),
                            integers[1],
                            0,
                            -1,
                            Score.SCORE_NONE
                    );
            }

            for (PlayerIndexWithSum total : totals) {
                row.getScores()[total.playerIndex].setTotalPoints(total.totalPoints);
            }

            playersToSend = new ArrayList<>(Arrays.asList(players));
            rowToSend = row;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            view.setUpInputList(playersToSend, rowToSend);
        }
    }

}
