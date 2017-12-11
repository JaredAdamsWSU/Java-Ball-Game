import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;

public class ShrinkBall extends Ball{
	
	private float initialRadius;

	public ShrinkBall(int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, Color color,
			AudioClip outSound, Player player, GameWindow gameW, int type) {
		super(radius, initXpos, initYpos, speedX, speedY, maxBallSpeed, color, outSound, player, gameW, type);
		// TODO Auto-generated constructor stub
		initialRadius = radius;
	}
	
	/*when the ball is hit, reset the ball location to its initial starting location*/
	/*when the ball is hit, reset the ball location to its initial starting location*/
	public void ballWasHit ()
	{	
		timesHit++;
		setRadius();
		resetBallPosition();
	}

	/*check whether the player hit the ball. If so, update the player score based on the current ball speed. */	
	public boolean userHit (int maus_x, int maus_y)
	{
		
		double x = maus_x - pos_x;
		double y = maus_y - pos_y;

		double distance = Math.sqrt ((x*x) + (y*y));
		
		//ball shrinks 3 times total, after third time being clicked, reset to origin size
		if (distance-this.radius < (int)(player.scoreConstant)) {
			if(radius == ((int)initialRadius))
				player.addScore (player.scoreConstant * Math.abs(x_speed) + player.scoreConstant);
			else if(radius == ((int)(initialRadius * .6))) {
				player.addScore ((player.scoreConstant * Math.abs(x_speed) + player.scoreConstant) * 2);
			}
			else if(radius == ((int)(initialRadius * .3))){
				player.addScore ((player.scoreConstant * Math.abs(x_speed) + player.scoreConstant) * 4);
			}
			else {
				
			}
			return true;
		}
		else return false;
	}
	
	private void setRadius()
	{
		if(timesHit % 3 == 1)
			radius = (int) (initialRadius * .6);
		if(timesHit % 3 == 2)
			radius = (int) (initialRadius * .3);
		if(timesHit % 3 == 0)
			radius = (int) (initialRadius);
	}
}
