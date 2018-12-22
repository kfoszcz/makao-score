package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Krzysztof on 2018-03-01.
 */

@Database(entities = {Player.class, Game.class, PlayerGame.class, Score.class}, version = 1)
@TypeConverters({Converter.class})
public abstract class MakaoDatabase extends RoomDatabase {

    private static MakaoDatabase instance;
    private static final String DB_NAME = "makao";

    public abstract MakaoDao dao();

    public static MakaoDatabase getDatabase(Context context) {
        if (instance == null) {
            copyDatabaseFromAssets(context, DB_NAME);
            instance = Room.databaseBuilder(context, MakaoDatabase.class, DB_NAME).build();
        }
        return instance;
    }

    private static void copyDatabaseFromAssets(Context context, String databaseName) {
        final File dbPath = context.getDatabasePath(databaseName);
        if (dbPath.exists()) {
            return;
        }
        dbPath.getParentFile().mkdirs();

        try {
            final InputStream inputStream = context.getAssets().open("databases/" + databaseName);
            final OutputStream outputStream = new FileOutputStream(dbPath);

            int bufferSize = 8192;
            byte[] buffer = new byte[bufferSize];
            int length;

            while ((length = inputStream.read(buffer, 0, bufferSize)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        }
        catch (IOException e) {
            Log.d("Database asset", "Failed to open file " + databaseName, e);
        }
    }

}
