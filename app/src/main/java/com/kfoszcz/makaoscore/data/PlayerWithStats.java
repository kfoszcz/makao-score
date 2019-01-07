package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Embedded;

public class PlayerWithStats {
    @Embedded
    private Player player;
    private int wins;
    private float successRate;
    private float halfRate;
    private float failRate;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(float successRate) {
        this.successRate = successRate;
    }

    public float getHalfRate() {
        return halfRate;
    }

    public void setHalfRate(float halfRate) {
        this.halfRate = halfRate;
    }

    public float getFailRate() {
        return failRate;
    }

    public void setFailRate(float failRate) {
        this.failRate = failRate;
    }
}
