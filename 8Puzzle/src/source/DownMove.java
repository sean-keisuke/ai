package source;

public class DownMove implements Action<EightPuzzleState>
{
	@Override
	public EightPuzzleState updateState(EightPuzzleState state)
    {
		char[] chars = state.board.toCharArray();
		char temp = chars[state.blank+3];
		chars[state.blank] = temp;
		chars[state.blank+3] = ' ';
        return new EightPuzzleState(new String(chars));
    }
}


