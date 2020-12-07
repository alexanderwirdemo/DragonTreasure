public class Item {
    // Variables
    protected String name;
    protected String description;

    // Constructor
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
