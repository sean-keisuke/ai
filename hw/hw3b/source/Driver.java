package source;

public abstract class Driver
{
    protected State initial;
	protected SearchFrontierStorage store;

    public Driver(State initial)
    {
        this.initial=initial;
    }

    public int search ()
    {
        store.add(initial);
        State s;
        while(!store.isEmpty())
        {
            s = store.next(); 
            if(s.isGoal())
            {
                //System.out.println(s.traverseFullList()); Won't compile
                return 0;
            }
            else if(s.canActOn())
            {
                Action[] list = s.getAvailableActions();
                for(Action a: list)
                {
                    store.add(a.updateState(s)); 
                }
            }
        }
        return -1;
    }
}

