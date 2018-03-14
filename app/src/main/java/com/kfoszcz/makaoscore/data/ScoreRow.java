package com.kfoszcz.makaoscore.data;

/**
 * Created by Krzysztof on 2018-03-14.
 */

public class ScoreRow {

    private int dealId;
    private Score[] scores;

    public ScoreRow(int dealId, int players) {
        this.dealId = dealId;
        this.scores = new Score[players];
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public Score[] getScores() {
        return scores;
    }

    public void setScores(Score[] scores) {
        this.scores = scores;
    }
}
