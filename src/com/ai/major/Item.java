/* 
 * @author Sarthak Ahuja
 */
package com.ai.major;

import java.awt.*;

public class Item
{
	final Position position;

	Color color;
	
	final int iShowCount=32;
	final int iHideCount=16;

	int frameCount;
	int showStatus;

	int iValid;

	Window applet;
	Graphics graphics;

	// the ghosts it controls
	Butler butler;

	// the power dot image
	Image item_image;

	// the blank image
	Image imageBlank;

	Item(Window a, Graphics g, Position position)
	{
		applet=a;
		graphics=g;
		this.color=Color.yellow;
		// initialize power dot	and image
		iValid = 1;
		this.position=position;
		item_image=applet.createImage(16,16);
		Visuals.drawItem(item_image,this.color);

		System.out.println("YUHU");
		imageBlank=applet.createImage(16,16);
		Graphics imageG=imageBlank.getGraphics();
		imageG.setColor(Color.black);
		imageG.fillRect(0,0,16,16);

		frameCount=iShowCount;
		showStatus=1;	// show
	}
	
	public void start()
	{
		iValid=1;
	}

	void clear()
	{
		System.out.println(position.getPixelRow()+"CLEAR"+position.getPixelColumn());
		graphics.drawImage(imageBlank, position.getPixelColumn(), position.getPixelRow(), applet);
	}

	void inBound(Butler butler){
		this.butler=butler;
		this.color=butler.color;
		Visuals.drawItem(item_image,this.color);

	}
	
	void pick()
	{
		iValid=0;
		clear();
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

			if (iValid==1 && showStatus==1){
				System.out.println(position.getPixelRow()+"VALID"+position.getPixelColumn());
				graphics.drawImage(item_image,position.getPixelRow(), position.getPixelColumn(), applet);
			}
			else{
				System.out.println(position.getPixelRow()+"INVALID"+position.getPixelColumn());
				graphics.drawImage(imageBlank,position.getPixelRow(), position.getPixelColumn(), applet);
			}
		
	} 
}
