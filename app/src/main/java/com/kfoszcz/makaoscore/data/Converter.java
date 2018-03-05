package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

/**
 * Created by Krzysztof on 2018-03-05.
 */

public class Converter {

    @TypeConverter
    public static Date timestampToDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
