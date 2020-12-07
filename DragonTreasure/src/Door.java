public class Door {
    // Variables
    private char position; // This variable holds the direction the Door is facing (N, S, E, W)
    private boolean locked; // This boolean holds if the Door is locked or not

    // Constructor
    public Door(char position, boolean locked) {
        this.position = position;
        this.locked = locked;
    }

    // Getters and setters
    public char getPosition() {
        return position;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
