package tech.seife.emily.datamanager.data.gamesystem.player;

import tech.seife.emily.datamanager.data.gamesystem.level.Level;

public class Player {

    private Level level;
    private double experience;

    public Level getLevel() {
        return level;
    }
    public void setLevel(Level level) {
        this.level = level;
    }

    public double getExperience() {
        return experience;
    }

    public void setExperience(double experience) {
        this.experience = experience;
    }
}
