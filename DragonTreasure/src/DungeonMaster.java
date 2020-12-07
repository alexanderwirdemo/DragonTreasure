import java.util.Scanner;

public class DungeonMaster {
    // Variables
    private static Dungeon dungeon; // This variable holds the Dungeon-object the game resolves around

    // These variables represents all 8 types of Doors
    private static Door northDoorOpen = new Door('n', false);
    private static Door northDoorLocked = new Door('n', true);
    private static Door southDoorOpen = new Door('s', false);
    private static Door southDoorLocked = new Door('s', true);
    private static Door eastDoorOpen = new Door('e', false);
    private static Door eastDoorLocked = new Door('e', true);
    private static Door westDoorOpen = new Door('w', false);
    private static Door westDoorLocked = new Door('w', true);

    // These variables represent the different Items
    private static Item key = new Key("Key","a rusty old key");
    private static Item sword = new Weapon("Sword", "a legendary blade, said to have belonged to Beowulf", 1);
    private static Item potion = new Potion("Potion", "a magic potion that heals even the most severe wounds");
    private static Item treasure = new Treasure("Treasure", "a hoarde of glimmering treasure");

    // These variables represent the different Monsters
    private static Monster beast = new Monster("beast",8,1,"A beast guards this chamber");
    private static Monster dragon = new Monster("dragon", 18, 1, "An angry dragon appears");

    public static void main(String[] args){
        startGame();
    }

    /**
     * This method starts the game by building all Rooms, inserting Items and Monsters into them and adding the Rooms to
     * a 2-dimensional Array of Rooms, and taking the player's name and building a Player object from it.
     * The method then constructs a Dungeon-object from all variables and runs the game
     */
    private static void startGame() {
        // Variables
        Scanner scanner = new Scanner(System.in); // This variable reads data from the player
        Item[] items = new Item[4]; // This array holds the 'backpack' of the Player

        // The player's name is given
        System.out.println("Welcome to Dragon Treasure");
        System.out.println("Write your name and press [Enter] to start a new game...");
        String name = scanner.nextLine();
        Player player = new Player(name, 10, 1, items); // The Player is constructed
        Room[][] rooms = new Room[3][3];

        // The Swordroom is constructed
        Door[] swordRoom = new Door[2];
        swordRoom[0] = southDoorOpen;
        swordRoom[1] = eastDoorOpen;
        rooms[0][0] = new Room(swordRoom, "You see a dead body on the floor", sword);

        // The entranceroom is constructed
        Door[] entranceRoom = new Door[3];
        entranceRoom[0] = northDoorOpen;
        entranceRoom[1] = southDoorOpen;
        entranceRoom[2] = eastDoorOpen;
        rooms[1][0] = new Room(entranceRoom, "The room is lit by a few candles sitting on a table in front of you");

        // The keyroom is constructed
        Door[] keyRoom = new Door[2];
        keyRoom[0] = northDoorOpen;
        keyRoom[1] = eastDoorOpen;
        rooms[2][0] = new Room(keyRoom, "You enter a small room", key);

        // The monsterroom is constructed
        Door[] monsterRoom = new Door[3];
        monsterRoom[0] = westDoorOpen;
        monsterRoom[1] = southDoorOpen;
        monsterRoom[2] = eastDoorOpen;
        rooms[0][1] = new Room(monsterRoom, "You enter a big room. The smell of death surrounds you.", beast);

        // The corridorroom is constructed
        Door[] corridorRoom = new Door[3];
        corridorRoom[0] = northDoorOpen;
        corridorRoom[1] = eastDoorOpen;
        corridorRoom[2] = southDoorOpen;
        rooms[1][1] = new Room(corridorRoom, "You enter a corridor");

        // The potionroom is constructed
        Door[] potionRoom = new Door[3];
        potionRoom[0] = northDoorOpen;
        potionRoom[1] = eastDoorLocked;
        potionRoom[2] = westDoorOpen;
        rooms[2][1] = new Room(potionRoom, "You enter a small room with a shelf.", potion);

        // The exit is constructed
        Door[] exitRoom = new Door[1];
        exitRoom[0] = westDoorOpen;
        rooms[0][2] = new Room(exitRoom, "You've found a way out of here, but you still haven't found what you came for: treasure and riches");

        // The secret room is constructed
        Door[] secretRoom = new Door[3];
        secretRoom[0] = northDoorOpen;
        secretRoom[1] = westDoorOpen;
        secretRoom[2] = southDoorOpen;
        rooms[1][2] = new Room(secretRoom, "You've found a secret room!");

        // The dragon chamber is constructed
        Door[] dragonRoom = new Door[2];
        dragonRoom[0] = northDoorOpen;
        dragonRoom[1] = westDoorOpen;
        rooms[2][2] = new Room(dragonRoom, "You enter the dragon's lair", treasure, dragon);

        String description = "Welcome "+player.getName()+" to your treasure hunt. Beware of the dragon!";

        dungeon = new Dungeon(rooms, rooms[1][0], player, description); // The Dungeon-object is constructed

        dungeon.playGame(); // The game starts
    }
}
