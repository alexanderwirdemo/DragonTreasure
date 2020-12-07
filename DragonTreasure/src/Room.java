public class Room {
    // Variables
    private Door[] doors; // This variable holds all the Door-objects in the Room
    private String description; // This variable holds a description of the Room
    private Item item; // This variable holds any Items present in the Room
    private Monster monster; // This variable holds any Monsters present in the Room

    // Constructor holding Doors and description
    public Room(Door[] doors, String description) {
        this.doors = doors;
        this.description = description;
    }

    // Constructor holding Doors, description and Item
    public Room(Door[] doors, String description, Item item) {
        this.doors = doors;
        this.description = description;
        this.item = item;
    }

    // Constructor holding Doors, description and Monster
    public Room(Door[] doors, String description, Monster monster) {
        this.doors = doors;
        this.description = description;
        this.monster = monster;
    }

    // Constructor holding Doors, description, Item and Monster
    public Room(Door[] doors, String description, Item item, Monster monster) {
        this.doors = doors;
        this.description = description;
        this.item = item;
        this.monster = monster;
    }

    // Getters and setters
    public Door[] getDoors() {
        return doors;
    }

    public void setDoors(Door[] doors) {
        this.doors = doors;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    /**
     * This method fights a battle between the hero and the Monster in the Room.
     * The healthpoints is reduced by the opponents damage until there is only one fighter left.
     * If the hero wins, true is returned, otherwise false
     * @return boolean
     */
    public boolean doBattle() {
        // First, the program checks wheather there is a Monster in the Room or not
        if(monster!=null){
            // If the Monster is a dragon, it is printed on screen
            if(monster.getName().equals("dragon")){
                printDragon();
            }
            // Variables
            boolean battle = true; // This variable represents the battle, it will remain true until there is only one fighter left alive
            int playersDamage = Dungeon.getPlayer().getDamage(); // This variable holds how much damage the Player can inflict
            int playersHealth = Dungeon.getPlayer().getHealthPoints(); // This variable holds how much health the Player has
            int monstersDamage = monster.getDamage(); // This variable holds how much damage the Monster can inflict
            int monstersHealth = monster.getHealthPoints(); // This variable holds how much health the Monster has
            int additionalDamage = 0; // This variable holds the additional damage that can be inflicted from Items. It is set to 0 per default.

            System.out.println(monster.getDescription());
            // This part of the program checks whether the Player has any Weapon-Items, and if so, adds their damage
            // Variable
            Item[] items = Dungeon.getPlayer().getItems(); // This variable represents all the Items the Player has
            // This loop checks if the Player has any Weapon-Items
            for(Item item:items){
                if(item!=null){
                    if(item instanceof Weapon){
                        additionalDamage = ((Weapon) item).getIncreaseDamage(); // If a Weapon-Item is found, the damage is added
                    }
                }
            }

            // The damage added from items (if any) is added to the Player's base damage
            int playersTotalDamage = playersDamage+additionalDamage;

            // The battle begins
            System.out.println("Battle");

            do{
                System.out.println("The "+monster.getName()+" attacks you and does "+monstersDamage+" damage");
                System.out.println("You attack the "+monster.getName()+" and do "+playersTotalDamage+" damage");
                playersHealth=playersHealth-monstersDamage; // The Player's health is reduced by the damage inflicted
                monstersHealth=monstersHealth-playersTotalDamage; // The Monster's health is reduced by the damage inflicted
                // This condition checks if the hero is slain, and if to, ends the battle with the hero slain (false)
                if(playersHealth<=0){
                    System.out.println("You are slain!");
                    return false;
                }
                // This condition checks if the Monster is slain, and if so, ends the battle with the hero alive (true)
                else if(monstersHealth<=0){
                    System.out.println("You defeat the "+monster.getName());
                    Dungeon.getPlayer().setHealthPoints(playersHealth);
                    System.out.println("You have "+Dungeon.getPlayer().getHealthPoints()+" health points left");
                    monster=null; // The Monster-object is removed from the Room-object
                    battle=false; // The battle ends
                }
            }while(battle);
        }
        return true;
    }

    /**
     * This method tells everything about the Room and gives the different options available to the Player
     */
    public void doNarrative() {
        // Variables
        Item[] items = Dungeon.getPlayer().getItems(); // This variable represents all the Items a player has

        // First off, the description of the Room is given
        System.out.println(description);

        // If there are Items in the Room, these are described and the option of picking them up is given
        if(item!=null){
            System.out.println("In the room, you find a "+item.getName()+".");
            System.out.println("You examine it closer, and find it to be "+item.getDescription());
            // If the Item is a Treasure, it is printed by calling the method printTreasure()
            if(item instanceof Treasure){
                printTreasure();
            }
            System.out.println("Pick up "+item.getName()+" [p]");
        }

        // Second of all, all Doors are printed and the options of walking through them or not is given
        for(Door door:doors){
            // The direction is given
            String direction ="";
            String position = String.valueOf(door.getPosition());
            if(position.equals("n")){
                direction = "north";
            }
            else if(position.equals("s")){
                direction = "south";
            }
            else if(position.equals("w")){
                direction = "west";
            }
            else if(position.equals("e")){
                direction = "east";
            }
            // If the Door is locked, and the player has a Key-Item, the option of going through that Door is given
            if(door.isLocked()){
                boolean hasKey = false;
                for(Item item:items){
                    if(item instanceof Key){
                        System.out.println("The door to the "+direction+" can now be unlocked ["+position+"]");
                        hasKey = true;
                    }
                }
                // If the player doesnät have a Key-Item, the Door is just described
                if(!hasKey){
                    System.out.println("You see a locked door to the "+direction);
                }
            }
            // This part of the program deals with the situation when the player approaces the exit
            else{
                if(Dungeon.getCurrentRoom()==Dungeon.getRooms()[0][1] && direction.equals("east")){
                    System.out.println("You see an exit to the "+direction+" ["+position+"]");
                }
                else{
                    System.out.println("You can go "+direction+" ["+position+"]");
                }
            }
        }

        // Finally, if the player has a Potion-Item and is wounded, the option of drinking the Potion is presented
        for(int backpackIndex=0; backpackIndex<4; backpackIndex++){
            if(items[backpackIndex] instanceof Potion){
                if(Dungeon.getPlayer().getHealthPoints()<10){ // This condition checks if the Player is wounded
                    System.out.println("You have "+Dungeon.getPlayer().getHealthPoints()+" healthpoints left. Might be a good idea to drink that healthpotion [d]");
                }
            }
        }
    }

    /**
     * This help-method prints the Dragon-Monster
     */
    private void printDragon() {
        System.out.println(
                "                                                  .~))>>\n"+
                        "                                                 .~)>>\n"+
                        "                                               .~))))>>>\n"+
                        "                                             .~))>>             ___\n"+
                        "                                           .~))>>)))>>      .-~))>>\n"+
                        "                                         .~)))))>>       .-~))>>)>\n"+
                        "                                       .~)))>>))))>>  .-~)>>)>\n"+
                        "                   )                 .~))>>))))>>  .-~)))))>>)>\n"+
                        "                ( )@@*)             //)>))))))  .-~))))>>)>\n"+
                        "              ).@(@@               //))>>))) .-~))>>)))))>>)>\n"+
                        "            (( @.@).              //))))) .-~)>>)))))>>)>\n"+
                        "          ))  )@@*.@@ )          //)>))) //))))))>>))))>>)>\n"+
                        "       ((  ((@@@.@@             |/))))) //)))))>>)))>>)>\n"+
                        "      )) @@*. )@@ )   (\\_(\\-\\b  |))>)) //)))>>)))))))>>)>\n"+
                        "    (( @@@(.@(@ .    _/`-`  ~|b |>))) //)>>)))))))>>)>\n"+
                        "     )* @@@ )@*     (@)  (@) /\\b|))) //))))))>>))))>>\n"+
                        "   (( @. )@( @ .   _/  /    /  \\b)) //))>>)))))>>>_._\n"+
                        "    )@@ (@@*)@@.  (6///6)- / ^  \\b)//))))))>>)))>>   ~~-.\n"+
                        " ( @jgs@@. @@@.*@_ VvvvvV//  ^  \\b/)>>))))>>      _.     `bb\n"+
                        " ((@@ @@@*.(@@ . - | o |' \\ (  ^   \\b)))>>        .'       b`,\n"+
                        "   ((@@).*@@ )@ )   \\^^^/  ((   ^  ~)_        \\  /           b `,\n"+
                        "     (@@. (@@ ).     `-'   (((   ^    `\\ \\ \\ \\ \\|             b  `.\n"+
                        "       (*.@*              / ((((        \\| | |  \\       .       b `.\n"+
                        "                         / / (((((  \\    \\ /  _.-~\\     Y,      b  ;\n"+
                        "                        / / / (((((( \\    \\.-~   _.`\" _.-~`,    b  ;\n"+
                        "                       /   /   `(((((()    )    (((((~      `,  b  ;\n"+
                        "                     _/  _/      `\"\"\"/   /'                  ; b   ;\n"+
                        "                 _.-~_.-~           /  /'                _.'~bb _.'\n"+
                        "               ((((~~              / /'              _.'~bb.--~\n"+
                        "                                  ((((          __.-~bb.-~\n"+
                        "                                              .'  b .~~\n"+
                        "                                              :bb ,' \n"+
                        "                                              ~~~~\n");
    }

    /**
     * This help-method prints the Treasure-Item
     */
    private void printTreasure() {
        System.out.println(
                "                  _.--.\n"+
                        "              _.-'_:-'||\n"+
                        "          _.-'_.-::::'||\n"+
                        "     _.-:'_.-::::::'  ||\n"+
                        "   .'`-.-:::::::'     ||\n"+
                        "  /.'`;|:::::::'      ||_\n"+
                        " ||   ||::::::'      _.;._'-._\n"+
                        " ||   ||:::::'   _.-!oo @.!-._'-.\n"+
                        " \'.  ||:::::.-!() oo @!()@.-'_.||\n"+
                        "   '.'-;|:.-'.&$@.& ()$%-'o.'\\U||\n"+
                        "     `>'-.!@%()@'@_%-'_.-o _.|'||\n"+
                        "      ||-._'-.@.-'_.-' _.-o  |'||\n"+
                        "      ||=[ '-._.-\\U/.-'    o |'||\n"+
                        "      || '-.]=|| |'|      o  |'||\n"+
                        "      ||      || |'|        _| ';\n"+
                        "      ||      || |'|    _.-'_.-'\n"+
                        "      |'-._   || |'|_.-'_.-'\n"+
                        "      '-._'-.|| |' `_.-'\n"+
                        "           '-.||_/.-'\n");
    }
}
