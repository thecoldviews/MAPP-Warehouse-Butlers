/* 
 * @author Sarthak Ahuja and Anchita Goel
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
import java.util.Collections;
import java.util.Iterator;

import sun.misc.Signal;

/**
 * the main class of the simulator
 */
public class Simulator extends Frame
implements Runnable, KeyListener, ActionListener, WindowListener
{
	/**
	 * 
	 */
	public static int statesExpanded = 0;
	public static ArrayList<Move> movesThisStep;
	public static boolean heredebug=true;
	public static int here=0;
	private static final long serialVersionUID = 1L;
	private static final long MEGABYTE = 1024L * 1024L;
	public static long maxmemory = 0;
	// the timer
	Thread timer;
	int timerPeriod=12;  // in miliseconds

	// Used to signal frame
	static int signalMove=0;

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
	
	//BOOLEAN MOVE FLAG
	public static boolean boolmove=false;

	// GLOBAL STATIC OBJECTS   
	public static Map map;
	public static ArrayList<Item> items;
	public static ArrayList<Position> workstations;
	public static ArrayList<Position> conveyorbelts;
	public static ArrayList<Butler> butlers;
	public static ArrayList<Butler> reachedTargetThisStep;
	public static ArrayList<Butler> IdleButlers;
	public static ArrayList<Butler> NonIdleButlers;
	public static ArrayList<Butler> ReachedButlers;
	

	// score
	int score;
	int hiScore = FindPath.statesExpanded;
	int scoreGhost;	// score of eat ghost, doubles every time
	int changeScore;	// signal score change
	int changeHiScore;  // signal change of hi score

	// score images
	Image imgScore;
	Graphics imgScoreG;
	Image imgExpand;
	Graphics imgStatesExpanded;

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
		workstations = new ArrayList<Position>();
		conveyorbelts = new ArrayList<Position>();
		butlers = new ArrayList<Butler>();
		IdleButlers = new ArrayList<Butler>();
		NonIdleButlers = new ArrayList<Butler>();
		ReachedButlers = new ArrayList<Butler>();
		reachedTargetThisStep = null;
		boolmove=false;
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
		workstations = map.getWS();
		conveyorbelts = map.getGS();
		
		//INITIALIZE BUTLERS
		for (int i=0; i<Utility.numButlers; i++)
		{
			Color color;
			color=Utility.ColorArray[i];
			Butler b =new Butler(this, offScreenG, map, color,i);
			butlers.add(b);
			IdleButlers.add(b);
		}
		

		imgScore=createImage(150,16);
		imgScoreG=imgScore.getGraphics();
		imgExpand=createImage(150,16);
		imgStatesExpanded=imgExpand.getGraphics();

		imgStatesExpanded.setColor(Color.black);
		imgStatesExpanded.fillRect(0,0,150,16);
		imgStatesExpanded.setColor(Color.green);
		imgStatesExpanded.setFont(new Font("Helvetica", Font.BOLD, 12));
		imgStatesExpanded.drawString("STATES:", 0, 14);

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
		sdebugger("inside startGame");
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
			signalMove++;
		}
		sdebugger("inside Start Round");
		map.draw();	
		KeyPressed=Utility.DOWN;
		gameState=STARTWAIT;
	}
	
	//SIGNAL FOR A MOVE
	public static void makeMove(){
		Simulator.sdebugger("Told Simulator that we are ready for move");
		boolmove=true;
		//System.exit(0);
	}
	
	//SIGNAL COMPLETION A MOVE
	public static void doneMove(){
		Simulator.sdebugger("Simulator Completed a move");
		boolmove=false;
	}
	
	//CHECK FOR A MOVE
	public static boolean checkMove(){
		return boolmove;
	}
	
	public static void debugger(){
		if (Simulator.heredebug) {System.out.println(here);Simulator.here+=Simulator.here+1;}
	}
	
	public static void sdebugger(String s){
		if (Simulator.heredebug) {System.out.println(s);Simulator.here+=Simulator.here+1;}
	}
	
	 public static long bytesToMegabytes(long bytes) {
		    return bytes / MEGABYTE;
		  }
	
	//MAKE A MOVE
	void move(){
		doProgression();
		doRepositioning(NonIdleButlers);
		 Runtime runtime = Runtime.getRuntime();
		    // Run the garbage collector
		    runtime.gc();
		    // Calculate the used memory
		    long memory = runtime.totalMemory() - runtime.freeMemory();
		    maxmemory = Math.max(bytesToMegabytes(memory),maxmemory);
		        
		 
		signalMove++;
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
			sdebugger("inside paint");
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
sdebugger("------------------"+statesExpanded+"----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		map.draw();
		for (int i=0; i<items.size(); i++)
			items.get(i).draw();
		
		Iterator<Butler> iterator = (Iterator<Butler>)NonIdleButlers.iterator();
		while(iterator.hasNext()){
			(iterator.next()).draw();
		}
		Iterator<Butler> iterator2 = (Iterator<Butler>)ReachedButlers.iterator();
		while(iterator2.hasNext()){
			(iterator2.next()).draw();
		}

		g.drawImage(offScreen, 
				iMazeX+ leftOffset, iMazeY+ topOffset, this); 


			imgStatesExpanded.setColor(Color.black);
			imgStatesExpanded.fillRect(70,0,80,16);
			imgStatesExpanded.setColor(Color.red);
			imgStatesExpanded.drawString(Integer.toString(statesExpanded), 70,14);
			g.drawImage(imgExpand, 
					8+ leftOffset, 0+ topOffset, this);



		
			imgScoreG.setColor(Color.black);
			imgScoreG.fillRect(70,0,80,16);
			imgScoreG.setColor(Color.green);
			imgScoreG.drawString(Long.toString(maxmemory), 70,14);
			g.drawImage(imgScore, 
					158+ leftOffset, 0+ topOffset, this);

			changeScore=0;
		
		
		
	}

	//CLOCK FUNCTION
	public void update(Graphics g)
	{
		Simulator.sdebugger("Checking Game Status!");
		
		if (gameState == INITIMAGE){
			return;
		}

		if (signalMove!=0)
		{
			Simulator.sdebugger("Processing Signal!");
			
			signalMove=0;

			switch (gameState)
			{
			case STARTWAIT: 
				Simulator.sdebugger("STARTWAIT");
				if (KeyPressed==Utility.UP){
					Simulator.sdebugger("PRESSED UP");
					if (testSlidable()){
						gameState=RUNNING;
						signalMove++;
					}
				}
				else{
					signalMove++;
					return;
				}
				break;
			case RUNNING:
					Simulator.sdebugger("RUNNING");
					if(!Simulator.NonIdleButlers.isEmpty())
						move();
				break;
			}
			key=NONE;
			
		}
		//System.exit(0);
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

			//signalMove++;
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
		workstations=null;
		conveyorbelts=null;

		butlers=null;
		IdleButlers=null;
		NonIdleButlers=null;

		imgScore=null;
		imgExpand=null;
		imgScoreG.dispose();
		imgScoreG=null;
		imgStatesExpanded.dispose();
		imgStatesExpanded=null;

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
	
	//---------MAPP ALGORITHM CLASSES--------//
	public static boolean testSlidable()
	{
		Simulator.sdebugger("TestSlidable");
		for (int i=0;i<Simulator.items.size();i++)
		{
			System.err.println("Finding path for butler "+i);
			while(Simulator.IdleButlers.remove(Simulator.butlers.get(i)));
			Simulator.NonIdleButlers.add(Simulator.butlers.get(i));
			Simulator.butlers.get(i).start(Simulator.items.get(i),Simulator.workstations.get(i));
			System.out.println("WS:"+Simulator.workstations.get(i));
			Simulator.map.environment[Simulator.butlers.get(i).currentPosition.getRow()][Simulator.butlers.get(i).currentPosition.getColumn()].butlerTarget =i;
			Simulator.map.environment[Simulator.butlers.get(i).currentPosition.getRow()][Simulator.butlers.get(i).currentPosition.getColumn()].isButler = true;
			Simulator.map.environment[Simulator.butlers.get(i).currentPosition.getRow()][Simulator.butlers.get(i).currentPosition.getColumn()].butler =Simulator.butlers.get(i);
			PathDetails pd = FindPath.findPathAstar(Simulator.map, i, Simulator.butlers.get(i).currentPosition,Simulator.butlers.get(i).assignment.position);
			 Runtime runtime = Runtime.getRuntime();
			long memory = runtime.totalMemory() - runtime.freeMemory();
		    maxmemory = Math.max(bytesToMegabytes(memory),maxmemory);
		        
			Simulator.butlers.get(i).setPath(pd.path);
			System.err.println(pd.statesExpanded);
			statesExpanded  += pd.statesExpanded;
			System.out.println("Sttstessss000000000000000       "+statesExpanded);
			System.out.println(Simulator.butlers.get(i));
			//System.exit(0);
			if (Simulator.butlers.get(i).path.piPath.isEmpty())
			{
				System.out.println("not slidable");
				System.err.println("NOTTTTTTTTT    SLIDABLE-----");
				return false;
			}
			else
			{
				//System.out.println("---------------Printing some important shizz----------------");
				Path p = Simulator.butlers.get(i).path;
				int piPathSize = p.piPath.size();
				/*for (int k=p.piPath.size()-1;k>=0;k--)
					System.out.println(p.piPath.get(k)+"->");*/
				
				
				/*for (int k=p.piPath.size()-1;k>=0;k--)
				
				{
					System.out.println("Alternate Path: ");
					ArrayList<Position> alternatePath = p.getAlternatePath(p.piPath.get(k));
					for (int m=alternatePath.size()-1;m>=0;m--)
					{
						System.out.println(alternatePath.get(m)+",");
					}
					
				}
				System.out.println("---------------End shizz----------------");*/
			}
		}
		Simulator.map.printWarehouse();
		//System.exit(0);
		return true;
	}
	
	public void doRepositioning(ArrayList<Butler> activeUnits)
	{
		System.out.println("Repositioning");
		for (int i=movesThisStep.size()-1;i>=0;i--)
		{
			Move move = movesThisStep.get(i);
			if (activeUnits.contains(move.butler))
			{
				System.out.println("Moving  "+move.butler+" from "+move.to+" to "+move.from);
				
				Simulator.map.move(move.butler,move.to, move.from);
				signalMove=1;
				paintUpdate(getGraphics());
				try {
					
				    Thread.sleep(100);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
			}
		}
		
	}
	
	
	public ArrayList<Butler> doProgression()
	{
		Simulator.sdebugger("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		Simulator.sdebugger("Inside Progression");
		movesThisStep = new ArrayList<Move>();
		ArrayList<Butler> reachedThisStep  = new ArrayList<>();
		System.err.println("New array for moves");
		ArrayList<ArrayList<Position>> statesVisited = new ArrayList<>();
		for (int i =0; i<NonIdleButlers.size();i++)
		{
			statesVisited.add(new ArrayList<Position>());
		}
		boolean change = false;
		do {
		change = false;
		Collections.sort(NonIdleButlers);
		for (int i=0;i<NonIdleButlers.size();i++)
		{
			
			Butler currentBut = NonIdleButlers.get(i);
			System.out.println("doProgression for butler "+currentBut.myNumber);
			System.out.println("Current position being acquired is "+currentBut.currentPosition.getRow()+","+currentBut.currentPosition.getColumn());
			Position currentPosition = Simulator.map.environment[currentBut.currentPosition.getRow()][currentBut.currentPosition.getColumn()];
			Position nextMove = currentBut.path.returnSuccessor(currentPosition);
			currentBut.path.printPath();
			System.out.println("The next move is "+nextMove);
			Position newBlank;
			if (!currentBut.path.piPath.contains(currentPosition))
			{
				System.out.println("Current position not in the path");
				continue;
			}
			else if (statesVisited.get(i).contains(nextMove))
			{
				System.out.println("Already visited the nextmove");
				continue;
			}
			else if (locationInPrivateZone(currentBut,NonIdleButlers,nextMove))
			{
				System.out.println("Location on the private zone of others");
				continue;
			}
			else if (!nextMove.isOcuupied())
			{
				
				System.out.println("Moving  "+currentBut.myNumber+" from "+currentPosition+" to "+nextMove);
				System.out.println("Moving  "+currentBut.myNumber+" from but "+currentBut.currentPosition.hashCode()+" to  "+currentPosition.hashCode());
				Simulator.map.move(currentBut, currentPosition, nextMove);
				signalMove=1;
				paintUpdate(getGraphics());
				try {
					
				    Thread.sleep(100);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				statesVisited.get(i).add(nextMove);
				Move newMove = new Move(currentBut,currentPosition,nextMove);
				movesThisStep.add(newMove);
				System.err.println("Added move for butler "+currentBut+" from "+currentPosition+" to "+nextMove);
				System.err.println("--------------------------------"+statesExpanded);
				change = true;
				if (currentBut.atDestination())
					{
						while(NonIdleButlers.remove(currentBut));
						ReachedButlers.add(currentBut);
						reachedThisStep.add(currentBut);
						//return reachedThisStep;
					}
			}
			else if ((newBlank = canCreateBlank(currentBut,NonIdleButlers,nextMove,currentBut.path.getAlternatePath(currentPosition)))!=null)
			{
				createBlank(currentBut, newBlank , nextMove, currentBut.path.getAlternatePath(currentPosition));
				System.out.println("Moving  "+currentBut.myNumber+" from "+currentPosition+" to "+nextMove);
				Simulator.map.move(currentBut, currentPosition, nextMove);
				signalMove=1;
				paintUpdate(getGraphics());
				try {
					
				    Thread.sleep(100);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
				    Thread.currentThread().interrupt();
				}
				Move newMove = new Move(currentBut,currentPosition,nextMove);
				statesVisited.get(i).add(nextMove);
				movesThisStep.add(newMove);
				change = true;
				if (currentBut.atDestination())
				{
					while(NonIdleButlers.remove(currentBut));
					ReachedButlers.add(currentBut);
					reachedThisStep.add(currentBut);
					//return reachedThisStep;
				}
			}
			else
			{
				System.out.println("Something else only");
				continue;
			}
			
		}
		
			
			
		}while(change);
		
		return reachedThisStep;
		
	}
	
	
	public void createBlank(Butler butler, Position sourceBlank, Position destBlank, ArrayList<Position> path)
	{
		System.out.println("Blank at "+sourceBlank);
		System.out.println("Moving Blank to "+destBlank);
		System.out.println("Moving blank along the path : ");
		for (int i=path.size()-1;i>=0;i--)
			System.out.println(path.get(i)+"->");
		
		destBlank = Simulator.map.environment[destBlank.getRow()][destBlank.getColumn()];
		while (destBlank.isOcuupied())
		{
			sourceBlank = Simulator.map.environment[sourceBlank.getRow()][sourceBlank.getColumn()];
			int moveFrom = path.indexOf(sourceBlank) - 1;
			
			System.out.println("Moving robot from "+path.get(moveFrom)+" to "+path.get(moveFrom+1));
			Position from = Simulator.map.environment[path.get(moveFrom).getRow()][path.get(moveFrom).getColumn()];
			Position to = Simulator.map.environment[path.get(moveFrom+1).getRow()][path.get(moveFrom+1).getColumn()];
			Move newMove = new Move(from.butler,from,to);
			movesThisStep.add(newMove);
			System.err.println("Added move in createBlank for butler "+from.butler+" from "+from+" to "+to);
			Simulator.map.move(from.butler, from, to);
			signalMove=1;
			paintUpdate(getGraphics());
			try {
				
			    Thread.sleep(100);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			
			
			sourceBlank = path.get(moveFrom);
			
			
		}
		
	}
	
	public static Position canCreateBlank(Butler butler, ArrayList<Butler> active, Position nextMove, ArrayList<Position> alternatePath)
	{
		Iterator iterator = alternatePath.iterator();
		Position l;
		while (iterator.hasNext())
		{
			l = (Position) iterator.next();
			l = Simulator.map.environment[l.getRow()][l.getColumn()];
			System.out.println("Checking for location "+l+" with hash code "+l.hashCode());
			if (locationInPrivateZone(butler, active, l))
			{
				return null;
			}
			else
			{
				if (!l.isOcuupied())
				{
					System.out.println(l+" is blank.");
					return l;
				}
				else
				{
					System.out.println(l+" was not blank.");
				}
			}
		}
		return null;
		
	}
	
	
	public static boolean locationInPrivateZone(Butler currentBut, ArrayList<Butler> active,Position nextMove)
	{
		Collections.sort(active);
		for (int i=0;i<active.size();i++)
		{
			if (active.get(i).priority < currentBut.priority)
			{
				if (active.get(i).getPrivateZone().contains(nextMove))
				{
					System.out.println(nextMove+" in the private zone of "+active.get(i).myNumber);
					return true;
				}
			}
			else
			{
				break;
			}
			
		}
		return false;
	}
}












