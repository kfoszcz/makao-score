package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by Krzysztof on 2018-03-01.
 */

@Database(entities = {Player.class, Game.class, PlayerGame.class, Score.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class MakaoDatabase extends RoomDatabase {

    private static MakaoDatabase instance;

    public abstract MakaoDao dao();

    public static MakaoDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, MakaoDatabase.class, "makao").build();
        }
        return instance;
    }

}
