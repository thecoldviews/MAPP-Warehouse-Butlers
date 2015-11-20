/* 
 * @author Sarthak Ahuja
 */

package com.ai.major;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * the main class of the simulator
 */
public class Simulator extends Frame
implements Runnable, KeyListener, ActionListener, WindowListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	Map map;
	ArrayList<Item> items;
	ArrayList<Butler> butlers;
	Set<Butler> IdleButlers;
	Set<Butler> NonIdleButlers;

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
	int gameState;

	final int WAITCOUNT=100;	// 100 frames for wait states
	int wait;	// the counter

	// whether it is played in a new maze
	boolean newMaze;

	// GUIs
	MenuBar menuBar;
	Menu help;
	MenuItem about;
	
	Warehouse warehouse;

	int KeyPressed;
	int key=0;
	final int NONE=0;
	
	//INITIALIZATION
	public Simulator(Warehouse warehouse)
	{
		super("MAPP: Warehouse Simulator");
		
		this.warehouse=warehouse;

		hiScore=0;

		gameState=INITIMAGE;

		initGUI();
		
		items = new ArrayList<Item>();
		butlers = new ArrayList<Butler>();
		IdleButlers = new HashSet<Butler>();
		NonIdleButlers = new HashSet<Butler>();

		addWindowListener(this);

		addKeyListener(this);

		about.addActionListener(this);

		setSize(canvasWidth, canvasHeight);

		show();

	}

	//INITIALIZE GUI
	void initGUI()
	{
		menuBar=new MenuBar();
		help=new Menu("File");
		about=new MenuItem("About");

		help.add(about);
		menuBar.add(help);

		setMenuBar(menuBar);

		addNotify(); 
	}

	//INITIALIZE GRAPHICS
	public void initImages()
	{
		// INITIALIZE CANVAS
		offScreen=createImage(Map.iWidth, Map.iHeight); 
		if (offScreen==null)
			System.out.println("createImage failed");
		offScreenG=offScreen.getGraphics();
		
		//INITIALIZE MAP
		map = new Map(this, offScreenG,warehouse);
		
		//GET ITEMS ON THE MAP
		items = map.getItems();
		
		//INITIALIZE BUTLERS
		for (int i=0; i<Utility.numButlers; i++)
		{
			Color color;
			color=Utility.ColorArray[i];
			Butler b =new Butler(this, offScreenG, map, color);
			butlers.add(b);
			IdleButlers.add(b);
		}


		imgScore=createImage(150,16);
		imgScoreG=imgScore.getGraphics();
		imgHiScore=createImage(150,16);
		imgHiScoreG=imgHiScore.getGraphics();

		imgHiScoreG.setColor(Color.black);
		imgHiScoreG.fillRect(0,0,150,16);
		imgHiScoreG.setColor(Color.green);
		imgHiScoreG.setFont(new Font("Helvetica", Font.BOLD, 12));
		imgHiScoreG.drawString("STATES:", 0, 14);

		imgScoreG.setColor(Color.black);
		imgScoreG.fillRect(0,0,150,16);
		imgScoreG.setColor(Color.green);
		imgScoreG.setFont(new Font("Helvetica", Font.BOLD, 12));
		imgScoreG.drawString("MEMORY:", 0, 14);
	}

	//START TIMER
	void startTimer()
	{   
		timer = new Thread(this);
		timer.start();
	}
	
	//START GAME
	void startGame()
	{
		score=0;
		changeScore=1;

		newMaze=true;

		startRound();
	}

	//START ROUND
	void startRound()
	{
		if (newMaze==true)
		{
			map.start();
			for (int i=0; i<items.size(); i++){
				items.get(i).start();
			}
			newMaze=false;
		}

		map.draw();	

		KeyPressed=Utility.DOWN;
		
		for (int i=0; i<items.size(); i++){
			IdleButlers.remove(butlers.get(i));
			NonIdleButlers.add(butlers.get(i));
			butlers.get(i).start(map.getWSX(),map.getWSY(),items.get(i));
		}
		gameState=STARTWAIT;
		wait=WAITCOUNT;
	}

	//PAINT EVERYTHING IN THE BEGINNING
	public void paint(Graphics g)
	{
		
		if (gameState == INITIMAGE)
		{
			initImages();

			Insets insets=getInsets();

			topOffset=insets.top;
			leftOffset=insets.left;

			setSize(canvasWidth+insets.left+insets.right,
					canvasHeight+insets.top+insets.bottom);

			setResizable(false);

			startGame();	  

			startTimer();

		}

		g.setColor(Color.black);
		g.fillRect(leftOffset,topOffset,canvasWidth, canvasHeight);

		changeScore=1;
		changeHiScore=1;

		paintUpdate(g);
	}

	void paintUpdate(Graphics g)
	{
		for (int i=0; i<items.size(); i++)
			items.get(i).draw();
		
		Iterator<Butler> iterator = (Iterator<Butler>)NonIdleButlers.iterator();
		while(iterator.hasNext()){
			(iterator.next()).draw();
		}

		g.drawImage(offScreen, 
				iMazeX+ leftOffset, iMazeY+ topOffset, this); 

		// DISPLAY STATS
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
		
		// UPDATE NUMBER OF AGENTS WORKING 
		Iterator<Butler> iterator_ = (Iterator<Butler>) IdleButlers.iterator();
		int count=0;
		while (iterator_.hasNext())
		{
			g.drawImage(iterator_.next().imageIdle, 
					16*count+ leftOffset, 
					canvasHeight-18+ topOffset, this);
			count++;
		}
	}

	//MAKE A MOVE FOR EACH BUTLER
	void move()
	{
		Iterator<Butler> iterator_ = (Iterator<Butler>) NonIdleButlers.iterator();
		while (iterator_.hasNext())
			iterator_.next().move();
	}	

	//CLOCK FUNCTION
	public void update(Graphics g)
	{
		if (gameState == INITIMAGE)
			return;

		if (signalMove!=0)
		{
			signalMove=0;

			switch (gameState)
			{
			case STARTWAIT: 
				if (KeyPressed==Utility.UP)
					gameState=RUNNING;
				else
					return;
				break;
			case RUNNING:
					move();
				break;
			}
			key=NONE;
		}

		paintUpdate(g);	
	}

	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
		case KeyEvent.VK_UP:
			KeyPressed=Utility.UP;
			break;
		}
	}

	public void keyTyped(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}

	public void actionPerformed(ActionEvent e)
	{
		new AboutWindow(this);
	}

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

	boolean finalized=false;

	public void dispose()
	{
		timer.interrupt();

		offScreenG.dispose();
		offScreenG=null;

		map=null;
		items=null;

		butlers=null;
		IdleButlers=null;
		NonIdleButlers=null;

		imgScore=null;
		imgHiScore=null;
		imgScoreG.dispose();
		imgScoreG=null;
		imgHiScoreG.dispose();
		imgHiScoreG=null;

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












