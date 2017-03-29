package main;

import java.awt.List;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.ArrayList;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
public class SecondaryContainer {
	public static ContainerController SudokuContainer = null;
public static void main(String[] args){
	String SECONDARY_PROPERTIES_FILE = "secd_container_ppt";
	Runtime rt = Runtime.instance();
	ProfileImpl p = null;
	try {
//	p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
//	SudokuContainer = rt.createAgentContainer(p);
//	AgentController ac = SudokuContainer.createNewAgent("SimulationAgent", "agent.SimulationAgent",null);
//	ac.start();
	System.out.println("SimulationAgent ready");
//	AgentController ac1 = SudokuContainer.createNewAgent("EnvAgent", "agent.EnvAgent",null);
//	ac1.start();
//	System.out.println("EnvironnementAgent ready");
//	int i = 1; 
//	AgentController an;
//	while(i<=27){
//		System.out.println("Nouvel agent"+i);
//		an = SudokuContainer.createNewAgent("AnAgent"+i, "agent.AnAgent",null);
//		an.start();
//		i = i +1 ;
//	}
	
	} catch (Exception ex) {
	ex.printStackTrace();
	System.out.println("test probleme scd container");
	}

}
}

