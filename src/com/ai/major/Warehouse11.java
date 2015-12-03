/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;

/**
 * the tables are used to speed up computation
 */
public class Warehouse11 extends Warehouse
{
	public Warehouse11() {
		// TODO Auto-generated constructor stub
		String[] MazeDefine3=
			{
				"XXXXXXXXXXXXXXXXXXXXX",	// 1
				"XO               O  X",	// 2
				"X  X  X  X  X  X  X X",	// 3
				"X  X  X  XOOX  X  X X",	// 4
				"X  X  X  XOOX  X  X X",	// 5
				"X  X  X  X  X OX  X X",	// 6
				"X  X  X  X  X  X  X X",	// 7
				"X  X  X  X  X  X  X X",	// 8
				"XO X  X  X  X  X  X X",	// 9
				"X  X  X  X  X  X  X X",	// 10
				"X                   X",	// 11
				"X                   X",	// 12
				"X                   X",	// 13
				"X                   X",	// 14
				"X W W W W W W W W   X",	// 15
				"XXXXXXXXXXXXXXXXXXXXX",	// 16
			};
		this.MazeDefine=MazeDefine3;
	}
}

