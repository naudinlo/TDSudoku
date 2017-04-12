package agent;

import java.awt.event.ItemEvent;
import java.io.IOException;
import java.security.acl.Acl;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.activation.registries.MailcapParseException;

import agent.SimulationAgent.tickerBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jdk.nashorn.internal.runtime.linker.LinkerCallSite;

import java.util.ArrayList;
import main.Cell;

public class AnAgent extends Agent{
@Override
protected void setup() {
	// TODO Auto-generated method stub
	super.setup();
	String op = getLocalName();
	//L'agent d'analyse va envoyer un message à l'agent de simulation durant son initialisation
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
				Cell[] cellules  = mapper.readValue(content, Cell[].class);
				//On reinitialise les cellules avant chaque traitement (algo2)
				cellules = algo2(cellules);
				cellules = algo4(cellules);

				cellules = algo2(cellules);
				cellules = algo1(cellules);
				
				cellules = algo2(cellules);
				cellules = algo3(cellules);
				cellules = algo2(cellules);

			
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


	
	private Cell[] algo1(Cell[] cellules){
		for (int i = 0; i < cellules.length; i++) {
			if(cellules[i].getPossibleValues() == null)
				continue;
			Cell cell = cellules[i];
			if(cell.getPossibleValues().length == 1 && cell.getPossibleValues() != null)
			{
				cell.setValue(cell.getPossibleValues()[0]);
				cell.setPossibleValues(null);
				cellules[i]=cell;
			}
		}
		return cellules;
		
	}
	private Cell[] algo2(Cell[] cellules)
	{	
		for (int i = 0; i < cellules.length; i++) {
			if(cellules[i].getValue() != 0)
			{
				Integer valeur= cellules[i].getValue();
				for (int j = 0; j < cellules.length; j++) {
					if(j!=i && cellules[j].getValue() == 0)
					{	
						Set<Integer> liste= new HashSet<Integer>(Arrays.asList(cellules[j].getPossibleValues()));
						liste.removeIf(cle->cle == valeur);
						cellules[j].setPossibleValues(liste.toArray(new Integer[liste.size()]));
					}
				}
				cellules[i].setPossibleValues(null);
			}
		}
		
		return cellules;
		
	}
	private Cell[] algo4(Cell[] cellules){
		
		Set<Cell> cells = new HashSet<Cell>(Arrays.asList(cellules));
		cells.removeIf(cle->cle.getValue() != 0);
		cells.removeIf(cle->cle.getPossibleValues().length > 2);
		Cell[] cel = cells.toArray(new Cell[cells.size()]);

		if(cells.size() > 0)
		{
			cells.forEach(cle->{
				Set<Integer> values_to_test = new HashSet<Integer>(Arrays.asList(cle.getPossibleValues()));
				int isIn = 0;
				for (int i = 0; i < cel.length; i++) {
					Set<Integer> values_to_test_with = new HashSet<Integer>(Arrays.asList(cel[i].getPossibleValues()));
					if(values_to_test_with.containsAll(values_to_test)) ++isIn;
				}
				if(isIn == 2)
				{
					for (int i = 0; i < cellules.length; i++) {
						if(cellules[i].getValue() != 0) continue;
						if(cellules[i].getPossibleValues().length <=2) continue;
						Set<Integer> values_to_test_with = new HashSet<Integer>(Arrays.asList(cellules[i].getPossibleValues()));
						values_to_test_with.removeIf(cle2->values_to_test.contains(cle2));
						cellules[i].setPossibleValues(values_to_test_with.toArray(new Integer[values_to_test_with.size()]));
					}
				}
			});
		}
		return cellules;
		
		
	}
	private Cell[] algo3(Cell[] cellules){
		for (int i = 0; i < cellules.length; i++) {
			if(cellules[i].getValue() != 0) continue;
			Cell cell = cellules[i];
			Integer[] valeurs_possibles = cell.getPossibleValues();
			for (int j = 0; j < valeurs_possibles.length; j++) {
				Integer val = valeurs_possibles[j];
				boolean isIn = false;
				for (int k = 0; k < cellules.length; k++) {
					if(cellules[k].getValue() != 0 || k == i) continue;
					Set<Integer> liste = new HashSet<Integer>(Arrays.asList(cellules[k].getPossibleValues()));
					if(liste.contains(val))
					{
						isIn = true;
						break;
					}				}
				if (isIn == false)
				{
					//System.out.println("Cellule ("+cell.getLine()+","+cell.getCol()+")");
					cellules[i].setValue(val);
					cellules[i].setPossibleValues(null);
				}
				else {
					continue;
				}
			}
		}
		return cellules;
		
	}
	@Override
	
public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
}
