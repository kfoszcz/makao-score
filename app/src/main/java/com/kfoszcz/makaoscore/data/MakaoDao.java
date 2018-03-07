package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-01.
 */

@Dao
public interface MakaoDao {

    @Query("SELECT * FROM player")
    List<Player> getAllPlayers();

    @Query("SELECT * FROM player WHERE id = :id")
    Player getPlayerById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertPlayers(Player player);

    @Delete
    void deletePlayer(Player player);

    @Query("SELECT * FROM game ORDER BY startDate DESC")
    List<Game> getAllGames();

    @Query("SELECT * FROM game WHERE id = :id")
    Game getGameById(int id);

    @Insert
    long insertGame(Game game);

    @Query("SELECT * FROM playergame " +
            "JOIN player ON playergame.playerIndex = player.id " +
            "WHERE gameId = :gameId " +
            "ORDER BY playerIndex ASC")
    Player[] getPlayersInGame(int gameId);

    @Insert
    long instertPlayerGame(PlayerGame playerGame);

}
