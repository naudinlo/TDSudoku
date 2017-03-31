/**
 * 
 */
package agent;

import java.util.HashMap;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

import agent.EnvAgent.updateSudokuBehaviour;
import agent.SimulationAgent.finishBehaviour;
import agent.SimulationAgent.registerBehaviour;
import agent.SimulationAgent.tickerBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.util.leap.ArrayList;
import jade.util.leap.List;
import main.cell;
import main.listof9cell;

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
		addBehaviour(new stimulateAnAgentBehaviour(this,op));
		addBehaviour(new updateSudokuBehaviour(this,op));
		
			
		
	}


	//Doit recevoir les messages des AnAgent
	//Vérifier si les cell transmises detiennent plus d'info que ce qu'il a déjà
	//Si c'est le cas mettre à jour sinon laisser
public class updateSudokuBehaviour extends Behaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}

	}
	
	
	
public class stimulateAnAgentBehaviour extends Behaviour {

	public stimulateAnAgentBehaviour(EnvAgent envAgent, String op) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage message =null;
		String localNameAnAgent;
		int nbmsg=0;
		while(message == null && nbmsg <= 27)
		{
			message=receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM).MatchConversationId("Ticker"));
			localNameAnAgent = message.getContent();
			message= new ACLMessage(ACLMessage.REQUEST);
			//Comment obtenir leur nom? "AnAgent"+i ?
			message.addReceiver(new AID(localNameAnAgent, AID.ISLOCALNAME));
			message.setConversationId(localNameAnAgent);
			//Envoie à chaque agent une liste de 9 cellule
			//Erreur ici : comment récupérer un objet ? Ils ont été créés à la chaine donc pas de pointeur ou autre.
			message.setContent(listOf9Cell);
			send(message);
		}
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
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
				//Création de la table Sudoku : a faire
				//int tableSudoku[][] = new int[9][9];
				int[][] tableSudoku={{5,0,0,0,0,4,0,0,8},{0,1,0,9,0,7,0,0,0},{0,9,2,8,5,0,7,0,6},{7,0,0,3,0,1,0,0,4},{0,0,0,0,0,0,0,0,0},{6,0,0,2,0,8,0,0,1},{1,0,8,0,3,2,4,9,0},{0,0,0,1,0,6,0,5,0},{3,0,0,7,0,0,0,0,2}};
				cell[][] tableCell = null;
				
				//Création des objets cellule + des lignes de cellules
				for(int i=1; i<=9; i++){
					//Création de l'objet ligne/colonne/bloc qui regroupe 9 cellules
					listof9cell aLine = new listof9cell(i,"line");
					for(int j=1; j<=9; j++){
						//Relou l'histoire du bloc + inutile
						cell acell = new cell(i, j, 1, tableSudoku[i][j]);
						aLine.listCell.add(acell);
						tableCell[i][j]=acell; //Attention faire un pointeur plutot 
						//listCellule : liste de taille 9*9 qui a tous les objets 
						//listCellule.add(acell);
					}
				}
				
				//Création des colomnes de cellules
				for(int i=1; i<=9; i++){
					//Création de l'objet colonne qui regroupe 9 cellules
					listof9cell aCol = new listof9cell(j,"column");
					for(int j=1; j<=9; j++){
						//Relou l'histoire du bloc + inutile
						aCol.listCell.add(tableCell[j][i]);
					}
				}
				
				//Création des blocks de cellules
				int k=1;
				for(int s=0; s<=6; s=s+3){
					for(int q=0; q<=6; q=q+3){
						//Création de l'objet block qui regroupe 9 cellules
						listof9cell aBlock = new listof9cell(k, "block");
						k++;
						for(int i=1+s; i<=3+s; i++){
							for(int j=1+q; j<=3+q; j++){
								//Relou l'histoire du bloc + inutile
								aBlock.listCell.add(tableCell[i][j]);
							}
						}
					}
				}

			}


		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}

	}

	
	
}
