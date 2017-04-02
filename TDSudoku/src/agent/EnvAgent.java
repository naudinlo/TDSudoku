/**
 * 
 */
package agent;

import java.security.acl.Acl;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

//import agent.EnvAgent.updateSudokuBehaviour;
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
import main.Cell;
import main.listof9cell;

/**
 * @author ia04p007
 *
 */
public class EnvAgent extends Agent {
	

	Cell[][] SudokuCellTable;
	
	
	
	protected void setup() {
		String op = getLocalName();
		System.out.println(op + " Hello World");
		//AgentMap= new HashMap<Integer, String>();
		addBehaviour(new createSudokuTableBehaviour(this, op));
		addBehaviour(new stimulateAnAgentBehaviour(this,op));
		//addBehaviour(new updateSudokuBehaviour(this,op));
		
			
		
	}


	//Doit recevoir les messages des AnAgent
	//Vérifier si les cell transmises detiennent plus d'info que ce qu'il a déjà
	//Si c'est le cas mettre à jour sinon laisser
	
	
	
public class stimulateAnAgentBehaviour extends Behaviour {
	int nbmsg=0;
	int row_given = 0;
	int col_given= 0;
	int grid_given = 0;
	Cell cell_to_give[];
	public Cell[] getLineOfGrid(Integer i)
	{
		return 	SudokuCellTable[i];
	}
	public Cell[] getColumnOfGrid(Integer i)
	{
		Cell[] cell_to_return = new Cell[9];
		for (int j = 0; j < SudokuCellTable[i].length; j++) {
			cell_to_return[j]= SudokuCellTable[j][i];
		}
		return cell_to_return;
	}
	public Cell[] getBlockOfGrid(Integer i){
		Cell[] cell_to_return= new Cell[9];
		int line = 0;
		int col = 0;
		switch (i) {
		case 1:
			col = 3;
			break;
		case 2 :
			col=6;
			break;

		case 3 :
			line=3;
			break;

		case 4 :
			line=3;
			col=3;
			break;

		case 5 :
			line=3;
			col=6;
			break;

		case 6:
			line=6;
			break;
			
		case 7:
			line=6;
			col=3;
			break;

		case 8:
			line=6;
			col=6;
			break;
		default:
			break;
		}
		int k =0;
		for (int j = line; j < line +3; j++) {
			for (int j2 = col; j2 < col +3; j2++) {
				cell_to_return[k] = SudokuCellTable[j][j2];
				k = k+1;
			}
		}
		return cell_to_return;
	}

	public stimulateAnAgentBehaviour(EnvAgent envAgent, String op) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		String localNameAnAgent;

		
		if (SimulationAgent.readyToTick == true)
		{
			while (nbmsg<27 ) {

				MessageTemplate mTemplate3 =MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
				ACLMessage message=receive(mTemplate3.MatchConversationId("Ticker"));
				if(message != null )
				{	
					System.out.println("Message recu par Env "+ nbmsg);
					localNameAnAgent = message.getContent();
					//Comment obtenir leur nom? "AnAgent"+i ?
					//Envoie à chaque agent une liste de 9 cellule
					cell_to_give = new Cell[8];
					if(row_given< 9)
					{
						cell_to_give = getLineOfGrid(row_given);
						row_given = row_given +1;
						System.out.println("Ligne à remplir "+row_given+" \n\n");
					}
					else if (col_given < 9) {
						cell_to_give = getColumnOfGrid(col_given);
						col_given = col_given +1 ;
						System.out.println("Col à remplir "+col_given+" \n\n");

					}
					else if (grid_given < 9) {
						cell_to_give = getBlockOfGrid(grid_given);
						grid_given = grid_given + 1;
						System.out.println("Bloc à remplir "+grid_given+" \n\n");

					}
					ObjectMapper mapper = new ObjectMapper();
					String s ;
					try {
						s = mapper.writeValueAsString(cell_to_give);
						System.out.println(s);
						ACLMessage message_to_send_to_Agent = new ACLMessage(ACLMessage.REQUEST);
						message_to_send_to_Agent.setConversationId(localNameAnAgent);
						message_to_send_to_Agent.setContent(s);
						message_to_send_to_Agent.addReceiver(new AID(localNameAnAgent, AID.ISLOCALNAME));
						send(message_to_send_to_Agent);
						nbmsg = nbmsg +1 ;
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (nbmsg == 27)
					{
						System.out.println("J'ai FINI !");
						SimulationAgent.readyToTick = false;
					}
					
				}
			
				
			}
		}
		else{
			block();
		}
		block();
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
			MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			message=receive(MessageTemplate.MatchConversationId("Start"));

			if(message != null)
			{
				int[][] tableSudoku={{5,0,0,0,0,4,0,0,8},{0,1,0,9,0,7,0,0,0},{0,9,2,8,5,0,7,0,6},{7,0,0,3,0,1,0,0,4},{0,0,0,0,0,0,0,0,0},{6,0,0,2,0,8,0,0,1},{1,0,8,0,3,2,4,9,0},{0,0,0,1,0,6,0,5,0},{3,0,0,7,0,0,0,0,2}};
				SudokuCellTable = new Cell[9][9];
				
				//On parcourt la table de sudoku, le i correspond à la ligne 
				for (int i=0; i<=8; i++)
				{
					//Parcours par colonne 
					for (int j = 0; j<=8; j ++)
					{
						int k = 1;
						Cell cell_to_insert = new Cell();
						cell_to_insert.setLine(i);
						cell_to_insert.setCol(j);
						cell_to_insert.setValue(tableSudoku[i][j]);
					
						k=(j/3 <= 1)?1:2;
						if(j/3 > 2) k=3;
												
						if(i/3 <= 1)
						{
							cell_to_insert.setBlock(k);
						}
						else if (i/3 <= 2) {
							cell_to_insert.setBlock(k+3);
						}
						else {
							cell_to_insert.setBlock(k+6);
						}
						
					SudokuCellTable[i][j] = cell_to_insert;
					}
					
				}
				System.out.println("Fin de l'initialisation de la grille");
				
				ACLMessage reponse = message.createReply();
				reponse.setPerformative(ACLMessage.CONFIRM);
				send(reponse);
				block();
			}
			
				block();
			


		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}

	}

	
	
}
