package number;

import java.util.HashSet;
import java.util.Set;

public class NumberPlaceBlock {
	private NumberPlaceTable numberTable;
	private int row;
	private int clumn;
	private int width;
	
	public NumberPlaceBlock(NumberPlaceTable numberTable, int row, int clumn, int width) {
		this.numberTable = numberTable;
		this.row = row;
		this.clumn = clumn;
		this.width = width;
	}
	
	public int getStartRow() {
		return row;
	}
	
	public int getEndRow() {
		return row + width - 1;
	}
	
	public int getStartClumn() {
		return clumn;
	}
	
	public int getEndClumn() {
		return clumn + width - 1;
	}
	
	public int getWidth() {
		return width;
	}
	
	public Set<Integer> numberSet(NumberPlaceCell cell)  {
		Set<Integer> enableNumberSet = new HashSet<Integer>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if ((i != cell.row()) || (j != cell.clumn())) {
					int rowNum = row + i;
					int clumnNum = clumn + j;
					if (numberTable.getCell(rowNum, clumnNum).isNotBlank()) {
						enableNumberSet.add(numberTable.getCell(rowNum, clumnNum).getNumber());
					}
				}
			}
		}
		return enableNumberSet;
	}
	
	public boolean existSameNumber(NumberPlaceCell cell) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				int rowNum = row + i;
				int clumnNum = clumn + j;
				if ((rowNum != cell.row()) || (clumnNum != cell.clumn())) {
					if (numberTable.getCell(rowNum, clumnNum).getNumber() == cell.getNumber()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public String toString() {
		String SEP = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				sb.append(numberTable.getCell(row + i, clumn + j).getNumber());
			}
			sb.append(SEP);
		}
		return sb.toString();
	}
}
