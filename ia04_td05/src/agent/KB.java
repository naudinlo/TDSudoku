package agent;

import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

import arq.rset;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class KB extends Agent{
@Override
protected void setup() {
	// TODO Auto-generated method stub
	super.setup();
	addBehaviour(new retrieveAssertionFromIdBehaviour());
	addBehaviour(new retrieveAssertionFromNameBehaviour());
	addBehaviour(new retrievePersonFromIdBehaviour());

}
public class retrieveAssertionFromIdBehaviour extends Behaviour{

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage message;
		MessageTemplate template= MessageTemplate.MatchPerformative(ACLMessage.REQUEST).MatchConversationId("FromId");
		if((message = receive(template) )== null)
		{
			block();
			return;
		}
		String id = message.getContent();
		String[] resultat = this.getPropertiesUsingId(id);
		 for (int i = 0; i < resultat.length; i++) {
				System.out.println(resultat[i]);
			}
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	public  String[] getPropertiesUsingId(String name)
	{
		ArrayList<String> re = new ArrayList<>();
		Model model = ModelFactory.createDefaultModel(); 
		FileManager.get().readModel(model, "bc.n3"); 
		String nstd5 = model.getNsPrefixURI("td5");
		String nsrdf = model.getNsPrefixURI("rdf");
		String nsrdfoaf = model.getNsPrefixURI("foaf");
		Resource resource = model.getResource(nstd5+name);
		 StmtIterator iterator= resource.listProperties();
		 while (iterator.hasNext()) {
			Statement statement = (Statement) iterator.next();
			re.add(statement.toString());
		}
		 String[] rse= re.toArray(new String[re.size()]); 
		 for (int i = 0; i < rse.length; i++) {
			System.out.println(rse[i]);
		}
		 //System.out.println();
		return rse;
	}	
}
public class retrieveAssertionFromNameBehaviour extends  Behaviour{

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage message;
		MessageTemplate template= MessageTemplate.MatchConversationId("FromName");
		if((message=receive(template)) == null)
		{
			block();
			return; 
		}
		String name = message.getContent();
		String[] resultat = getPropertiesUsingName(name);
		if(resultat == null)
		{
			System.out.println("Pas de ressource associée à ce nom");
		}
		else
		{
			 for (int i = 0; i < resultat.length; i++) {
					System.out.println(resultat[i]);
				}
		}
	}

	private String[] getPropertiesUsingName(String name) {
		// TODO Auto-generated method stub
		ArrayList<String> re = new ArrayList<>();
		Model model = ModelFactory.createDefaultModel(); 
		FileManager.get().readModel(model, "bc.n3"); 
		String nstd5 = model.getNsPrefixURI("td5");
		String nsrdf = model.getNsPrefixURI("rdf");
		String nsrdfoaf = model.getNsPrefixURI("foaf");		
		
		Property property= model.getProperty(nsrdfoaf+"givenname");
		ResIterator iterator= model.listResourcesWithProperty(property);
		Resource resource = null;
		while (iterator.hasNext()) {
			resource = (Resource) iterator.next();
			String chaine = resource.getProperty(property).toString();
			String[] tab= chaine.split(",");
			chaine = tab[2].substring(0,tab[2].length() -1);
			if (chaine.contains(name)) {
				break;
			}
		}
		if(resource == null)
		{
			return null;
		}
		  StmtIterator stmt_iterator = resource.listProperties();
		 while (iterator.hasNext()) {
			Statement statement = (Statement) iterator.next();
			re.add(statement.toString());
		}
		 String[] rse= re.toArray(new String[re.size()]); 
		 for (int i = 0; i < rse.length; i++) {
			System.out.println(rse[i]);
		}
		return rse;
	}
	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
public class retrievePersonFromIdBehaviour extends Behaviour{

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage message ; 
		MessageTemplate template = MessageTemplate.MatchConversationId("People");
		if((message =receive(template)) == null)
		{
			block();
			return;
		}
		String name = message.getContent();
		String[] res = getPersonKnowingUsingName(name);
	}

	private String[] getPersonKnowingUsingName(String name) {
		//TODO : Récupérer l'id selon le nom puis faire ce traitement ci dessous
		ArrayList<String> re = new ArrayList<>();

		Model model = ModelFactory.createDefaultModel(); 
		FileManager.get().readModel(model, "bc.n3"); 
		String nstd5 = model.getNsPrefixURI("td5");
		String nsrdf = model.getNsPrefixURI("rdf");
		String nsrdfoaf = model.getNsPrefixURI("foaf");

		Property property= model.getProperty(nsrdfoaf+"knows");
		Property property2= model.getProperty(nsrdfoaf+"givenname");

		ResIterator iterator= model.listResourcesWithProperty(property);
		while (iterator.hasNext()) {
			Resource resource = (Resource) iterator.next();
			String chaine = resource.getProperty(property).toString();
			String[] tab= chaine.split(",");
			chaine = tab[2].substring(0,tab[2].length() -1);

			
			if (chaine.contains(name)) {
				re.add(resource.getProperty(property2).toString());
				//System.out.println(resource.getProperty(property2).toString());
			}
		}
		 String[] rse= re.toArray(new String[re.size()]); 
	
		return rse;
		// TODO Auto-generated method stub
	
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
}
