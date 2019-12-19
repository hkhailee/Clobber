
import java.awt.geom.*;
import java.awt.*;
import java.util.*;

/**
 * This is a lot like ClobberBot3, but has an even stronger tendency to keep
 * moving in the same direction. Also, I've given you an example of how to read
 * the WhatIKnow state to see where all the bullets and other bots are.
 */
public class haileeKiesecker extends ClobberBot {

	ClobberBotAction currAction, shotAction;
	int shotclock;
	int numMovesBullet = 0;
	WhatIKnow prevState;

	public haileeKiesecker(Clobber game) {
		super(game);
		mycolor = Color.red;
	}

	/** Here's an example of how to read teh WhatIKnow data structure */
	private void showWhatIKnow(WhatIKnow currState) {
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

	public ClobberBotAction takeTurn(WhatIKnow currState) {
		// bullet and bot positions every turn
		shotclock--;
		if (shotclock <= 0) {
			shotclock = game.getShotFrequency() + 1;
			return shotBot(currState);
		} else {
			currAction = decideMove(currState);
		}
		prevState = currState;
		return currAction;
	}
	
	private BulletPoint2D getBullet(WhatIKnow state, int id) {
		Iterator<BulletPoint2D> it = state.bullets.iterator();
		while (it.hasNext()) {

			BulletPoint2D p = it.next();
			if(p.getID() == id) {
				return p;
			}
		}
		return null;
	}

	private ClobberBotAction shotBot(WhatIKnow currState) {
		Iterator<BotPoint2D> bots = currState.bots.iterator();
		BotPoint2D deadBot = null;
		double randAngle = 0;
		double closestbot = Double.MAX_VALUE;
		while (bots.hasNext()) {
			BotPoint2D closeBot = (BotPoint2D) (bots.next());

			// getting the closest
			// bott=============================================================
			double x = Math.abs(closeBot.getX() - currState.me.getX());
			double y = Math.abs(closeBot.getY() - currState.me.getY());
			double x2 = Math.pow(x, 2);
			double y2 = Math.pow(y, 2);
			double c = Math.sqrt(x2 + y2);
			// System.out.println("bot id: closeBot" + closeBot.getID() + " distance from
			// me"+ c);

			if (c < closestbot) {
				deadBot = closeBot; // assigned the closest bot
				closestbot = c;
				// find the angle of a
				// bot=============================================================
				double u = (deadBot.getX() * currState.me.getX());
				double v = (deadBot.getY() * currState.me.getY());

				double sU = Math.sqrt(Math.pow(deadBot.getX(), 2) + Math.pow(deadBot.getY(), 2));
				double sV = Math.sqrt(Math.pow(currState.me.getX(), 2) + Math.pow(currState.me.getY(), 2));

				double cos = ((u + v) / (sU * sV));
				randAngle = (Math.acos(cos) * 100);
				// if(deadBot.getY()>currState.me.getY() )
				// randAngle = randAngle;

				// System.out.println(randAngle) ;
			}

		}

		// given the angle and the closest bot

		// top left
		if ((deadBot.getX() < currState.me.getX() && 10 <= (deadBot.getY() - currState.me.getY())
				&& (randAngle > 0 && randAngle < 18))) {
			return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.LEFT);
		}
		if ((deadBot.getX() > currState.me.getX() && 10 <= (deadBot.getY() - currState.me.getY())
				&& (randAngle > 0 && randAngle < 18))) {
			return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.RIGHT);
		}
		if (10 <= (deadBot.getX() - currState.me.getX()) && deadBot.getY() < currState.me.getY()
				&& (randAngle > 0 && randAngle < 18)) {
			return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.UP);
		}
		if (10 <= (deadBot.getX() - currState.me.getX()) && deadBot.getY() > currState.me.getY()
				&& (randAngle > 0 && randAngle < 18)) {
			return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.DOWN);
		}
		if ((deadBot.getX() < currState.me.getX() && deadBot.getY() < currState.me.getY())) {
			return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.LEFTUP);
		}
		if ((deadBot.getX() > currState.me.getX() && deadBot.getY() < currState.me.getY())) {
			return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.RIGHTUP);

		}
		if ((deadBot.getX() > currState.me.getX() && deadBot.getY() > currState.me.getY())) {
			return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.RIGHTDOWN);
		}
		if ((deadBot.getX() < currState.me.getX() && deadBot.getY() > currState.me.getY())) {
			return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.LEFTDOWN);
		}

		return new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.RIGHTDOWN);

	}

	private ClobberBotAction decideMove(WhatIKnow currState) {
		
		if(prevState == null) {
			currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.NONE);
			return currAction;
		}
		
		Iterator<BulletPoint2D> it = currState.bullets.iterator();
		BulletPoint2D closestBullet = null;
		double closeBullet = Double.MAX_VALUE;
		while (it.hasNext()) {
			BulletPoint2D bullet = (BulletPoint2D) (it.next());
			BulletPoint2D prevBullet = getBullet(prevState, bullet.getID());
			if(prevBullet!= null) {	
				if(isBulletGoingToHitMe(prevBullet, bullet, currState.me.getX(), currState.me.getY())) {
					
				}
				
				double x = Math.abs(bullet.getX() - currState.me.getX());
				
				double y = Math.abs(bullet.getY() - currState.me.getY());
				double x2 = Math.pow(x, 2);
				double y2 = Math.pow(y, 2);
				double c = Math.sqrt(x2 + y2);
				if(c < closeBullet) {
					closeBullet = c;
					closestBullet = bullet;
				}
			}
		}
		
		if (closeBullet < 70.0) { // we need to avoid the bullet
			int quad = whatQuad(closestBullet.getX(), closestBullet.getY(), currState);
			System.out.println(quad);
			switch (quad) {
			case 1:
				currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
				break;
			case 2:
				currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFTUP);
				break;
			case 3:
				currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
				break;
			case 4:
				currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFTDOWN);
				break;
			case 5:
				currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
				break;
			case 6:
				currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHTDOWN);
				break;
			case 7:
				currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
				break;
			case 8:
				currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHTUP);
				break;
			default:
				currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.NONE);
				break;
			}

		}else { // we need to get closer to another bot
			currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.NONE);
		}
		return currAction;
	}

	private boolean isBulletGoingToHitMe(BulletPoint2D before, BulletPoint2D after, double x, double y) {
		// TODO Auto-generated method stub
		return false;
	}

	private int whatQuad(double bulletX, double bulletY, WhatIKnow currState) { // where is it relative to me
		if ((bulletX < currState.me.getX()+10) && (bulletX > currState.me.getX()-10) && (bulletY < currState.me.getY())) {// up
			return 1;
		}
		if (bulletX > currState.me.getX() && (bulletY > currState.me.getY()-10) && (bulletY < currState.me.getY()+10)) { // right
			return 3;
		}
		if((bulletX < currState.me.getX()+10) && (bulletX > currState.me.getX()-10) && (bulletY > currState.me.getY())) {// down
			return 5;
		}
		if (bulletX < currState.me.getX() && (bulletY > currState.me.getY()-10) && (bulletY < currState.me.getY()+10)) { // left
			return 7;
		}
		if (bulletX < currState.me.getX() && bulletY < currState.me.getY()) { // up left
			return 8;
		}
		if (bulletX > currState.me.getX() && bulletY < currState.me.getY()) { // up right
			return 2;
		}
		if (bulletX > currState.me.getX() && bulletY > currState.me.getY()) { // right down
			return 4;
		}
		if (bulletX < currState.me.getX() && bulletY > currState.me.getY()) { // left down
			return 6;
		}

		return 0;

	}

	public String toString() {
		return "tiberuisv2 by Hailee lucas";
	}
	
	public class Vector2D {
		public Vector2D(double x1, double y1, double x2, double y2) {
			
		}
	}
}