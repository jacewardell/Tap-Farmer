//Total time on this class so far: 2 hours.

package com.sadostrich.tapfarmer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The main game object for Tap Farmer.
 */
public class Game
{
    private static Game _instance = null;

    //The total amount of coins a user has
    private double total;
    //Total coins earned by user
    private double runningTotal;
    //Total coins spent by user
    private double totalSpent;
    //time since last save
    private int saveTime;
    //The coins per second a user makes
    private double cps;
    //The amount one tap of the pig is worth
    private int tap;
    //Should be assigned to main activity
    private CpsListener cpsListener;
    //The list of items.
    ArrayList<Item> items;

    //Achievements
    private ArrayList<Achievement> unlockedAchievements;
    private ArrayList<Achievement> lockedAchievements;


    // --- Trivial vars --- //
    private int totalTaps;

    public Game()
    {
        total = 0;
        runningTotal = total;
        cps = 0; //5 for testing purposes
        tap = 1;
        saveTime = 0;

        items = new ArrayList<Item>();

        //Set up achievements
        unlockedAchievements = new ArrayList<Achievement>();
        lockedAchievements = new ArrayList<Achievement>();

        //items
        setUpItems();
        //Achievements
        setUpAchievements();

        //CPS Timer
        Timer cpsTimer = new Timer();
        cpsTimer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run() //Every second increment the total by the CPS.
            {
                total = total + cps;
                runningTotal += cps;
                cpsListener.TimerTick();

                //for debug
                //System.out.println("Tick - Total is: " + total);

                checkAchievements();

                if(saveTime >= 20) //save the game every 20 seconds.
                {
                    cpsListener.SaveGame();
                    saveTime = 0;
                }
                else
                {
                    saveTime++;
                }

            }
        }, 0, 1000);

        //set up trivial vars.
        totalTaps = 0;
    }

    public static synchronized Game getInstance()
    {
        if(_instance == null)
            _instance = new Game();
        return _instance;
    }

    public void loadFromSave(SavedGame save)
    {
        total = save.total;
        runningTotal = save.runningTotal;
        totalSpent = save.totalSpent;
        cps = save.cps;
        items = save.items;
        totalTaps = save.totalTaps;
        unlockedAchievements = save.unlockedAchievements;
        lockedAchievements = save.lockedAchievements;

        System.out.println("Game Loaded!");
    }

    // ---- Begin Helper Methods ----//

    /** Returns the total number of coins in the bank **/
    public double getTotal()
    {
        return total;
    }

    /** Returns the running total for the game **/
    public double getRunningTotal() { return runningTotal; }

    /** Returns the total coins spent for the game **/
    public double getTotalSpent() { return totalSpent; }

    /** Returns the cps for the game **/
    public double getCps()
    {
        return cps;
    }

    /** Increments the total by the tap amounts
     * and returns the new total back **/
    public double tap()
    {
        total = total + tap;
        runningTotal += tap;
        totalTaps++;

        //check  achievements
        checkAchievements();


        return total;
    }

    /** Set the new tap value **/
    public void setTap(int newTap)
    {
        tap = newTap;
    }

    /** Set the coins per second
     * @param newCps**/
    public void setCps(double newCps)
    {
        cps = newCps;
    }

    /** Call this to decrement total coins upon a purchase. Returns the new total
     * This may need to be refactored based upon how we decide to handle purchases.**/
    public double purchase(int cost, double cpsBonus)
    {
        total = total - cost;
        totalSpent += cost;
        cps = cps + cpsBonus;
        checkAchievements();

        cpsListener.TimerTick();


        return total;
    }


    /** Set this to main activity to automate CPS **/
    public void setCpsListener(CpsListener listener)
    {
        cpsListener = listener;
    }

    /** Returns the list of items for sale **/
    public ArrayList<Item> getItems()
    {
        return items;
    }

    public ArrayList<Achievement> getUnlockedAchievements()
    {
        return unlockedAchievements;
    }

    public ArrayList<Achievement> getLockedAchievements()
    {
        return lockedAchievements;
    }


    /** Resets game by erasing all data structures **/
    public void resetGame()
    {
        total = 0;
        runningTotal = 0;
        totalSpent = 0;
        totalTaps = 0;
        cps = 0; //5 for testing purposes
        tap = 1;

        items = new ArrayList<Item>();

        //Set up achievements
        unlockedAchievements = new ArrayList<Achievement>();
        lockedAchievements = new ArrayList<Achievement>();

        //items
        setUpItems();
        //Achievements
        setUpAchievements();
    }


    public int getTotalTaps()
    {
        return totalTaps;
    }

    private void setUpAchievements()
    {
        //Tap Achievements
        lockedAchievements.add(new TapAchievement("Wahoo isn't this fun?!", "Tap the piggy bank one time", 1, 1));
        lockedAchievements.add(new TapAchievement("Now you're getting it!", "Tap the piggy bank 100 times", 100, 1));
        lockedAchievements.add(new TapAchievement("Addicting isn't it?", "Tap the piggy bank 1,000 times", 1000, 1));
        lockedAchievements.add(new TapAchievement("How is your finger feeling?", "Tap the piggy bank 10,000 times.", 10000, 1));

        //Item Achievements
        for(Item i : items)
        {
            lockedAchievements.add(new ItemCountAchievement("So many " + i.getName() + "s", "Buy 100 " + i.getName() + "'s", i.getName()));
        }

        //Coin achievements
        lockedAchievements.add(new CoinAchievement("You should frame this", "Received after making your first coin.", 1));
        lockedAchievements.add(new CoinAchievement("Don't spend it all at once!", "Make 1,000 coins.", 1000));
        lockedAchievements.add(new CoinAchievement("Money Bags!", "Make your first million coins", 1000000));
        lockedAchievements.add(new CoinAchievement("If only this was real money...", "Make a trillion coins!", 1000000000));
    }

    private void setUpItems()
    {
        Upgrade up1;
        Upgrade up2;
        Upgrade up3;

        //Corn item and upgrades
        {
            up1 = new Upgrade("Corn Shucker", "Shucks corn in preparation for further processing\n\n10% increase to \"Corn Plot\" cps", 1000, .10);
            up2 = new Upgrade("Corn Scraper", "Scrapes the corn off the cob for sell in cans\n\n10% increase to \"Corn Plot\" cps", 1000000, .15);
            up3 = new Upgrade("Corn Harvester", "Harvests corn more effectively\n\n10% increase to \"Corn Plot\" cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item cornPlot = new Item("Corn Plot", "#1 grown crop in America\n\n + 0.1 cps", 15, 0.1, R.drawable.corn_icon, upgradeQueue);
            cornPlot.setHasUpgrade(true);
            items.add(cornPlot);
        }

        //Apple orchard item and upgrades
        {
            up1 = new Upgrade("Apple Corer", "Gets those poisonous seeds out by coring the apple\n\n10% increase to \"Apple Orchard\" cps", 1000, .10);
            up2 = new Upgrade("Apple Slicer", "One of those really cool ones that spins the apple, except this one is industrial\n\n10% increase to \"Apple Orchard\" cps", 1000000, .15);
            up3 = new Upgrade("Apple Juicer", "Extracts the delicious life water from the apple\n\n10% increase to \"Apple Orchard\"  cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item appleOrchard = new Item("Apple Orchard", "Don't get hit on the head while taking a nap\n\n+0.5 cps", 100, 0.5, R.drawable.apple_orchard_icon, upgradeQueue);
            appleOrchard.setHasUpgrade(true);
            items.add(appleOrchard);
        }

        //Chicken item and upgrades
        {
            up1 = new Upgrade("Chicken Coup", "Too bad you can't hire someone to keep this smelling better\n\n10% increase to \"Chicken\" cps", 1000, .10);
            up2 = new Upgrade("Better Chicken Feed", "Equivalent to a five star meal, except for chickens\n\n10% increase to \"Chicken\" cps", 1000000, .15);
            up3 = new Upgrade("Bigger Chicken Coup", "With this, your chickens would fit in living in Beverly Hills\n\n10% increase to \"Chicken\" cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item chicken = new Item("Chicken", "Obviously tastes like chicken\n\n+4.5 cps", 500, 4.5, R.drawable.chicken_icon, upgradeQueue);
            chicken.setHasUpgrade(true);
            items.add(chicken);
        }

        //Tractor item and upgrades
        {
            up1 = new Upgrade("Plow Attachment", "Like the tractor is essential, so to is a plow attachment\n\n10% increase to \"Tractor\" cps", 1000, .10);
            up2 = new Upgrade("Aerator Attachment", "Aerates your soil so your crop has some stretching room for its roots\n\n10% increase to \"Tractor\" cps", 1000000, .15);
            up3 = new Upgrade("Fertilizer Attachment", "It flings manure everywhere\n\n10% increase to \"Tractor\" cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item tractor = new Item("Tractor", "Essential to any aspiring farm monopolist\n\n+15 cps", 3000, 15, R.drawable.tractor_icon, upgradeQueue);
            tractor.setHasUpgrade(true);
            items.add(tractor);
        }

        //Carrot item and upgrades
        {
            up1 = new Upgrade("Carrot Peeler", "Peels carrots better than that cheap peeler from Walmart\n\n10% increase to \"Carrot Plot\" cps", 1000, .10);
            up2 = new Upgrade("Carrot Slicer", "Slices carrots, which somehow increases their value substantially\n\n10% increase to \"Carrot Plot\" cps", 1000000, .15);
            up3 = new Upgrade("Carrot Harvester", "Harvests carrots more effectively\n\n10% increase to \"Carrot Plot\" cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item carrot = new Item("Carrot Plot", "Eat enough and they'll turn your skin orange\n\n+55 cps", 10000, 55, R.drawable.carrot_icon, upgradeQueue);
            carrot.setHasUpgrade(true);
            items.add(carrot);
        }

        //Sheep item and upgrades
        {
            up1 = new Upgrade("Wool Shears", "These work better than normal scissors\n\n10% increase to \"Sheep\" cps", 1000, .10);
            up2 = new Upgrade("Sheep Dog", "Make them run around herding, so you don't have to\n\n10% increase to \"Sheep\" cps", 1000000, .15);
            up3 = new Upgrade("Shepherd", "Your empire's too big to worry about each individual sheep...just hire a shepherd\n\n10% increase to \"Sheep\" cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item carrot = new Item("Sheep", "Little Bo-Peek lost her sheep, we found it, and didn't give it back\n\n+154.5 cps", 40000, 154.5, R.drawable.sheep_icon, upgradeQueue);
            carrot.setHasUpgrade(true);
            items.add(carrot);
        }

        //Peach orchard item and upgrades
        {
            up1 = new Upgrade("Peach Pitter", "Prevents broken teeth and expensive dental appointments\n\n10% increase to \"Peach Orchard\" cps", 1000, .10);
            up2 = new Upgrade("Peach Canner", "This way you can have delicious peaches all year round\n\n10% increase to \"Peach Orchard\" cps", 1000000, .15);
            up3 = new Upgrade("Peach Harvester", "Harvests peaches more effectively\n\n10% increase to \"Peach Orchard\" cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item peach = new Item("Peach Orchard", "The most delicious fruit ever\n\n+555 cps", 200000, 555, R.drawable.peach_orchard_icon, upgradeQueue);
            peach.setHasUpgrade(true);
            items.add(peach);
        }

        //Baler item and upgrades
        {
            up1 = new Upgrade("Hand Baler", "This unlucky person could be hire by you to bale hay by hand\n\n10% increase to \"Baler\" cps", 1000, .10);
            up2 = new Upgrade("Mechanical Baler", "The hand baler would be thankful to simply drive this\n\n10% increase to \"Baler\" cps", 1000000, .15);
            up3 = new Upgrade("Tighter Bales", "New technology that forms tighter bales, thus allowing more hay in a bale\n\n10% increase to \"Baler\" cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item baler = new Item("Baler", "Packs hay together so your cows have something to eat\n\n+7200 cps", 1555000, 7200, R.drawable.baler_icon, upgradeQueue);
            baler.setHasUpgrade(true);
            items.add(baler);
        }

        //Potato item and upgrades
        {
            up1 = new Upgrade("Potato Peeler", "Peels potatoes for people who don't like skin on their potatoes\n\n10% increase to \"Potato Plot\" cps", 1000, .10);
            up2 = new Upgrade("Potato Slicer", "Slices potatoes because who doesn't like potatoes sliced?\n\n10% increase to \"Potato Plot\" cps", 1000000, .15);
            up3 = new Upgrade("Potato Harvester", "Harvests potatoes more effectively\n\n10% increase to \"Potato Plot\" cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item potato = new Item("Potato Plot", "If you don't like them, you're not human\n\n+105985 cps", 123456789, 105985, R.drawable.potato_icon, upgradeQueue);
            potato.setHasUpgrade(true);
            items.add(potato);
        }

        //Strawberry field item and upgrades
        {
            up1 = new Upgrade("Strawberry Pickers", "Tired of picking those strawberries all by yourself? Why not hire some people?\n\n10% increase to \"Strawberry field\" cps", 1000, .10);
            up2 = new Upgrade("Mechanical Strawberry Picker", "Are your \"Strawberry Pickers\" tired of picking all the strawberries by themselves? Why not buy this?\n\n10% increase to \"Strawberry field\" cps", 1000000, .15);
            up3 = new Upgrade("Strawberry Slicer", "Because you see pre-sliced strawberries all the time, right?\n\n10% increase to \"Strawberry field\" cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item potato = new Item("Strawberry field", "Tasty on pancakes, in jams, and anywhere else you can think to put them\n\n+1100005 cps", 500000000, 1100005, R.drawable.strawberry_field_icon, upgradeQueue);
            potato.setHasUpgrade(true);
            items.add(potato);
        }

        //Cow item and upgrades
        {
            up1 = new Upgrade("Fences", "Keeps cows in, but doesn't really keep things out\n\n10% increase to \"Cow\" cps", 1000 , .10);
            up2 = new Upgrade("Cow Milker", "Your hands are bound to get tired, so you should buy one of these\n\n10% increase to \"Cow\" cps", 1000000, .15);
            up3 = new Upgrade("Herding Horses", "Why herd on foot, when you could herd on horse?\n\n10% increase to \"Cow\"  cps", 1000000000, .20);
            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
            upgradeQueue.add(up1);
            upgradeQueue.add(up2);
            upgradeQueue.add(up3);
            Item cow = new Item("Cow", "Moooooooooooooooooooo\n\n+11000059 cps", 900000000, 11000059, R.drawable.cow_icon, upgradeQueue);
            cow.setHasUpgrade(true);
            items.add(cow);
        }

        //Seeder item and upgrades
//        {
//            up1 = new Upgrade("Seeder v2000", "Plants seeds faster\n\n10% increase to \"Seeder\" cps", 99, .10);
//            up2 = new Upgrade("Seeder v3000", "Plants seeds really fast\n\n10% increase to \"Seeder\" cps", 999, .10);
//            up3 = new Upgrade("Seeder v4000", "Plants seeds at an insanely fast rate\n\n10% increase to \"Seeder\"  cps", 9999, .10);
//            Queue<Upgrade> upgradeQueue = new LinkedList<Upgrade>();
//            upgradeQueue.add(up1);
//            upgradeQueue.add(up2);
//            upgradeQueue.add(up3);
//            Item seeder = new Item("Seeder", "Kind of like Johnny Appleseed except in robot form\n\n+5 cps", 20, 5, R.drawable.seeder_icon, upgradeQueue);
//            seeder.setHasUpgrade(true);
//            items.add(seeder);
//        }
    }

    private void checkAchievements()
    {
        //System.out.println("Checking achievements");
        for(int i = 0; i < lockedAchievements.size(); i++)
        {
            if(lockedAchievements.get(i).completed())
            {
                unlockedAchievements.add(lockedAchievements.get(i));
                lockedAchievements.remove(i);
            }
        }
        //System.out.println("Done checking");
    }

//end class
}
