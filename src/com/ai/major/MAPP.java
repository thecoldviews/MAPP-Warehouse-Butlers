/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;


public class MAPP implements Runnable{
	
	static ArrayList<Move> movesThisStep;
	static Scanner scan;
		
	public static boolean testSlidable()
	{
		Simulator.sdebugger("TestSlidable");
		for (int i=0;i<Simulator.items.size();i++)
		{
			System.err.println("Finding path for butler "+i);
			Simulator.IdleButlers.remove(Simulator.butlers.get(i));
			Simulator.NonIdleButlers.add(Simulator.butlers.get(i));
			Simulator.butlers.get(i).start(Simulator.items.get(i),Simulator.workstations.get(i));
			System.out.println("WS:"+Simulator.workstations.get(i));
			Simulator.map.environment[Simulator.butlers.get(i).currentPosition.getRow()][Simulator.butlers.get(i).currentPosition.getColumn()].butlerTarget =i;
			Simulator.map.environment[Simulator.butlers.get(i).currentPosition.getRow()][Simulator.butlers.get(i).currentPosition.getColumn()].isButler = true;
			Simulator.map.environment[Simulator.butlers.get(i).currentPosition.getRow()][Simulator.butlers.get(i).currentPosition.getColumn()].butler =Simulator.butlers.get(i);
			Simulator.butlers.get(i).setPath(FindPath.findPathAstar(Simulator.map, i, Simulator.butlers.get(i).currentPosition,Simulator.butlers.get(i).assignment.position));
			System.out.println(Simulator.butlers.get(i));
			//System.exit(0);
			if (Simulator.butlers.get(i).path.piPath.isEmpty())
			{
				System.out.println("not slidable");
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
	
	public static void basicMAPP()
	{
		
		
		
	}
	
	public static void doRepositioning(ArrayList<Butler> activeUnits)
	{
		System.out.println("Repositioning");
		for (int i=movesThisStep.size()-1;i>=0;i--)
		{
			Move move = movesThisStep.get(i);
			if (activeUnits.contains(move.butler))
			{
				System.out.println("Moving  "+move.butler+" from "+move.to+" to "+move.from);
				
				Simulator.map.move(move.butler,move.to, move.from);
				
			}
		}
		
	}
	
	
	public static ArrayList<Butler> doProgression(ArrayList<Butler> active)
	{
		Simulator.sdebugger("Inside Progression");
		movesThisStep = new ArrayList<Move>();
		ArrayList<Butler> reachedThisStep  = new ArrayList<>();
		System.err.println("New array for moves");
		ArrayList<ArrayList<Position>> statesVisited = new ArrayList<>();
		for (int i =0; i<active.size();i++)
		{
			statesVisited.add(new ArrayList<Position>());
		}
		boolean change = false;
		do {
		change = false;
		Collections.sort(active);
		for (int i=0;i<active.size();i++)
		{
			
			Butler currentBut = active.get(i);
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
			else if (locationInPrivateZone(currentBut,active,nextMove))
			{
				System.out.println("Location on the private zone of others");
				continue;
			}
			else if (!nextMove.isOcuupied())
			{
				
				System.out.println("Moving  "+currentBut.myNumber+" from "+currentPosition+" to "+nextMove);
				System.out.println("Moving  "+currentBut.myNumber+" from but "+currentBut.currentPosition.hashCode()+" to  "+currentPosition.hashCode());
				Simulator.map.move(currentBut, currentPosition, nextMove);
				statesVisited.get(i).add(nextMove);
				Move newMove = new Move(currentBut,currentPosition,nextMove);
				movesThisStep.add(newMove);
				System.err.println("Added move for butler "+currentBut+" from "+currentPosition+" to "+nextMove);
				change = true;
				if (currentBut.atDestination())
					{
						active.remove(currentBut);
						reachedThisStep.add(currentBut);
						//return reachedThisStep;
					}
			}
			else if ((newBlank = canCreateBlank(currentBut,active,nextMove,currentBut.path.getAlternatePath(currentPosition)))!=null)
			{
				createBlank(currentBut, newBlank , nextMove, currentBut.path.getAlternatePath(currentPosition));
				System.out.println("Moving  "+currentBut.myNumber+" from "+currentPosition+" to "+nextMove);
				Simulator.map.move(currentBut, currentPosition, nextMove);
				Move newMove = new Move(currentBut,currentPosition,nextMove);
				statesVisited.get(i).add(nextMove);
				movesThisStep.add(newMove);
				change = true;
				if (currentBut.atDestination())
				{
					active.remove(currentBut);
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
	
	
	public static void createBlank(Butler butler, Position sourceBlank, Position destBlank, ArrayList<Position> path)
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Simulator.sdebugger("BasicMAPP");
		ArrayList<Butler> reachedTargetThisStep = null;
		if (testSlidable())
		{
			while (!Simulator.NonIdleButlers.isEmpty())
			{
				//Simulator.sdebugger("Checking Non Idle Butlers");
				//SIGNAL A MOVE TO THE SIMULATOR
				//Simulator.sdebugger(String.valueOf(Simulator.checkMove()));
				if(!Simulator.checkMove()){
					Simulator.sdebugger("Signalling a move to Simulator");
				//WAIT FOR IT TO MAKE THE MOVE
					Simulator.makeMove();
					//System.exit(0);
				}
				else{
					//Simulator.sdebugger("Simulator is already doing something");
				}
				//reachedTargetThisStep = doProgression(Simulator.NonIdleButlers);
				//doRepositioning(Simulator.NonIdleButlers);
			}
			
		}
		else
		{
		
		}
	}
	
	
}
