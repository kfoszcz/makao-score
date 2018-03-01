package com.kfoszcz.makaoscore.data;

/**
 * Created by Krzysztof on 2018-03-01.
 */

public class Player {

    private String initial;
    private String name;

    public Player(String initial, String name) {
        this.initial = initial;
        this.name = name;
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
}
