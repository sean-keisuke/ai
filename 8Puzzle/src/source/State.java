package source;

public interface State
{
    /** returns a list of available actions determined by the state */
    public Action[] getAvailableActions();
    public boolean isGoal();
    public boolean canActOn();
//	public String traverseFullList();
}


