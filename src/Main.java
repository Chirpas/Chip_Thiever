//osbot libraries.
import org.osbot.rs07.api.filter.ActionFilter;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.api.model.NPC;

//for graphics/paint/ui
import java.awt.*;
import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ScriptManifest(
        author = "Chirpas",
        version = 1.00,
        info = "memey thiever",
        logo = "",
        name = "Chip Thiever"
)

public class Main extends Script
{
    //create handle for script
    private Handler stateHandler;

    //GUI selection variable
    private GUI gui;


    private List<NPC> npcList = null;
    private List<String> npcNames = new ArrayList<>();
    private Set<String> tmp = new HashSet<>();

    //main selection variable
    public String pikTarget = null;
    private Banking banking = new Banking(this);


    @Override
    public void onStart()
    {
        //get list of closest npcs with pickpocket option
        npcList = getNpcs().filter(new ActionFilter<>("Pickpocket"));
        for(int i = 0; i < npcList.size(); i++)
        {
            if(npcList.get(i) != null)
            {
                npcNames.add(npcList.get(i).getName());
            }
        }
        //remove duplicate strings
        tmp.addAll(npcNames);
        npcNames.clear();
        npcNames.addAll(tmp);


        try
        {
            SwingUtilities.invokeAndWait(() -> {
                gui = new GUI(npcNames);
                gui.open();
            });
        } catch (InterruptedException | InvocationTargetException e) {
            e.printStackTrace();
            stop();
            return;
        }

        if (!gui.isStarted())
        {
            stop();
            return;
        }
        //get variable from gui.
        pikTarget = gui.getNpc();

        //create thieving handle
        stateHandler = new ThieveHandle(this, pikTarget, banking);
    }

    @Override
    public int onLoop() throws InterruptedException
    {

        if(stateHandler.isStopped())
        {
            log("StateHandler has stopped!");
            stop();
        }
        else
        {
            stateHandler.handleNextState();
        }

        return 200;
    }

    @Override
    public void onExit()
    {
        if (gui != null)
        {
            gui.close();
        }
    }

    public void onPaint(Graphics2D g)
    {

    }
}
