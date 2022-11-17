package tech.seife.emily.datamanager.data.gamesystem.inventory;

public class Item {

    private final String name;
    private final String description;
    private final  String icon;
    private final long quantity;

    public Item(String name, String description, String icon, long quantity) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.quantity = quantity;
    }
}
