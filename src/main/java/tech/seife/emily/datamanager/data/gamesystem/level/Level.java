package tech.seife.emily.datamanager.data.gamesystem.level;

public class Level {

    private final long id;
    private final double maxExperience;
    private final double experienceToLevelUp;
    private final boolean canExceedRequirements;
    private final boolean canLoseLevel;
    /**
     *
     * @param id The id of the level.
     * @param maxExperience How much experience he can accumulate within this level before forcing him to level up.
     * @param experienceToLevelUp How much experience is needed for the player to level up.
     * @param canExceedRequirements If the player can exceed the experienceToLevelUp.
     * @param canLoseLevel If he can lose experience from various actions.
     */
    protected Level(long id, double maxExperience, double experienceToLevelUp, boolean canExceedRequirements, boolean canLoseLevel) {
        this.id = id;
        this.maxExperience = maxExperience;
        this.experienceToLevelUp = experienceToLevelUp;
        this.canExceedRequirements = canExceedRequirements;
        this.canLoseLevel = canLoseLevel;
    }

    /**
     *
     * @param id The id of the level.
     * @param experienceToLevelUp How much experience is needed for the player to level up.
     */
    protected Level(long id, double experienceToLevelUp) {
        this.id = id;
        this.experienceToLevelUp = experienceToLevelUp;

        this.maxExperience = experienceToLevelUp * 3.5;
        this.canExceedRequirements = false;
        this.canLoseLevel = false;
    }


    public long getId() {
        return id;
    }

    public double getMaxExperience() {
        return maxExperience;
    }

    public double getExperienceToLevelUp() {
        return experienceToLevelUp;
    }

    public boolean getCanExceedRequirements() {
        return canExceedRequirements;
    }

    public boolean getCanLoseLevel() {
        return canLoseLevel;
    }
}
