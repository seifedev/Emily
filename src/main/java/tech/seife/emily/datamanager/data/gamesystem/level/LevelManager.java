package tech.seife.emily.datamanager.data.gamesystem.level;

import org.jetbrains.annotations.NotNull;
import tech.seife.emily.datamanager.data.gamesystem.GameFileHandler;
import tech.seife.emily.datamanager.data.gamesystem.player.Player;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class LevelManager {
    private final GameFileHandler gameFileHandler;
    private final PriorityQueue<Level> levels;

    public LevelManager(@NotNull GameFileHandler gameFileHandler) {
        this.gameFileHandler = gameFileHandler;

        if (gameFileHandler.hasLevels()) {
            levels = gameFileHandler.loadLevels();
        } else {
            levels = new PriorityQueue<>();
        }
    }

    /**
     *
     * @param retainMinimumExperience If the player is going to retain part of the experience and take it with him on the next level.
     * @param maxExperience How much experience he can accumulate within this level before forcing him to level up.
     * @param experienceToLevelUp How much experience is needed for the player to level up.
     * @param canExceedRequirements If the player can exceed the experienceToLevelUp.
     * @param canLoseLevel If he can lose experience from various actions.
     */
    public void createLevel(boolean retainMinimumExperience, double maxExperience, double experienceToLevelUp, boolean canExceedRequirements, boolean canLoseLevel) {
        Level level = new Level(levels.size() + 1, maxExperience, experienceToLevelUp, canExceedRequirements, canLoseLevel);
        levels.add(level);
    }

    /**
     * To create levels in bulk it's suggested to use this method if you don't need to configure anything and just let the system handle it.
     * @param experienceToLevelUp How much experience is needed to level up.
     */
    public void createLevel(double experienceToLevelUp) {
        Level level = new Level(levels.size() + 1, experienceToLevelUp);
        levels.add(level);
    }

    public boolean decreasePlayerLevel(@NotNull Player player, double amount) {
        if (decreasePlayerLevel(player, amount)) {
            if ((player.getExperience() - amount) < 0) {
                player.setLevel(getLevel(player.getLevel().getId() - 1));
            } else {
                player.setExperience(player.getExperience() - amount);
            }
            return true;
        }
        return false;
    }

    /**
     * @param player The player to check if he can lose more exp.
     * @param amount The amount of experience to lose.
     * @return true if he can lose experience, otherwise it returns false.
     */
    public boolean canDecreaseExperience(@NotNull Player player, double amount) {
        if ((player.getExperience() - amount) < 0) {
            return player.getLevel().getCanLoseLevel();
        }
        return true;
    }

    /**
     * @param player The player to increase the level.
     * @return true, if we were able to increase successfully the level of the player.
     */
    public boolean increasePlayerLevel(@NotNull Player player) {
        if (canIncreaseLevel(player) && getLevel(player.getLevel().getId() + 1) != null) {
            player.setLevel(getLevel(player.getLevel().getId() + 1));
            return true;
        }
        return false;
    }

    /**
     * @param player The player we want to check to see if we can increase his/her level.
     * @return true if we can increase it, false if we can't.
     */
    public boolean canIncreaseLevel(@NotNull Player player) {
        return player.getLevel().getExperienceToLevelUp() >= player.getExperience();
    }

    private boolean saveLevel() {
        return false;
    }

    @Nullable
    public Level getLevel(long id) {
        return levels.stream().filter(level -> level.getId() == id).findAny().orElse(null);
    }
}
