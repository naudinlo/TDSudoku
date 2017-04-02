/**
 * 
 */
package main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategy.PascalCaseStrategy;

/**
 * @author ia04p007
 *
 */
public class Cell {
	private Integer line;
	private Integer col;
	private Integer block;
	private  Integer possibleValues[];
	private Integer valeur;

	/**
	 * @param line
	 * @param col
	 * @param block
	 * @param valeur
	 * @param possibleValues
	 */
//	public Cell(Integer line, Integer col, Integer block, Integer valeur,ArrayList<Integer> vals) {
//		this.line = line;
//		this.col = col;
//		this.block = block;
//		if(vals != null)
//			possibleValues = vals;
//		else
//
//		setValue(valeur);
//	}
	public Cell() {
	}
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
	@SuppressWarnings("null")
	public void setValue(Integer val) {
		this.valeur = val;
		this.possibleValues = new Integer[9];
		if(val == 0)
		{		

			
			for (int i = 0; i < 9 ;i++) {
				possibleValues[i]=i;
			}
		}
		
	}
	/**
	 * @return the possibleValues
	 */
	public Integer[] getPossibleValues() {
		return possibleValues;
	}






}
