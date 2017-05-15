package model;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public interface RegisterToService {
static void registerToDf(Agent agent,String name, String type){
	DFAgentDescription dfAgentDescription = new DFAgentDescription();
	dfAgentDescription.setName(agent.getAID());
	ServiceDescription serviceDescription = new ServiceDescription();
	serviceDescription.setName(name);
	serviceDescription.setType(type);
	dfAgentDescription.addServices(serviceDescription);
	try {
		if(DFService.search(agent, dfAgentDescription) == null)
		DFService.register(agent, dfAgentDescription);
	} catch (FIPAException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
static AID searchRegisteredAgent(Agent agent, String name, String type){
	DFAgentDescription template = new DFAgentDescription();
	ServiceDescription description = new ServiceDescription();
	description.setName(name);
	description.setType(type);
	template.addServices(description);
	try {
		DFAgentDescription[] dfAgentDescriptions;
		dfAgentDescriptions=DFService.search(agent, template);
		return dfAgentDescriptions[0].getName();
	} catch (FIPAException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return null;
}
}
