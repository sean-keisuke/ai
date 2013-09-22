package source;

import java.util.HashSet;
import java.util.Stack;

public class SearchFrontierStack implements SearchFrontierStorage<EightPuzzleState>
{

	Stack<EightPuzzleState> stack = new Stack<EightPuzzleState>();
	HashSet<EightPuzzleState> seen = new HashSet<EightPuzzleState>();

	@Override
	public boolean isEmpty()
	{
		return stack.isEmpty();
	}

	@Override
	public void add(EightPuzzleState s)
	{
		if (seen.add(s))
		{
			stack.push(s);	
		}
	}

	@Override
	public EightPuzzleState next()
	{
		return stack.pop();
	}

	public void clear()
	{
		seen.clear();
		stack.clear();
	}
}
