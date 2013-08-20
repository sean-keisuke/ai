import java.awt.geom.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;
import javax.imageio.ImageIO;

/**
 * This is a lot like ClobberBot3, but has an even stronger tendency to keep moving in the same direction. Also, I've
 * given you an example of how to read the WhatIKnow state to see where all the bullets and other bots are.
 */
public class TheThing extends ClobberBot
{
    ClobberBotAction currAction, shotAction;
    int myOwnInt;
    static int numOfMe;
    int shotclock;
    private HashMap<Integer, TheThing.Bullet> bullets;
	BufferedImage image;
	String file = "C:\\Users\\dbrouwer\\Desktop\\ClobberinTime\\trunk\\src\\main\\java\\thing.gif";
	
    //private List<Bots> bullets

    private class Bullet
    {
        BulletPoint2D pos;
        BulletPoint2D oldpos;
        int ID;
        boolean dangerous;
        boolean knowDirection;
		double angleDirection;
		
		public Bullet(BulletPoint2D bullet)
		{
			pos = bullet;
			this.ID = bullet.getID();
			angleDirection = Math.atan2(bullet.getYPlus(), bullet.getXPlus());
		}

//        public boolean dangerous()
//        {
//            if (!dangerous)
//                return dangerous;
//            return (dangerous == checkMovingTowardsMe());
//        }
		private boolean isWithinTolerance(double angleDirection, double bulletsDirection, WhatIKnow currstate, BulletPoint2D oldpos)
		{
			
			return true;
		}	
		
        private boolean checkMovingTowardsMe(WhatIKnow currstate, BulletPoint2D oldpos, double angleDirection)
        {
			// compare angle of vector from bullet to me with angleDirection
			double bulletTowardsUs = Math.atan2(currstate.me.y - oldpos.y, currstate.me.x - oldpos.x);
			if (isWithinTolerance(angleDirection, bulletTowardsUs, currstate, oldpos))
			{
				return true;
			}
			else
			{
				return false;
			}
        }
		
		@Override
		public int hashCode()
		{
			return ID;
		}
    }
	
	private void getBulletInformation(WhatIKnow currstate)
	{
		Vector<BulletPoint2D> currentBullets = currstate.bullets;
		for (BulletPoint2D bullet : currentBullets)
		{
			if (bullet != null && bullets.containsKey(bullet.getID()))
			{
				Bullet updateBullet = bullets.get(bullet.getID());
				
			}
			else if (bullet != null)
			{
				//add new bullets
				bullets.put(bullet.getID(), new Bullet(bullet));
			}
		}
		
		for (Entry<Integer, TheThing.Bullet> entry : bullets.entrySet())
		{
			int id = entry.getKey();
			boolean containsBullet = false;
			for (BulletPoint2D bullet : currstate.bullets)
			{
				if (bullet.getID() == id)
				{
					containsBullet = true;
					break;
				}
			}
			if (!containsBullet) {
				bullets.remove(id);
			}
			
		}
		
	}

//    List<Bullet> getDangerousBullets(WhatIKnow currstate)
//    {
//        int x, y;
//        ImmutablePoint2D pt;
//        Bullet bul;
//        List<Bullet> badones = new ArrayList<Bullet>();
//
//        x = 0;
//        y = 0;
//
//        while (x < currstate.bullets.size() && y < bullets.size()) {
//            pt = (ImmutablePoint2D) (currstate.bullets.get(x));
//            bul = bullets.get(y);
//
//            assert (pt.getID() >= bul.ID);  // We shouldn't see an id less than what we're already storing
//
//            if (pt.getID() > bul.ID) {
//                // Add new bullet
//            }
//            else {
//                if (!bullets.get(y).knowDirection) {
//                    // Figure out direction
//                }
//                if (!bul.dangerous) {
//                    x++;
//                    y++;
//                }
//                else {
//					
//                }
//
//            }
//        }
//        return null;
//    }

    public TheThing(Clobber game)
    {
        super(game);
		
        myOwnInt = numOfMe++;
        mycolor = Color.ORANGE;
    }
	
//	@Override
//	public void drawMe(Graphics page, Point2D me)
//     {
//         try{
//             image = ImageIO.read(new File(file));
//         }
//         catch (IOException e) {
//             e.printStackTrace();
//         }
//
//         int x,y;
//         x=(int)me.getX() - Clobber.MAX_BOT_GIRTH/2 -1;
//         y=(int)me.getY() - Clobber.MAX_BOT_GIRTH/2 -1;
//
//         page.drawImage(image, x, y, null);
//
//     }


    public void myOwnMethod()
    {
        System.out.println("Unit " + myOwnInt + " reporting, sir.");
    }

    @Override
    public ClobberBotAction takeTurn(WhatIKnow currState)
    {
//		getDangerousBullets(currState);
        //showWhatIKnow(currState); // @@@ Uncomment this line to see it print out all bullet and bot positions every turn

        for (int x = 0; x < teammates.size(); x++) {
            ((ClobberBot5) (teammates.get(x))).myOwnMethod();
        }

        shotclock--;
        if (shotclock <= 0) {
            shotclock = game.getShotFrequency() + 1;
            switch (rand.nextInt(8)) {
                case 0:
                    shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.UP);
                    break;
                case 1:
                    shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.DOWN);
                    break;
                case 2:
                    shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.LEFT);
                    break;
                case 3:
                    shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.RIGHT);
                    break;
                case 4:
                    shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.UP | ClobberBotAction.LEFT);
                    break;
                case 5:
                    shotAction = new ClobberBotAction(ClobberBotAction.SHOOT,
                                                      ClobberBotAction.UP | ClobberBotAction.RIGHT | ClobberBotAction.DOWN | ClobberBotAction.LEFT);
                    break;
                case 6:
                    shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.DOWN | ClobberBotAction.LEFT);
                    break;
                default:
                    shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.DOWN | ClobberBotAction.RIGHT);
                    break;
            }
            return shotAction;
        }
        else if (currAction == null || rand.nextInt(20) > 18) {
            switch (rand.nextInt(4)) {
                case 0:
                    currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
                    break;
                case 1:
                    currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
                    break;
                case 2:
                    currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
                    break;
                case 3:
                    currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
                    break;
                case 4:
                    currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP | ClobberBotAction.LEFT);
                    break;
                case 5:
                    currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP | ClobberBotAction.RIGHT);
                    break;
                case 6:
                    currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN | ClobberBotAction.LEFT);
                    break;
                default:
                    currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN | ClobberBotAction.RIGHT);
                    break;
            }
        }
        return currAction;
    }

    @Override
    public String toString()
    {
        return "TheThing";
    }

    /**
     * Here's an example of how to read the WhatIKnow data structure
     */
    private void showWhatIKnow(WhatIKnow currState)
    {
        System.out.println("My id is " + ((ImmutablePoint2D) (currState.me)).getID() + ", I'm at position ("
                           + currState.me.getX() + ", " + currState.me.getY() + ")");
        System.out.print("Bullets: ");
        Iterator<BulletPoint2D> it = currState.bullets.iterator();
        while (it.hasNext()) {
            ImmutablePoint2D p = (ImmutablePoint2D) (it.next());
            System.out.print(p + ", ");
        }
        System.out.println();

        System.out.print("Bots: ");
        Iterator<BotPoint2D> bit = currState.bots.iterator();
        while (bit.hasNext()) {
            System.out.print(bit.next() + ", ");
        }
        System.out.println();
    }
}
