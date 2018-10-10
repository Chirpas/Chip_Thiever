
import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.utility.ConditionalSleep;
import org.osbot.rs07.api.model.RS2Object;


import java.util.Random;


public class ThieveHandle extends Handler
{
    private Random r = new Random();

    private String target = null;
    private Area thieveArea = null;
    private Area startArea = null;
    private Banking banking = null;

    private String food = "Shrimps";
    private String food1 = "Anchovies";
    private String food2 = "Tuna";

    //construct Handle object, and accept thieving target
    public ThieveHandle(MethodProvider methodProvider, String target, Banking banking)
    {
        super(methodProvider);
        this.target = target;

        //get area of current thieve location
        this.thieveArea = api.myPlayer().getArea(10);
        this.startArea = api.myPlayer().getArea(2);
        this.banking = banking;
    }

    private enum State
    {
        BANKING,
        WALKING,
        LOWHP,
        ANIMATING,
        STEALING
    }

    public State getState()
    {
        boolean containsfood = !((api.inventory.contains(food)) || (api.inventory.contains(food1)) || (api.inventory.contains(food2)));
        //if no food in invent and low on hp, bank
        if((containsfood && (api.skills.getDynamic(Skill.HITPOINTS)) < api.random(13,18)) || (containsfood && (api.inventory.getEmptySlotCount() == 0)))
        {
            return State.BANKING;
        }
        //if outside of thieve area, move to center point
        else if(!thieveArea.contains(api.myPlayer()))
        {
            return State.WALKING;
        }
        else if((api.skills.getDynamic(Skill.HITPOINTS)) < api.random(10,14))
        {
            return State.LOWHP;
        }
        else if(api.myPlayer().isAnimating())
        {
            return State.ANIMATING;
        }
        return State.STEALING;
    }

    //Main control sequence
    public void handleNextState() throws InterruptedException
    {
        //var to hold current bot state
        State state = getState();

        //perform actions depending on state.
        switch(state)
        {
            case BANKING:
                api.log("Banking...");
                banking.runToBank(banking.getClosestBank());
                if(api.bank.open())
                {
                    new ConditionalSleep(2000) {
                        @Override
                        public boolean condition() throws InterruptedException {
                            return api.bank.isOpen();
                        }
                    }.sleep();
                }
                if(api.bank.isOpen())
                {
                    api.bank.depositAll();
                    api.sleep(api.random(550,750));
                    if(api.bank.contains(food))
                    {
                        api.bank.withdraw(food, 25);
                    }
                    else if(api.bank.contains(food1))
                    {
                        api.bank.withdraw(food1, 25);
                    }
                    else if(api.bank.contains(food2))
                    {
                        api.bank.withdraw(food2, 25);
                    }
                    else
                    {
                        api.log("No more food left!");
                        stop();
                    }

                    api.sleep(api.random(550,750));
                    api.bank.close();
                    api.sleep(api.random(550,750));
                }
                break;

            case WALKING:
                api.getWalking().webWalk(startArea);
                break;

            case LOWHP:
                if(api.inventory.contains(food))
                {
                    api.inventory.getItem(food).interact();
                }
                else if(api.inventory.contains(food1))
                {
                    api.inventory.getItem(food1).interact();
                }
                else if(api.inventory.contains(food2))
                {
                    api.inventory.getItem(food2).interact();
                }
                api.sleep(api.random(750, 1100));

                break;

            case ANIMATING:
                api.sleep(100);
                break;

            case STEALING:
                //unpack coinbags if there are too many
                if(api.inventory.getAmount("Coin pouch") > api.random(18,27))
                {
                    api.inventory.getItem("Coin pouch").interact();
                    api.sleep(api.random(650,712));
                }

                //get closest npc
                NPC targetNpc = api.getNpcs().closest(o->o.getName().equals(target));
                if(targetNpc != null && api.map.canReach(targetNpc))
                {
                    if(api.myPlayer().getHeight() < 210)
                    {
                        targetNpc.interact("Pickpocket");
                        //wait until moved to target
                        new ConditionalSleep(2000) {
                            @Override
                            public boolean condition() throws InterruptedException {
                                return api.myPlayer().isMoving();
                            }
                        }.sleep();
                        new ConditionalSleep(5000) {
                            @Override
                            public boolean condition() throws InterruptedException {
                                return !api.myPlayer().isMoving();
                            }
                        }.sleep();
                        api.sleep(randomGauss(601,75));
                    }
                    else
                    {
                        api.log("You are stunned!");
                        api.sleep(300);
                    }
                }
                else if(targetNpc != null && !api.map.canReach(targetNpc))
                {
                    //target npc is blocked attempt to interact with a door.
                    RS2Object door = api.getObjects().closest("Door");
                    if (door != null && door.hasAction("Open"))
                    {
                        door.interact("Open");
                        new ConditionalSleep(5000) {
                            @Override
                            public boolean condition() throws InterruptedException {
                                return door.hasAction("Close");
                            }
                        }.sleep();
                    }
                }
                else
                {
                    api.log("Target not found!");
                }
                break;
        }
    }


    public int randomGauss(int mean, int std)
    {
        return (int)(r.nextGaussian()*std) + mean;
    }

}
