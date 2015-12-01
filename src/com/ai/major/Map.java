/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.util.ArrayList;

import javafx.geometry.Pos;

/* define the maze */
public class Map
{
	// constant definitions
	static final int BLANK=0;
	static final int WALL=1;
	static final int WSDOOR=2;
	static final int BELTDOOR=3;
	static final int WORKSTATION=4;
	static final int BELT=6;
	static final int ITEM=8;

	static final int HEIGHT=16;
	static final int WIDTH=21;
	
	ArrayList<Position> ws;
	ArrayList<Position> gs;

	static final int iHeight=HEIGHT*16;
	static final int iWidth=WIDTH*16;

	// the applet the object associate with
	Window applet;
	// the graphics it will be using
	Graphics graphics;

	// the maze image
	Image imageMaze;

	// the dot image
	Image imageDot;

	// total dots left
	int iTotalDotcount;

	// the status of maze
	Position[][] environment;
	
	ArrayList<Item> items;
	
	Warehouse warehouse;


	// initialize the maze
	Map(Window a, Graphics g, Warehouse warehouse)
	{
		// setup associations
		applet=a;
		graphics=g;
		this.warehouse=warehouse;
		imageMaze=applet.createImage(iWidth, iHeight);
		imageDot=applet.createImage(2,2);
		items=new ArrayList<Item>();
		ws=new ArrayList<Position>();
		gs=new ArrayList<Position>();
		environment=new Position[HEIGHT][WIDTH];
		for (int i =0; i<HEIGHT;i++){
			for (int j =0;j<WIDTH;j++)
			{
				this.environment[i][j] = new Position();
				this.environment[i][j].setColumn(j);
				this.environment[i][j].setRow(i);
				
			}
		}
	}

	public void printWarehouse()
	{
		for (int i =0;i<environment.length;i++)
		{
			for (int j = 0; j<environment[0].length;j++)
			{
				if (environment[i][j].isWall)
				   System.out.print("W");
				else if (environment[i][j].isButler)
					System.out.print(environment[i][j].butler.myNumber+1);
				else 
					System.out.print("0");
				System.out.print(" ");
			}
			System.out.println();
		}
		
	}
	
	public void move(Butler butler, Position from , Position to)
	{
		System.out.println("Before moving "+butler+" from "+from+" to "+to);
		//printWarehouse();
		if (from.butler!=butler)
		{
			System.err.println("BUtlers MISMATCH!!!");
			System.out.println(" butler's current position is "+butler.currentPosition.hashCode()+ "and position is "+from.hashCode());
			System.out.println("From butler is "+from.butler+" and moving is "+butler);
			return;
		}
		butler.move(to);
		from.isButler = false;
		System.out.println("Setting "+from.hashCode()+"to false");
		from.butler = null;
		to.isButler = true;
		System.out.println("Setting "+to.hashCode()+"to true");
		to.butler = butler;
		System.out.println("After moving "+butler+" from "+from+" to "+to);
		//printWarehouse();
		//System.exit(0);
		
	}
	public void start()
	{
		int i,j,k;
		iTotalDotcount=0;
		for (i=0; i<HEIGHT; i++)
			for (j=0; j<WIDTH; j++)
			{
				switch (warehouse.MazeDefine[i].charAt(j))
				{
				case ' ':
					k=BLANK;
					break;
				case 'X':
					k=WALL;
					break;
				case 'O':
					k=ITEM;
					break;
				case 'W':
					k=WORKSTATION;
					break;
				case 'G':
					k=BELT;
					break;
				default:
					k=BLANK;
					break;
				}
				environment[i][j].setType(k);
			}
		// create initial maze image
		createImage();
	}
	
	public void draw()
	{
		Simulator.sdebugger("Draw Map");
		graphics.drawImage(imageMaze,0,0,applet);
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}

	void createImage()
	{
		// create the image of the maze
		Graphics gmaze=imageMaze.getGraphics();

		// background
		gmaze.setColor(Color.black);
		gmaze.fillRect(0,0,iWidth,iHeight);

		DrawWall(gmaze);
	}

	ArrayList<Position> getWS(){
		return ws;
	}
	
	ArrayList<Position> getGS(){
		return gs;
	}
	
	void DrawWall(Graphics g)
	{
		int i,j;
		int iDir;

		g.setColor(Color.green);

		for (i=0; i<HEIGHT; i++)
		{
			for (j=0; j<WIDTH; j++)
			{
				if (environment[i][j].getType()==WSDOOR)
				{
					g.drawLine(j*16,i*16+8,j*16+16,i*16+8);
					continue;
				}
				if (environment[i][j].getType()==BELTDOOR)
				{	
					g.setColor(Color.red);
					g.drawLine(j*16,i*16+8,j*16+16,i*16+8);
					g.setColor(Color.green);
					continue;
				}
				if(environment[i][j].getType()==ITEM){
					//System.out.println(i);
					//System.out.println(j);
					items.add(new Item(applet, graphics,environment[i][j]));
				}
				if(environment[i][j].getType()==WORKSTATION){
					ws.add(new Position(i,j));
				}
				if(environment[i][j].getType()==BELT){
					gs.add(new Position(i,j));
				}
				for (iDir=Utility.RIGHT; iDir<=Utility.DOWN; iDir++)
				{
					
					if (environment[i][j].getType()!=WALL)	continue;
					switch (iDir)
					{
					case Utility.UP:
						if (i==0)	break;
						if (environment[i-1][j].getType()==WALL)
							break;
						DrawBoundary(g, j, i-1, Utility.DOWN);
						break;
					case Utility.RIGHT:
						if (j==WIDTH-1)	break;
						if (environment[i][j+1].getType()==WALL)
							break;
						DrawBoundary(g, j+1,i, Utility.LEFT);
						break;
					case Utility.DOWN:
						if (i==HEIGHT-1)	break;
						if (environment[i+1][j].getType()==WALL)
							break;
						DrawBoundary(g, j,i+1, Utility.UP);
						break;
					case Utility.LEFT:
						if (j==0)	break;
						if (environment[i][j-1].getType()==WALL)
							break;
						DrawBoundary(g, j-1,i, Utility.RIGHT);
						break;
					default:	
					}
				}
			}
		}
	}

	void DrawBoundary(Graphics g, int col, int row, int iDir)
	{
		int x,y;

		x=col*16;	y=row*16;

		switch (iDir)
		{
		case Utility.LEFT:
			// draw lower half segment 
			if (environment[row+1][col].getType()!=WALL)
				// down empty
				if (environment[row+1][col-1].getType()!=WALL)
					// left-down empty
				{
					//arc(x-8,y+8,270,0,6);
					g.drawArc(x-8-6, y+8-6, 12, 12, 270, 100);
				}
				else
				{
					g.drawLine(x-2,y+8,x-2,y+16);
				}
			else
			{
				g.drawLine(x-2,y+8,x-2,y+17);
				g.drawLine(x-2,y+17,x+7,y+17);
			}

			// Draw upper half segment
			if (environment[row-1][col].getType()!=WALL)
				// upper empty
				if (environment[row-1][col-1].getType()!=WALL)
					// upper-left empty
				{
					//						arc(x-8,y+7,0,90,6);
					g.drawArc(x-8-6, y+7-6, 12,12,0, 100);
				}
				else
				{
					g.drawLine(x-2,y,x-2,y+7);
				}
			else
			{
				g.drawLine(x-2,y-2,x-2,y+7);
				g.drawLine(x-2,y-2,x+7,y-2);
			}	
			break;

		case Utility.RIGHT:
			// draw lower half segment 
			if (environment[row+1][col].getType()!=WALL)
				// down empty
				if (environment[row+1][col+1].getType()!=WALL)
					// down-right empty
				{
					//						arc(x+16+7,y+8,180,270,6);
					g.drawArc(x+16+7-6, y+8-6, 12,12,180, 100);
				}
				else
				{
					g.drawLine(x+17,y+8,x+17,y+15);
				}
			else	
			{
				g.drawLine(x+8,y+17,x+17,y+17);
				g.drawLine(x+17,y+8,x+17,y+17);
			}	
			// Draw upper half segment 
			if (environment[row-1][col].getType()!=WALL)
				// upper empty
				if (environment[row-1][col+1].getType()!=WALL)
					// upper-right empty
				{
					//						arc(x+16+7,y+7,90,180,6);
					g.drawArc(x+16+7-6, y+7-6, 12,12, 90, 100);
				}
				else
				{
					g.drawLine(x+17,y,x+17,y+7);
				}
			else
			{
				g.drawLine(x+8,y-2,x+17,y-2);
				g.drawLine(x+17,y-2,x+17,y+7);
			}
			break;

		case Utility.UP:
			// draw left half segment 
			if (environment[row][col-1].getType()!=WALL)
				// left empty
				if (environment[row-1][col-1].getType()!=WALL)
					// left-upper empty
				{
					//  arc(x+7,y-8,180,270,6);
					g.drawArc(x+7-6, y-8-6, 12,12, 180, 100);
				}
				else
				{
					g.drawLine(x,y-2,x+7,y-2);
				}

			// Draw right half segment
			if (environment[row][col+1].getType()!=WALL)
				// right empty
				if (environment[row-1][col+1].getType()!=WALL)
					// right-upper empty
				{
					//						arc(x+8,y-8,270,0,6);
					g.drawArc(x+8-6, y-8-6, 12,12, 270, 100);
				}
				else
				{
					g.drawLine(x+8,y-2,x+16,y-2);
				}
			break;

		case Utility.DOWN:
			// draw left half segment
			if (environment[row][col-1].getType()!=WALL)
				// left empty
				if (environment[row+1][col-1].getType()!=WALL)
					// left-down empty
				{
					//						arc(x+7,y+16+7,90,180,6);
					g.drawArc(x+7-6, y+16+7-6, 12,12, 90, 100);
				}
				else
				{
					g.drawLine(x,y+17,x+7,y+17);
				}

			// Draw right half segment
			if (environment[row][col+1].getType()!=WALL)
				// right empty
				if (environment[row+1][col+1].getType()!=WALL)
					// right-down empty
				{
					//						arc(x+8,y+16+7,0,90,6);
					g.drawArc(x+8-6, y+16+7-6, 12,12, 0, 100);
				}
				else
				{	
					g.drawLine(x+8,y+17,x+15,y+17);
				}
			break;
		}	
	}

}

