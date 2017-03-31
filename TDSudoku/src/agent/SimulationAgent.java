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

/**
 * @author ia04p007
 *
 */
public class SimulationAgent extends Agent {
	private Map<Integer, String> AgentMap;

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
			String content;
			message=receive(MessageTemplate.MatchPerformative(ACLMessage.CONFIRM).MatchConversationId("Finish"));

			if(message != null)
			{
				System.out.println("Fin de la simulation");
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
			super(a,10000);
		}

		/* (non-Javadoc)
		 * @see jade.core.behaviours.TickerBehaviour#onTick()
		 */
		@Override
		protected void onTick() {
			// TODO Auto-generated method stub
				if(AgentMap.size() != 27)
				{
					this.done();
				}
				else 
				{
					ACLMessage message = new ACLMessage(ACLMessage.REQUEST);
					message.addReceiver(new AID("EnvAgent", AID.ISLOCALNAME));
					message.setConversationId("Ticker");
					AgentMap.forEach((key,value)->{
						message.setContent(value);
						send(message);
					});
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
			ACLMessage message =null;
			String content;
			while(message == null && AgentMap.size() <= 27)
			{
				message=receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
				content = message.getContent();
				AgentMap.put(AgentMap.size() + 1, content);					
			}
			System.out.println("D�but de la simulation");
			ACLMessage message1 = new ACLMessage(ACLMessage.REQUEST);
			message1.addReceiver(new AID("EnvAgent", AID.ISLOCALNAME));
			message1.setConversationId("Start");
			send(message1);
			
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
