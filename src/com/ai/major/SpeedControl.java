/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major;

/**
 * speed control
 * 
 * use init(s,f) to set the frame/step ratio
 * NOTE: s must <= f
 * call start() to reset counters
 * call isMove per frame to see if step are to be taken
 */
public class SpeedControl
{
	// move steps per frames
	int steps;
	int frames;

	int frameCount;
	int stepCount;

	float frameStepRatio;

	SpeedControl()
	{
		start(1,1);
	}

	public void start(int s, int f)
	throws Error
	{
		steps=s;
		frames=f;
		frameStepRatio=(float)frames/(float)steps;

		stepCount=steps;
		frameCount=frames;
	}

	// return 1 if move, 0 not move
	public int isMove()	
	{
		frameCount--;

		float ratio=(float)frameCount/(float)stepCount;

		if (frameCount==0)
			frameCount=frames;

		if (ratio < frameStepRatio)
		{
			stepCount--;
			if (stepCount==0)
				stepCount=steps;
			return(1);		
		}
		return(0);
	}
}
