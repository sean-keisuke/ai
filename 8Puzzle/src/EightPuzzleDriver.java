import java.util.regex.Pattern;
import source.Action;
import source.EightPuzzleState;
import source.SearchFrontierStack;

public class EightPuzzleDriver
{
	EightPuzzleState initial;
	SearchFrontierStack store;
	static int numExpanded = 0;
	
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
			++numExpanded;
			if(numExpanded%500==0) System.out.print("\rNumExpanded = " + numExpanded);
            if(s.isGoal())
            {
                System.out.println("\n" + s.traverseFullList(""));
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
		store.clear();
		return false;
    }
	
	public static void main(String[] args)
	{
		Pattern pattern = Pattern.compile("[ 1-8]{9}");
		String puzzle = args[0];
		if (puzzle.length() != 9 || !pattern.matcher(puzzle).matches())
		{
			throw new NullPointerException("The puzzle you provided was not valid");
		}
		EightPuzzleDriver driver = new EightPuzzleDriver(new EightPuzzleState(puzzle));
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		{
			if (driver.search(i))
			{
				System.out.println("Nodes expanded: " + numExpanded);
				System.exit(0);
			}
		}
	}
}