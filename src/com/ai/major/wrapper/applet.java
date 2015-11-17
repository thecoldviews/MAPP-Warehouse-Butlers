/**
 * Modified from JavaiPacman by Junyang Gu
 * 
 * @author Sarthak Ahuja
 */
package com.ai.major.wrapper;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

import com.ai.major.cpcman;

/**
 * Main Java Applet for Starting Simulation
 */
public class applet extends Applet
implements ActionListener
{
	private static final long serialVersionUID = -749993332452315528L;

	static cpcman pacMan=null;

	public void init()
	{
		setSize(50,50);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		Button play=new Button("PLAY");
		add(play);
		play.addActionListener(this);
	}

	void newGame()
	{
		pacMan=new cpcman();
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( pacMan != null && ! pacMan.isFinalized() )
			return;
		newGame();
	}

}
