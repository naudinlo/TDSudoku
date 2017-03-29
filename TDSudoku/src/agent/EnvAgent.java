/**
 * 
 */
package agent;

import java.util.HashMap;

import agent.SimulationAgent.finishBehaviour;
import agent.SimulationAgent.registerBehaviour;
import agent.SimulationAgent.tickerBehaviour;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import main.cell;

/**
 * @author ia04p007
 *
 */
public class EnvAgent extends Agent {
	
	ArrayList listCellule; //?
	protected void setup() {
		String op = getLocalName();
		System.out.println(op + " Hello World");
		//AgentMap= new HashMap<Integer, String>();
		addBehaviour(new createSudokuTableBehaviour(this, op));
		
		//Création de la table Sudoku
		for(int i=1; i<=9; i++){
			for(int j=1; j<=9; j++){
				//Relou l'histoire du bloc + inutile
				//Mettre mieux à jour la valeur 
				cell acell = new cell(i, j, 1, i+j);
				//valeur a ajouter !!! Comment ? Hashtable?
				listCellule.add(acell);
			}
		}
		
		
		
		
		
		
	}
	
	public class createSudokuTableBehaviour extends Behaviour {
		public createSudokuTableBehaviour(EnvAgent envAgent, String op) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage message =null;
			String content;
			message=receive(MessageTemplate.MatchPerformative(ACLMessage.REQUEST).MatchConversationId("Start"));

			if(message != null)
			{
				System.out.println("Création de la table sudoku");
				
			}


		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}

	}

	
	
}
