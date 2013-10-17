package number;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NumberPlaceTable {
	
	private int width;
	private NumberPlaceCell[][] numberCells;
	private Map<String, NumberPlaceBlock> numberBlocks;
	
	public NumberPlaceTable(int width) {
		if (width != 9 && width != 16 && width != 25) {
			throw new IllegalArgumentException();
		}
		
		this.width = width;
		numberCells = new NumberPlaceCell[width][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				numberCells[i][j] = new NumberPlaceCell(i, j, width);
			}
		}
		
		createBlocks();
	}
	
	public NumberPlaceTable(NumberPlaceTable numberTable) {
		this.width = numberTable.width;
		numberCells = new NumberPlaceCell[width][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				numberCells[i][j] = new NumberPlaceCell(i, j, width);
				numberCells[i][j].setNumber(numberTable.getCell(i, j).getNumber());
//				numberCells[i][j].disableNumber(numberTable.getCell(i, j).disableNumberList());
			}
		}
		
		createBlocks();
	}
	
	private void createBlocks() {
		numberBlocks = new HashMap<String, NumberPlaceBlock>();
		int blockWidth = (int)Math.sqrt(width);
		for (int i = 0; i < blockWidth; i++) {
			for (int j = 0; j < blockWidth; j++) {
				String key = Integer.toString(i) + Integer.toString(j);
				int rowIndex = blockWidth * i;
				int clumnIndex = blockWidth * j;
				NumberPlaceBlock block = new NumberPlaceBlock(this, rowIndex, clumnIndex, blockWidth);
				numberBlocks.put(key, block);
			}
		}
	}
	
	private void checkRowClumn(int row, int clumn) {
		if (row < 0 || row >= width || clumn < 0 || clumn >= width) {
			throw new IllegalArgumentException();
		}
	}
	
	private void checkBlockRowClumn(int blockRow, int blockClumn) {
		int blockWidth = (int)Math.sqrt(width);
		if (blockRow < 0 || blockRow >= blockWidth || blockClumn < 0 || blockClumn >= blockWidth) {
			throw new IllegalArgumentException();
		}
	}
	
	private void checkNumber(int number) {
		if (number < 0 || number > width) {
			throw new IllegalArgumentException();
		}
	}
	
	public void setNumber(int row, int clumn, int number) {
		checkRowClumn(row, clumn);
		checkNumber(number);
		numberCells[row][clumn].setNumber(number);
	}
	
	public NumberPlaceCell getCell(int row, int clumn) {
		checkRowClumn(row, clumn);
		return numberCells[row][clumn];
	}
	
	public void setCell(NumberPlaceCell cell) {
		numberCells[cell.getPoint().row()][cell.getPoint().clumn()] = cell;
	}
	
	public NumberPlaceBlock getBlock(int blockRow, int blockClumn) {
		checkBlockRowClumn(blockRow, blockClumn);
		String key = Integer.toString(blockRow) + Integer.toString(blockClumn);
		return numberBlocks.get(key);
	}
	
	public int getWidth() {
		return width;
	}
	
	public boolean isBlank(int row, int clumn) {
		checkRowClumn(row, clumn);
		return (numberCells[row][clumn].getNumber() == 0 ? true : false);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				sb.append(numberCells[i][j].getNumber());
			}
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				result = prime * result + numberCells[i][j].hashCode();
			}
		}
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NumberPlaceTable other = (NumberPlaceTable) obj;
		if (!Arrays.equals(numberCells, other.numberCells))
			return false;
		if (width != other.width)
			return false;
		return true;
	}
}
