/**
 * 
 */
package main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author ia04p007
 *
 */
public class cell {
	/**
	 * @param line
	 * @param col
	 * @param block
	 * @param valeur
	 * @param possibleValues
	 */
	public cell(Integer line, Integer col, Integer block, Integer valeur) {
		super();
		this.line = line;
		this.col = col;
		this.block = block;
		setValue(valeur);
	}
	private Integer line;
	private Integer col;
	private Integer block;
	private Integer valeur;
	private  ArrayList<Integer> possibleValues;
	/**
	 * @return the line
	 */
	public Integer getLine() {
		return line;
	}
	/**
	 * @param line the line to set
	 */
	public void setLine(Integer line) {
		this.line = line;
	}
	/**
	 * @return the col
	 */
	public Integer getCol() {
		return col;
	}
	/**
	 * @param col the col to set
	 */
	public void setCol(Integer col) {
		this.col = col;
	}
	/**
	 * @return the block
	 */
	public Integer getBlock() {
		return block;
	}
	/**
	 * @param block the block to set
	 */
	public void setBlock(Integer block) {
		this.block = block;
	}
	/**
	 * @return the value
	 */
	public Integer getValue() {
		return valeur;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(Integer val) {
		this.valeur = val;
		if(val == 0)
		{

			List<Integer> places = Arrays.asList(1,2,3,4,5,6,7,8,9);
			setPossibleValues((ArrayList<Integer>) places);
		}
		else{
			this.possibleValues = new ArrayList<>();
		}
	}
	/**
	 * @return the possibleValues
	 */
	public ArrayList<Integer> getPossibleValues() {
		return possibleValues;
	}
	/**
	 * @param possibleValues the possibleValues to set
	 */
	public void setPossibleValues(ArrayList<Integer> possibleValues) {
		this.possibleValues = possibleValues;
	}





}
