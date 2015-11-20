/* 
 * @author Sarthak Ahuja
 */
package com.ai.major;

import java.lang.Error;
import com.ai.major.Utility;
import java.awt.*;

public class Butler
{
	final int IDLE=0;
	final int FETCH=1;
	final int DELIVER=2;
	final int RETURN=3;

	final int[] steps=	{7, 7, 1, 1};
	final int[] frames=	{8, 8, 2, 1};

	Item assignment;
	
	SpeedControl speed=new SpeedControl();

	int iX, iY, iDir, iStatus;
	int iBlink, iBlindCount;

	final int DIR_FACTOR=2;
	final int POS_FACTOR=10;

	Window applet;
	Graphics graphics;

	Map maze;

	Image imageIdle;
	Image imageOnDuty;
	
	Color color;
	
	//INITIALIZE
	Butler(Window a, Graphics g, Map m, Color color)
	{
		applet=a;
		graphics=g;
		maze=m;

		this.color=color;
		
		imageOnDuty=applet.createImage(18,18);

		imageIdle=applet.createImage(18,18);
		Visuals.drawButler(imageIdle,1, Color.white);
		
		Visuals.drawButler(imageOnDuty,1, color);
		
		assignment=null;
	}

	//PLACE BUTLER
	public void start(int X ,int Y, Item assignment)
	{
		iX=Y; iY=X;
		iDir=3;
		iStatus=FETCH;
		this.assignment=assignment;
		this.assignment.inBound(this);
		speed.start(steps[iStatus], frames[iStatus]);
	}

	//
	public void draw()
	{
		if (iStatus==IDLE || iStatus==RETURN || iStatus==DELIVER)
			graphics.drawImage(imageIdle, iX-1, iY-1, applet);
		else if (iStatus==FETCH)
			graphics.drawImage(imageOnDuty, iX-1, iY-1, applet);
	}  

	//MAKE A MOVE ACCORDING TO STATUS
	public void move()
	{
		if (speed.isMove()==0)
			// no move
			return;

		if (iX%16==0 && iY%16==0)
		{
			switch (iStatus)
			{
			case IDLE:
				iDir=IdleSelect();
				break;
			case FETCH:
				iDir=ItemDirectionSelect(0,0,0);
				break;
			case DELIVER:
				iDir=BeltDirectionSelect(0,0,0);
				break;
			case RETURN:
				iDir=ReturnDirectionSelect(0,0,0);
			}
		}

		if (iStatus!=DELIVER)
		{
			iX+= Utility.iXDirection[iDir];
			iY+= Utility.iYDirection[iDir];
		}
		else
		{	
			iX+=2* Utility.iXDirection[iDir];
			iY+=2* Utility.iYDirection[iDir];
		}

	}
	
	//STAY STILL
	public int IdleSelect()
	// count available directions
	throws Error
	{
		int iM,i,iRand;
		int iDirTotal=0;

		for (i=0; i<4; i++)
		{
			iM=maze.iMaze[iY/16 + Utility.iYDirection[i]]
			              [iX/16 + Utility.iXDirection[i]];
			if (iM!=Map.WALL && i != Utility.iBack[iDir] )
			{
				iDirTotal++;
			}
		}
		// randomly select a direction
		if (iDirTotal!=0)
		{
			iRand=Utility.RandSelect(iDirTotal);
			if (iRand>=iDirTotal)
				throw new Error("iRand out of range");
			//				exit(2);
			for (i=0; i<4; i++)
			{
				iM=maze.iMaze[iY/16+ Utility.iYDirection[i]]
				              [iX/16+ Utility.iXDirection[i]];
				if (iM!=Map.WALL && i != Utility.iBack[iDir] )
				{
					iRand--;
					if (iRand<0)
						// the right selection
					{
						if (iM== Map.WSDOOR)
							iStatus=FETCH;
						iDir=i;	break;
					}
				}
			}
		}	
		return(iDir);	
	}

	//RETURN TO WORKSTATION
	public int ReturnDirectionSelect(int iPacX, int iPacY, int iPacDir)
	// count available directions
	throws Error
	{
		int iM,i,iRand;
		int iDirTotal=0;
		int[] iDirCount=new int [4];

		for (i=0; i<4; i++)
		{
			iDirCount[i]=0;
			iM=maze.iMaze[iY/16 + Utility.iYDirection[i]]
			              [iX/16+ Utility.iXDirection[i]];
			if (iM!=Map.WALL && i!= Utility.iBack[iDir] && iM!= Map.WSDOOR )
				// door is not accessible for OUT
			{
				iDirCount[i]++;
				iDirCount[i]+=iDir==iPacDir?
						DIR_FACTOR:0;
				switch (i)
				{
				case 0:	// right
					iDirCount[i] += iPacX > iX ? POS_FACTOR:0;
					break;
				case 1:	// up
					iDirCount[i]+=iPacY<iY?
							POS_FACTOR:0;
					break;
				case 2:	// left
					iDirCount[i]+=iPacX<iX?
							POS_FACTOR:0;
					break;
				case 3:	// down
					iDirCount[i]+=iPacY>iY?
							POS_FACTOR:0;
					break;
				}
				iDirTotal+=iDirCount[i];
			}
		}	
		// randomly select a direction
		if (iDirTotal!=0)
		{	
			iRand=Utility.RandSelect(iDirTotal);
			if (iRand>=iDirTotal)
				throw new Error("iRand out of range");
			// exit(2);
			for (i=0; i<4; i++)
			{
				iM=maze.iMaze[iY/16+ Utility.iYDirection[i]]
				              [iX/16+ Utility.iXDirection[i]];
				if (iM!=Map.WALL && i!= Utility.iBack[iDir] && iM!= Map.WSDOOR )
				{	
					iRand-=iDirCount[i];
					if (iRand<0)
						// the right selection
					{
						iDir=i;	break;
					}
				}
			}	
		}
		else	
			throw new Error("iDirTotal out of range");
		// exit(1);
		return(iDir);
	}

	public void checkStatus()
	{
		if (iStatus==DELIVER || iStatus==FETCH)
		{
			iStatus=DELIVER;
		}
	}

	//GO AFTER ITEM
	public int ItemDirectionSelect(int q,int w, int e)
	throws Error
	{
		int iM,i,iRand;
		int iDirTotal=0;
		int [] iDirCount= new int [4];

		for (i=0; i<4; i++)
		{
			iDirCount[i]=0;
			iM=maze.iMaze[iY/16 + Utility.iYDirection[i]]
			              [iX/16+Utility.iXDirection[i]];
			if (iM!= Map.WALL && i!= Utility.iBack[iDir])
			{
				iDirCount[i]++;
				switch (i)
				{
				// door position 10,6
				case 0:	// right
					iDirCount[i]+=160>iX?
							POS_FACTOR:0;
					break;
				case 1:	// up
					iDirCount[i]+=96<iY?
							POS_FACTOR:0;
					break;
				case 2:	// left
					iDirCount[i]+=160<iX?
							POS_FACTOR:0;
					break;
				case 3:	// down
					iDirCount[i]+=96>iY?
							POS_FACTOR:0;
					break;
				}
				iDirTotal+=iDirCount[i];
			}	
		}
		// randomly select a direction
		if (iDirTotal!=0)
		{
			iRand= Utility.RandSelect(iDirTotal);
			if (iRand>=iDirTotal)
				throw new Error("RandSelect out of range");
			//				exit(2);
			for (i=0; i<4; i++)
			{
				iM=maze.iMaze[iY/16+ Utility.iYDirection[i]]
				              [iX/16+ Utility.iXDirection[i]];
				if (iM!= Map.WALL && i!= Utility.iBack[iDir])
				{
					iRand-=iDirCount[i];
					if (iRand<0)
						// the right selection
					{
						if (iM== Map.WSDOOR)
							iStatus=IDLE;
						iDir=i;	break;
					}
				}
			}
		}
		else
			throw new Error("iDirTotal out of range");
		return(iDir);	
	}	

	//GO TOWARDS CONVEYOR BELT
	public int BeltDirectionSelect(int iPacX, int iPacY, int iPacDir)
	// count available directions
	throws Error
	{
		int iM,i,iRand;
		int iDirTotal=0;
		int [] iDirCount = new int [4];

		for (i=0; i<4; i++)
		{
			iDirCount[i]=0;
			iM=maze.iMaze[iY/16+ Utility.iYDirection[i]][iX/16+ Utility.iXDirection[i]];
			if (iM != Map.WALL && i != Utility.iBack[iDir] && iM != Map.WSDOOR)
				// door is not accessible for OUT
			{
				iDirCount[i]++;
				iDirCount[i]+=iDir==iPacDir?
						DIR_FACTOR:0;
				switch (i)
				{
				case 0:	// right
					iDirCount[i]+=iPacX<iX?
							POS_FACTOR:0;
					break;
				case 1:	// up
					iDirCount[i]+=iPacY>iY?
							POS_FACTOR:0;
					break;
				case 2:	// left
					iDirCount[i]+=iPacX>iX?
							POS_FACTOR:0;
					break;
				case 3:	// down
					iDirCount[i]+=iPacY<iY?
							POS_FACTOR:0;
					break;
				}
				iDirTotal+=iDirCount[i];
			}	
		}
		// randomly select a direction
		if (iDirTotal!=0)
		{
			iRand=Utility.RandSelect(iDirTotal);
			if (iRand>=iDirTotal)
				throw new Error("RandSelect out of range");
			//				exit(2);
			for (i=0; i<4; i++)
			{	
				iM=maze.iMaze[iY/16+ Utility.iYDirection[i]]
				              [iX/16+ Utility.iXDirection[i]];
				if (iM!= Map.WALL && i!= Utility.iBack[iDir])
				{	
					iRand-=iDirCount[i];
					if (iRand<0)
						// the right selection
					{
						iDir=i;	break;
					}
				}
			}
		}
		else
			throw new Error("iDirTotal out of range");
		return(iDir);
	}

}


