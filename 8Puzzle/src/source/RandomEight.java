
import java.util.Random;
import source.Action;
import source.EightPuzzleState;

public class RandomEight
{
    public static void main(String[] args)
    {
       Random r = new Random();
       int ranMoves = Integer.parseInt(args[0]);
       EightPuzzleState s = new EightPuzzleState("12345678 ");
       for(int i=0;i<ranMoves;i++)
       {
           Action[] actions = s.getAvailableActions();
           s = (EightPuzzleState)(actions[r.nextInt(actions.length)].updateState(s));
       }

       System.out.print(s);
    }
}
