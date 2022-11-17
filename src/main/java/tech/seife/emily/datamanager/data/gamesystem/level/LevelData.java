package tech.seife.emily.datamanager.data.gamesystem.level;

public interface LevelData {

    /**
     * @param level The level we want to save.
     * @return true, if it was saved successfully.
     */
    boolean saveLevel(Level level);

    /**
     * @param level The level we want to delete.
     * @return true, if it was deleted successfully.
     */
    boolean deleteLevel(Level level);
}
