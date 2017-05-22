package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import model.Beings;
import model.Food;
import model.Insect;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Inspector;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;


public class BeingsWithUI extends GUIState {
	public static int FRAME_SIZE = 600;
	public Display2D display;
	public JFrame displayFrame;
	SparseGridPortrayal2D SparsePortrayal = new SparseGridPortrayal2D();
	
	public BeingsWithUI(SimState state) {
		super(state);
	}
	public static String getName() {
		return "Simulation d'insectes"; 
	}
	public void start() {
	  super.start();
	  setupPortrayals();
	}

	public void load(SimState state) {
	  super.load(state);
	  setupPortrayals();
	}
	public void setupPortrayals() {
//	  Beings beings = (Beings) state;	
	  
	  SparsePortrayal.setField(Beings.sparse );
	  SparsePortrayal.setPortrayalForClass(Insect.class, getInsectPortrayal());
	  SparsePortrayal.setPortrayalForClass(Food.class, getFoodPortrayal());
	  //yardPortrayal.setPortrayalForClass(TypeB.class, new StrangeOvalPortrayal());
	  display.reset();
	  display.setBackdrop(Color.orange);
		// redraw the display
	  //addBackgroundImage();
	  display.repaint();
	}
	private OvalPortrayal2D getInsectPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.paint = Color.RED;
		r.filled = true;
		return r;
	}
	private OvalPortrayal2D getFoodPortrayal() {
		OvalPortrayal2D r = new OvalPortrayal2D();
		r.paint = Color.GRAY;
		r.filled = true;
		return r;
	}
	public void init(Controller c) {
		  super.init(c);
		  display = new Display2D(FRAME_SIZE,FRAME_SIZE,this);
		  display.setClipping(false);
		  displayFrame = display.createFrame();
		  displayFrame.setTitle("Beings");
		  c.registerFrame(displayFrame); // so the frame appears in the "Display" list
		  displayFrame.setVisible(true);
		  display.attach( SparsePortrayal, "Yard" );
		}
	private void addBackgroundImage() {
	  Image i = new ImageIcon(getClass().getResource("back.jpg")).getImage();
	  int w = i.getWidth(null)/5;
	  int h = i.getHeight(null)/5;
	  BufferedImage b = display.getGraphicsConfiguration().createCompatibleImage(w,h);
	  Graphics g = b.getGraphics();
	  g.drawImage(i,0,0,w,h,null);
	  g.dispose();
	  display.setBackdrop(new TexturePaint(b, new Rectangle(0,0,w,h)));
	}
	public  Object  getSimulationInspectedObject()  {  return  state;  }
	public  Inspector  getInspector() {
	Inspector  i  =  super.getInspector();
	  i.setVolatile(true);
	  return  i;
	}
}