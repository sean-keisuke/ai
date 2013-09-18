package test;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import source.*;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author reuben
 */

public class ActionTest
{
	
	public ActionTest()
	{
		rightActionTest();
		leftActionTest();
		upActionTest();
		downActionTest();
	}

	@Test
	public void rightActionTest()
	{
		EightPuzzleState s = new EightPuzzleState("1 2345678");
		RightMove r = new RightMove();
		EightPuzzleState sprime = (EightPuzzleState)r.updateState(s);
		assertTrue(sprime.board.equals("12 345678"));
	}
	
	@Test
	public void leftActionTest()
	{
		EightPuzzleState s = new EightPuzzleState("1 2345678");
		LeftMove l = new LeftMove();
		EightPuzzleState sprime = (EightPuzzleState)l.updateState(s);
		assertTrue(sprime.board.equals(" 12345678"));
	}
	
	@Test
	public void upActionTest()
	{
		EightPuzzleState s = new EightPuzzleState("123 45678");
		UpMove u = new UpMove();
		EightPuzzleState sprime = (EightPuzzleState)u.updateState(s);
		assertTrue(sprime.board.equals(" 23145678"));
	}
	
	@Test
	public void downActionTest()
	{
		EightPuzzleState s = new EightPuzzleState("1 2345678");
		DownMove d = new DownMove();
		EightPuzzleState sprime = (EightPuzzleState)d.updateState(s);
		assertTrue(sprime.board.equals("1423 5678"));
	}
}