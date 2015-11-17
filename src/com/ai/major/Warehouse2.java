/* 
 * @author Sarthak Ahuja
 */
package com.ai.major;

/**
 * the tables are used to speed up computation
 */
public class Warehouse2 extends Warehouse
{
	public Warehouse2() {
		// TODO Auto-generated constructor stub
		String[] MazeDefine2=
			{
				"XXXXXXXXXXXXXXXXXXXXX",	// 1
				"X.........X.........X",	// 2
				"XOXXX.XXX.X.XXX.XXXOX",	// 3
				"X......X..X.........X",	// 4
				"XXX.XX.X.XXX.XX.X.X.X",	// 5
				"X....X..........X.X.X",	// 6
				"X.XX.X.XXX-XXX.XX.X.X",	// 7
				"X.XX.X.X     X......X",	// 8
				"X.XX...X     X.XXXX.X",	// 9
				"X.XX.X.XXXXXXX.XXXX.X",	// 10
				"X....X.... .........X",	// 11
				"XXX.XX.XXXXXXX.X.X.XX",	// 12
				"X.........X....X....X",	// 13
				"XOXXXXXXXXXXXXXXXXXOX",	// 14
				"X...................X",	// 15
				"XXXXXXXXXXXXXXXXXXXXX",	// 16
			};
		this.MazeDefine=MazeDefine2;
	}
}

