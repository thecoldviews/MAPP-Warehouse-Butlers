/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;

/**
 * the tables are used to speed up computation
 */
public class Warehouse8 extends Warehouse
{
	public Warehouse8() {
		// TODO Auto-generated constructor stub
		String[] MazeDefine3=
			{
				"XXXXXXXXXXXXXXXXXXXXX",	// 1
				"X                   X",	// 2
				"X  X  X  X X  X  X  X",	// 3
				"X  X  X  XXX  X  X  X",	// 4
				"X  X  X  OOO  X  X  X",	// 5
				"X  X  X  OOO  X  X  X",	// 6
				"X  X  X  XXX  X  X  X",	// 7
				"X  X  X  X X  X  X  X",	// 8
				"X  X  X  X X  X  X  X",	// 9
				"X  X  X  X X  X  X  X",	// 10
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

