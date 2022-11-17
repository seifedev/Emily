package tech.seife.emily.datamanager.data.gamesystem.pet;

import tech.seife.emily.datamanager.data.gamesystem.enums.PetType;
import tech.seife.emily.datamanager.data.gamesystem.inventory.Inventory;
import tech.seife.emily.datamanager.data.gamesystem.level.Level;

public class Pet {

    private final  String name;
    private final String description;
    private final double health;
    private final double stamina;
    private final  PetType type;
    private final  Level level;
    private final  Inventory inventory;

    public Pet(String name, String description, double health, double stamina, PetType type, Level level, Inventory inventory) {
        this.name = name;
        this.description = description;
        this.health = health;
        this.stamina = stamina;
        this.type = type;
        this.level = level;
        this.inventory = inventory;
    }
}

