package agent;

import java.io.IOException;
import java.security.acl.Acl;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
			ObjectMapper mapper = new ObjectMapper();

			try {
				String content =message_from_env.getContent();
//				System.out.println(content);
				Cell[] cellules  = mapper.readValue(content, Cell[].class);
				cellules = findCommonValuesForCells(cellules);
			
				String content_to_send =mapper.writeValueAsString(cellules); 
				ACLMessage reply_to_env = message_from_env.createReply();
				reply_to_env.setPerformative(ACLMessage.CONFIRM);
				reply_to_env.setContent(content_to_send );
				send(reply_to_env);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			block();
		}
	
		
	}

	private Cell[] findCommonValuesForCells(Cell[] cellule) {
		// TODO Auto-generated method stub
		Integer value_prec[] = new Integer[9];
		for (int i = 0; i < value_prec.length; i++) {
			value_prec[i]=i+1;
		}
		
		//System.out.println("Calculs des cell de l'agent "+getLocalName());
		for (int i = 0; i < cellule.length; i++) {
			Integer values_to_test[]=  cellule[i].getPossibleValues();
			if(values_to_test[0] == null)
			{
				Set<Integer> s1 = new HashSet<Integer>();

				for (int j = 1; j <= 9; j++) {
					if(j != cellule[i].getValue())
						s1.add(j);
				}
				values_to_test = s1.toArray(new Integer[s1.size()]);
				
			}
		
				Set<Integer> s1 = new HashSet<Integer>(Arrays.asList(value_prec));
				Set<Integer> s2 = new HashSet<Integer>(Arrays.asList(values_to_test));
				s1.retainAll(s2);
				
				value_prec = s1.toArray(new Integer[s1.size()]);

			
		}
		Set<Integer> s1 = new HashSet<Integer>(Arrays.asList(value_prec));
		for (int i = 0; i < cellule.length; i++) {
			if(cellule[i].getValue() == 0)
			{
			cellule[i].setPossibleValues(value_prec);
			}
		}
		return cellule;
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
}
