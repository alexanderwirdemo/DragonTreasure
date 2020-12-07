public class Weapon extends Item{
    // Variable
    private int increaseDamage; // This variable holds how much damage the Player can add when wielding the Weapon

    // Constructor
    public Weapon(String name, String description, int increaseDamage) {
        super(name, description);
        this.increaseDamage = increaseDamage;
    }

    // Getter
    public int getIncreaseDamage() {
        return increaseDamage;
    }
}
