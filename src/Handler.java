
import org.osbot.rs07.script.MethodProvider;

public abstract class Handler
{

    protected MethodProvider api;
    protected String status;
    protected boolean stopped;


    public Handler(MethodProvider methodProvider)
    {
        // @Standard your status should probably be an Enum so that you have defined
        // states, instead of allowing any string to be a status.
        status = "Initializing";
        api = methodProvider;
    }

    public abstract void handleNextState() throws InterruptedException;

    //@Standard
    // Since you are using a status variable and a stopped variable,
    // it could be considered to put all these into one Enum.
    public void start()
    {
        stopped = false;
    }

    public void stop()
    {
        stopped = true;
    }

    public String getStatus()
    {
        return status;
    }

    public boolean isStopped()
    {
        return stopped;
    }
}
