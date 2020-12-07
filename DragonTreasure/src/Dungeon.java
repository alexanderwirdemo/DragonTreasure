import java.util.Scanner;

public class Dungeon {
    // Variables
    private static Room[][] rooms; // This two-dimensional array holds all Room-object connected to the Dungeon
    private static Room currentRoom; // This variable represents the Room where the player is currently at
    private static Player player; // This variable represents the Player-object
    private static String description; // This variable hold the description of the Dungeon

    // Constructor
    public Dungeon(Room[][] rooms, Room currentRoom, Player player, String description) {
        this.rooms = rooms;
        this.currentRoom = currentRoom;
        this.player = player;
        this.description = description;
    }

    // Getters and setters
    public static Room[][] getRooms() {
        return rooms;
    }

    public static Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    public static Player getPlayer() {
        return player;
    }

    /**
     * This method is the main method of the game. It plays the game by calling different options until the game is over
     */
    public void playGame() {
        // Variables
        Scanner scanner = new Scanner(System.in); // This scanner takes input from the player
        boolean game = true; // While this is set to true, the game goes on. It switches to false when the hero is slain or victory is achieved
        Room [][] rooms = getRooms(); // This variable holds all the rooms in the dungeon

        System.out.println(description);
        System.out.println("You are standing outside a cave. There is a smell of sulfur coming from the opening.");
        System.out.println("The cave opening is to your east. Write \"e\" and press [Enter] to enter the cave");

        String option = "";

        boolean correctInput = false;
        while(!correctInput){
            option = scanner.nextLine();
            if(option.equals("e")){
                System.out.println("As you enter the cave the entrance collapses behind you.");
                correctInput = true;
                // This while-loop will repeat as long as the game is active, presenting moving options as well as enemies and objects interactively to the player
                while(game) {
                    // Variables
                    Room currentRoom = getCurrentRoom(); // This variable represents the room the player is currently in
                    int currentRow = getRow(currentRoom); // This variable represents the row in the 2-dimensional Room-array corresponding to the players position
                    int currentCol = getCol(currentRoom); // This variable represents the column in the 2-dimensional Room-array corresponding to the players position
                    Item[] items = player.getItems(); // This variable holds all the player's items, and updates dynamically

                    // First off, when the player enters a Room, a battle is fought through the method doBattle().
                    // If the battle is won or there are no enemies to fight, the player is still alive and the game moves on
                    boolean alive = currentRoom.doBattle();
                    if (!alive) {
                        game = false;

                        // The next thing to pass is a win-condition. Should the player be at the exit and have the treasure, the game is won
                    } else {
                        if (currentRoom == Dungeon.getRooms()[0][2]) {
                            for (int backpackIndex = 0; backpackIndex < 4; backpackIndex++) {
                                if (items[backpackIndex] instanceof Treasure) {
                                    System.out.println("You're winner!");
                                    game=false;
                                }
                            }

                        }
                        // This part of the program runs the narrative and presents the player with options and validates those options to progress the game
                        if(game){
                            currentRoom.doNarrative(); // The current room is narrated through the method doNarrative in the Room-class

                            // After the narrative of options has been displayed, the player makes a choice
                            System.out.print("Choose option: ");
                            option = scanner.nextLine();
                            char direction = option.toCharArray()[0];
                            // If the player decides to drink a potion, this part of the program assesses whether the player is in possession of a potion, and if so, restores life
                            if (option.equals("d")) {
                                // Variable
                                boolean hasPotion = false; // This variable controls if the player has a healing potion. It is by default set to false.

                                // This for-loop checks the players array of Items and if a potion exists, it is removed (replaced by null) and life set to max
                                for (int backpackIndex = 0; backpackIndex < 4; backpackIndex++) {
                                    if (items[backpackIndex] instanceof Potion) {
                                        hasPotion = true;
                                        player.setHealthPoints(10); // Life is set to maximum
                                        items[backpackIndex] = null; // The potion is removed, ie the place in the array is set to null
                                        System.out.println("You drink the potion and your life is now restored to maximum");
                                    }
                                }
                                // Should the player be without a potion, the player is made aware of that
                                if (!hasPotion) {
                                    System.out.println("Sorry, you don't have a potion");
                                }
                            }

                            // If the player selects to pick up an Item, this part of the program lets the player add the Item
                            // to the player's array of Items
                            else if (option.equals("p")) {
                                // Variable
                                Item itemInRoom = currentRoom.getItem(); // This variable represents the Item in the Room, if any

                                // This part of the program checks the Item-array for a free space, and adds the Item to that space
                                if (itemInRoom != null) {

                                    System.out.println("You pick it up and toss it into your backpack.");

                                    // This for-loop checks the Item-array for empty space and replaces it with the Item
                                    for (int backpackIndex = 0; backpackIndex < items.length; backpackIndex++) {
                                        if (items[backpackIndex] == null) {
                                            items[backpackIndex] = itemInRoom;
                                            break; // The iteration breaks, otherwise it would have added the Item to all empty places
                                        }
                                    }

                                    currentRoom.setItem(null); // Once the Item has been picked up, the Item is removed from the Room-object

                                    // Finally, an overview of the Items is given
                                    System.out.println("You now have these items in your backpack:");
                                    for (Item item : items) {
                                        if (item != null) {
                                            System.out.print(item.name + " ");
                                        }
                                    }
                                    System.out.println();
                                }

                            }

                            // This part of the program controls where the player is heading and validates if the player can move there
                            boolean existingChamber = checkChamber(direction, currentRow, currentCol); // First off, the method checkChamber determines weather there is a Room-object in that direction
                            // If a Room exists in that direction, this part of the program investigates if the Room is locked or not
                            if (existingChamber) {
                                boolean isLocked = checkLocked(direction, currentRow, currentCol); // This part of the program uses the method isLocked to determine id the Room is locked, and returns the status as a boolean
                                // If the Door is locked, this part of the program checks if the player has a Key to unlock it
                                if (isLocked) {
                                    // This loop checks all the Item-objects in the player's Item-array in search of a Key-Item
                                    for (Item item : items) {
                                        if (item != null) {
                                            if (item.getName().equals("Key")) {
                                                System.out.println("You unlock the door using your key in your backpack.");
                                                Door[] unlockedDoors = unlockDoor(direction, currentRow, currentCol); // The Door gets unlocked
                                                currentRoom.setDoors(unlockedDoors); // The Rom is updated with the new array of Doors
                                                isLocked = false; // The boolean is set to false
                                                int rowRoomToEnter = currentRow;
                                                int colRoomToEnter = currentCol;
                                                if (direction == 'n') {
                                                    rowRoomToEnter--;
                                                } else if (direction == 's') {
                                                    rowRoomToEnter++;
                                                } else if (direction == 'w') {
                                                    colRoomToEnter--;
                                                } else if (direction == 'e') {
                                                    colRoomToEnter++;
                                                }
                                                setCurrentRoom(rooms[rowRoomToEnter][colRoomToEnter]); // The Room is updated
                                            }
                                        }
                                    }
                                    // If the Door is locked and the player doesn't have the key, the player is informed of this
                                    if (isLocked) {
                                        System.out.println("Sorry, the door is locked. You should seek out the key...");
                                    }

                                }
                                // If the Door is unlocked, the Room is updated to the new room
                                else {
                                    int rowRoomToEnter = currentRow;
                                    int colRoomToEnter = currentCol;
                                    if (direction == 'n') {
                                        rowRoomToEnter--;
                                    } else if (direction == 's') {
                                        rowRoomToEnter++;
                                    } else if (direction == 'w') {
                                        colRoomToEnter--;
                                    } else if (direction == 'e') {
                                        colRoomToEnter++;
                                    }
                                    setCurrentRoom(rooms[rowRoomToEnter][colRoomToEnter]);
                                }

                            }
                            // If there isn't a Room in the chosen direction, the player is alerted of this
                            else {
                                System.out.println("Sorry, there is no chamber in that direction");
                            }
                        }

                    }

                }
            }
            else{
                System.out.println("Try again");
            }
        }

    }

    /**
     * This method sets the locked-status to false for the target Door and returns the entire array of Doors
     * @param direction
     * @param currentRow
     * @param currentCol
     * @return Door[] doors
     */
    private Door[] unlockDoor(char direction, int currentRow, int currentCol) {
        Room currentRoom = rooms[currentRow][currentCol];
        Door[] doors = currentRoom.getDoors();
        for(Door door:doors){
            if(door.getPosition()==direction){
                door.setLocked(false);
                return doors;
            }
        }
        return doors;
    }

    /**
     * This method checks if the Room is locked or not by checking the status of the variable locked
     * @param direction
     * @param currentRow
     * @param currentCol
     * @return boolean
     */
    private boolean checkLocked(char direction, int currentRow, int currentCol) {
        Room currentRoom = rooms[currentRow][currentCol];
        for(Door door:currentRoom.getDoors()){
            if(door.getPosition()==direction){
                return door.isLocked(); // Getter-method
            }
        }
        return false;
    }

    /**
     * This method takes the direction given and checks if there is a chamber in that direction.
     * If there exists a chamber, true is returned, otherwise false
     * @param direction
     * @param currentRow
     * @param currentCol
     * @return boolean
     */
    private boolean checkChamber(char direction, int currentRow, int currentCol) {
        if(direction=='n'){
            currentRow--;
        }
        else if(direction=='s'){
            currentRow++;
        }
        else if(direction=='w'){
            currentCol--;
        }
        else if(direction=='e'){
            currentCol++;
        }

        if(currentCol < 0 || currentCol > 2){
            return false;
        }
        else if(currentRow < 0 || currentRow > 2){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * This help-method returns the column number of the Rooms array
     * @param currentRoom
     * @return int col
     */
    private int getCol(Room currentRoom) {
        for(int row=0; row<3; row++){
            for(int col=0; col<3; col++){
                if(rooms[row][col]==currentRoom){
                    return col;
                }
            }
        }
        return 0;
    }

    /**
     * This help-method returns the row number of the Rooms array
     * @param currentRoom
     * @return int row
     */
    private int getRow(Room currentRoom) {
        for(int row=0; row<3; row++){
            for(int col=0; col<3; col++){
                if(rooms[row][col]==currentRoom){
                    return row;
                }
            }
        }
        return 0;
    }
}
