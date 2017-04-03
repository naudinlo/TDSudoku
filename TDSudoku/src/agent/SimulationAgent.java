/**
 * 
 */
package agent;

import java.security.acl.Acl;
import java.util.HashMap;
import java.util.Map;

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
			// TODO Auto-generated method stub
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
			
			// TODO Auto-generated method stub
			if(GridIsSet == true && readyToTick == true){
				System.out.println("C'est parti \n");
				ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
				message.addReceiver(new AID("EnvAgent", AID.ISLOCALNAME));
				message.setConversationId("Ticker");
				AgentMap.forEach((key,value)->{
					message.setContent(value);
					send(message);
				});
//				System.out.println("Fini la distrib");
				readyToTick = false;
			}			

		}
		
	}
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
			// TODO Auto-generated method stub
			if(GridIsSet == true)
			{
				block();
			}
//			System.out.println("Agent Registration");
			ACLMessage message =null;
			String content;
			MessageTemplate mTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM).MatchConversationId("register");
			message=receive(mTemplate);				
			if(message != null){			
				content = message.getContent();
				AgentMap.put(AgentMap.size() + 1, content);	
			}
		
			if(AgentMap.size() == 27 && GridIsSet == false)
			{
				System.out.println("Agent Registration Done");

				ACLMessage message1 = new ACLMessage(ACLMessage.REQUEST);
				message1.addReceiver(new AID("EnvAgent", AID.ISLOCALNAME));
				message1.setConversationId("Start");
//				System.out.println("Creation Grille");
				send(message1);
			
			
			
			MessageTemplate mTemplate2=MessageTemplate.MatchPerformative(ACLMessage.CONFIRM).MatchConversationId("Start");
			ACLMessage responseFromEnvAgent = receive(mTemplate2);
			if(responseFromEnvAgent != null)
			{
//				System.out.println("Création de la grille Done: Début de la simulation");
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
