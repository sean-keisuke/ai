package source;

public class RightMove implements Action<EightPuzzleState>
{
	@Override
	public EightPuzzleState updateState(EightPuzzleState state)
    {
		char[] chars = state.board.toCharArray();
		char temp = chars[state.blank+1];
		chars[state.blank] = temp;
		chars[state.blank+1] = ' ';
        return new EightPuzzleState(new String(chars));
    }	
}


