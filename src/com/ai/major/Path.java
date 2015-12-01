/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;
import java.util.ArrayList;
import java.util.LinkedList;


public class Path {

	ArrayList<Position> piPath;
	ArrayList<ArrayList<Position>> alternatePaths;
	
	
	public Path()
	{
		
		this.piPath = new ArrayList();
		this.alternatePaths = new ArrayList<>();
		
	}
	
	public Position returnParent(Position p)
	{
		int pos = piPath.indexOf(p);
		if (pos  < this.piPath.size()-1 && pos >=0)
			return piPath.get(pos + 1);
		else
			return null;
		
	}
	
	public Position returnSuccessor(Position p)
	{
		int pos = piPath.indexOf(p);
		if (pos  < this.piPath.size() && pos >0)
			return piPath.get(pos - 1);
		else
			return null;
		
	}
	
	public void addLocation(Position nextLocation, ArrayList alternatePath)
	{
		piPath.add(nextLocation);
		alternatePaths.add(alternatePath);
		
	}
	
	public ArrayList<Position> getAlternatePath(Position to)
	{
		//System.out.println("Alternative to "+to);
		int pos = piPath.indexOf(to);
		if (pos>0)
		  return alternatePaths.get(pos-1);
		else
			return new ArrayList<Position>();
		
	}
	
	public void printPath()
	{
		for (int i=this.piPath.size()-1;i>=0;i--)
			System.out.print(this.piPath.get(i)+" -> ");
	}
	
	
	
	
}
