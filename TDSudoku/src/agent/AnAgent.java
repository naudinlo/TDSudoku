package agent;

import java.io.IOException;
import java.security.acl.Acl;

import com.fasterxml.jackson.databind.ObjectMapper;

import agent.SimulationAgent.tickerBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import main.Cell;

public class AnAgent extends Agent{
@Override
protected void setup() {
	// TODO Auto-generated method stub
	super.setup();
	String op = getLocalName();
	ACLMessage register_message =new ACLMessage(ACLMessage.INFORM);
	register_message.setConversationId("register");
	register_message.addReceiver(new AID("SimulationAgent", AID.ISLOCALNAME));
	register_message.setContent(getLocalName());
	send(register_message);
	
	addBehaviour(new calculateBehaviour(this, op));
	
	
}
public class calculateBehaviour extends Behaviour{
	Cell cell[];

	public calculateBehaviour(AnAgent anAgent, String op) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate mTemplate = MessageTemplate.MatchConversationId(getLocalName()).MatchPerformative(ACLMessage.REQUEST);
		ACLMessage message_from_env = receive(mTemplate);
		if(message_from_env != null){
			System.out.println("Je suis l'agent "+getLocalName());
			ObjectMapper mapper = new ObjectMapper();

			try {
				String content =message_from_env.getContent();

				Cell[] cellule  = mapper.readValue(content, Cell[].class);
				System.out.println("Réussi");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			block();
		}
	
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
}
