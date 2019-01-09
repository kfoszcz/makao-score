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
        "GROUP BY initials " +
        "ORDER BY gameCount DESC")
    List<PlayerGroup> getPlayerGroups();

    @Query("SELECT COUNT(*) FROM Game")
    int getGameCount();

    @Query("WITH list AS ( " +
        "SELECT id FROM Player " +
        "WHERE id IN (:players) " +
        ") SELECT gameId FROM PlayerGame " +
        "GROUP BY gameId " +
        "HAVING SUM(playerId NOT IN list) = 0 " +
        "AND SUM(playerId IN list) = (SELECT COUNT(*) FROM list)")
    int[] getGamesByPlayers(int[] players);

    @Query("WITH results AS (\n" +
        "\tSELECT gameId, playerId, SUM(points) AS result, COUNT(DISTINCT dealId) AS deals FROM Score\n" +
        "\tGROUP BY gameId, playerId\t\n" +
        "), best AS (\n" +
        "\tSELECT gameId, MAX(result) AS best FROM results\n" +
        "\tGROUP BY gameId\n" +
        "), winners AS (\n" +
        "\tSELECT playerId, SUM(result = best) AS wins FROM results\n" +
        "\tJOIN best ON results.gameId = best.gameId\n" +
        "\tGROUP BY playerId\n" +
        "), stats AS (\n" +
        "\tSELECT\n" +
        "\t\tplayerId,\n" +
        "\t\t1.0 * SUM(scoreType = 4) / COUNT(*) AS successRate,\n" +
        "\t\t1.0 * SUM(scoreType IN (2,3)) / COUNT(*) AS halfRate,\n" +
        "\t\t1.0 * SUM(scoreType = 1) / COUNT(*) AS failRate\n" +
        "\tFROM Score\n" +
        "\tGROUP BY playerId\n" +
        ")\n" +
        "SELECT Player.*, wins, successRate, halfRate, failRate FROM winners\n" +
        "JOIN stats ON winners.playerId = stats.playerId\n" +
        "JOIN Player ON winners.playerId = Player.id\n" +
        "ORDER BY wins DESC, successRate DESC, halfRate DESC, failRate ASC")
    List<PlayerWithStats> getPlayersStats();

    @Query("WITH results AS (\n" +
        "\tSELECT gameId, playerId, SUM(points) AS result, COUNT(DISTINCT dealId) AS deals FROM Score\n" +
        "\tWHERE gameId IN (:games)\n" +
        "\tGROUP BY gameId, playerId\t\n" +
        "), best AS (\n" +
        "\tSELECT gameId, MAX(result) AS best FROM results\n" +
        "\tGROUP BY gameId\n" +
        "), winners AS (\n" +
        "\tSELECT playerId, SUM(result = best) AS wins FROM results\n" +
        "\tJOIN best ON results.gameId = best.gameId\n" +
        "\tGROUP BY playerId\n" +
        "), stats AS (\n" +
        "\tSELECT\n" +
        "\t\tplayerId,\n" +
        "\t\t1.0 * SUM(scoreType = 4) / COUNT(*) AS successRate,\n" +
        "\t\t1.0 * SUM(scoreType IN (2,3)) / COUNT(*) AS halfRate,\n" +
        "\t\t1.0 * SUM(scoreType = 1) / COUNT(*) AS failRate\n" +
        "\tFROM Score\n" +
        "\tWHERE gameId IN (:games)\n" +
        "\tGROUP BY playerId\n" +
        ")\n" +
        "SELECT Player.*, wins, successRate, halfRate, failRate FROM winners\n" +
        "JOIN stats ON winners.playerId = stats.playerId\n" +
        "JOIN Player ON winners.playerId = Player.id\n" +
        "ORDER BY wins DESC, successRate DESC, halfRate DESC, failRate ASC")
    List<PlayerWithStats> getPlayersStats(int[] games);
}
