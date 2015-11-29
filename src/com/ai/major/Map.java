/* 
 * @author Sarthak Ahuja
 */
package com.ai.major;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.util.ArrayList;

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
	
	int wsX;
	int wsY;
	
	int gX;
	int gY;

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
	int[][] iMaze;
	
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
		iMaze=new int[HEIGHT][WIDTH];
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
				case '-':
					k=WSDOOR;
					break;
				case '*':
					k=BELTDOOR;
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
				iMaze[i][j]=k;
			}
		// create initial maze image
		createImage();	
	}

	public void draw()
	{
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

	int getWSX(){
		return wsX;
	}
	
	int getWSY(){
		return wsY;
	}
	
	int getGX(){
		return gX;
	}
	
	int getGY(){
		return gY;
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
				if (iMaze[i][j]==WSDOOR)
				{
					g.drawLine(j*16,i*16+8,j*16+16,i*16+8);
					continue;
				}
				if (iMaze[i][j]==BELTDOOR)
				{	
					g.setColor(Color.red);
					g.drawLine(j*16,i*16+8,j*16+16,i*16+8);
					g.setColor(Color.green);
					continue;
				}
				if(iMaze[i][j]==ITEM){
					items.add(new Item(applet, graphics,j,i));
				}
				if(iMaze[i][j]==WORKSTATION){
					wsY=j;
					wsX=i;
				}
				if(iMaze[i][j]==BELT){
					gY=j;
					gX=i;
				}
				for (iDir=Utility.RIGHT; iDir<=Utility.DOWN; iDir++)
				{
					
					if (iMaze[i][j]!=WALL)	continue;
					switch (iDir)
					{
					case Utility.UP:
						if (i==0)	break;
						if (iMaze[i-1][j]==WALL)
							break;
						DrawBoundary(g, j, i-1, Utility.DOWN);
						break;
					case Utility.RIGHT:
						if (j==WIDTH-1)	break;
						if (iMaze[i][j+1]==WALL)
							break;
						DrawBoundary(g, j+1,i, Utility.LEFT);
						break;
					case Utility.DOWN:
						if (i==HEIGHT-1)	break;
						if (iMaze[i+1][j]==WALL)
							break;
						DrawBoundary(g, j,i+1, Utility.UP);
						break;
					case Utility.LEFT:
						if (j==0)	break;
						if (iMaze[i][j-1]==WALL)
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
			if (iMaze[row+1][col]!=WALL)
				// down empty
				if (iMaze[row+1][col-1]!=WALL)
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
			if (iMaze[row-1][col]!=WALL)
				// upper empty
				if (iMaze[row-1][col-1]!=WALL)
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
			if (iMaze[row+1][col]!=WALL)
				// down empty
				if (iMaze[row+1][col+1]!=WALL)
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
			if (iMaze[row-1][col]!=WALL)
				// upper empty
				if (iMaze[row-1][col+1]!=WALL)
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
			if (iMaze[row][col-1]!=WALL)
				// left empty
				if (iMaze[row-1][col-1]!=WALL)
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
			if (iMaze[row][col+1]!=WALL)
				// right empty
				if (iMaze[row-1][col+1]!=WALL)
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
			if (iMaze[row][col-1]!=WALL)
				// left empty
				if (iMaze[row+1][col-1]!=WALL)
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
			if (iMaze[row][col+1]!=WALL)
				// right empty
				if (iMaze[row+1][col+1]!=WALL)
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

