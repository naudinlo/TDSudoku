/**
 * 
 */
package Agent;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;

import Agent.PropagateSparqlAgent.GetPersonInterestedBySameCountryThenPersonBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.event.MessageAdapter;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * @author ia04p007
 *
 */
public class PropagateSparqlAgent extends Agent{


/* (non-Javadoc)
 * @see jade.core.Agent#setup()
 */
@Override
protected void setup() {
	// TODO Auto-generated method stub
	super.setup();
	addBehaviour(new GetPersonKnowingBehaviour());
	addBehaviour(new ReceiveResponseBehaviour());
	addBehaviour(new GetPersonInterestedBySameCountryThenPersonBehaviour());
}
public class GetPersonKnowingBehaviour extends Behaviour{

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate template = MessageTemplate.MatchConversationId("know").MatchPerformative(ACLMessage.REQUEST);
		ACLMessage message;
		if((message = receive(template))== null)
		{
			block();
			return;
		}
			String q = "PREFIX td5: 	 <http://example.org/> "
					+ "PREFIX foaf:    <http://xmlns.com/foaf/0.1/>"
					+ "PREFIX xsd:        <http://www.w3.org/2001/XMLSchema#> "
					+ " SELECT distinct ?x " +
					 "where {?x foaf:knows ?y."
					 + "?y foaf:givenname ?name. FILTER(?name =\""+message.getContent() 
					 +"\"^^xsd:string)}";
			System.out.println(q);
			ACLMessage response = new ACLMessage(ACLMessage.QUERY_REF);
			response.setConversationId("know");
			response.addReceiver(new AID("KB",AID.ISLOCALNAME));
			response.setContent(q);
			send(response);
		
	}

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}}
public class ReceiveResponseBehaviour extends Behaviour{

	/* (non-Javadoc)
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF);
		ACLMessage message;
		if((message = receive(template))== null)
		{
			block();
			return;
		}
		System.out.println(message.getContent());
		
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


public class GetPersonInterestedBySameCountryThenPersonBehaviour extends Behaviour {

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate messageTemplate= MessageTemplate.MatchConversationId("Country").MatchPerformative(ACLMessage.REQUEST);
		ACLMessage message ;
		if((message=receive(messageTemplate)) == null)
		{
			block();
			return;
		}
		String sql = "PREFIX td5: 	 <http://example.org/> "
				+ "PREFIX geo: <http://linkedgeodata.org/triplify/>"
				+ "PREFIX foaf:    <http://xmlns.com/foaf/0.1/>"
						+ "PREFIX xsd:        <http://www.w3.org/2001/XMLSchema#> "
						+" SELECT ?x WHERE{"
				+ " ?y foaf:topic_interest ?z ; foaf:givenname ?name."
				+ " ?x foaf:topic_interest ?z. FILTER ((?x != ?y)"
				+ " && (?name = \""+message.getContent()+"\"^^xsd:string))}";
		System.out.println(sql);
		ACLMessage response = new ACLMessage(ACLMessage.QUERY_REF);
		response.setConversationId("Country");
		response.addReceiver(new AID("KB",AID.ISLOCALNAME));
		response.setContent(sql);
		send(response);
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
}
