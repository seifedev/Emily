package tech.seife.emily.datamanager.data.gamesystem;

import tech.seife.emily.datamanager.data.gamesystem.level.Level;

import java.util.PriorityQueue;

public interface GameSystemData {

    boolean hasLevels();

    PriorityQueue<Level> loadLevels();

    boolean addLevel(Level level);

    boolean doesLevelExist(long id);

}
