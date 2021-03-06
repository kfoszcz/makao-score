package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

/**
 * Created by Krzysztof on 2018-03-05.
 */

@Entity(
        primaryKeys = {"gameId", "playerId"},
        foreignKeys = @ForeignKey(
                entity = Game.class,
                parentColumns = "id",
                childColumns = "gameId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        )
)
public class PlayerGame {

    private int gameId;
    private int playerId;
    private int playerIndex;

    public PlayerGame(int gameId, int playerId, int playerIndex) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.playerIndex = playerIndex;
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

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }
}
