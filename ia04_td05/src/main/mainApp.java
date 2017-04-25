package main;


import java.util.ArrayList;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.FileManager;

public class mainApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getPropertiesUsingId("jean");
	}
	public static void connait(String name)
	{
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
				
				System.out.println(resource.getProperty(property2).toString());
			}
		}
	}
	public static String[] getPropertiesUsingId(String name)
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
