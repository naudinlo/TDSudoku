package Agent;

import java.io.IOException;
import java.security.acl.Acl;
import java.util.List;

import org.apache.jena.query.QuerySolution;

import com.fasterxml.jackson.databind.ObjectMapper;

import Agent.propagateGeoSparql.getCapitalOfCountryInterestedBehaviour;
import Agent.propagateGeoSparql.getCountryInterestedBehaviour;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class propagateGeoSparql extends Agent {


@Override
protected void setup() {
	// TODO Auto-generated method stub
	super.setup();
//	addBehaviour(new getCapitalOfCountryInterestedBehaviour());
	addBehaviour(new getCountryInterestedBehaviour());
	addBehaviour(new getCapitalOfCountryBehaviour());
}
public class getCapitalOfCountryInterestedBehaviour extends Behaviour {

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId("Capital").MatchPerformative(ACLMessage.REQUEST);
		ACLMessage message; 
		if((message =receive(messageTemplate)) == null)
		{
			block();
			return;
		}
		String person_name = message.getContent();
		String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ " PREFIX lgd: <http://linkedgeodata.org/>"
				+ " PREFIX lgdo: <http://linkedgeodata.org/ontology/>"
				+ " SELECT * FROM <http://linkedgeodata.org>"
				+ " WHERE { ?country a lgdo:Country ;"
				+ " lgdo:capital_city ?city ;"
				+ " lgdo:wikipedia ?name .} ";
		ACLMessage message2 = new ACLMessage(ACLMessage.QUERY_REF);
		message2.setConversationId("Country");
		message2.addReceiver(new AID("GeodataAgent", AID.ISLOCALNAME));
		message2.setContent(sparql);
		message2.addReplyTo(new AID("PropagateSparqlAgent", AID.ISLOCALNAME));
		send(message2);
		

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
public class getCountryInterestedBehaviour extends Behaviour {

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage message;
		if((message= receive(messageTemplate))==null)
		{
			block();
			return;
		}
		String person_name= message.getContent();
		String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ " PREFIX lgd: <http://linkedgeodata.org/>"
				+ " PREFIX lgdo: <http://linkedgeodata.org/ontology/> "
				+ " PREFIX foaf: <http://xmlns.com/foaf/0.1/> "
				+ " PREFIX xsd:        <http://www.w3.org/2001/XMLSchema#> "
				+" SELECT ?y "
				+ "WHERE{ ?x foaf:givenname ?name ; "
				+ "foaf:topic_interest ?y."
				+ " FILTER(?name=\""+person_name+"\"^^xsd:string)}";
		ACLMessage message2 = new ACLMessage(ACLMessage.QUERY_REF);
		message2.setConversationId("Country");
		message2.addReceiver(new AID("GeodataAgent", AID.ISLOCALNAME));
		message2.setContent(sparql);
		//message2.addReplyTo(new AID("PropagateSparqlAgent", AID.ISLOCALNAME));
		send(message2);
		

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
public class getCapitalOfCountryBehaviour extends Behaviour{

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM_REF).MatchConversationId("Country");
		ACLMessage message; 
		if((message = receive(messageTemplate)) == null)
		{
			block();
			return;
		}
		String sparql = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ " PREFIX lgd: <http://linkedgeodata.org/>"
				+ " PREFIX lgdo: <http://linkedgeodata.org/ontology/>"
				+ " SELECT ?city FROM <http://linkedgeodata.org>"
				+ " WHERE { ?country a lgdo:Country ;"
				+ " lgdo:capital_city ?city ;"
				+ " lgdo:wikipedia ?name . FILTER(";
		String[] geo;
		geo = message.getContent().split(",");
		for (int i = 0; i < geo.length; i++) {
			if(i!=0)sparql+=" || ";
			String string = geo[i];
			geo[i]= string.substring(string.indexOf("<"), string.indexOf(">")+1);
			System.out.println(geo[i]);
			sparql+="?country ="+geo[i];
		}
		sparql+=" )}";
		System.out.println(sparql);
		ACLMessage message2 = new ACLMessage(ACLMessage.QUERY_REF);
		message2.setConversationId("Capital");
		message2.addReceiver(new AID("GeodataAgent", AID.ISLOCALNAME));
		message2.setContent(sparql);
		message2.addReplyTo(new AID("PropagateSparqlAgent", AID.ISLOCALNAME));
		send(message2);
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
}
