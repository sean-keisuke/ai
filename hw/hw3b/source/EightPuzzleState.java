package source;

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
	
	@Override
	public String traverseFullList(String sequence)
	{
		if (this.parent == null)
		{
			return sequence = this.toString() + "\n" + sequence;
		}
		sequence = this.toString() + "\n" + sequence;
		return this.parent.traverseFullList(sequence);
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
	
	@Override
	public String toString()
	{
		return "<Depth:" + depth + ">\n" +
				board.charAt(0) + " " + board.charAt(1) + " " + board.charAt(2) + "\n" +
				board.charAt(3) + " " + board.charAt(4) + " " + board.charAt(5) + "\n" +
				board.charAt(6) + " " + board.charAt(7) + " " + board.charAt(8) + "\n";
	}

	@Override
	public int hashCode()
	{
		return board.hashCode();
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof EightPuzzleState)
		{
			EightPuzzleState that = (EightPuzzleState)obj;
			return this.board.equals(that.board);
		}
		return false;
	}
}

