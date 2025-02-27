import java.awt.*;

/**
 * The Town Class is where it all happens.
 * The Town is designed to manage all the things a Hunter can do in town.
 * This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
 */

public class Town {
    // instance variables
    private Hunter hunter;
    private Shop shop;
    private Terrain terrain;
    private String printMessage;
    private String treasure;
    private boolean searchStatus = false;
    private boolean digStatus = false;
    private boolean toughTown;
    private boolean easy;
    private boolean lookedForTrouble = false;
    private boolean winLose = false;

    /**
     * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
     *
     * @param shop The town's shoppe.
     * @param toughness The surrounding terrain.
     */
    public Town(Shop shop, double toughness, boolean easy) {
        this.shop = shop;
        this.terrain = getNewTerrain();
        this.easy = easy;
        this.treasure = treasure();
        // the hunter gets set using the hunterArrives method, which
        // gets called from a client class
        hunter = null;
        printMessage = "";

        // higher toughness = more likely to be a tough town
        toughTown = (Math.random() < toughness);
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public String getLatestNews() {
        String message = printMessage;
        if (lookedForTrouble) {
            if (winLose) {
                message += "\nYou won the brawl";
            } else {
                message += "\nYou lost the brawl";
            }
        }
        printMessage = "";
        return message;
    }

    public String huntTreasure() {
        if (!searchStatus) {
            searchStatus = true;
            if (this.hunter.hasTreasure(this.treasure)) {
                printMessage = "You already collected a " + this.treasure + " so don't collect it again";
            } else {
                printMessage = "You found a " + this.treasure;
            }
        } else {
            printMessage = "You have already searched this town";
        }
        return treasure;
    }

    public void dig() {
        if (hunter.checkIfHasShovel()) {
            if (!digStatus) {
                double rand = Math.random();
                if (rand < .5) {
                    int amount = (int) (Math.random() * 20) + 1;
                    printMessage = "You dug up " + Colors.YELLOW + amount + Colors.RESET + " gold!";
                    hunter.changeGold(amount);
                } else {
                    printMessage = "You dug but only found dirt";
                }
                digStatus = true;
            } else {
                printMessage = "You already dug for gold in this town";
            }
        } else {
            printMessage = "You can't dig for gold without a shovel";
        }
    }

    /**
     * Assigns an object to the Hunter in town.
     *
     * @param hunter The arriving Hunter.
     */
    public void hunterArrives(Hunter hunter) {
        this.hunter = hunter;
        printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
        if (toughTown) {
            printMessage += "\nIt's pretty rough around here, so watch yourself.";
        } else {
            printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
        }
    }

    /**
     * Handles the action of the Hunter leaving the town.
     *
     * @return true if the Hunter was able to leave town.
     */
    public boolean leaveTown() {
        boolean canLeaveTown = terrain.canCrossTerrain(hunter);
        if (canLeaveTown) {
            String item = terrain.getNeededItem();
            printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
            if (checkItemBreak() && !easy) {
                hunter.removeItemFromKit(item);
                printMessage += "\nUnfortunately, you lost your " + item + ".";
            }
            return true;
        }

        printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
        return false;
    }

    /**
     * Handles calling the enter method on shop whenever the user wants to access the shop.
     *
     * @param choice If the user wants to buy or sell items at the shop.
     */
    public void enterShop(String choice) { printMessage = shop.enter(hunter, choice); }

    /**
     * Gives the hunter a chance to fight for some gold.<p>
     * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
     * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
     */
    public void lookForTrouble() {
        double noTroubleChance;
        if (toughTown) {
            noTroubleChance = 0.66;
        } else {
            noTroubleChance = 0.33;
        }
        if (Math.random() > noTroubleChance) {
            printMessage = "You couldn't find any trouble";
        } else {
            printMessage = Colors.RED + "You want trouble, stranger!  You got it!\nOof! Umph! Ow!\n" + Colors.RESET;
            int goldDiff = (int) (Math.random() * 10) + 1;
            double chance = noTroubleChance;
            if (easy) {
                chance *= 0.75;
            }
            if (hunter.checkIfHasSword()) {
                printMessage += "A legendary samurai? I can't beat you, I'm leavin'!";
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                winLose = true;
                hunter.changeGold(goldDiff);
            } else if (Math.random() > chance) {
                printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
                printMessage += "\nYou won the brawl and receive " + Colors.YELLOW + goldDiff + Colors.RESET + " gold.";
                winLose = true;
                hunter.changeGold(goldDiff);
            } else {
                printMessage += Colors.RED + "That'll teach you to go lookin' fer trouble in MY town! Now pay up!" + Colors.RESET;
                printMessage += "\nYou lost the brawl and pay " + Colors.RED + goldDiff + Colors.RESET + " gold.";
                hunter.changeGold(-goldDiff);
                winLose = false;
                hunter.lose(goldDiff);
            }
            lookedForTrouble = true;
        }
    }

    public String infoString() {
        return "This nice little town is surrounded by " + Colors.CYAN + terrain.getTerrainName() + Colors.RESET + ".";
    }

    /**
     * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
     *
     * @return A Terrain object.
     */
    private Terrain getNewTerrain() {
        double rnd = Math.random();
        if (rnd < ((double) 1/6)) {
            return new Terrain("Mountains", "Rope");
        } else if (rnd < ((double) 2/6)) {
            return new Terrain("Ocean", "Boat");
        } else if (rnd < ((double) 3/6)) {
            return new Terrain("Plains", "Horse");
        } else if (rnd < ((double) 4/6)) {
            return new Terrain("Desert", "Water");
        } else if (rnd < ((double) 5/6)) {
            return new Terrain("Jungle", "Machete");
        } else {
            return new Terrain ("Marsh", "Boots");
        }
    }

    /**
     * Determines whether a used item has broken.
     *
     * @return true if the item broke.
     */
    private boolean checkItemBreak() {
        double rand = Math.random();
        return (rand < 0.5);
    }

    private String treasure() {
        double rand = Math.random();
        if (rand < .25) {
            return "crown";
        } else if (rand < .50) {
            return "trophy";
        } else if (rand < .75) {
            return "gem";
        } else {
            return "dust";
        }
    }
}