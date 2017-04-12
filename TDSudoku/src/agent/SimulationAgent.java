/**
 * 
 */
package agent;

import java.security.acl.Acl;
import java.util.HashMap;
import java.util.Map;

import com.sun.xml.internal.ws.org.objectweb.asm.Label;

import FIPA.stringsHelper;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.ContainerController;

/**
 * @author ia04p007
 *
 */
public class SimulationAgent extends Agent {
	private Map<Integer, String> AgentMap;
	public static boolean readyToTick = false;
	public static boolean GridIsSet = false;
	public static boolean isFinished = false;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	protected void setup() {
		String op = getLocalName();
		System.out.println(op + " Hello World");
		AgentMap= new HashMap<Integer, String>();
		addBehaviour(new registerBehaviour(this, op));
		addBehaviour(new tickerBehaviour(this, op));
		addBehaviour(new finishBehaviour(this, op));


	}
	
/**
 *Behaviour used when the sudoku is done
 *
 */
public class finishBehaviour extends Behaviour{

		/**
		 * @param simulationAgent
		 * @param op
		 */
		public finishBehaviour(SimulationAgent simulationAgent, String op) {
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			// Recoit le message de ConversationID End de performative CONFIRM qui contient la grille
			ACLMessage message =null;
			message=receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM).MatchConversationId("End"));

			if(message != null)
			{
				System.out.println("Sudoku Résolu ! \n"+message.getContent());
			}
			else {
				block();
			}
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#done()
		 */
		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

	/**
	 * Behaviour used to send the 27 messages to the Environement agent
	 *
	 */
	public class tickerBehaviour extends TickerBehaviour{
		/**
		 * @param a
		 * @param op
		 */
		public tickerBehaviour(SimulationAgent a, String op) {
			// TODO Auto-generated constructor stub
			super(a,2000);
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {
			
			// Si la grille est initialisée et que l'agent de simulation est autorisé à envoyer les 27 messages
			if(GridIsSet == true && readyToTick == true){
				System.out.println("C'est parti \n");
				ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
				message.addReceiver(new AID("EnvAgent", AID.ISLOCALNAME));
				message.setConversationId("Ticker");
				//Pour chaque nom d'agent contenu dans la Map des agents
				AgentMap.forEach((key,value)->{
					message.setContent(value);
					send(message);
				});
				//Une fois le message envoyé, on bloque temporairement le tic
				readyToTick = false;
			}			

		}
		
	}
	
	/**
	 *Behaviour used for the registration of the Analyze agents into the map and the creation of the sudoku Grid
	 *
	 */
	public class registerBehaviour extends Behaviour{

		/**
		 * @param simulationAgent
		 * @param op
		 */
		public registerBehaviour(SimulationAgent simulationAgent, String op) {
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			//Si la grille sudoku est déjà en place, ce behaviour doit être bloqué 
			if(GridIsSet == true)
			{
				block();
			}
			ACLMessage message =null;
			String content;
			MessageTemplate mTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM).MatchConversationId("register");
			message=receive(mTemplate);	
			//Si il reçoit un message de la part d'un agent d'analyse de conversationId register 
			//Cela signifie qu'il va l'ajouter à sa Map d'agents
			if(message != null){			
				content = message.getContent();
				AgentMap.put(AgentMap.size() + 1, content);	
			}
			//Si il a rempli sa Map des 27 messages, l'enregistrement est terminé
			//Alors on débute l'initialisation de la grille de sudoku
			if(AgentMap.size() == 27 && GridIsSet == false)
			{
				System.out.println("Agent Registration Done");
				//On envoir un message d'ID Start à l'agent d'environnement pour qu'il crée la grille
				ACLMessage message1 = new ACLMessage(ACLMessage.REQUEST);
				message1.addReceiver(new AID("EnvAgent", AID.ISLOCALNAME));
				message1.setConversationId("Start");
				send(message1);
			
			
			//Une fois le message reçu de la part de l'agent d'environnement
			MessageTemplate mTemplate2=MessageTemplate.MatchPerformative(ACLMessage.CONFIRM).MatchConversationId("Start");
			ACLMessage responseFromEnvAgent = receive(mTemplate2);
			if(responseFromEnvAgent != null)
			{
				//La grille est mise en place , le travail de simulation peut commencer
				GridIsSet = true;
				readyToTick = true;
				block();			
			}
			
			block();			
			}
			

		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.Behaviour#done()
		 */
		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}

}
