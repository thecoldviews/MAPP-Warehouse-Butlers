/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
import java.util.ArrayList;
import java.util.LinkedList;

public class Butler implements Comparable<Butler>
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
	
	Path path;

	Map maze;
	
	int priority;
	int myNumber;

	Image imageIdle;
	Image imageOnDuty;
	
	Color color;
	
	//INITIALIZE
	Butler(Window a, Graphics g, Map m, Color color, int number)
	{
		applet=a;
		graphics=g;
		maze=m;

		flag=0;
		this.color=color;
		
		this.myNumber=number;
		this.priority=number;
		
		imageOnDuty=applet.createImage(18,18);

		imageIdle=applet.createImage(18,18);
		Visuals.drawButler(imageIdle,1, Color.white);
		
		Visuals.drawButler(imageOnDuty,1, color);
		
		assignment=null;
	}

	//PLACE BUTLER
	public void start(Item assignment, Position currentPosition)
	{
		System.out.println(currentPosition);
		//System.exit(0);
		this.assignment=assignment;
		this.currentPosition=currentPosition;
		iStatus=FETCH;
		this.assignment.inBound(this);
		
	}
	
	public void setPath(Path positionList){
		this.path=positionList;
	}
	
	//DRAW THE BUTLER
	public void draw()
	{
		if (iStatus==IDLE || iStatus==RETURN || iStatus==DELIVER){
			graphics.drawImage(imageIdle, currentPosition.getPixelRow()-1, currentPosition.getPixelColumn()-1, applet);
		}
		else if (iStatus==FETCH){
			graphics.drawImage(imageOnDuty, currentPosition.getColumn()*16-1, currentPosition.getRow()*16-1, applet);
		}
		//System.exit(0);
	}  

	//MAKE A MOVE ACCORDING TO STATUS
	public void move(Position p)
	{
		if (speed.isMove()==0)
			// no move
		return;
		nextPosition=p;
//		boolean transitioning=false;
		//if(nextPosition!=null){
		int dir=getDirection(currentPosition,nextPosition);
		currentPosition = p;
		/*currentPosition.setPixelRow(currentPosition.getPixelRow()+Utility.iXDirection[dir]*16);
		currentPosition.setPixelColumn(currentPosition.getPixelColumn()+Utility.iYDirection[dir]*16);*/
		System.out.println(currentPosition.getRow()+"CURRENTCOORDINATE"+currentPosition.getColumn());
		//System.out.println(currentPosition.getPixelRow()+"CURRENTPIXEL"+currentPosition.getPixelColumn());
		System.out.println("COMPLETED MY MOVE");
		System.out.println("COMPLETED MY MOVE");
		System.out.println("COMPLETED MY MOVE");
		System.out.println("COMPLETED MY MOVE");
		System.out.println("COMPLETED MY MOVE");
		System.out.println("COMPLETED MY MOVE");
		System.out.println("COMPLETED MY MOVE");
		if (currentPosition.getPixelRow()%16==0 && currentPosition.getPixelColumn()%16==0)
		{
			
			switch (iStatus)
			{
			case FETCH:
				System.out.println("FETCHING");
				break;
			case DELIVER:
				System.out.println("DELIVERED");
				break;
			}
		}
		else{
			Simulator.sdebugger("WHOOPS");
		}
//		
//		if(transitioning==true){
//			move(p);
//		}
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
	
	public void checkStatus()
	{
		if (iStatus==DELIVER || iStatus==FETCH)
		{
			iStatus=DELIVER;
		}
	}

	public ArrayList<Position> getPrivateZone()
	{
		LinkedList<Position> myPath = new LinkedList<>(path.piPath);
		ArrayList<Position> privateZone = new ArrayList<>();
		myPath.removeFirst();
		myPath.removeLast();
		if (myPath.contains(currentPosition))
		{
			privateZone.add(path.returnParent(currentPosition));
			privateZone.add(currentPosition);
		}
		else
		{
			privateZone.add(currentPosition);
		}
		return privateZone;
	}
	
	public boolean atDestination()
	{
		return this.currentPosition.equals(assignment.position);
	}
	
	@Override
	public int compareTo(Butler arg0) {
		Butler butler = (Butler)arg0;
		return ((Integer)this.priority).compareTo((Integer)butler.priority);
	}
}


