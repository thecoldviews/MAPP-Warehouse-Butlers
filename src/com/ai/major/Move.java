/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;

public class Move {
	
	Butler butler;
	Position from;
	Position to;
	
	public Move(Butler butler, Position from, Position to)
	{
		this.butler = butler;
		this.from = from;
		this.to = to;
	}

}
