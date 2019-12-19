
import java.awt.geom.*;
import java.awt.*;
import java.util.*;

/** This is a lot like ClobberBot3, but has an even stronger tendency to keep moving in the same direction.  Also,
 * I've given you an example of how to read the WhatIKnow state to see where all the bullets and other bots are. */
public class haileeKiesecker extends ClobberBot
{
    ClobberBotAction currAction, shotAction, preferedMove;

    WhatIKnow prevState;
    int shotclock;
    boolean canLeft = true;
    boolean canRight = true;
    boolean canUp = true;
    boolean canDown = true;
   
    boolean canUpLeft = true;
    boolean canUpRight = true;
    boolean canDownLeft = true;
    boolean canDownRight = true;


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
    /*
    modify this method to be a smarter bot than a random
    */
    public ClobberBotAction takeTurn(WhatIKnow currState)
    {
        double x2 =currState.me.getX();
        double y2 =currState.me.getY();

        
        //showWhatIKnow(currState); // @@@ Uncomment this line to see it print out all bullet and bot positions every turn
        whatCanIDo(currState);
        prevState = currState;
        shotclock--;
        if(shotclock<=0) //CAN SHOOT
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
               
        else 
        {
            if(currState.me.getX()<50){
                
                if(canRight){
                    // System.out.println("rigth");
                     return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
                 }

            }
            if(currState.me.getX()>550){
                if(canLeft){
                    //System.out.println("left");
                    return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
                }
                
            }
            if(currState.me.getY()>550){
                if(canUp){
                    // System.out.println("up, "+currState.me.getY());
                     return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
                }     

            }

            if(currState.me.getY()<50){
            
                 if(canDown){
                     //System.out.println("down");
                     return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
                 }
            }
            else{

                       
                if(canRight){
                    // System.out.println("rigth");
                     return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
                 }
                if(canLeft){
                    //System.out.println("left");
                    return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
                }

                if(canDown){
                    //System.out.println("down");
                    return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
                }
                if(canUp){
                    // System.out.println("up, "+currState.me.getY());
                     return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
                } 
                if(canUpRight){
                    //System.out.println("upright");
                    return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHTUP);
                }
                if(canDownRight){
                    // System.out.println("downright");
                     return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHTDOWN);
                 }
                if(canDownLeft){
                    //System.out.println("down left");
                    return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFTDOWN);
                }
             
                if(canUpLeft){
                    //System.out.println("up left");
                    return new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFTUP);
                }
            }
            

            
            
           
            
            // if(preferedMove != null){
                // return preferedMove;  
            // }
            // else{
                // return randomMove();
            // }
        }
        return null;
    }//end method

    public void whatCanIDo(WhatIKnow currState){
        // canLeft = false;
        // canRight = false;
        // canUp = false;
        // canDown = false;
        // canUpLeft = false;
        // canUpRight = false;
        // canDownLeft = false;
        // canDownRight = false;
        checkBorders(currState);
        BulletPoint2D bullet = whatBulletWillHitMe(currState);

        if(bullet != null){
            //System.out.println("there is a bullet");
            avoidObjectArea(bullet.getX(), bullet.getY(), currState);
        }
    }

    private void avoidObjectArea(double bulletX, double bulletY, WhatIKnow currState){
        if(bulletX < currState.me.getX() && bulletY < currState.me.getY()){
            canUp = false;
            canUpRight = true;
            canRight = true;
            canDownRight = false;
            canDown = true;
            canDownLeft = true;
            canLeft = false;
            canUpLeft = false;
        }
        if(bulletX == currState.me.getX() && bulletY < currState.me.getY()){
            System.out.println("I got completely equal to the bullet hahahaha");
            canUp = false;
            canDown = false;
            canLeft = true;
            canRight = true;
        }
        if(bulletX > currState.me.getX() && bulletY < currState.me.getY()){
            canUp = false;
            canUpRight = false;
            canRight = false;
            canDownRight = true;
            canDown = true;
            canDownLeft = false;
            canLeft = true;
            canUpLeft = true;
        }
        if(bulletX > currState.me.getX() && bulletY == currState.me.getY()){
            System.out.println("I got completely equal to the bullet hahahaha");
            canRight = false;
            canLeft = false;
            canUp = true;
            canDown = true;
        }
        if(bulletX > currState.me.getX() && bulletY > currState.me.getY()){
            canUp = true;
            canUpRight = true;
            canRight = false;
            canDownRight = false;
            canDown = false;
            canDownLeft = true;
            canLeft = true;
            canUpLeft = false;
        }
        if(bulletX == currState.me.getX() && bulletY > currState.me.getY()){
            System.out.println("I got completely equal to the bullet hahahaha");
            canUp = false;
            canDown = false;
            canRight = true;
            canLeft = true;
        }
        if(bulletX < currState.me.getX() && bulletY > currState.me.getY()){
            canUp = true;
            canUpRight = false;
            canRight = true;
            canDownRight = true;
            canDown = false;
            canDownLeft = true;
            canLeft = false;
            canUpLeft = true;
        }
        if(bulletX < currState.me.getX() && bulletY == currState.me.getY()){
            System.out.println("I got completely equal to the bullet hahahaha");
            canLeft = false;
            canRight = false;
            canUp = true;
            canDown = true;
        }
    }

    public BulletPoint2D whatBulletWillHitMe(WhatIKnow currState){
        
        Iterator<BulletPoint2D> it = currState.bullets.iterator();
        BulletPoint2D closestBullet = null;
        double closeBullet = Double.MAX_VALUE;
        
        while(it.hasNext()){
            BulletPoint2D p = (BulletPoint2D)(it.next());
            double c = Math.sqrt(Math.pow(2, Math.abs(p.getX() - currState.me.getX())) + Math.pow(2, Math.abs(p.getY() - currState.me.getY())));
                if (c < closeBullet && c < 500.0){
                    System.out.println(c);
                    closeBullet = c;
                    closestBullet = p;
                }
        }
        
      
        return closestBullet;
    }

    public void checkBorders(WhatIKnow currState){
        double x =currState.me.getX();
        double y =currState.me.getY();
        //when too far up cant up
        if(y < 50){
            canUp = false;
            canDown = true;
        }

        //when too far down cant down
        if(y > 550){
            canDown = false;
            canUp= true;
        }
        

        //too far left cant left
        if(x <50 ){
            canLeft = false;
            canRight= true;
        }

        //too far right cant right
        if(x > 550 ){
            canRight = false;
            canLeft = true;
        }

        //too far left up 
        if(y<50 && x <50){
            canUpLeft = false;
            canUp = false;
            canLeft = false;
            //canDownRight = true;
        }

        //too far left down
        if(y>550 && x <50){
            canDownLeft = false;
            canDown = false;
            canLeft = false;
            //canUpRight = true;
        }

        //too far right up
        if(y<50 && x >550){
            canUpRight = false;
            canRight = false;
            canUp = false;
           // canDownLeft = true;
        }

        //too far right down
        if(y>550 && x >550){
            canDownRight = false;
            canDown = false;
            canRight = false;

           // canUpLeft = true;
        }
    }

    public String toString()
    {
        return "tiberius by Hailee Lucas";
    }
}
    
   