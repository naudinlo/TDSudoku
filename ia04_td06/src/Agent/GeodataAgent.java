package Agent;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class GeodataAgent extends Agent{


@Override
protected void setup() {
	// TODO Auto-generated method stub
	super.setup();
	addBehaviour(new RetrieveGeodataBehaviour());
	addBehaviour(new retrieveCountriesOfPersonInterestedBehaviour());
}
public class RetrieveGeodataBehaviour extends Behaviour {

	@Override
	public void action() {
		// TODO Auto-generated method stub
		MessageTemplate messageTemplate = MessageTemplate.MatchConversationId("Capital").MatchPerformative(ACLMessage.QUERY_REF);
		ACLMessage message; 
		if((message = receive(messageTemplate)) == null)
		{
			block();
			return;
		}
		String q = message.getContent();
		Query query = QueryFactory.create(q);
		System.setProperty("http.proxyHost","proxyweb.utc.fr");
		System.setProperty("http.proxyPort","3128");
		System.out.println("Query sent");
		QueryExecution qexec = QueryExecutionFactory.sparqlService( "http://linkedgeodata.org/sparql",
		query );
		ResultSet concepts = qexec.execSelect();
		ResultSetFormatter.out(concepts);
		qexec.close();

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
public class retrieveCountriesOfPersonInterestedBehaviour extends Behaviour {

	@Override
	public void action() {
		// TODO Auto-generated method stub	
		MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.QUERY_REF).MatchConversationId("Country");
		ACLMessage message;
		if((message = receive(template))== null)
		{
			block();
			return;
		}


		String q =message.getContent();

			 	Model model = ModelFactory.createDefaultModel();
				FileManager.get().readModel(model, "bc.n3");
				Query query = QueryFactory.create(q);
				 QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
				 ResultSet r = queryExecution.execSelect();
				 List<QuerySolution> liste =  ResultSetFormatter.toList(r);
//				 System.out.println(liste.toString());
					 ACLMessage reponse = message.createReply();
					 reponse.setPerformative(ACLMessage.INFORM_REF);
					 reponse.setContent(liste.toString());
					 send(reponse);
			
				 queryExecution.close();
			
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
}
