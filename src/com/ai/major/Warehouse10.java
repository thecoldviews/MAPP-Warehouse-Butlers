/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;

/**
 * the tables are used to speed up computation
 */
public class Warehouse10 extends Warehouse
{
	public Warehouse10() {
		// TODO Auto-generated constructor stub
		String[] MazeDefine3=
			{
				"XXXXXXXXXXXXXXXXXXXXX",	// 1
				"X                   X",	// 2
				"X   XXXXXXXXXXXX    X",	// 3
				"X  XO  O   O   OX   X",	// 4
				"X X              X  X",	// 5
				"X XO            OX  X",	// 6
				"X X              X  X",	// 7
				"X  XO          OX   X",	// 8
				"X   X          X    X",	// 9
				"X                   X",	// 10
				"X                   X",	// 11
				"X                   X",	// 12
				"X                   X",	// 13
				"X                   X",	// 14
				"XWWWWWWWW   GGGGGGGGX",	// 15
				"XXXXXXXXXXXXXXXXXXXXX",	// 16
			};
		this.MazeDefine=MazeDefine3;
	}
}

