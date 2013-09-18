import java.util.Stack;
import source.Action;
import source.EightPuzzleState;
import source.SearchFrontierStack;

public class EightPuzzleDriver
{
	EightPuzzleState initial;
	SearchFrontierStack store;
	
	public EightPuzzleDriver(EightPuzzleState initial)
	{
		this.initial = initial;
		store = new SearchFrontierStack();
	}
	
    public boolean search (int depthLimit)
    {
		initial.depth = 0;
		initial.parent = null;
        store.add(initial);
        EightPuzzleState s;
		
        while(!store.isEmpty())
        {
            s = store.next(); 
	
            if(s.isGoal())
            {
                s.traverseFullList(new Stack<String>()); 
                return true;
            }
            if(s.canActOn())
            {
				if (s.depth < depthLimit)
				{
					Action<EightPuzzleState>[] list = s.getAvailableActions();
					for(Action<EightPuzzleState> a: list)
					{
						EightPuzzleState newState = a.updateState(s);
						newState.setDepth(s.depth + 1);
						newState.setParent(s);
						store.add(newState); 
					}
				}
            }
        }
		searchFail();
		return false;
    }
	
	public void searchFail()
	{
		store.clear();
	}
	
	public static void main(String[] args)
	{
		EightPuzzleDriver driver = new EightPuzzleDriver(new EightPuzzleState(" 23146758"));
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		{
			if (driver.search(i))
			{
				System.out.println("Win");
				System.exit(0);
			}
		}
		
	}
}