package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 * Created by Krzysztof on 2018-03-05.
 */

@Entity
public class Game {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Date startDate;
    private boolean finished;

    public Game(Date startDate, boolean finished) {
        this.startDate = startDate;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
