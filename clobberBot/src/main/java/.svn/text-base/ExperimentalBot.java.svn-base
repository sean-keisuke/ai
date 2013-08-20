import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.imageio.ImageIO;

public class ExperimentalBot extends ClobberBot
{
	private BufferedImage image;
	private final static String file = "C:\\Users\\dbrouwer\\Desktop\\ClobberinTime\\trunk\\src\\main\\resources\\thing.gif";
	private int turnsTillShot;
	private double[][] dangerZones;
	private final int MAX_X;
	private final int MAX_Y;
	private static final double BULLET_DANGER = 20;
	private static final double BOT_DANGER = 10;
	private static final double WALL_DANGER = 1;
	private static final double DANGER_THRESHHOLD = 0;
	
    public ExperimentalBot(Clobber game)
    {
        super(game);
        mycolor = Color.ORANGE;
		turnsTillShot = 0;
		MAX_X = game.getMaxX();
		MAX_Y = game.getMaxY();
		dangerZones = new double[MAX_Y + 1][MAX_X + 1];
    }

    @Override
    public ClobberBotAction takeTurn(WhatIKnow currState)
    {
		determineDangerZones(currState);
		ClobberBotAction action;
		if (isInSafePosition(currState)) {
			if (canShoot()) {
				action = takeBestShot();
				turnsTillShot = Clobber.SHOTFREQUENCY;
			} else {
				action = staySafe(currState);
			}
		} else {
			action = getSafe(currState);
		}
		turnsTillShot--;
		return action;
    }
	
	/* Creates 'danger map' of the board, checks to see if the space occupied by the clobberbot has danger below a 
	 * certain threshhold */
	private boolean isInSafePosition(WhatIKnow currState) {
		return (dangerZones[(int) currState.me.y][(int) currState.me.x] <= DANGER_THRESHHOLD);
	}
	
	private ClobberBotAction staySafe(WhatIKnow currState)
	{
		int bestDirection = ClobberBotAction.NONE;
		double lowestDanger = dangerZones[(int) currState.me.y][(int) currState.me.x];
		
		double directionDanger = dangerZones[(int) currState.me.y - 1][(int) currState.me.x];
		if (directionDanger < lowestDanger)
		{
			bestDirection = ClobberBotAction.UP;
			lowestDanger = directionDanger;
		}
		
		directionDanger = dangerZones[(int) currState.me.y - 1][(int) currState.me.x + 1];
		if (directionDanger < lowestDanger)
		{
			bestDirection = ClobberBotAction.UP | ClobberBotAction.RIGHT;
			lowestDanger = directionDanger;
		}
		
		directionDanger = dangerZones[(int) currState.me.y - 1][(int) currState.me.x - 1];
		if (directionDanger < lowestDanger)
		{
			bestDirection = ClobberBotAction.UP | ClobberBotAction.LEFT;
			lowestDanger = directionDanger;
		}
		
		directionDanger = dangerZones[(int) currState.me.y][(int) currState.me.x + 1];
		if (directionDanger < lowestDanger)
		{
			bestDirection = ClobberBotAction.RIGHT;
			lowestDanger = directionDanger;
		}
		
		directionDanger = dangerZones[(int) currState.me.y][(int) currState.me.x - 1];
		if (directionDanger < lowestDanger)
		{
			bestDirection = ClobberBotAction.LEFT;
			lowestDanger = directionDanger;
		}
		
		directionDanger = dangerZones[(int) currState.me.y + 1][(int) currState.me.x];
		if (directionDanger < lowestDanger)
		{
			bestDirection = ClobberBotAction.DOWN;
			lowestDanger = directionDanger;
		}
		
		directionDanger = dangerZones[(int) currState.me.y + 1][(int) currState.me.x + 1];
		if (directionDanger < lowestDanger)
		{
			bestDirection = ClobberBotAction.DOWN | ClobberBotAction.RIGHT;
			lowestDanger = directionDanger;
		}
		
		directionDanger = dangerZones[(int) currState.me.y + 1][(int) currState.me.x - 1];
		if (directionDanger < lowestDanger)
		{
			bestDirection = ClobberBotAction.DOWN | ClobberBotAction.LEFT;
		}
		
		return new ClobberBotAction(ClobberBotAction.MOVE, bestDirection);
	}
	
	private ClobberBotAction getSafe(WhatIKnow currState)
	{
		return staySafe(currState);
	}
	
	/* Checks shot timer to see if bot can shoot */
	private boolean canShoot() {
		return turnsTillShot <= 0;
	}
	
	/* Determines best direction to shoot */
	private ClobberBotAction takeBestShot()
	{
		return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.RIGHT);
	}

    @Override
    public String toString()
    {
        return "TheHorribleExperiment";
    }
	
	@Override
	public void drawMe(Graphics page, Point2D me)
     {
         try{
             image = ImageIO.read(new File(file));
         }
         catch (IOException e) {
             e.printStackTrace();
         }

         int x,y;
         x=(int)me.getX() - Clobber.MAX_BOT_GIRTH/2 -1;
         y=(int)me.getY() - Clobber.MAX_BOT_GIRTH/2 -1;

         page.drawImage(image, x, y, null);

     }

	// need to set danger for radius for 
	private void determineDangerZones(WhatIKnow currState)
	{
		resetDangerZones();
		
		// bullet danger
		for (BulletPoint2D bulletPoint2D : currState.bullets)
		{
			for (int bulletPathX = (int) bulletPoint2D.x, bulletPathY = (int) bulletPoint2D.y; bulletPathX <= MAX_X && bulletPathX >= 0 && bulletPathY <= MAX_Y && bulletPathY >= 0; bulletPathX += bulletPoint2D.getXPlus(), bulletPathY += bulletPoint2D.getYPlus())
			{
				dangerZones[bulletPathY][bulletPathX] += (BULLET_DANGER / bulletPoint2D.distance(bulletPathX, bulletPathY));
			}
		}
		
		// bot danger
		for (BotPoint2D botPoint2D : currState.bots)
		{
			for (int botX = 0; botX <= MAX_X && botX >= 0; botX++)
			{
				dangerZones[(int) botPoint2D.y][botX] += (BOT_DANGER / botPoint2D.distance(botX, botPoint2D.y));
			}

			for (int botY = 0; botY <= MAX_Y && botY >= 0; botY++)
			{
				dangerZones[botY][(int) botPoint2D.x] += (BOT_DANGER / botPoint2D.distance(botPoint2D.x, botY));
			}
			
			for (int botPathX = (int) botPoint2D.x, botPathY = (int) botPoint2D.y; botPathX <= MAX_X && botPathX >= 0 && botPathY <= MAX_Y && botPathY >= 0; botPathX++, botPathY++)
			{
				dangerZones[botPathY][botPathX] += (BOT_DANGER / botPoint2D.distance(botPathX, botPathY));
			}
			
			for (int botPathX = (int) botPoint2D.x, botPathY = (int) botPoint2D.y; botPathX <= MAX_X && botPathX >= 0 && botPathY <= MAX_Y && botPathY >= 0; botPathX++, botPathY--)
			{
				dangerZones[botPathY][botPathX] += (BOT_DANGER / botPoint2D.distance(botPathX, botPathY));
			}
			
			for (int botPathX = (int) botPoint2D.x, botPathY = (int) botPoint2D.y; botPathX <= MAX_X && botPathX >= 0 && botPathY <= MAX_Y && botPathY >= 0; botPathX--, botPathY++)
			{
				dangerZones[botPathY][botPathX] += (BOT_DANGER / botPoint2D.distance(botPathX, botPathY));
			}
			
			for (int botPathX = (int) botPoint2D.x, botPathY = (int) botPoint2D.y; botPathX <= MAX_X && botPathX >= 0 && botPathY <= MAX_Y && botPathY >= 0; botPathX--, botPathY--)
			{
				dangerZones[botPathY][botPathX] += (BOT_DANGER / botPoint2D.distance(botPathX, botPathY));
			}
		}
		
		// wall/corner danger
		for (int x = 0, y = 0; x <= MAX_X; x++)
		{
			dangerZones[y][x] += WALL_DANGER;
		}
		for (int x = 0, y = MAX_Y; x <= MAX_X; x++)
		{
			dangerZones[y][x] += WALL_DANGER;
		}
		for (int x = 0, y = 0; y <= MAX_Y; y++)
		{
			dangerZones[y][x] += WALL_DANGER;
		}
		for (int x = MAX_X, y = 0; y <= MAX_Y; y++)
		{
			dangerZones[y][x] += WALL_DANGER;
		}
	}
	
	private void resetDangerZones()
	{
		for (int i = 0; i < MAX_X; i++)
		{
			for (int j = 0; j < MAX_Y; j++)
			{
				dangerZones[j][i] = 0;
			}
		}
	}
}
