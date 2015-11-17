/* 
 * @author Sarthak Ahuja
 */
package com.ai.major;

import java.awt.*;

public class cpac
{
	// frames to wait after eaten a dot
	final int DOT_WAIT=4;

	int iDotWait;

	// current position
	int iX, iY;
	// current direction
	int iDir;

	// the applet this object is associated to
	Window applet;
	Graphics graphics;

	// the pac image
	Image [][] imagePac;

	// the knowledge of the maze
	Map maze;

	// the knowledge of the power dots
	Item powerDot;

	//    cpacmove cAuto;

	//  cpac(Window a, Graphics g, Map m, cpowerdot p, cghost cghost[])
	cpac(Window a, Graphics g, Map m, Item p)    {
		applet=a;
		graphics=g;
		maze=m;
		powerDot=p;

		//      cAuto=new cpacmove(this, cghost, m);

		// initialize pac and pac image
		imagePac=new Image[4][4];
		for (int i=0; i<4; i++)
			for (int j=0; j<4; j++)
			{
				imagePac[i][j]=applet.createImage(18,18);
				Visuals.drawPac(imagePac[i][j],i,j);
			}	
	}

	public void start()
	{
		iX=10*16;
		iY=10*16;
		iDir=1;		// downward, illegal and won't move
		iDotWait=0;
	}

	public void draw()
	{
		maze.DrawDot(iX/16, iY/16);
		maze.DrawDot(iX/16+(iX%16>0?1:0), iY/16+(iY%16>0?1:0));

		int iImageStep=(iX%16 + iY%16)/2; 	// determine shape of PAc
		if (iImageStep<4)
			iImageStep=3-iImageStep;
		else
			iImageStep-=4;
		graphics.drawImage(imagePac[iDir][iImageStep], iX-1, iY-1, applet);
	}	

	// return 1 if eat a dot
	// return 2 if eat power dot
	public int move(int iNextDir)
	{
		int eaten=0;

		//      iNextDir=cAuto.GetNextDir();

		if (iNextDir!=-1 && iNextDir!=iDir)	// not set or same
			// change direction
		{
			if (iX%16!=0 || iY%16!=0)
			{
				// only check go back
				if (iNextDir%2==iDir%2)
					iDir=iNextDir;
			}	
			else    // need to see whether ahead block is OK
			{
				if ( mazeOK(iX/16+ Utility.iXDirection[iNextDir],
						iY/16+ Utility.iYDirection[iNextDir]) )
				{
					iDir=iNextDir;
					iNextDir=-1;
				}
			}
		}
		if (iX%16==0 && iY%16==0)
		{

			// see whether has eaten something
			switch (maze.iMaze[iY/16][iX/16])
			{
			case Map.DOT:
				eaten=1;
				maze.iMaze[iY/16][iX/16]=Map.BLANK;	// eat dot
				maze.iTotalDotcount--;
				iDotWait=DOT_WAIT;
				break;
			case Map.POWER_DOT:
				eaten=2;
				powerDot.eat(iX/16, iY/16);
				maze.iMaze[iY/16][iX/16]=Map.BLANK;
				break;
			}

			if (maze.iMaze[iY/16+ Utility.iYDirection[iDir]]
			               [iX/16+ Utility.iXDirection[iDir]]==1)
			{
				return(eaten);	// not valid move
			}
		}
		if (iDotWait==0)
		{
			iX+= Utility.iXDirection[iDir];
			iY+= Utility.iYDirection[iDir];
		}
		else	iDotWait--;
		return(eaten);
	}	

	boolean mazeOK(int iRow, int icol)
	{
		if ( (maze.iMaze[icol][iRow] & ( Map.WALL | Map.DOOR)) ==0)
			return(true);
		return(false);
	}
}









