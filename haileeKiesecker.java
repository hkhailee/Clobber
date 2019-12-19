
import java.awt.geom.*;
import java.awt.*;
import java.util.*;

/** This is a lot like ClobberBot3, but has an even stronger tendency to keep moving in the same direction.  Also,
 * I've given you an example of how to read the WhatIKnow state to see where all the bullets and other bots are. */
public class haileeKiesecker extends ClobberBot
{

    ClobberBotAction currAction, shotAction;
    int shotclock;
    int numMovesBullet=0;
    Queue<ClobberBotAction> toDo = new LinkedList<ClobberBotAction>();
    public haileeKiesecker(Clobber game)
    {
        super(game);
        mycolor = Color.red;
    }

    /** Here's an example of how to read teh WhatIKnow data structure */
    private void showWhatIKnow(WhatIKnow currState)
    {
        System.out.println("My id is " + ((ImmutablePoint2D)(currState.me)).getID() + ", I'm at position (" + 
                           currState.me.getX() + ", " + currState.me.getY() + ")");
        System.out.print("Bullets: ");
        Iterator<BulletPoint2D> it = currState.bullets.iterator();
        while(it.hasNext())
        {
            ImmutablePoint2D p = (ImmutablePoint2D)(it.next());
            System.out.print(p + ", ");
        }
        System.out.println();

        System.out.print("Bots: ");
        Iterator<BotPoint2D> bit = currState.bots.iterator();
        while(bit.hasNext())
        {
            System.out.print(bit.next() + ", ");
        }
        System.out.println();
    }

    public ClobberBotAction takeTurn(WhatIKnow currState)
    {
        //showWhatIKnow(currState); // @@@ Uncomment this line to see it print out all bullet and bot positions every turn
        shotclock--;
        if(shotclock<=0)
        {
            shotclock=game.getShotFrequency()+1;
            switch(rand.nextInt(8))
            {
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
        else if(currAction==null || rand.nextInt(20)>18)
        {
            System.out.println(toDo.toString());
            if(toDo.isEmpty()){
               currAction= decideMove(currState);
            }
            else{
                currAction = toDo.remove();

            }
        }
        return currAction;
    }

    private ClobberBotAction decideMove(WhatIKnow currState){
        Iterator<BulletPoint2D> it = currState.bullets.iterator();
        BulletPoint2D closestBullet =null;
        double closeBullet = Double.MAX_VALUE;
        while(it.hasNext())
        {
            
            BulletPoint2D p = (BulletPoint2D)(it.next());
            double x =  Math.abs(p.getX() - currState.me.getX());
            
            double y = Math.abs(p.getY() - currState.me.getY());
            double x2 = Math.pow(x,2);
            double y2  = Math.pow(y,2);
            double c = Math.sqrt( x2+y2 );
            
            if (c < 200.0){
                closeBullet = c;
                closestBullet = p;
                int quad = whatQuad(closestBullet.getX(), closestBullet.getY(), currState);
                System.out.println(quad);
                switch(quad)
                {
                    case 1:
                        currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
                        for(int i = 0 ; i <= numMovesBullet ; i++){
                        toDo.add(new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT));
                        }
                        
                    break;
                    case 2:
                        currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFTUP);
                        for(int i = 0 ; i <= numMovesBullet ; i++){
                            toDo.add(new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFTUP));
                            }
                    break;
                    case 3:
                        currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
                        for(int i = 0 ; i <= numMovesBullet ; i++){
                            toDo.add(new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP));
                            }
                    break;
                    case 4:
                        currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFTDOWN);
                        for(int i = 0 ; i <= numMovesBullet ; i++){
                            toDo.add(new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFTDOWN));
                            }
                    break;
                    case 5:
                        currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
                        for(int i = 0 ; i <= numMovesBullet ; i++){
                            toDo.add(new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT));
                            }
                    break;
                    case 6:
                        currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHTDOWN );
                        for(int i = 0 ; i <= numMovesBullet ; i++){
                            toDo.add(new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHTDOWN));
                            }
                    break;
                    case 7:
                        currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN );
                        for(int i = 0 ; i <= numMovesBullet ; i++){
                            toDo.add(new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN));
                            }
                    break;
                    case 8:
                        currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHTUP );
                        for(int i = 0 ; i <= numMovesBullet ; i++){
                            toDo.add(new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHTUP));
                            }
                    break;
                    default:
                        currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.NONE);
                  
                    break;
                }
                
            }
            return currAction;
            
        }
        return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.NONE);
       
    }

    private int whatQuad(double bulletX, double bulletY, WhatIKnow currState){
        
        if(bulletX < currState.me.getX() && bulletY < currState.me.getY()){
            return 8;
        }
        if(bulletX == currState.me.getX() && bulletY < currState.me.getY()){
            return 1;
        }
        if(bulletX > currState.me.getX() && bulletY < currState.me.getY()){
           return 2;
        }
        if(bulletX > currState.me.getX() && bulletY == currState.me.getY()){
           return 3;
        }
        if(bulletX > currState.me.getX() && bulletY > currState.me.getY()){
            return 4;
        }
        if(bulletX == currState.me.getX() && bulletY > currState.me.getY()){
            return 5;
        }
        if(bulletX < currState.me.getX() && bulletY > currState.me.getY()){
            return 6;
        }
        if(bulletX < currState.me.getX() && bulletY == currState.me.getY()){
            return 7;
        }
       
        return 0;
        

    }

    public String toString()
    {
        return "tiberuisv2 by Hailee lucas";
    }
}