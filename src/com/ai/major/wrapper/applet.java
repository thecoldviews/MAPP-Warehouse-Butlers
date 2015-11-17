/* 
 * @author Sarthak Ahuja
 */
package com.ai.major.wrapper;

import java.applet.Applet;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ai.major.Simulator;
import com.ai.major.Warehouse1;
import com.ai.major.Warehouse2;

/**
 * Main Java Applet for Starting Simulation
 */
public class applet extends Applet
implements ActionListener
{
	static Simulator simulator=null;
	Button warehouse1;
	Button warehouse2;

	public void init()
	{
		setSize(50,100);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		warehouse1=new Button("WAREHOUSE1");
		warehouse2=new Button("WAREHOUSE2");

		add(warehouse1);
		add(warehouse2);
		warehouse1.addActionListener(this);
		warehouse2.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if ( simulator != null && ! simulator.isFinalized() )
			return;
		System.out.println(e.getSource());
		if(e.getSource()==warehouse1){
			simulator=new Simulator(new Warehouse1());
		}
		else if(e.getSource()==warehouse2){
			simulator=new Simulator(new Warehouse2());
		}
		

	}

}
