package main;

import jade.util.leap.ArrayList;

public class listof9cell {
	public int IDlist;
	public ArrayList listCell = new ArrayList();
	private String type; //type can be "column", "line" or "block"
	
	
	
	public listof9cell(int theID, String thetype) {
		this.IDlist = theID;
		this.type = thetype;
	}
	
	
}
