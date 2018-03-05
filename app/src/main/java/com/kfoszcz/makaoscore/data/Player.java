package com.kfoszcz.makaoscore.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by Krzysztof on 2018-03-01.
 */

@Entity
public class Player implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String initial;
    private String name;

    @Ignore
    private boolean selected;

    public Player(String initial, String name) {
        this.initial = initial;
        this.name = name;
        this.selected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
