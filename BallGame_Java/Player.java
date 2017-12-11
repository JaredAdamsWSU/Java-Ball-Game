public class Player
{
	private int score;			   //player score
	private int lives;			   //player lives
	private int scoreForLife;	   //Score needed to gain life
	private int defaultScoreForLife;
	private boolean gameover=false;	
	public int scoreConstant = 15; //This constant value is used in score calculation. You don't need to change this. 
	
	
	public Player(int numLives, int score2EarnLife)
	{
		score = 0; //initialize the score to 0
		lives = numLives;
		defaultScoreForLife = score2EarnLife;
		scoreForLife = score2EarnLife;

	}
	
	/* get player score*/
	public int getScore ()
	{
		return score;
	}
	/* get player score for life*/
	public int getScoreForLife ()
	{
		return scoreForLife;
	}
	public void updateScoreForLife ()
	{
		scoreForLife = scoreForLife + defaultScoreForLife;
	}
	/* get player lives*/
	public int getLives ()
	{
		return lives;
	}
	/* Set player lives*/
	public void setLives (int numLives)
	{
		lives = numLives;
	}
	//add a life
	public void addLife ()
	{
		lives++;
	}
	/*check if the game is over*/
	public boolean isGameOver ()
	{
		return this.gameover;
	}
	/*update player score*/
	public void addScore (int plus)
	{
		score += plus;
	}
	/*update "game over" status*/
	public void gameIsOver ()
	{
			gameover = true;
	}
}