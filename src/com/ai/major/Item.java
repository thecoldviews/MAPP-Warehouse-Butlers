/* 
 * @author Sarthak Ahuja
 */
package com.ai.major;

import java.awt.*;

public class Item
{
	final int iX[]={1,19,1,19};
	final int iY[]={2,2,13,13};

	final int iShowCount=32;
	final int iHideCount=16;

	int frameCount;
	int showStatus;

	int iValid[];

	// the applet this object is associated to
	Window applet;
	Graphics graphics;

	// the ghosts it controls
	Agent [] ghosts;

	// the power dot image
	Image imagePowerDot;

	// the blank image
	Image imageBlank;

	Item(Window a, Graphics g, Agent [] gh)
	{
		applet=a;
		graphics=g;
		ghosts=gh;

		// initialize power dot	and image
		iValid = new int[4];

		imagePowerDot=applet.createImage(16,16);
		Visuals.drawPowerDot(imagePowerDot);

		imageBlank=applet.createImage(16,16);
		Graphics imageG=imageBlank.getGraphics();
		imageG.setColor(Color.black);
		imageG.fillRect(0,0,16,16);

		frameCount=iShowCount;
		showStatus=1;	// show
	}

	public void start()
	{
		// all power dots available
		for (int i=0; i<4; i++)
			iValid[i]=1;
	}

	void clear(int dot)
	{
		graphics.drawImage(imageBlank, iX[dot]*16, iY[dot]*16, applet);
	}

	void eat(int iCol, int iRow)
	{
		for (int i=0; i<4; i++)
		{
			if (iX[i]==iCol && iY[i]==iRow)
			{
				iValid[i]=0;
				clear(i);
			}
		}
		for (int i=0; i<4; i++)
			ghosts[i].blind();
	}

	public void draw()
	{
		frameCount--;
		if (frameCount==0)
		{
			if (showStatus==1)
			{
				showStatus=0;
				frameCount=iHideCount;
			}
			else
			{
				showStatus=1;
				frameCount=iShowCount;
			}
		}
		for (int i=0; i<4; i++)
		{
			if (iValid[i]==1 && showStatus==1)
				graphics.drawImage(imagePowerDot,iX[i]*16, iY[i]*16, applet);
			else
				graphics.drawImage(imageBlank, iX[i]*16, iY[i]*16, applet);
		}
	} 
}
