package main;

import java.awt.List;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.ArrayList;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
public class SecondContainer {
	public static ContainerController td05Container = null;
public static void main(String[] args){
	String SECONDARY_PROPERTIES_FILE = "secd_container_ppt";
	Runtime rt = Runtime.instance();
	ProfileImpl p = null;
	
	try {
		p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
		td05Container = rt.createAgentContainer(p);
		AgentController ac = td05Container.createNewAgent("KB", "agent.KB",null);
		ac.start();

	
	} catch (Exception ex) {
	ex.printStackTrace();
	System.out.println("test probleme scd container");
	}

}
}
