package com.kfoszcz.makaoscore.data;

public class PlayerGroup {
    private String ids;
    private String initials;
    private int gameCount;

    public PlayerGroup(String ids, String initials, int gameCount) {
        this.ids = ids;
        this.initials = initials;
        this.gameCount = gameCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(initials);
        sb.append(" (");
        sb.append(gameCount);
        sb.append(")");
        return sb.toString();
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public int getGameCount() {
        return gameCount;
    }

    public void setGameCount(int gameCount) {
        this.gameCount = gameCount;
    }
}
