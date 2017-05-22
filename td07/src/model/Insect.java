package model;

import java.util.ArrayList;
import java.util.HashMap;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;

public class Insect implements Steppable{
	public int x;
	public int y;
	public boolean hasDoneAthing =false;
	private int energy;
	private int distance_perception;
	private int distance_deplacement;
	private int charge_max;
	private int charge; 
	private ArrayList<Bag> elemnts_nearBy;

	public Stoppable stoppable;
	@Override
	public void step(SimState arg0) {
		// TODO Auto-generated method stub

	}

	protected void vivre(){
		if(energy == 0)
		{	
			Beings.sparse.remove(this);
			stoppable.stop(); 
		}
	}
	protected void percevoir(){
		elemnts_nearBy.clear();
		int l = this.x;
		int c = this.y;
		for(int i=l-distance_deplacement ; i<= l+distance_deplacement; i ++)
		{
			if(i <0) continue;
			for (int j = c-distance_deplacement; j<=c+distance_deplacement; j++)
			{
				if( j < 0 ) continue;
				elemnts_nearBy.add(Beings.sparse.getObjectsAtLocation(i,j));

			}
		}
	}
	protected void manger(){
		if(hasDoneAthing == true) return;
		if(charge > 0)
		{
			charge-=charge;
			if(energy < Beings.MAX_ENERGY) energy++;
			hasDoneAthing = true;
		}
		else 
		{
			Bag object = null;
			int l = this.x;
			int c = this.y;
			for(int i=l-1 ; i<= l+1; i ++)
			{
				if (hasDoneAthing == true) break;
				if(i <0) continue;
				for (int j = c-1; j<=c+1; j++)
				{
					if (hasDoneAthing == true) break;	
					if( j < 0  || i ==j) continue;
					if((object = Beings.sparse.getObjectsAtLocation(i, j)) != null)
					{
						if(object.componentType() == Food.class && hasDoneAthing == false)
						{
							decreaseFoodQuantity(i,j);
							if(energy < Beings.MAX_ENERGY) energy++;
							hasDoneAthing = true;
							break;
						}
					}
				}
			}
		}
	}
	private void decreaseFoodQuantity(int l, int c){
		try {
			((Food)(Beings.sparse.getObjectsAtLocation(l, c).clone())).decreaseQuantity();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	protected void deplacer(int u, int v){
		if(hasDoneAthing == true) return;
		if(u <= x+distance_deplacement || u >=x-distance_deplacement)
		{
			if(v<=y+distance_deplacement || v >=y-distance_deplacement)
			{
				Beings.sparse.setObjectLocation(this, x, y);
				hasDoneAthing = true;
			}
		}
	}
	protected void charger(){
		if(charge < Beings.MAX_LOAD && hasDoneAthing == false)
		{
			Bag object = null;
			int l = this.x;
			int c = this.y;
			for(int i=l-1 ; i<= l+1; i ++)
			{
				if (hasDoneAthing == true) break;
				if(i <0) continue;
				for (int j = c-1; j<=c+1; j++)
				{
					if (hasDoneAthing == true) break;	
					if( j < 0  || i ==j) continue;
					if((object = Beings.sparse.getObjectsAtLocation(i, j)) != null)
					{
						if(object.componentType() == Food.class && hasDoneAthing == false && charge < Beings.MAX_LOAD)
						{
							decreaseFoodQuantity(i,j);
							charge = charge +1;
							hasDoneAthing = true;
							break;
						}
					}
				}
			}
		}
	}
	
	public int getDistance_perception() {
		return distance_perception;
	}

	public void setDistance_perception(int distance_perception) {
		this.distance_perception = distance_perception;
	}

	public int getDistance_deplacement() {
		return distance_deplacement;
	}

	public void setDistance_deplacement(int distance_deplacement) {
		this.distance_deplacement = distance_deplacement;
	}

	public int getCharge_max() {
		return charge_max;
	}

	public void setCharge_max(int charge_max) {
		this.charge_max = charge_max;
	}

	public int getCharge() {
		return charge;
	}

	public void setCharge(int charge) {
		this.charge = charge;
	}

}

