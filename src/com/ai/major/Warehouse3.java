/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;

/**
 * the tables are used to speed up computation
 */
public class Warehouse3 extends Warehouse
{
	public Warehouse3() {
		// TODO Auto-generated constructor stub
		String[] MazeDefine3=
			{
				"XXXXXXXXXXXXXXXXXXXXX",	// 1
				"XO                  X",	// 2
				"XO X  X  X  X  X  X X",	// 3
				"XO X  X  X  X  X  X X",	// 4
				"XO X  X  X  X  X  X X",	// 5
				"XO X  X  X  X  X  X X",	// 6
				"XO X  X  X  X  X  X X",	// 7
				"X  X  X  X  X  X  X X",	// 8
				"XO X  X  X  X  X  X X",	// 9
				"X  X  X  X  X  X  X X",	// 10
				"X                   X",	// 11
				"X                   X",	// 12
				"X                   X",	// 13
				"X                   X",	// 14
				"XWWWWWWW    GGGGGGGGX",	// 15
				"XXXXXXXXXXXXXXXXXXXXX",	// 16
			};
		this.MazeDefine=MazeDefine3;
	}
}

