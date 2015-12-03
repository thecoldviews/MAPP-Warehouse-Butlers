/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;
import java.io.ObjectInputStream.GetField;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class FindPath {
	
	
	public static final int V_H_COST = 1;
	static PriorityQueue<Position> open;
	static PriorityQueue<Position> alternateOpen;
	static boolean[][] alternateClosed;
	static Path path;
	static boolean closed[][];
	static Position start;
	static Position end;
	static int forBultler;
	static Position[][] environment;
	static int statesExpanded = 0;
	
	
	//COMPARE TWO POSITIONS
	static class PositionComparator implements Comparator<Position>
    {

		@Override
		public int compare(Position o1, Position o2) {
			
			Position c1 = (Position)o1;
			Position c2 = (Position)o2;

                return c1.finalCost<c2.finalCost?-1:
                        c1.finalCost>c2.finalCost?1:0;
           
		}
    	
    }

	//A STAR
	public static PathDetails findPathAstar(Map map, int butler,Position source, Position destination)
	{
		Simulator.sdebugger("BasicMAPP");
		forBultler = butler;
		int rows = map.environment.length;
		int columns = map.environment[0].length;
		environment = new Position[rows][columns];
		start = source;
		end = destination;
		PositionComparator pc = new PositionComparator();
		open = new PriorityQueue<>(10, pc );
		for(int i=0;i<rows;++i){
            for(int j=0;j<columns;++j){
                map.environment[i][j].finalCost = 0;
	            map.environment[i][j].heuristicCost = Math.abs(i-destination.getRow())+Math.abs(j-destination.getColumn());
	            map.environment[i][j].parent = null;
                environment[i][j] = new Position(map.environment[i][j]);
            }
           
         }
		map.environment[source.getRow()][source.getColumn()].finalCost = 0;
		open.add(source);
		
		Position current = null;
		path = new Path();
		closed = new boolean[rows][columns];
		System.out.println("Starting A star: ---------");
        while(true){ 
        	
        	current = open.poll();
        	
            if(current==null)
            	{
            	System.out.println("popped is null");
            	Path p =  new Path();
            	return new PathDetails(p, statesExpanded);
            	
            	}
            System.out.println("popped position at "+current.getRow()+","+current.getColumn());
            closed[current.getRow()][current.getColumn()]=true; 
                        	
            if(current.equals(destination)){
              Path p =  constructPath(map); 
              return new PathDetails(p,statesExpanded);
            } 

            Position neighbour; 
            System.out.println("Expanding position "+current.getRow()+","+current.getColumn());
            
            statesExpanded++;
            System.out.println("Expandiiiiingggg ++++++++++++++++++++++++++ "+statesExpanded);
            if(current.getRow()-1>=0){
            	neighbour = map.environment[current.getRow()-1][current.getColumn()];
                checkAndUpdateCost(current, neighbour, current.finalCost+V_H_COST);                
            } 

            if(current.getColumn()-1>=0){
            	neighbour = map.environment[current.getRow()][current.getColumn()-1];
                checkAndUpdateCost(current, neighbour, current.finalCost+V_H_COST); 
            }

            if(current.getColumn()+1<columns){
            	neighbour = map.environment[current.getRow()][current.getColumn()+1];
                checkAndUpdateCost(current, neighbour, current.finalCost+V_H_COST); 
            }

            if(current.getRow()+1<rows){
            	neighbour = map.environment[current.getRow()+1][current.getColumn()];
                checkAndUpdateCost(current, neighbour, current.finalCost+V_H_COST); 

              }  
            }
        
        
	}
		
	//CHECK AND UPDATE
	static void checkAndUpdateCost(Position current, Position neighbour, int cost){
			
			
			System.out.println("Checking for "+neighbour.getRow()+","+neighbour.getColumn());
			    if(neighbour.isWall || closed[neighbour.getRow()][neighbour.getColumn()])
		    	{
		    	System.out.println("Did not add "+neighbour.getRow()+","+neighbour.getColumn()+ " to open bcoz it is a wall or already expanded");
		    		return;
		    	}
			    if (!neighbour.equals(end))
					
				{
		    if (neighbour.isOthersTarget(forBultler))
		    	{
		    	System.out.println("Did not add "+neighbour.getRow()+","+neighbour.getColumn()+ " to open bcoz it is somebody else's target");
		    	return;
		    	}
		    if (current.equals(start) && neighbour.isButler)
		    	{
		    	System.out.println("Did not add "+neighbour.getRow()+","+neighbour.getColumn()+ " to open bcoz it is being checked for initial blank ");
		    	return;
		    	}
		    if (current.parent!=null)
		    {
		    	ArrayList<Position> ap = getAlternatePath(current.parent,neighbour,current);
		    	if (ap.isEmpty())
		    		{
		    		System.out.println("Did not add "+neighbour.getRow()+","+neighbour.getColumn()+ " to open bcoz no alternate path found");
		    		return;
		    		}
		    }
			}
		    int t_final_cost = neighbour.heuristicCost+cost;
		    
		    boolean inOpen = open.contains(neighbour);
		    if(!inOpen || t_final_cost<neighbour.finalCost){
		        neighbour.finalCost = t_final_cost;
		        neighbour.parent = current;
		        System.out.println("Add "+neighbour.getRow()+","+neighbour.getColumn()+ " to open with cost "+t_final_cost);
		        if(!inOpen)
		        	{
		        	open.add(neighbour);
		        	
		        	}
		    }
		}
			
	//IS SOURCE CHECK?
	public boolean isSource(Position test, Position source)
	{
		
		return source.equals(test);
	}
	
	
	//IS DESTINATION CHECK?
	public boolean isDestination(Position test, Position destination)
	{
		
		return destination.equals(test);
	}
	
	//GET ALTERNATE PATH
	public static ArrayList<Position> getAlternatePath(Position from, Position to , Position notVia)
	{
		System.out.println("Finding alternate path between "+from.getRow()+","+from.getColumn()+ " and "+to.getRow()+","+to.getColumn()+ " and avoid "+notVia.getRow()+","+notVia.getColumn());
		int rows = environment.length;
		int columns = environment[0].length;
		alternateClosed = new boolean[rows][columns];
		alternateOpen = new PriorityQueue<>(10, new PositionComparator());
		for(int i=0;i<rows;++i){
            for(int j=0;j<columns;++j){
                environment[i][j].finalCost = 0;
                environment[i][j].parent = null;
                environment[i][j].heuristicCost = Math.abs(i-to.getRow())+Math.abs(j-to.getColumn());
            }

         }
         environment[from.getRow()][from.getColumn()].finalCost = 0;
         alternateOpen.add(environment[from.getRow()][from.getColumn()]);
        
         
         Position current;
         
         System.out.println("STarting A star for alternate path:*************");
         while(true){ 
             current = alternateOpen.poll();
             
             if(current==null)
	         {
            	 System.out.println("popped null");
	            return new ArrayList<Position>();
	         }
             System.out.println("popped "+current.getRow()+", "+current.getColumn());
             alternateClosed[current.getRow()][current.getColumn()]=true; 

             if(current.equals(to)){
                 return constructAlternatePath(to);
             } 
             statesExpanded++;
             Position neighbour;  
             if(current.getRow()-1>=0){
                 neighbour = environment[current.getRow()-1][current.getColumn()];
                 alternateCheckAndUpdateCost(current, neighbour, notVia,current.finalCost+V_H_COST); 

                 
             } 

             if(current.getColumn()-1>=0){
                 neighbour = environment[current.getRow()][current.getColumn()-1];
                 alternateCheckAndUpdateCost(current, neighbour, notVia, current.finalCost+V_H_COST); 
             }

             if(current.getColumn()+1<environment[0].length){
                 neighbour = environment[current.getRow()][current.getColumn()+1];
                 alternateCheckAndUpdateCost(current, neighbour, notVia,current.finalCost+V_H_COST); 
             }

             if(current.getRow()+1<environment.length){
                 neighbour = environment[current.getRow()+1][current.getColumn()];
                 alternateCheckAndUpdateCost(current, neighbour, notVia,current.finalCost+V_H_COST); 
 
                 }  
             }
         
         
	}
	
	//ALTERNATE CHECK AND UPDATE COST
	static void alternateCheckAndUpdateCost(Position altCurrent, Position neighbour, Position notVia,  int cost){
        if(neighbour.isWall || alternateClosed[neighbour.getRow()][neighbour.getColumn()] || neighbour.equals(notVia) || neighbour.isOthersTarget(forBultler))
        	return;
        int t_final_cost = neighbour.heuristicCost+cost;
        
        boolean inOpen = alternateOpen.contains(neighbour);
        if(!inOpen || t_final_cost<neighbour.finalCost){
            neighbour.finalCost = t_final_cost;
            neighbour.parent = altCurrent;
            if(!inOpen)
            	{
            	alternateOpen.add(neighbour);
            	
            	}
        }
    }
	
	//CONSTRUCT ALTERNATE PATH
	static ArrayList<Position> constructAlternatePath(Position to)
	{
		Position current;
		ArrayList<Position> path = new ArrayList<Position>();
		if(alternateClosed[to.getRow()][to.getColumn()]){
            //Trace back the path 
             System.out.println("Alternate Path to : "+to.getRow()+","+to.getColumn());
             current = environment[to.getRow()][to.getColumn()];
             System.out.println(current.getRow()+","+current.getColumn());
             while(current.parent!=null){
                 path.add(current);
                 current = current.parent;
                 System.out.print(" -> "+current.getRow()+","+current.getColumn());
             } 
             path.add(current);
             return path;
        }
        else 
        {  return path;}
	}
	
	
	// CONSTRUCT PATH
	static Path constructPath(Map wh)
	{
		// Now construct the path
		System.out.println("Constructing the path");
		path = new Path();
        if(closed[end.getRow()][end.getColumn()]){
            //Trace back the path 
             System.out.println("Path: ");
             Position current;
             current = wh.environment[end.getRow()][end.getColumn()];
             System.out.print(current.getRow()+","+current.getColumn());
             while(current.parent!=null && current.parent.parent!=null){
                 System.out.print(" -> "+current.parent.getRow()+","+current.parent.getColumn());
                 ArrayList<Position> alternatePath;
                 if (!current.equals(end))
                	 {
                	 alternatePath = getAlternatePath(current.parent.parent, current, current.parent);}
                 else
                 {
                	 alternatePath = new ArrayList<Position>();
                 }
                 path.addLocation(current, alternatePath);
                 current = current.parent;
             } 
             ArrayList<Position> alternatePath = new ArrayList<Position>();
             path.addLocation(current, alternatePath);
             path.addLocation(current.parent, alternatePath);
             System.out.println();
             return path;
             
        }
        else 
        { 
        	return path;
        
        }
	}
	
}
