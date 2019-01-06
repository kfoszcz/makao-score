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
            "JOIN player ON playergame.playerId = player.id " +
            "WHERE gameId = :gameId " +
            "ORDER BY playerIndex ASC")
    Player[] getPlayersInGame(int gameId);

    @Insert
    long insertPlayerGame(PlayerGame playerGame);

    @Query("SELECT game.*, COUNT(*) AS playerCount FROM game " +
            "JOIN playergame ON game.id = playergame.gameId " +
            "GROUP BY game.id " +
            "ORDER BY startDate DESC")
    List<GameWithPlayers> getAllGamesWithPlayerCount();

    @Query("SELECT score.*, player.*, playergame.playerIndex FROM score " +
            "JOIN playergame ON playergame.playerId = score.playerId AND playergame.gameId = score.gameId " +
            "JOIN player ON player.id = playergame.playerId " +
            "WHERE score.gameId = :gameId " +
            "ORDER BY dealId ASC, playerIndex ASC")
    List<ScoreWithPlayer> getScoresForGame(int gameId);

    @Query("SELECT score.*, player.*, playergame.playerIndex FROM score " +
            "JOIN playergame ON playergame.playerId = score.playerId AND playergame.gameId = score.gameId " +
            "JOIN player ON player.id = playergame.playerId " +
            "WHERE score.gameId = :gameId AND dealId = :dealId " +
            "ORDER BY dealId ASC, playerIndex ASC")
    ScoreWithPlayer[] getScoresForDeal(int gameId, int dealId);

    @Query("WITH points AS ( " +
            "SELECT Game.id AS gameId, Player.initial, ifnull(SUM(Score.points), 0) AS points, COUNT(Score.gameId) AS deals FROM Game " +
            "JOIN PlayerGame ON Game.id = PlayerGame.gameId " +
            "JOIN Player ON Player.id = PlayerGame.playerId " +
            "LEFT JOIN Score ON PlayerGame.gameId = Score.gameId AND PlayerGame.playerId = Score.playerId " +
            "GROUP BY Game.id, Player.id " +
        "), winners AS ( " +
            "SELECT gameId, MAX(points) as best FROM points " +
            "GROUP BY gameId " +
        ") " +
        "SELECT Game.*, points.initial, points.deals, (points.points == winners.best) AS winner FROM points " +
        "JOIN winners ON points.gameId = winners.gameId " +
        "JOIN Game ON points.gameId = Game.id " +
        "ORDER BY startDate DESC")
    List<GameWithWinners> getGamesWithWinners();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inesrtScores(Score... scores);

    @Query("SELECT MAX(Score.dealId) FROM score WHERE gameId = :gameId")
    int getLatestDealIdForGame(int gameId);

    @Query("SELECT MIN(Score.dealId) FROM score WHERE gameId = :gameId")
    int getFirstDealIdForGame(int gameId);

    @Query("SELECT playergame.playerIndex, SUM(score.points) AS totalPoints FROM playergame " +
            "JOIN player ON playergame.playerId = player.id " +
            "LEFT JOIN score ON player.id = score.playerId AND playergame.gameId = score.gameId " +
            "WHERE playergame.gameId = :gameId " +
            "GROUP BY player.id " +
            "ORDER BY playerIndex ASC")
    PlayerIndexWithSum[] getTotalPointsForPlayers(int gameId);

    @Delete
    void deleteScores(Score... scores);

    @Delete
    void deleteGame(Game game);

    @Query("WITH groups AS ( " +
        "SELECT Game.id, " +
        "GROUP_CONCAT(Player.initial, '') AS initials, " +
        "GROUP_CONCAT(Player.id, ',') AS ids " +
        "FROM Game " +
        "JOIN PlayerGame ON Game.id = PlayerGame.gameId " +
        "JOIN Player ON PlayerGame.playerId = Player.id " +
        "GROUP BY Game.id " +
        "ORDER BY Player.initial) " +
        "SELECT ids, initials, COUNT(*) AS gameCount FROM groups " +
        "GROUP BY initials")
    List<PlayerGroup> getPlayerGroups();

}
