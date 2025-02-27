/**
 * Hunter Class<br /><br />
 * This class represents the treasure hunter character (the player) in the Treasure Hunt game.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Hunter {
    //instance variables
    private String hunterName;
    private String[] kit;
    private String[] playersTreasures = new String[3];
    private int treasuresIdx = 0;
    private int gold;

    /**
     * The base constructor of a Hunter assigns the name to the hunter and an empty kit.
     *
     * @param hunterName The hunter's name.
     * @param startingGold The gold the hunter starts with.
     */
    public Hunter(String hunterName, int startingGold) {
        this.hunterName = hunterName;
        kit = new String[7]; // only 7 possible items can be stored in kit
        gold = startingGold;
    }

    public void changeKitSize() {
        kit = new String[8];
    }
    //Accessors
    public String getHunterName() {
        return hunterName;
    }

    /**
     * Updates the amount of gold the hunter has.
     *
     * @param modifier Amount to modify gold by.
     */
    public void changeGold(int modifier) {
        gold += modifier;

    }

    /**
     * Buys an item from a shop.
     *
     * @param item The item the hunter is buying.
     * @param costOfItem The cost of the item.
     * @return true if the item is successfully bought.
     */
    public boolean buyItem(String item, int costOfItem) {
        if (!checkIfHasSword()) {
            if (costOfItem == -1 || gold < costOfItem || hasItemInKit(item)) {
                return false;
            }
        } else {
            if (hasItemInKit(item)) {
                return false;
            }
        }
        if (!checkIfHasSword()) {
            gold -= costOfItem;
        }
        addItem(item);
        return true;
    }

    /**
     * The Hunter is selling an item to a shop for gold.<p>
     * This method checks to make sure that the seller has the item and that the seller is getting more than 0 gold.
     *
     * @param item The item being sold.
     * @param buyBackPrice the amount of gold earned from selling the item
     * @return true if the item was successfully sold.
     */
    public boolean sellItem(String item, int buyBackPrice) {
        if (buyBackPrice <= -1 || !hasItemInKit(item)) {
            return false;
        }
        gold += buyBackPrice;
        removeItemFromKit(item);
        return true;
    }

    /**
     * Removes an item from the kit by setting the index of the item to null.
     *
     * @param item The item to be removed.
     */
    public void removeItemFromKit(String item) {
        int itmIdx = findItemInKit(item);

        // if item is found
        if (itmIdx >= 0) {
            kit[itmIdx] = null;
        }
    }

    /**
     * Checks to make sure that the item is not already in the kit.
     * If not, it assigns the item to an index in the kit with a null value ("empty" position).
     *
     * @param item The item to be added to the kit.
     * @return true if the item is not in the kit and has been added.
     */
    private boolean addItem(String item) {
        if (!hasItemInKit(item)) {
            int idx = emptyPositionInKit();
            kit[idx] = item;
            return true;
        }
        return false;
    }

    /**
     * Checks if the kit Array has the specified item.
     *
     * @param item The search item
     * @return true if the item is found.
     */
    public boolean hasItemInKit(String item) {
        for (String tmpItem : kit) {
            if (item.equals(tmpItem)) {
                // early return
                return true;
            }
        }
        return false;
    }

     /**
     * Returns a printable representation of the inventory, which
     * is a list of the items in kit, with a space between each item.
     *
     * @return The printable String representation of the inventory.
     */
    public String getInventory() {
        String printableKit = "";
        String space = " ";

        for (String item : kit) {

            if (item != null) {
                if (item.equals("sword")) {
                    printableKit += Colors.RED + item + Colors.RESET + space;
                } else {
                    printableKit += Colors.PURPLE + item + Colors.RESET + space;
                }
            }
        }
        return printableKit;
    }

    /**
     * @return A string representation of the hunter.
     */
    public String infoString() {
        String str = hunterName + " has " + Colors.YELLOW + gold + Colors.RESET + " gold";
        if (!kitIsEmpty()) {
            str += " and " + getInventory();
        }

        str += "\nTreasure found: ";
        if (!treasureListIsEmpty()) {
            for (String treasure : playersTreasures) {
                if (treasure != null) {
                    str += "a " + Colors.BLUE + treasure + Colors.RESET + " ";
                }
            }
        } else {
            str += "none";
        }
        return str;
    }

    public void addTreasure(String treasure) {
        boolean in = false;
        for (String treasures : playersTreasures) {
            if (treasure.equals(treasures)) {
                in = true;
            }
        }
        if (!in) {
            playersTreasures[treasuresIdx] = treasure;
            treasuresIdx++;
        }
        win();
    }

    public boolean hasTreasure(String treasure) {
        for (String treasures : playersTreasures) {
            if (treasures != null && treasures.equals(treasure)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfHasSword() {
        for (String item : kit) {
            if (item != null && item.equals("sword")) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfHasShovel() {
        for (String item : kit) {
            if (item != null && item.equals("shovel")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Searches kit Array for the index of the specified value.
     *
     * @param item String to look for.
     * @return The index of the item, or -1 if not found.
     */
    private int findItemInKit(String item) {
        for (int i = 0; i < kit.length; i++) {
            String tmpItem = kit[i];

            if (item.equals(tmpItem)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if the kit is empty - meaning all elements are null.
     *
     * @return true if kit is completely empty.
     */
    private boolean kitIsEmpty() {
        for (String string : kit) {
            if (string != null) {
                return false;
            }
        }
        return true;
    }

    private boolean treasureListIsEmpty() {
        for (String treasure : playersTreasures) {
            if (treasure != null) {
                return false;
            }
        }
        return true;
    }


    /**
     * Finds the first index where there is a null value.
     *
     * @return index of empty index, or -1 if not found.
     */
    private int emptyPositionInKit() {
        for (int i = 0; i < kit.length; i++) {
            if (kit[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public void win() {
        int count = 0;
        for (String treasure : playersTreasures) {
            if (treasure != null) {
                count++;
            }
        }
        if (count == 3) {
            System.out.println(infoString());
            System.out.println("Congratulations, you have found the last of the three treasures, you win!");
            System.exit(0);
        }
    }

    public void lose(int goldDiff) {
        if (this.gold < 0) {
            System.out.println();
            gold = 0;
            System.out.println(Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET);
            System.out.println("You lost the brawl and pay " + Colors.RED + goldDiff + Colors.RESET + " gold.");
            System.out.println("\n" + "GAME OVER" + "\n" + "You are in gold deficit of " + goldDiff);
            System.out.println();
            System.out.println("Player Stats: \n" + infoString());
            System.exit(0);
        }
    }
}