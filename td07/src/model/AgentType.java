package model;
import sim.engine.*;
public class AgentType implements Steppable{
	public int x,y;
	
	@Override
	public void step(SimState state) {
		
		Beings beings = (Beings) state;
		//TODO: A compléter avec les moves à effectuer :)
		move(beings);
		
	}

	private void move(Beings beings) {
		// TODO Auto-generated method stub
		boolean done = false;
		int n = beings.random.nextInt(Beings.NB_DIRECTIONS);
		//Je vois pas trop comment aborder les mouvements 
	}

}
