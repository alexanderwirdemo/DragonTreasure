public class Player {
    // Variables
    private String name;
    private int healthPoints;
    private int damage;
    private Item[] items;

    // Constructor
    public Player(String name, int healthPoints, int damage, Item[] items) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.damage = damage;
        this.items = items;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getDamage() {
        return damage;
    }

    public Item[] getItems() {
        return items;
    }
}
