package model;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;

public class Beings extends SimState {
	
	public static int GRID_SIZE = 30; 
	public static int NUM_INSECT = 10; 
	public static int NUM_FOOD_CELL = 15; 
	public static int MAX_ENERGY = 15; 
	public static int MAX_LOAD = 5; 
	public static int MAX_FOOD = 5; 
	public static int FOOD_ENERGY = 3; 

	public static int CAPACITY = 10;
	public static SparseGrid2D sparse = new SparseGrid2D(GRID_SIZE,GRID_SIZE);
	public Beings(long seed) {
		super(seed);
	}
	public void start() {
		System.out.println("Simulation started");
		super.start();
	    sparse.clear();
	    addAgentsInsect();
	    addFood();
  }
  private void addAgentsInsect() {
	
	for(int  i  =  0;  i  <  NUM_INSECT;  i++) {
      Insect  a  =  new  Insect();
      Int2D location = getFreeLocation();
      
      sparse.setObjectLocation(a,location.x,location.y);
      a.x = location.x;
      a.y = location.y;
      Stoppable stoppable= schedule.scheduleRepeating(a);
      a.stoppable = stoppable;
    }  
  }
  private Int2D getAvailableFoodSpot(){
	  Int2D location = new Int2D(random.nextInt(sparse.getWidth()),
	           random.nextInt(sparse.getHeight()) );
	  Object ag;
	  try {
		while ( sparse.getObjectsAtLocation(location.x,location.y).componentType() != Food.class
				  || ((Food)(Beings.sparse.getObjectsAtLocation(location.x,location.y).clone())).getQuantity() == MAX_FOOD)
		  {
			  location = new Int2D(random.nextInt(sparse.getWidth()),
		   	           random.nextInt(sparse.getHeight()) );
		  }
		return location;

	} catch (CloneNotSupportedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return location;
  }
  private void addFood() {
	  
	for(int  i  =  0;  i  <  NUM_FOOD_CELL;  i++) {
      Food  b  =  new  Food();
      Int2D location = getFreeLocation();
      sparse.setObjectLocation(b,location.x,location.y);
      b.x = location.x;
      b.y = location.y;
    }  
  }
  public boolean free(int x,int y) {
	 int xx = sparse.stx(x);
	 int yy = sparse.sty(y);
	 return sparse.getObjectsAtLocation(xx, yy) == null;
  }
  private Int2D getFreeLocation() {
	  Int2D location = new Int2D(random.nextInt(sparse.getWidth()),
	           random.nextInt(sparse.getHeight()) );
	  Object ag;
	  while ((ag = sparse.getObjectsAtLocation(location.x,location.y)) != null) {
	   	  location = new Int2D(random.nextInt(sparse.getWidth()),
	   	           random.nextInt(sparse.getHeight()) );
	  }
	  return location;
  }
  public  int  getNumInsects()  {  return  NUM_INSECT;  }

}
