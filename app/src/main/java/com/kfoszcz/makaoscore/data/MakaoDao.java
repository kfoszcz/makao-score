package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Krzysztof on 2018-03-01.
 */

@Dao
public interface MakaoDao {

    @Query("SELECT * FROM player")
    List<Player> getAllPlayers();

    @Insert
    long insertPlayers(Player player);

    @Delete
    void deletePlayer(Player player);
}
