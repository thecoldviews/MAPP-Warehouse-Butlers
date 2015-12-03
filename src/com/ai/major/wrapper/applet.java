/* 
 * @author Sarthak Ahuja and Anchita Goel
 */
package com.ai.major.wrapper;

import java.applet.Applet;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.ai.major.Simulator;
import com.ai.major.Warehouse1;
import com.ai.major.Warehouse10;
import com.ai.major.Warehouse11;
import com.ai.major.Warehouse2;
import com.ai.major.Warehouse3;
import com.ai.major.Warehouse4;
import com.ai.major.Warehouse5;
import com.ai.major.Warehouse6;
import com.ai.major.Warehouse7;
import com.ai.major.Warehouse8;
import com.ai.major.Warehouse9;
/**
 * Main Java Applet for Starting Simulation
 */
public class applet extends Applet
implements ActionListener
{
	static Simulator simulator=null;
	Button warehouse1;
	Button warehouse2;
	Button warehouse3;
	Button warehouse4;
	Button warehouse5;
	Button warehouse6;
	Button warehouse7;
	Button warehouse8;
	Button warehouse9;
	Button warehouse10;
	Button warehouse11;
	public void init()
	{
		setSize(50,400);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		warehouse1=new Button("WAREHOUSE1");
		warehouse2=new Button("WAREHOUSE2");
		warehouse3=new Button(" W3- Slow ");
		warehouse4=new Button(" W4- Non  ");
		warehouse5=new Button("WAREHOUSE5");
		warehouse6=new Button(" W6- Non  ");
		warehouse7=new Button("Thin Tunnel");
		warehouse8=new Button("Big Collection");
		warehouse9=new Button("Ideal Condition");
		warehouse10=new Button("Circular");
		warehouse11=new Button("Space Workstations");
		add(warehouse1);
		add(warehouse2);
		add(warehouse3);
		add(warehouse4);
		add(warehouse5);
		add(warehouse6);
		add(warehouse7);
		add(warehouse8);
		add(warehouse9);
		add(warehouse10);
		add(warehouse11);
		warehouse1.addActionListener(this);
		warehouse2.addActionListener(this);
		warehouse3.addActionListener(this);
		warehouse4.addActionListener(this);
		warehouse5.addActionListener(this);
		warehouse6.addActionListener(this);
		warehouse7.addActionListener(this);
		warehouse8.addActionListener(this);
		warehouse9.addActionListener(this);
		warehouse10.addActionListener(this);
		warehouse11.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		Simulator.statesExpanded=0;
		Simulator.maxmemory=0;
		if ( simulator != null && ! simulator.isFinalized() )
			return;
		//System.out.println(e.getSource());
		if(e.getSource()==warehouse1){
			simulator=new Simulator(new Warehouse1());
		}
		else if(e.getSource()==warehouse2){
			simulator=new Simulator(new Warehouse2());
		}
		else if(e.getSource()==warehouse3){
			simulator=new Simulator(new Warehouse3());
		}
		else if(e.getSource()==warehouse4){
			simulator=new Simulator(new Warehouse4());
		}
		else if(e.getSource()==warehouse5){
			simulator=new Simulator(new Warehouse5());
		}
		else if(e.getSource()==warehouse6){
			simulator=new Simulator(new Warehouse6());
		}
		else if(e.getSource()==warehouse7){
			simulator=new Simulator(new Warehouse7());
		}
		else if(e.getSource()==warehouse8){
			simulator=new Simulator(new Warehouse8());
		}
		else if(e.getSource()==warehouse9){
			simulator=new Simulator(new Warehouse9());
		}
		
		else if(e.getSource()==warehouse10){
			simulator=new Simulator(new Warehouse10());
		}
		else if(e.getSource()==warehouse11){
			simulator=new Simulator(new Warehouse11());
		}

	}

}
