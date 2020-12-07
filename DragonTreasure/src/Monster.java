public class Monster {
    // Variables
    private String name; // This variable holds what kind of monster it is
    private int healthPoints; // This variable holds how many healthPoints remaining the Monster has
    private int damage; // This variable holds the damage inflicted by the Monster
    private String description; // This variable holds a description of the Monster

    //Constructor
    public Monster(String name, int healthPoints, int damage, String description) {
        this.name = name;
        this.healthPoints = healthPoints;
        this.damage = damage;
        this.description = description;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public int getDamage() {
        return damage;
    }

    public String getDescription() {
        return description;
    }
}
