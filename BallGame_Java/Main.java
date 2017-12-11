import java.awt.*;
import java.util.*;
import java.applet.*;
import java.net.*;
import java.security.AccessController;
import java.awt.event.MouseEvent;
import javax.swing.event.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FilePermission;
/*<applet code="Main" height=400 width=400></applet>*/


public class Main extends Applet implements Runnable
{

/* Configuration arguments. These should be initialized with the values read from the config.xml file*/					
    private int numBalls;
/*end of config arguments*/

    private int refreshrate = 15;	           //Refresh rate for the applet screen. Do not change this value. 
	private boolean isStoped = true;		     
    Font f = new Font ("Arial", Font.BOLD, 18);
	
	private Player player;			           //Player instance.
	private Ball[] BallList;                   //Ball instance. You need to replace this with an array of balls.
	Thread th;						           //The applet thread. 

	AudioClip shotnoise;	
	AudioClip hitnoise;		
	AudioClip outnoise;		
	  
    Cursor c;				
    private GameWindow gwindow;                 // Defines the borders of the applet screen. A ball is considered "out" when it moves out of these borders.
	private Image dbImage;
	private Graphics dbg;

	private int numClicks = 0;					//Statistics
	private int numHits = 0;

	class HandleMouse extends MouseInputAdapter 
	{

    	public HandleMouse() 
    	{
            addMouseListener(this);
        }
		
    	public void mouseClicked(MouseEvent e) 
    	{
    		//add mouse statistics counter here
    		numClicks++;
    		
        	if (!isStoped) {
        		for(int i = 0; i < numBalls; i++)
	        		if (BallList[i].userHit (e.getX(), e.getY())) {
	        			numHits++;
	        			hitnoise.play();
		        		BallList[i].ballWasHit ();
		        		if(player.getScore() >= player.getScoreForLife()) {
		        			player.addLife();
		    				player.updateScoreForLife();
		    			}
		        	}
	        		else {
						shotnoise.play();
					}
			}
			else if (isStoped && e.getClickCount() == 2) {
				isStoped = false;
				init ();
			}
    	}

    	public void mouseReleased(MouseEvent e) 
    	{
           
    	}
        
    	public void RegisterHandler() 
    	{

    	}
    }
	
	
	//xml reader here
	//used https://www.mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/ for reference
	public void xmlReader()
	{
     	try
		{	
			File fXmlFile = new File(".\\config.xml");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
			//init GameWindow
			
			NodeList nList = doc.getElementsByTagName("GameWindow");
			Node nNode = nList.item(0);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				Element eElement = (Element) nNode;
				
				gwindow = new GameWindow(
						Integer.parseInt( eElement.getElementsByTagName("x_leftout").item(0).getTextContent()),
						Integer.parseInt( eElement.getElementsByTagName("x_rightout").item(0).getTextContent()),
						Integer.parseInt( eElement.getElementsByTagName("y_upout").item(0).getTextContent()),
						Integer.parseInt( eElement.getElementsByTagName("y_downout").item(0).getTextContent())
						); 
			}
			
			//Init Player			
			nList = doc.getElementsByTagName("Player");
			nNode = nList.item(0);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) 
			{
				Element eElement = (Element) nNode;
				player = new Player(
						Integer.parseInt( eElement.getElementsByTagName("numLives").item(0).getTextContent()),
						Integer.parseInt( eElement.getElementsByTagName("score2EarnLife").item(0).getTextContent())
						);
			}
			
			//Init NumOfBalls
			nList = doc.getElementsByTagName("numBalls");
			nNode = nList.item(0);
			this.numBalls = Integer.parseInt(nNode.getTextContent());
			
			
			//Ball[] BallList = new Ball[this.numBalls];
			//Ball newBall = this.BallList[0];
			this.BallList = new Ball[numBalls];
			int i = 0;
			
			//Parse the XML file and extract the config items 
			nList = doc.getElementsByTagName("Ball");
			for (int temp = 0; temp < nList.getLength(); temp++) 
			{
				nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) 
				{

					Element eElement = (Element) nNode;
					Color color = (Color) Color.class.getField(eElement.getElementsByTagName("color").item(0).getTextContent()).get(null);
					
					if(eElement.getElementsByTagName("type").item(0).getTextContent().toString().equals("basicball"))
					{
						this.BallList[i] = new Ball(
								Integer.parseInt( eElement.getElementsByTagName("radius").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initXpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initYpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedX").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedY").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("maxBallSpeed").item(0).getTextContent()),
								color, 
								outnoise, player, gwindow, 1
								);	
					}
					// TODO- add the conditions for bounceball and shrink ball
					if(eElement.getElementsByTagName("type").item(0).getTextContent().toString().equals("bounceball"))
					{
						this.BallList[i] = new BounceBall(
								Integer.parseInt( eElement.getElementsByTagName("radius").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initXpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initYpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedX").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedY").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("maxBallSpeed").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("bounceCount").item(0).getTextContent()),
								color,
								outnoise, player, gwindow, 2
								);	
					}
					
					if(eElement.getElementsByTagName("type").item(0).getTextContent().toString().equals("shrinkball"))
					{
						this.BallList[i] = new ShrinkBall(
								Integer.parseInt( eElement.getElementsByTagName("radius").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initXpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("initYpos").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedX").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("speedY").item(0).getTextContent()),
								Integer.parseInt( eElement.getElementsByTagName("maxBallSpeed").item(0).getTextContent()),
								color,
								outnoise, player, gwindow, 3
								);	
					}
					
					i++;
				}
			}
				
		}
		catch (Exception e)
		{
		
			e.printStackTrace();
		}
	}
	
	HandleMouse hm = new HandleMouse();	
	
    /*initialize the game*/
	public void init ()
	{	
		//reads info from XML doc
		this.xmlReader();
		c = new Cursor (Cursor.CROSSHAIR_CURSOR);
		this.setCursor (c);
				
        Color superblue = new Color (0, 0, 255);  
		setBackground (Color.black);
		setFont (f);
		
		if (getParameter ("refreshrate") != null) {
			refreshrate = Integer.parseInt(getParameter("refreshrate"));
		}
		else refreshrate = 15;

		numHits = 0;
		numClicks = 0;
		hitnoise = getAudioClip (getCodeBase() , "gun.au");
		hitnoise.play();
		hitnoise.stop();
		shotnoise = getAudioClip (getCodeBase() , "miss.au");
		shotnoise.play();
		shotnoise.stop();
		outnoise = getAudioClip (getCodeBase() , "error.au");
		outnoise.play();
		outnoise.stop();

		this.setSize(gwindow.x_rightout, gwindow.y_downout); //set the size of the applet window.
	}
	
	/*start the applet thread and start animating*/
	public void start ()
	{		
		if (th==null){
			th = new Thread (this);
		}
		th.start ();
	}
	
	/*stop the thread*/
	public void stop ()
	{
		th=null;
	}

    
	public void run ()
	{	
		/*Lower this thread's priority so it won't infere with other processing going on*/
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

        /*This is the animation loop. It continues until the user stops or closes the applet*/
		while (true) {
			if (!isStoped) {
				for(int i = 0; i < numBalls; i++)
					BallList[i].move();
			}
            /*Display it*/
			repaint();
            
			try {
				
				Thread.sleep (refreshrate);
			}
			catch (InterruptedException ex) {
				
			}			
			Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		}
	}

	
	public void paint (Graphics g)
	{
		/*if the game is still active draw the ball and display the player's score. If the game is active but stopped, ask player to double click to start the game*/ 
		if (!player.isGameOver()) {
			g.setColor (Color.yellow);
			
			g.drawString ("Score: " + player.getScore(), 10, 40);
			g.drawString ("Lives: " + player.getLives(), 10, 70);
			g.drawString ("Next Life at: " + player.getScoreForLife(), 10, 100);
			for(int i = 0; i < numBalls; i++)
				BallList[i].DrawBall(g);
			
			if (isStoped) {
				g.setColor (Color.yellow);
				g.drawString ("Doubleclick on Applet to start Game!", 40, 200);
			}
		}
		/*if the game is over (i.e., the ball is out) display player's score*/
		else {
			g.setColor (Color.yellow);

			g.drawString ("Game over!", 130, 100);	
			
			if (player.getScore() < 300) g.drawString ("Well, it could be better!", 100, 190);
			else if (player.getScore() < 600 && player.getScore() >= 300) g.drawString ("That was not so bad", 100, 190);
			else if (player.getScore() < 900 && player.getScore() >= 600) g.drawString ("That was really good", 100, 190);
			else if (player.getScore() < 1200 && player.getScore() >= 900) g.drawString ("You seem to be very good!", 90, 190);
			else if (player.getScore() < 1500 && player.getScore() >= 1200) g.drawString ("That was nearly perfect!", 90, 190);
			else if (player.getScore() >= 1500) g.drawString ("You are the Champion!",100, 190);
			
			g.setColor (Color.green);
			g.drawString("Game Statistics: " , 100, 220);
			g.setColor (Color.red);
			g.drawString("Times Clicked: " + numClicks,100, 250);
			g.drawString("% of successful Hits: %" + ((float)numHits/(float)numClicks) * 100,100, 280);
			g.drawString("Most hit Type: " + (getMostHitType()),100, 310);
			 
			g.setColor (Color.yellow);
			g.drawString ("Doubleclick on the Applet, to play again!", 20, 340);

			isStoped = true;	
		}
	}
	
	public void update (Graphics g)
	{
		
		if (dbImage == null)
		{
			dbImage = createImage (this.getSize().width, this.getSize().height);
			dbg = dbImage.getGraphics ();
		}
		
		dbg.setColor (getBackground ());
		dbg.fillRect (0, 0, this.getSize().width, this.getSize().height);

		
		dbg.setColor (getForeground());
		paint (dbg);

		
		g.drawImage (dbImage, 0, 0, this);
	}
	
	public String getMostHitType()
	{
		String result;
		int basicCounter = 0;
		int bouncyCounter = 0;
		int shrinkCounter = 0;
		
		for(int i = 0; i < numBalls; i++)
			switch(BallList[i].getType())
			{
				case 1: basicCounter+=BallList[i].getTimesHit();
						break;
				case 2: bouncyCounter+=BallList[i].getTimesHit();
						break;
				case 3: shrinkCounter+=BallList[i].getTimesHit();
						break;
			}
		if(basicCounter > bouncyCounter && basicCounter > shrinkCounter)
			result = "Basic";
		else if(bouncyCounter > basicCounter && bouncyCounter > shrinkCounter)
			result = "Bouncy";
		else if(shrinkCounter > basicCounter && shrinkCounter > bouncyCounter)
			result = "Shrink";
		else
			result = "Ball type TIE for most hit!";
		return result;
	}
}


