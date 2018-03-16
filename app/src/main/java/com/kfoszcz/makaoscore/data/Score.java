package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;

/**
 * Created by Krzysztof on 2018-03-05.
 */

@Entity(
        primaryKeys = {"gameId", "playerId", "dealId"},
        foreignKeys = @ForeignKey(
                entity = Game.class,
                parentColumns = "id",
                childColumns = "gameId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
)
public class Score {

    public static final int SCORE_SUCCESS = 4;
    public static final int SCORE_HALF_HIGH = 3;
    public static final int SCORE_HALF_LOW = 2;
    public static final int SCORE_FAIL = 1;
    public static final int SCORE_NONE = 0;

    private int gameId;
    private int playerId;
    private int dealId;
    private int points;
    private int declared;
    private int scoreType;

    @Ignore
    private int totalPoints;

    public Score(int gameId, int playerId, int dealId, int points, int declared, int scoreType) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.dealId = dealId;
        this.points = points;
        this.declared = declared;
        this.scoreType = scoreType;
    }

    public void calculateAndSetScore() {
        points = calculateScore();
    }

    public int calculateScore() {
        if (scoreType == SCORE_SUCCESS)
            return declared + 10;
        if (scoreType == SCORE_HALF_HIGH)
            return (declared + 12) / 2;
        if (scoreType == SCORE_HALF_LOW)
            return (declared + 10) / 2;
        return 0;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getDeclared() {
        return declared;
    }

    public void setDeclared(int declared) {
        this.declared = declared;
    }

    public int getScoreType() {
        return scoreType;
    }

    public void setScoreType(int scoreType) {
        this.scoreType = scoreType;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}
