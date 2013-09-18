/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import source.DownMove;
import source.EightPuzzleState;
import source.LeftMove;
import source.RightMove;
import source.UpMove;

/**
 *
 * @author reuben
 */
public class ActionTest
{
	public static void main(String[] args)
	{
		rightActionTest();
		leftActionTest();
		upActionTest();
		downActionTest();
	}
	public static void rightActionTest()
	{
		EightPuzzleState s = new EightPuzzleState("1 2345678");
		RightMove r = new RightMove();
		EightPuzzleState sprime = (EightPuzzleState)r.updateState(s);
		assert sprime.board.equals("12 845673");
	}
	public static void leftActionTest()
	{
		EightPuzzleState s = new EightPuzzleState("1 2345678");
		LeftMove l = new LeftMove();
		EightPuzzleState sprime = (EightPuzzleState)l.updateState(s);
		assert(sprime.board.equals(" 12345678"));
	}
	public static void upActionTest()
	{
		EightPuzzleState s = new EightPuzzleState("123 45678");
		UpMove u = new UpMove();
		EightPuzzleState sprime = (EightPuzzleState)u.updateState(s);
		assert(sprime.board.equals(" 23145678"));
	}
	public static void downActionTest()
	{
		EightPuzzleState s = new EightPuzzleState("1 2345678");
		DownMove d = new DownMove();
		EightPuzzleState sprime = (EightPuzzleState)d.updateState(s);
		assert(sprime.board.equals("1423 5678"));
	}
	
}
