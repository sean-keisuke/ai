package source;

import java.util.Stack;

public class SearchFrontierStack implements SearchFrontierStorage<EightPuzzleState>
{
   Stack<EightPuzzleState> stack = new Stack<>();
   
   @Override
   public boolean isEmpty()
   {
       return stack.isEmpty();
   }
   
   @Override
   public void add(EightPuzzleState s)
   {
       stack.push(s);
   }

   @Override
   public EightPuzzleState next()
   {
       return stack.pop();
   }
   
   public void clear()
   {
	   stack.clear();
   }
}
