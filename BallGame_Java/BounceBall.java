import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;

public class BounceBall extends Ball {

	protected int bounceCount;
	private int resetBounceCount;
	
	public BounceBall(int radius, int initXpos, int initYpos, int speedX, int speedY, int maxBallSpeed, int bounceCountXML, Color color,
			AudioClip outSound, Player player, GameWindow gameW, int type) {
		super(radius, initXpos, initYpos, speedX, speedY, maxBallSpeed, color, outSound, player, gameW, type);
		
		bounceCount = bounceCountXML;
		resetBounceCount = bounceCount;
		// TODO Auto-generated constructor stub
	}

	/*check if the ball is out of the game borders. if so, game is over, unless bouncy!*/ 
	protected boolean isOut ()
	{
		if (((pos_x < gameW.x_leftout + radius) || (pos_x > gameW.x_rightout - radius) || (pos_y < gameW.y_upout + radius) || (pos_y > gameW.y_downout - radius)) && bounceCount == 0) {	
			bounceCount = resetBounceCount;
			resetBallPosition();
			outSound.play();
			player.setLives(player.getLives() - 1);
			if (player.getLives() == 0) {
				player.gameIsOver();
			}
		
			return true;
		}	
		else {
			//change direction, decrement bounceCount
			//change x, y speeds according to which wall was hit
			
			//Hit left wall and y speed is positive(downward), must continue downward just change x
			if (pos_x < gameW.x_leftout + radius) {
				bounceCount--;
				x_speed = -x_speed;
			}
			if (pos_x > gameW.x_rightout - radius) {
				bounceCount--;
				x_speed = -x_speed;
			}
			if (pos_y > gameW.y_downout - radius) {
				bounceCount--;
				y_speed = -y_speed;
			}
			if (pos_y < gameW.y_upout + radius) {
				bounceCount--;
				y_speed = -y_speed;
			}
			return false;
		}
	}
}
