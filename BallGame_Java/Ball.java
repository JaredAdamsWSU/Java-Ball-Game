import java.applet.*;
import java.awt.*;
import java.util.*;
import java.net.*;
import java.lang.*;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Ball
{
	/*Number of times this ball was hit and its type */
	protected int timesHit = 0;
	protected int ballType;
    /*Properties of the basic ball. These are initialized in the constructor using the values read from the config.xml file*/
	protected int pos_x;			
	protected int pos_y; 				
	protected int radius;
	protected int first_x;			
	protected int first_y;					
	protected int x_speed;			
	protected int y_speed;			
	protected int maxspeed;
	Color color;
	AudioClip outSound;
	
    GameWindow gameW;
	Player player;
	public Random rand = new Random();
	
	/*constructor*/
	public Ball (int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color, AudioClip outSound, Player player,  GameWindow gameW, int type)
	{	
		this.radius = radius;

		pos_x = initXpos;
		pos_y = initYpos;

		first_x = initXpos;
		first_y = initYpos;

		x_speed = speedX;
		y_speed = speedY;

		maxspeed = maxBallSpeed;

		this.color = color;

		this.outSound = outSound;

		this.player = player;
		this.gameW = gameW;
		
		ballType = type;
	}

	public int getRandX(int passedRadius)
	{
		int x = rand.nextInt(gameW.x_rightout - passedRadius) + passedRadius;
		return x;
	}
	public int getRandY(int passedRadius)
	{
		int y = rand.nextInt(gameW.y_downout - passedRadius) + passedRadius;
		return y;
	}
	/* Get the balls type */
	public int getType()
	{
		return ballType;
	}
	
	public int getTimesHit()
	{
		return timesHit;
	}
	/*update ball's location based on it's speed*/
	public void move ()
	{
		pos_x += x_speed;
		pos_y += y_speed;
		isOut();
	}

	/*when the ball is hit, reset the ball location to its initial starting location*/
	public void ballWasHit ()
	{	
		timesHit++;
		resetBallPosition();
	}

	/*check whether the player hit the ball. If so, update the player score based on the current ball speed. */	
	public boolean userHit (int maus_x, int maus_y)
	{
		
		double x = maus_x - pos_x;
		double y = maus_y - pos_y;

		double distance = Math.sqrt ((x*x) + (y*y));
		
		if (distance-this.radius < (int)(player.scoreConstant)) {
			player.addScore (player.scoreConstant * Math.abs(x_speed) + player.scoreConstant);
			return true;
		}
		else return false;
	}

    /*reset the ball position to its initial starting location*/
	protected void resetBallPosition()
	{
		pos_x = gameW.x_rightout / 2;
		pos_y = gameW.y_downout / 2;
		x_speed = ThreadLocalRandom.current().nextInt(-maxspeed, maxspeed + 1);
		y_speed = ThreadLocalRandom.current().nextInt(-maxspeed, maxspeed + 1);
	}
	
	/*check if the ball is out of the game borders. if so, game is over!*/ 
	protected boolean isOut ()
	{
		if ((pos_x < (gameW.x_leftout + radius)) || (pos_x > (gameW.x_rightout - radius)) || (pos_y < gameW.y_upout + radius) || (pos_y > gameW.y_downout - radius)) {	
			resetBallPosition();
			outSound.play();
			player.setLives(player.getLives() - 1);
			if (player.getLives() == 0) {
				player.gameIsOver();
			}
		
			return true;
		}	
		else return false;
	}

	/*draw ball*/
	public void DrawBall (Graphics g)
	{
		g.setColor (color);
		g.fillOval (pos_x - radius, pos_y - radius, 2 * radius, 2 * radius);
	}

}
