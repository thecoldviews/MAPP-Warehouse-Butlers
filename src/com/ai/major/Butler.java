/* 
 * @author Sarthak Ahuja
 */
package com.ai.major;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.util.ArrayList;

public class Butler
{
	final int IDLE=0;
	final int FETCH=1;
	final int DELIVER=2;
	final int RETURN=3;

	int flag;
	int stepcount;
	Item assignment;
	
	SpeedControl speed=new SpeedControl();

	Position currentPosition;
	Position nextPosition;
	int iStatus;
	int iBlink, iBlindCount;

	final int DIR_FACTOR=2;
	final int POS_FACTOR=10;

	Window applet;
	Graphics graphics;
	
	ArrayList<Position> path;

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

		flag=0;
		this.color=color;
		
		imageOnDuty=applet.createImage(18,18);

		imageIdle=applet.createImage(18,18);
		Visuals.drawButler(imageIdle,1, Color.white);
		
		Visuals.drawButler(imageOnDuty,1, color);
		
		assignment=null;
	}

	//PLACE BUTLER
	public void start(ArrayList<Position> positionList, Item assignment)
	{
		currentPosition=positionList.get(0);
		System.out.println(currentPosition);
		path=positionList;
		//iX=Y*16; iY=X*16;
		//iDir=3;
		iStatus=FETCH;
		this.assignment=assignment;
		this.assignment.inBound(this);
	}

	//DRAW THE BUTLER
	public void draw()
	{
		if (iStatus==IDLE || iStatus==RETURN || iStatus==DELIVER)
			graphics.drawImage(imageIdle, currentPosition.getPixelRow()-1, currentPosition.getPixelColumn()-1, applet);
		else if (iStatus==FETCH)
			graphics.drawImage(imageOnDuty, currentPosition.getPixelRow()-1, currentPosition.getPixelColumn()-1, applet);
	}  

	//MAKE A MOVE ACCORDING TO STATUS
	public void move()
	{
		if (speed.isMove()==0)
			// no move
			return;

		System.out.println(currentPosition.getRow()+"CURRENTCOORDINATE"+currentPosition.getColumn());
		System.out.println(currentPosition.getPixelRow()+"CURRENTPIXEL"+currentPosition.getPixelColumn());
		if (currentPosition.getPixelRow()%16==0 && currentPosition.getPixelColumn()%16==0)
		{
			System.out.println("GETTING NEW DIRECTION");
			switch (iStatus)
			{
//			case IDLE:
//				nextPosition=DirectionSelect();
//				break;
			case FETCH:
				nextPosition=PositionSelect();
				break;
			case DELIVER:
				System.out.println("DELIVERED");
				break;
//			case RETURN:
//				nextPosition=DirectionSelect();
			}
		}
		if(nextPosition!=null){
			int dir=getDirection(currentPosition,nextPosition);
			currentPosition.setPixelRow(currentPosition.getPixelRow()+Utility.iXDirection[dir]);
			currentPosition.setPixelColumn(currentPosition.getPixelColumn()+Utility.iYDirection[dir]);
		}
	}
	
	public int getDirection(Position source, Position destination){
		if(source.getRow()-destination.getRow()>1||source.getColumn()-destination.getColumn()>1){
			return 4;
		}
		if(source.getRow()-destination.getRow()<0){
			return 0;
		}
		else if(source.getColumn()-destination.getColumn()<0){
			return 1;
		}
		else if(source.getRow()-destination.getRow()>0){
			return 2;
		}
		else if(source.getColumn()-destination.getColumn()>0){
			return 3;
		}
		else{
			return 4;
		}
	}
	
	//Get The Next Position
	public Position PositionSelect()
	throws Error
	{
		Position p;
		if(stepcount<path.size()){
			p = path.get(stepcount);
			stepcount++;
		}
		else{
			p=null;
			iStatus=DELIVER;
		}
		return(p);	
	}


	public void checkStatus()
	{
		if (iStatus==DELIVER || iStatus==FETCH)
		{
			iStatus=DELIVER;
		}
	}
}


