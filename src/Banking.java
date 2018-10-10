import java.util.List;

import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.Position;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.NPC;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.script.MethodProvider;
import org.osbot.rs07.script.Script;


public class Banking
{

    private Script script;

    /**
     * Creates a new Banking Class
     * @param s - script (this)
     */

    public Banking(Script s)
    {
        this.script = s;
    }


    /**
     * Utilizes the webWalk class to walk to an area
     *
     * @param bank - An area returned by the getClosestBank() method
     * @return True if we arrived in the bank, otherwise false
     */
    public boolean runToBank(Area bank)
    {
        this.script.walking.webWalk(bank.getRandomPosition());
        if(bank.contains(this.script.myPlayer()))
        {
            return true;
        }
        else
        { // @Standard Use of a consistant bracing style is important to aid readability of code.
          // This else statement did net have brackets before.
            return false;
        }
    }

    /**
     * Gets the closest bank from your position and returns that Area object<br>
     * @return Area
     */

    public Area getClosestBank()
    {
        Area closestBank = null;
        // @Standard Since this is a constant array ( in that it does not change
        // between function calls ), you probably should make it a private class field.
        // Having it in this function is cluttering to the code.
        Area[] banks =
                {
                        Banks.AL_KHARID,
                        Banks.DRAYNOR,
                        Banks.EDGEVILLE,
                        Banks.FALADOR_EAST,
                        Banks.FALADOR_WEST,
                        Banks.VARROCK_EAST,
                        Banks.VARROCK_WEST,
                        Banks.CAMELOT,
                        Banks.CATHERBY,
                        Banks.ARDOUGNE_NORTH,
                        Banks.ARDOUGNE_SOUTH,
                        Banks.CANIFIS,
                        Banks.CASTLE_WARS,
                        Banks.DUEL_ARENA,
                        Banks.GNOME_STRONGHOLD,
                };
        int l = banks.length;
        if(this.script.myPlayer() != null)
        {
            Position p = this.script.myPosition();
            for(int i = 0; i < l; i ++)
            {
                Area currBank = banks[i];
                // @Standard again, consistant bracing style is important
                if(closestBank == null)
                    closestBank = currBank;
                else
                if(currBank.getRandomPosition().distance(p) < closestBank.getRandomPosition().distance(p))
                    closestBank = currBank;
            }
        }
        return closestBank;
    }

    public void depositAllItems()
    {
        this.script.bank.depositAll();
    }


}
