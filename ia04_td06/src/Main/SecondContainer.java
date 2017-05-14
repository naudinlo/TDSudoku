package Main;

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
		AgentController ac = td05Container.createNewAgent("KB", "Agent.KB",null);
		ac.start();
		AgentController ac2 = td05Container.createNewAgent("PropagateSparqlAgent", "Agent.PropagateSparqlAgent",null);
		ac2.start();

		AgentController ac3 = td05Container.createNewAgent("propagateGeoSparql", "Agent.propagateGeoSparql",null);
		ac3.start();
		AgentController ac4 = td05Container.createNewAgent("GeodataAgent", "Agent.GeodataAgent",null);
		ac4.start();
	
	} catch (Exception ex) {
	ex.printStackTrace();
	System.out.println("test probleme scd container");
	}

}
}
