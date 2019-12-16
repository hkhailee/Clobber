
import java.awt.geom.*;
import java.awt.*;
import java.util.*;

/** This is a lot like ClobberBot3, but has an even stronger tendency to keep moving in the same direction.  Also,
 * I've given you an example of how to read the WhatIKnow state to see where all the bullets and other bots are. */
public class haileeKiesecker extends ClobberBot
{
    ClobberBotAction currAction, shotAction;
    int shotclock;

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
        //showWhatIKnow(currState); // @@@ Uncomment this line to see it print out all bullet and bot positions every turn
        shotclock--;
        if(shotclock<=0)
        {
            shotAction =null;
            double x2 =currState.me.getX();
            double y2 =currState.me.getY();
            shotAction = stuck(x2,y2);
            if(shotAction != null){
                return shotAction;
            }
            else{
            //-----move first if there is a bullet---------------
            double closerPoint=10000000000000.0;
            shotclock=game.getShotFrequency()+1;
            Iterator<BulletPoint2D> it = currState.bullets.iterator();
            while(it.hasNext())
            {
                ImmutablePoint2D p = (ImmutablePoint2D)(it.next());
                double x1 = p.getX(); 
                double y1 = p.getY();
            
                double x =x1-x2;
                double y =y1-y2;

                double closer = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
                
                //if bullet is within current bot move
                if(closer< 150.0 && closer <= closerPoint){
                    closerPoint=closer;
                    //System.out.println(closer);
                    shotAction= shootActionMoveMethod(x1, x2, y1, y2); //calls method to deal with logic  
                }
            } 
            
            //-----if there is not a bullet shoot at a close bot---------------
            if(shotAction==null){
                //this is repetitive I know
                shotAction = stuck(x2,y2);
                if(shotAction != null){
                    return shotAction;
                }
            
                //get all the bots 
                Iterator<BotPoint2D> bit = currState.bots.iterator();
                double canHitBetter = 1000000000.0;
                while(bit.hasNext())
                {
                    ImmutablePoint2D p = (ImmutablePoint2D)(bit.next());
                    
                    double x1 = p.getX();
                    double y1 = p.getY();
                   
                    double x =x1-x2;
                    double y =y1-y2;

                    double canHit = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
                        
                    if(canHit < 250 && canHit<= canHitBetter){
                        canHitBetter = canHit;
                        //System.out.println(canHitBetter + "--------");
                        
                        //bot too close evasive action
                       if(canHitBetter < 20.0){
                           shotAction = stuck(x2,y2);
                           if(shotAction == null )
                           shotAction = shootActionMoveMethod(x1,x2,y1,y2);
                       }
                        //kill the intruders tiberus!!
                       else{
                            if(x1> x2 && y1==y2){
                                shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.LEFT);
                            }
                            if(x1<x2 && y1==y2){
                                shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.RIGHT);
                            }
                            if(x1== x2 && y1<y2){
                                shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.UP);
                        
                            }
                            if(x1== x2 && y1>y2){
                                shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.DOWN);
                            }
                        }
                    }
                    //if cannot shoot directly at bot randomly shoot------------------
                    else{
                        shotAction = randomShoot();
                    }//end else

                }//end while
                
            }//end if shotnull
        
            return shotAction; 
         }
        }//end first if
   
        //---this hardly ever happens --------------------------------               
        else if(currAction==null || rand.nextInt(20)>18)
        {   
            //System.out.println("-----random move-----");
            currAction = randomMove();
        }//end else if
        return currAction;  
    }//end method
    
    /**
     * logic for moving away from a bullet in clobber currently without taking 
     * in the angle of the bullet
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return
     */
    public ClobberBotAction shootActionMoveMethod(double x1,double x2,double y1,double y2){
        shotAction = null;
        if(x1> x2 && y1==y2 || x1<x2 && y1==y2){
            //too far up 
            if(y2<= 25){
                shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
            }

            //too far down
            else if(y2 >= 585){
                shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
            }
            else{
                switch(rand.nextInt(2)){
                    case 0:
                    shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
                    break;
                    case 1:
                    shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
                    break;

                } 
            }
        } 
        if(x1== x2 && y1<y2 || x1== x2 && y1>y2){
            //too far right
            if(x2>= 585){
                shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
            }
            //too far left
            else if(x2<=25){
                shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
            }
            else{
                switch(rand.nextInt(2)){
                    case 0:
                    shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
                    break;
                    case 1:
                    shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
                    break;
                }
            }
        }
        if(x1> x2 && y1<y2){
            shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
        } 
        if(x1< x2 && y1>y2){
            shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
        }
        if(x1> x2 && y1>y2){
            shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
        }
        if(x1< x2 && y1<y2){
            shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
        }
       
        if (shotAction != null){
            return shotAction;
        }
        else return null;
      
    }

    /*
    a random move generator 
    */
    public ClobberBotAction randomMove(){

        switch(rand.nextInt(7))
        {
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

        return currAction;

    }

    /*
    a random shot generator 
    */
    public ClobberBotAction randomShoot(){
        switch(rand.nextInt(7))
        {
            case 0:
                shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.RIGHTUP);
            break;
            case 1:
                shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.RIGHTDOWN);
            break;
            case 2:
                shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.LEFTUP);
            break;
            case 3:
                shotAction = new ClobberBotAction(ClobberBotAction.SHOOT, ClobberBotAction.RIGHTUP);
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
        }//end switch

        return shotAction;

    }
    /**make sure that we dont get stuck on the boarder */
    public ClobberBotAction stuck(double x1,double y1){
        shotAction = null;
        //if stuck on top
        if(y1<=10){
            //move down
            shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
        }
        //stuck on left wall
        if(x1<=10){
            //move right
            shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
        }

        //stuck on bottom
        if(y1>=590){
            //move up
            shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
        }
        if(x1>=590){
            //move left
            shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
        }

        //stuck in top corner
        if(x1 <=10 && y1 <= 10){
            shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFTDOWN);
        }

        if(shotAction != null){
             return shotAction;
        }
        else return null;
    }

    public String toString()
    {
        return "smarterBot by Hailee Kiesecker";
    }
}