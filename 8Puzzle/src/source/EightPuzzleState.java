package source;

public class EightPuzzleState implements State
{
    public String board;
    public int blank;
	public int depth;
	
    static Action actionList[][] = 
    {
        { new UpMove(), new LeftMove() },
        { new UpMove(), new LeftMove(), new RightMove() },
        { new UpMove(), new RightMove() },
        { new UpMove(), new LeftMove(), new DownMove() },
        { new RightMove(), new UpMove(), new LeftMove(), new DownMove() },
        { new UpMove(), new RightMove(), new DownMove() },
        { new LeftMove(), new DownMove() },
        { new RightMove(), new LeftMove(), new DownMove() },
        { new RightMove(), new DownMove() }
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
	
	public void setDepth(int depth)
	{
		this.depth = depth;
	}
	
	public String toString()
	{
		return "<Depth:" + depth + "> " + board;
	}
}

