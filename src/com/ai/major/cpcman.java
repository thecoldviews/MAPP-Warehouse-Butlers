/**
 * Modified from JavaiPacman by Junyang Gu
 * 
 * @author Sarthak Ahuja
 */

package com.ai.major;

import java.awt.*;
import java.awt.event.*;

/**
 * the main class of the simulator
 */
public class cpcman extends Frame
implements Runnable, KeyListener, ActionListener, WindowListener
{
	private static final long serialVersionUID = 3582431359568375379L;
	// the timer
	Thread timer;
	int timerPeriod=12;  // in miliseconds

	// the timer will increment this variable to signal a frame
	int signalMove=0;

	// for graphics
	final int canvasWidth=368;
	final int canvasHeight=288+1;

	// the canvas starting point within the frame
	int topOffset;
	int leftOffset;

	// the draw point of maze within the canvas
	final int iMazeX=16;
	final int iMazeY=16;

	// the off screen canvas for the maze
	Image offScreen;
	Graphics offScreenG;

	// the objects    
	cmaze maze;
	cpac pac;
	cpowerdot powerDot;
	cghost [] ghosts;

	// game counters
	final int PAcLIVE=3;
	int pacRemain;
	int changePacRemain;  // to signal redraw remaining pac

	// score
	int score;
	int hiScore;
	int scoreGhost;	// score of eat ghost, doubles every time
	int changeScore;	// signal score change
	int changeHiScore;  // signal change of hi score

	// score images
	Image imgScore;
	Graphics imgScoreG;
	Image imgHiScore;
	Graphics imgHiScoreG;

	// game status
	final int INITIMAGE=100;  // need to wait before paint anything
	final int STARTWAIT=0;  // wait before running
	final int RUNNING=1;
	final int DEADWAIT=2;   // wait after dead
	final int SUSPENDED=3;  // suspended during game
	int gameState;

	final int WAITCOUNT=100;	// 100 frames for wait states
	int wait;	// the counter

	// rounds
	int round;  // the round of current game;

	// whether it is played in a new maze
	boolean newMaze;

	// GUIs
	MenuBar menuBar;
	Menu help;
	MenuItem about;

	// the direction specified by key
	int pacKeyDir;
	int key=0;
	final int NONE=0;
	final int SUSPEND=1;  // stop/start
	final int BOSS=2;      // boss

	////////////////////////////////////////////////
	// initialize the object
	// only called once at the beginning
	////////////////////////////////////////////////
	public cpcman()
	{
		super("P*C MAN");

		// init variables
		hiScore=0;

		gameState=INITIMAGE;

		initGUI();

		addWindowListener(this);

		addKeyListener(this);

		about.addActionListener(this);

		setSize(canvasWidth, canvasHeight);

		show();

		// System.out.println("cpcman done");

	}

	void initGUI()
	{
		menuBar=new MenuBar();
		help=new Menu("HELP");
		about=new MenuItem("About");

		help.add(about);
		menuBar.add(help);

		setMenuBar(menuBar);

		addNotify();  // for updated inset information

		// System.out.println("initGUI done.");
	}

	public void initImages()
	{
		// initialize off screen drawing canvas
		offScreen=createImage(cmaze.iWidth, cmaze.iHeight); 
		if (offScreen==null)
			System.out.println("createImage failed");
		offScreenG=offScreen.getGraphics();

		// initialize maze object
		maze = new cmaze(this, offScreenG);

		// initialize ghosts object
		// 4 ghosts
		ghosts = new cghost[4];
		for (int i=0; i<4; i++)
		{
			Color color;
			if (i==0)
				color=Color.red;
			else if (i==1)
				color=Color.blue;
			else if (i==2)
				color=Color.white;
			else 
				color=Color.orange;
			ghosts[i]=new cghost(this, offScreenG, maze, color);
		}

		// initialize power dot object
		powerDot = new cpowerdot(this, offScreenG, ghosts);

		// initialize pac object
		//      	pac = new cpac(this, offScreenG, maze, powerDot, ghosts);
		pac = new cpac(this, offScreenG, maze, powerDot);

		// initialize the score images
		imgScore=createImage(150,16);
		imgScoreG=imgScore.getGraphics();
		imgHiScore=createImage(150,16);
		imgHiScoreG=imgHiScore.getGraphics();

		imgHiScoreG.setColor(Color.black);
		imgHiScoreG.fillRect(0,0,150,16);
		imgHiScoreG.setColor(Color.red);
		imgHiScoreG.setFont(new Font("Helvetica", Font.BOLD, 12));
		imgHiScoreG.drawString("HI SCORE", 0, 14);

		imgScoreG.setColor(Color.black);
		imgScoreG.fillRect(0,0,150,16);
		imgScoreG.setColor(Color.green);
		imgScoreG.setFont(new Font("Helvetica", Font.BOLD, 12));
		imgScoreG.drawString("SCORE", 0, 14);
	}

	void startTimer()
	{   
		// start the timer
		timer = new Thread(this);
		timer.start();
	}

	void startGame()
	{
		pacRemain=PAcLIVE;
		changePacRemain=1;

		score=0;
		changeScore=1;

		newMaze=true;

		round=1;

		startRound();
	}

	void startRound()
	{
		// new round for maze?
		if (newMaze==true)
		{
			maze.start();
			powerDot.start();
			newMaze=false;
		}

		maze.draw();	// draw maze in off screen buffer

		pac.start();
		pacKeyDir=ctables.DOWN;
		for (int i=0; i<4; i++)
			ghosts[i].start(i,round);

		gameState=STARTWAIT;
		wait=WAITCOUNT;
	}

	///////////////////////////////////////////
	// paint everything
	///////////////////////////////////////////
	public void paint(Graphics g)
	{
		if (gameState == INITIMAGE)
		{
			// System.out.println("first paint(...)...");

			// init images, must be done after show() because of Graphics
			initImages();

			// set the proper size of canvas
			Insets insets=getInsets();

			topOffset=insets.top;
			leftOffset=insets.left;

			//  System.out.println(topOffset);
			//  System.out.println(leftOffset);

			setSize(canvasWidth+insets.left+insets.right,
					canvasHeight+insets.top+insets.bottom);

			setResizable(false);

			// now we can start timer
			startGame();	  

			startTimer();

		}

		g.setColor(Color.black);
		g.fillRect(leftOffset,topOffset,canvasWidth, canvasHeight);

		changeScore=1;
		changeHiScore=1;
		changePacRemain=1;

		paintUpdate(g);
	}

	void paintUpdate(Graphics g)
	{
		// updating the frame

		powerDot.draw();

		for (int i=0; i<4; i++)
			ghosts[i].draw();

		pac.draw();

		// display the offscreen
		g.drawImage(offScreen, 
				iMazeX+ leftOffset, iMazeY+ topOffset, this); 

		// display extra information
		if (changeHiScore==1)
		{
			imgHiScoreG.setColor(Color.black);
			imgHiScoreG.fillRect(70,0,80,16);
			imgHiScoreG.setColor(Color.red);
			imgHiScoreG.drawString(Integer.toString(hiScore), 70,14);
			g.drawImage(imgHiScore, 
					8+ leftOffset, 0+ topOffset, this);

			changeHiScore=0;
		}

		if (changeScore==1)
		{
			imgScoreG.setColor(Color.black);
			imgScoreG.fillRect(70,0,80,16);
			imgScoreG.setColor(Color.green);
			imgScoreG.drawString(Integer.toString(score), 70,14);
			g.drawImage(imgScore, 
					158+ leftOffset, 0+ topOffset, this);

			changeScore=0;
		}

		// update pac life info
		if (changePacRemain==1)
		{
			int i;
			for (i=1; i<pacRemain; i++)
			{
				g.drawImage(pac.imagePac[0][0], 
						16*i+ leftOffset, 
						canvasHeight-18+ topOffset, this);
			}
			g.drawImage(powerDot.imageBlank, 
					16*i+ leftOffset, 
					canvasHeight-17+ topOffset, this); 

			changePacRemain=0;
		}
	}

	////////////////////////////////////////////////////////////
	// controls moves
	// this is the routine running at the background of drawings
	////////////////////////////////////////////////////////////
	void move()
	{
		int k;

		int oldScore=score;

		for (int i=0; i<4; i++)
			ghosts[i].move(pac.iX, pac.iY, pac.iDir);

		k=pac.move(pacKeyDir);

		if (k==1)	// eaten a dot
		{
			changeScore=1;
			score+= 10 * ((round+1)/2) ;
		}
		else if (k==2)	// eaten a powerDot
		{
			scoreGhost=200;
		}

		if (maze.iTotalDotcount==0)
		{
			gameState=DEADWAIT;
			wait=WAITCOUNT;
			newMaze=true;
			round++;
			return;
		}

		for (int i=0; i<4; i++)
		{
			k=ghosts[i].testCollision(pac.iX, pac.iY);
			if (k==1)	// kill pac
			{
				pacRemain--;
				changePacRemain=1;
				gameState=DEADWAIT;	// stop the game
				wait=WAITCOUNT;
				return;	
			}
			else if (k==2)	// caught by pac
			{
				score+= scoreGhost * ((round+1)/2) ;
				changeScore=1;
				scoreGhost*=2;
			}		
		}

		if (score>hiScore)
		{
			hiScore=score;
			changeHiScore=1;
		}

		if ( changeScore==1 )
		{
			if ( score/10000 - oldScore/10000 > 0 )
			{
				pacRemain++;			// bonus
				changePacRemain=1;
			}
		}
	}	

	///////////////////////////////////////////
	// this is the routine draw each frames
	///////////////////////////////////////////
	public void update(Graphics g)
	{
		// System.out.println("update called");
		if (gameState == INITIMAGE)
			return;

		// seperate the timer from update
		if (signalMove!=0)
		{
			// System.out.println("update by timer");
			signalMove=0;

			if (wait!=0)
			{
				wait--;
				return;
			}

			switch (gameState)
			{
			case STARTWAIT: 
				if (pacKeyDir==ctables.UP)	// the key to start game
					gameState=RUNNING;
				else
					return;
				break;
			case RUNNING:
				if (key==SUSPEND)
					gameState=SUSPENDED;
				else
					move();
				break;
			case DEADWAIT:
				if (pacRemain>0)
					startRound();
				else
					startGame();
				gameState=STARTWAIT;
				wait=WAITCOUNT;
				pacKeyDir=ctables.DOWN;
				break;
			case SUSPENDED:
				if (key==SUSPEND)
					gameState=RUNNING;
				break;
			}
			key=NONE;
		}

		paintUpdate(g);	
	}

	///////////////////////////////////////
	// process key input
	///////////////////////////////////////
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_L:
			pacKeyDir=ctables.RIGHT;
			// e.consume();
			break;
		case KeyEvent.VK_UP:
			pacKeyDir=ctables.UP;
			// e.consume();
			break;
		case KeyEvent.VK_LEFT:
			pacKeyDir=ctables.LEFT;
			// e.consume();
			break;
		case KeyEvent.VK_DOWN:
			pacKeyDir=ctables.DOWN;
			// e.consume();
			break;
		case KeyEvent.VK_S:
			key=SUSPEND;
			break;
		case KeyEvent.VK_B:
			key=BOSS;
			break;
		}
	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	/////////////////////////////////////////////////
	// handles menu event
	/////////////////////////////////////////////////
	public void actionPerformed(ActionEvent e)
	{
		if (gameState==RUNNING)
			key=SUSPEND;
		new cabout(this);
		// e.consume();
	}

	///////////////////////////////////////////////////
	// handles window event
	///////////////////////////////////////////////////
	public void windowOpened(WindowEvent e)
	{}

	public void windowClosing(WindowEvent e)
	{
		dispose();
	}

	public void windowClosed(WindowEvent e)
	{}

	public void windowIconified(WindowEvent e)
	{}

	public void windowDeiconified(WindowEvent e)
	{}

	public void windowActivated(WindowEvent e)
	{}

	public void windowDeactivated(WindowEvent e)
	{}

	/////////////////////////////////////////////////
	// the timer
	/////////////////////////////////////////////////
	public void run()
	{
		while (true)
		{	
			try { Thread.sleep(timerPeriod); } 
			catch (InterruptedException e)
			{
				return;
			}

			signalMove++;
			repaint();
		}
	}

	// for applet the check state
	boolean finalized=false;

	public void dispose()
	{
		//      timer.stop();	// deprecated
		// kill the thread
		timer.interrupt();

		// the off screen canvas
//		Image offScreen=null;
		offScreenG.dispose();
		offScreenG=null;

		// the objects    
		maze=null;
		pac=null;
		powerDot=null;
		for (int i=0; i<4; i++)
			ghosts[i]=null;
		ghosts=null;

		// score images
		imgScore=null;
		imgHiScore=null;
		imgScoreG.dispose();
		imgScoreG=null;
		imgHiScoreG.dispose();
		imgHiScoreG=null;

		// GUIs
		menuBar=null;
		help=null;
		about=null;

		super.dispose();

		finalized=true;
	}

	public boolean isFinalized() {
		return finalized;
	}

	public void setFinalized(boolean finalized) {
		this.finalized = finalized;
	}
}












