
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
        double x2 =currState.me.getX();
        double y2 =currState.me.getY();
        showWhatIKnow(currState); // @@@ Uncomment this line to see it print out all bullet and bot positions every turn
        shotclock--;
        if(shotclock<=0) //CAN SHOOT
        {
        shotclock=game.getShotFrequency()+1;
        shotAction =null;
       //System.out.println(shotclock);

            return takeShot(x2, y2, currState);
        }
       
   
        //---this hardly ever happens --------------------------------               
        else 
        {   
            //currAction = stuck(x2,y2);
        
            // if(currAction != null){
            //     return currAction;
            // }
           // else{
                //-----move first if there is a bullet---------------
                double closerPoint=Double.MAX_VALUE;
                //shotclock=game.getShotFrequency()+1;
                Iterator<BulletPoint2D> it = currState.bullets.iterator();
                while(it.hasNext())
                {
                    ImmutablePoint2D p = (ImmutablePoint2D)(it.next());
                    double x1 = p.getX(); 
                    double y1 = p.getY();
                
                    double x =Math.abs(x1-x2);
                    double y =Math.abs(y1-y2);

                    double closer = Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
                    //System.out.println(closer);
                    //if bullet is within current bot move
                    if(closer< 150.0 && closer <= closerPoint){
                        //System.out.println(closer);
                        closerPoint=closer;
                        currAction= shootActionMoveMethod(x1, x2, y1, y2); //calls method to deal with logic  
                    }
                } 
                if(currAction == null){
                //System.out.println("-----random move-----");
                //scurrAction = randomMove();
                }
           // }

        }//end else if
        
        return currAction;  

    }//end method
    
    double botDist =250;
    public ClobberBotAction takeShot(double x2, double y2, WhatIKnow currState){

         //-----if there is not a bullet shoot at a close bot---------------
         if(shotAction==null){
            //this is repetitive I know
           // shotAction = stuck(x2,y2);
            // if(shotAction != null){
            //     return shotAction;
            // }
        
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
                    
                if(canHit < botDist && canHit<= canHitBetter){
                    botDist +=5.0;
                    canHitBetter = canHit;
                    //System.out.println(canHitBetter + "--------");
                    
                    //bot too close evasive action
                   if(canHitBetter < 20.0){
                       //shotAction = stuck(x2,y2);
                       //if(shotAction == null )
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
        currAction = null;
        if(x1> x2 && y1==y2 || x1<x2 && y1==y2){ //ON SAME Y LEVEL
            //too far up 
            if(y2<= 25){
                currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
            }

            //too far down
            else if(y2 >= 585){
                currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
            }
            else{
                
                    currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
                    

                } 
            
        } 
        else if(x1== x2 && y1<y2 || x1== x2 && y1>y2){
            //too far right
            if(x2>= 585){
                currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
            }
            //too far left
            else if(x2<=25){
                currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
            }
            else{
                
                    currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
                 
            }
        }
        else if(x1> x2 && y1<y2){ //top right quad
            currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
        } 
        else if(x1< x2 && y1>y2){//lower left quad
            currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
        }
        else if(x1> x2 && y1>y2){//lower right quad
            currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
        }
        else if(x1< x2 && y1<y2){ //TOP left quad
            currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
        }
       
        if (currAction != null){
            return currAction;
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
    // /**make sure that we dont get stuck on the boarder */
    // public ClobberBotAction stuck(double x1,double y1){
    //     currAction = null;
    //     //if stuck on top
    //     if(y1<=10){
    //         //move down
    //         currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.DOWN);
    //     }
    //     //stuck on left wall
    //     else if(x1<=10){
    //         //move right
    //         currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.RIGHT);
    //     }

    //     //stuck on bottom
    //     else if(y1>=590){
    //         //move up
    //         currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.UP);
    //     }
    //     else if(x1>=590){
    //         //move left
    //         currAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFT);
    //     }

    //     //stuck in top corner
    //     else if(x1 <=10 && y1 <= 10){
    //         shotAction = new ClobberBotAction(ClobberBotAction.MOVE, ClobberBotAction.LEFTDOWN);
    //     }

    //     if(currAction != null){
    //          return currAction;
    //     }
    //     else return null;
    // }

    public String toString()
    {
        return "smarterBot by Hailee Kiesecker";
    }
}