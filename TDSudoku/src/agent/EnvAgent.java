/**
 * 
 */
package agent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.acl.Acl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.org.apache.bcel.internal.classfile.InnerClass;
import com.sun.org.apache.bcel.internal.generic.I2F;
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
import jade.util.leap.List;
import java.util.ArrayList;
import main.Cell;
import main.listof9cell;
import sun.security.util.UntrustedCertificates;

/**
 * @author ia04p007
 *
 */
public class EnvAgent extends Agent {
	

	Cell[][] SudokuCellTable;
	ArrayList agentSentTo=  new ArrayList();
	Integer solved_cells = 0;
	
	
	
protected void setup() {
		String op = getLocalName();
		System.out.println(op + " Hello World");
		addBehaviour(new createSudokuTableBehaviour(this, op));
		addBehaviour(new stimulateAnAgentBehaviour(this,op));
		addBehaviour(new updateSudokuBehaviour(this,op));
			
	}
	

public class updateSudokuBehaviour extends Behaviour{
		private int updateDone = 0;

		public updateSudokuBehaviour(EnvAgent envAgent, String op) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void action() {
			// TODO Auto-generated method stub
		ACLMessage message_received_from_agent;
		MessageTemplate mTemplate = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
		while (agentSentTo.size() >= 1) {
			for (int i = 0; i < agentSentTo.size(); i++) {
				mTemplate.MatchConversationId((String) agentSentTo.get(i));
				message_received_from_agent = receive(mTemplate);
				if (message_received_from_agent == null)
					continue;
				String content = message_received_from_agent.getContent();
					ObjectMapper mapper = new ObjectMapper();
				try {
					Cell[] cells = mapper.readValue(content, Cell[].class);
					for (int j = 0; j < cells.length; j++) {
						Cell cellule = SudokuCellTable[cells[j].getLine()][cells[j].getCol()];
						if (SudokuCellTable[cells[j].getLine()][cells[j].getCol()].getValue() == 0)
						{		
						SudokuCellTable[cells[j].getLine()][cells[j].getCol()] = choseValuesPossibleForCell(SudokuCellTable[cells[j].getLine()][cells[j].getCol()],cells[j]);
						}
						}
					agentSentTo.remove(i);
				

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} 
		}
	
		if(agentSentTo.size() == 0 && SimulationAgent.GridIsSet == true)
		{
			System.out.println(drawSudokuGrill());
			solved_cells = 0;

			for (int i = 0; i < SudokuCellTable.length; i++) {
				for (int j = 0; j < SudokuCellTable.length; j++) {
					if(SudokuCellTable[i][j].getValue() !=0)
					{
						solved_cells = solved_cells + 1;
					}
				}
			}
		
			if(solved_cells < 9*9)
			{
				System.out.println(81-solved_cells+"cellules à compléter");
				SimulationAgent.readyToTick = true;
			}
			else
			{
				if(SimulationAgent.isFinished == false)
				{
				ACLMessage finish_message = new ACLMessage(ACLMessage.INFORM);
				finish_message.setConversationId("End");
				finish_message.addReceiver(new AID("SimulationAgent", AID.ISLOCALNAME));
				finish_message.setContent(drawSudokuGrill());
				send(finish_message);
				SimulationAgent.isFinished = true;
				}
				block();
			}
		}
		}
		
	private String drawSudokuGrill(){
		String string="\t .  .   .   .   .   .   .   .   .\n";
		for (int j1 = 0; j1 < 9; j1++) {
			string += "\t  ";
			for (int k = 0; k < 9; k++) {
			
				string = string+SudokuCellTable[j1][k].getValue()+" . ";
			}
			string+=" \n \t .   .   .   .   .   .   .   .   .\n";
			
		}
		//System.out.println(string);
		return string; 
	}
	private void drawSudokuGrill(Cell[] cells ,Integer j) {
			// TODO Auto-generated method stub
		for (int j1 = 0; j1 < 9; j1++) {
			String line = "\t |";
			for (int k = 0; k < 9; k++) {
				if(j1 == cells[j].getLine() && k==cells[j].getCol())
					line = line+" . ["+SudokuCellTable[j1][k].getValue()+"]";
					else
				line = line+" . "+SudokuCellTable[j1][k].getValue();
			}
			System.out.println(line+" | \n");
			
		}
		System.out.println("\n ------------------------\n");
		}

	/** Chooses the right value for the cell 
	 * @param cell
	 * @param cell2
	 * @return
	 */
	private Cell choseValuesPossibleForCell(Cell cell, Cell cell2) {
			if (cell2.getValue() == 0 && cell.getValue() == cell2.getValue()) {
				Set<Integer> s1 = new HashSet<Integer>(Arrays.asList(cell.getPossibleValues()));
				Set<Integer> s2 = new HashSet<Integer>(Arrays.asList(cell2.getPossibleValues()));
				s1.retainAll(s2);
				//System.out.println("Résultat comparaison" + s1);
				Integer[] resultat = s1.toArray(new Integer[s1.size()]);
				
				if (s1.size() == 1 && resultat[0] != null && cell.getValue() == 0) {

					cell.setValue(resultat[0]);
					cell.setPossibleValues(null);
				} 
				else {
					cell.setPossibleValues(resultat);
				}
			}
			else {
				cell.setValue(cell2.getValue());
			}
			return cell;
			
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

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
//			InitalizeSudokuGrid();

			while (nbmsg<27 ) {

				MessageTemplate mTemplate3 =MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
				ACLMessage message=receive(mTemplate3.MatchConversationId("Ticker"));
				if(message != null )
				{	
					localNameAnAgent = message.getContent();
					//Comment obtenir leur nom? "AnAgent"+i ?
					//Envoie à chaque agent une liste de 9 cellule
					cell_to_give = new Cell[8];
					if(row_given< 9)
					{
						cell_to_give = getLineOfGrid(row_given);
						row_given = row_given +1;
					}
					else if (col_given < 9) {
						cell_to_give = getColumnOfGrid(col_given);
						col_given = col_given +1 ;

					}
					else if (grid_given < 9) {
						cell_to_give = getBlockOfGrid(grid_given);
						grid_given = grid_given + 1;

					}
					ObjectMapper mapper = new ObjectMapper();
					String s ;
					try {
						s = mapper.writeValueAsString(cell_to_give);
						ACLMessage message_to_send_to_Agent = new ACLMessage(ACLMessage.REQUEST);
						message_to_send_to_Agent.setConversationId(localNameAnAgent);
						agentSentTo.add(localNameAnAgent);
						message_to_send_to_Agent.setContent(s);
						message_to_send_to_Agent.addReceiver(new AID(localNameAnAgent, AID.ISLOCALNAME));
						send(message_to_send_to_Agent);
						nbmsg = nbmsg +1 ;
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (nbmsg == 27)
					
						SimulationAgent.readyToTick = false;	
					
					
				}
			
				
			}
			nbmsg = 0;
			col_given = 0;
			row_given =0;
			grid_given = 0;
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

			if(message != null && SimulationAgent.GridIsSet == false)
			{
				System.out.println("je recrée la table de sudoku");

				//int[][] tableSudoku={{5,0,0,0,0,4,0,0,8},{0,1,0,9,0,7,0,0,0},{0,9,2,8,5,0,7,0,6},{7,0,0,3,0,1,0,0,4},{0,0,0,0,0,0,0,0,0},{6,0,0,2,0,8,0,0,1},{1,0,8,0,3,2,4,9,0},{0,0,0,1,0,6,0,5,0},{3,0,0,7,0,0,0,0,2}};
				Scanner s;
				Integer[][] tableSudoku = new Integer[9][9];

				try {
					s = new Scanner(new File(System.getProperty("user.dir")+"/TDSudoku/src/sample/grid1.txt"));
					int i = 0;
					int j =0;

				    ArrayList<Integer[]> outer = new ArrayList<Integer[]>();
				    ArrayList<Integer> inner = new ArrayList<Integer>();   
				    
					while (s.hasNext()){
						inner.add(s.nextInt());
						i = i+1;
						if(i==9)
						{
							j = j+1;
							outer.add(inner.toArray(new Integer[inner.size()]));
							inner = new ArrayList<Integer>(); 
							i = 0;
						}
						if(j == 9)
						break;
					}
					tableSudoku= outer.toArray(new Integer[outer.size()][9]);
					s.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
				
				SudokuCellTable = new Cell[9][9];
				//On parcourt la table de sudoku, le i correspond à la ligne 
				for (int i1=0; i1<=8; i1++)
				{
					//Parcours par colonne 
					for (int j1 = 0; j1<=8; j1 ++)
					{
						int k = 1;
						Cell cell_to_insert = new Cell();
						cell_to_insert.setLine(i1);
						cell_to_insert.setCol(j1);
						cell_to_insert.setValue(tableSudoku[i1][j1]);
						if(cell_to_insert.getValue() !=0)
							cell_to_insert.setPossibleValues(null);
						else
						{
							Set<Integer> s1 = new HashSet<Integer>();

							for (int j11 = 1; j11 <= 9; j11++) {
									s1.add(j11);
							}
							cell_to_insert.setPossibleValues(s1.toArray( new Integer[s1.size()]));
						}
						
						k=(j1/3 <= 1)?1:2;
						if(j1/3 > 2) k=3;
												
						if(i1/3 <= 1)
						{
							cell_to_insert.setBlock(k);
						}
						else if (i1/3 <= 2) {
							cell_to_insert.setBlock(k+3);
						}
						else {
							cell_to_insert.setBlock(k+6);
						}
						
					SudokuCellTable[i1][j1] = cell_to_insert;
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

public void InitalizeSudokuGrid(){
	for (int i = 0; i < 9; i++) {
		for (int j = 0; j < 9; j++) {
			Cell cellule = SudokuCellTable[i][j];
			
			Set<Integer> s1 = new HashSet<Integer>();

			for (int j1 = 1; j1 <= 9; j1++) {
				if(j1 != cellule.getValue())
					s1.add(j1);
			}
			cellule.setPossibleValues(s1.toArray( new Integer[s1.size()]));
		}
	}
}

	
}
