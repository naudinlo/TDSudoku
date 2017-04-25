package main;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
public class MainContainer  {

	public static void main(String[] args) {
		String MAIN_PPT_FILE = "main_container_ppt";
		Runtime rt = Runtime.instance();
		Profile p = null;
		try{
			p = new ProfileImpl(MAIN_PPT_FILE);
			AgentContainer mc = rt.createMainContainer(p);
			mc.start();
			System.out.println("MainContainer ready");

		}
		catch(Exception ex){
		
		}
	
}

}