/* 
 * @author Sarthak Ahuja
 */
package com.ai.major;

import java.awt.Color;

/**
 * provide some global public utility functions
 */
public class Utility
{
	public static final Color[] ColorArray={Color.BLUE,Color.CYAN,Color.GREEN,Color.MAGENTA,Color.ORANGE,Color.PINK,Color.RED,Color.YELLOW};
	// for direction computation
	public static final int numButlers = 8;
	public static final int[] iXDirection={1,0,-1,0,0};
	public static final int[] iYDirection={0,-1,0,1,0};
	public static final int[] iDirection={
		-1,	// 0:
		1,	// 1: x=0, y=-1
		-1,	// 2:
		-1,	// 3:
		2,	// 4: x=-1, y=0
		-1,	// 5:
		0,	// 6: x=1, y=0
		-1,	// 7
		-1,     // 8
		3     	// 9: x=0, y=1
	};

	// backward direction
	public static int[] iBack={2,3,0,1};

	// direction code
	public static final int RIGHT=0;
	public static final int UP=1;
	public static final  int LEFT=2;
	public static final int DOWN=3;
	
	public static int RandDo(int iOdds)
	{
		if ( Math.random()*iOdds < 1 )
			return(1);
		return(0);
	}	

	public static int RandSelect(int iTotal)
	{
		double a;
		a=Math.random();
		a=a*iTotal;
		return( (int) a );
	}

	public static int IntSign(int iD)
	{
		if (iD==0)
			return(0);
		if (iD>0)
			return(1);
		else
			return(-1);
	}
}
