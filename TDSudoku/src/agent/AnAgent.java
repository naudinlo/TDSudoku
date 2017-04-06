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
				//cellules = findCommonValuesForCells(cellules);
				cellules = algo2(cellules);
				cellules = algo1(cellules);
				cellules = algo3bis(cellules);
				//cellules = algo4(cellules);
				//System.out.println(mapper.writeValueAsString(cellules));
				//cellules = algo3(cellules);

			
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
		Set<Cell> liste = new HashSet<Cell>(Arrays.asList(cellules));
		Set<Cell> liste2 = new HashSet<Cell>(Arrays.asList(cellules));

		liste.removeIf(cle->cle.getValue() != 0 );
		Set<Cell> liste_a_2_elements= liste;

		liste2.removeIf(cle->cle.getValue() == 0 );
		liste_a_2_elements.removeIf(cle->cle.getPossibleValues().length != 2);
		
		Cell[] tester=liste2.toArray(new Cell[liste2.size()]);
		Cell[] cellule_a_2_poss=liste_a_2_elements.toArray(new Cell[liste_a_2_elements.size()]);

		for (int i = 0; i < cellule_a_2_poss.length; i++) {
			Set<Integer> valeurs = new HashSet<Integer>(Arrays.asList(cellule_a_2_poss[i].getPossibleValues()));
			int presente = 0;
			for (int j = 0; j < tester.length; j++) {
				Set<Integer> valeurs_a_test = new HashSet<Integer>(Arrays.asList(tester[j].getPossibleValues()));
				if(valeurs_a_test.containsAll(valeurs))
				{
					presente = presente + 1 ;
				}
			}
			if(presente == 2)
			{
				for (int j = 0; j < tester.length; j++) {
					Set<Integer> valeurs_a_test = new HashSet<Integer>(Arrays.asList(tester[j].getPossibleValues()));
					if(valeurs_a_test.containsAll(valeurs) == false)
					{
						valeurs_a_test.removeIf(cle->valeurs.contains(cle));
						tester[j].setPossibleValues(valeurs_a_test.toArray(new Integer[valeurs_a_test.size()]));
					}
				}
				break;
			}
		}
		liste.addAll(liste2);
		cellules = liste.toArray(new Cell[liste.size()]);
		return cellules;
		
	}
	private Cell[] algo3bis(Cell[] cellules){
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
					System.out.println("Cellule ("+cell.getLine()+","+cell.getCol()+")");
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
