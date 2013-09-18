package source;

import java.util.Stack;

public class EightPuzzleState implements State
{
    public String board;
    public int blank;
	public int depth;
	public EightPuzzleState parent;
	
    static Action actionList[][] = 
    {
        { new DownMove(), new RightMove() },
        { new DownMove(), new LeftMove(), new RightMove() },
        { new DownMove(), new LeftMove() },
        { new UpMove(), new RightMove(), new DownMove() },
        { new RightMove(), new UpMove(), new LeftMove(), new DownMove() },
        { new UpMove(), new LeftMove(), new DownMove() },
        { new RightMove(), new UpMove() },
        { new RightMove(), new LeftMove(), new UpMove() },
        { new LeftMove(), new UpMove() }
    };

    public EightPuzzleState(String board)
    {
        this.board=board;
        blank = board.indexOf(' ');
    }

	@Override
    public Action[] getAvailableActions()
    {
       return actionList[blank];
    }
	
	@Override
    public boolean isGoal()
    {
        return "12345678 ".equals(board);
    }
	
	@Override
    public boolean canActOn()
    {
        return true;
    }
	
	public void traverseFullList(Stack<String> sequence)
	{
		if (this.parent == null || this == null)
		{
			System.out.println(sequence);
			return;
		}
		sequence.push(this.toString() + "\n");
		this.parent.traverseFullList(sequence);
	}

	public void setParent(EightPuzzleState parent)
	{
		this.parent = parent;
	}

	public EightPuzzleState getParent()
	{
		return parent;
	}
	
	public void setDepth(int depth)
	{
		this.depth = depth;
	}
	
	public String toString()
	{
		return "<Depth:" + depth + "> " + board;
	}
}

