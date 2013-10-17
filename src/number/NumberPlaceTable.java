package number;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class NumberPlaceTable {
	
	private int width;
	private int blockWidth;
	private List<NumberPlaceCell> cellsCache;
	private NumberPlaceCell[][] numberCells;
	private Map<String, NumberPlaceBlock> numberBlocks;
	
	public NumberPlaceTable(int width) {
		if (width != 9 && width != 16 && width != 25) {
			throw new IllegalArgumentException();
		}
		this.width = width;
		this.blockWidth = (int)Math.sqrt(width);
		this.cellsCache = null;
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
		this.blockWidth = numberTable.blockWidth;
		numberCells = new NumberPlaceCell[width][width];
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				numberCells[i][j] = new NumberPlaceCell(i, j, width);
				numberCells[i][j].setNumber(numberTable.getCell(i, j).getNumber());
				numberCells[i][j].disableNumber(numberTable.getCell(i, j).disableNumberList());
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
	
	public List<NumberPlaceCell> getCells() {
		if (cellsCache != null) {
			return cellsCache;
		}
		
		cellsCache = new ArrayList<NumberPlaceCell>();
		for (int row = 0; row < width; row++) {
			for (int clumn = 0; clumn < width; clumn++) {
				cellsCache.add(getCell(row, clumn));
			}
		}
		return cellsCache;
	}
	
	@Deprecated
	public void setCell(NumberPlaceCell cell) {
		numberCells[cell.row()][cell.clumn()] = cell;
	}
	
	public NumberPlaceBlock getBlock(int blockRow, int blockClumn) {
		checkBlockRowClumn(blockRow, blockClumn);
		String key = Integer.toString(blockRow) + Integer.toString(blockClumn);
		return numberBlocks.get(key);
	}
	
	public NumberPlaceBlock getBlock(NumberPlaceCell cell) {
		return numberBlocks.get(Integer.toString(getBlockRow(cell)) + Integer.toString(getBlockClumn(cell)));
	}
	
	public Set<Integer> numberSetRow(NumberPlaceCell cell) {
		Set<Integer> inputNumbers = new HashSet<Integer>();
		for (int index = 0; index < width; index++) {
			if (index != cell.clumn()) {
				if (getCell(cell.row(), index).isNotBlank()) {
					inputNumbers.add(getCell(cell.row(), index).getNumber());
				}
			}
		}
		return inputNumbers;
	}
	
	public Set<Integer> numberSetClumn(NumberPlaceCell cell) {
		Set<Integer> inputNumbers = new HashSet<Integer>();
		for (int index = 0; index < width; index++) {
			if (index != cell.row()) {
				if (getCell(index, cell.clumn()).isNotBlank()) {
					inputNumbers.add(getCell(index, cell.clumn()).getNumber());
				}
			}
		}
		return inputNumbers;
	}
	
	public NumberPlaceCell getHiPriorityCell() {
		Map<Integer, NumberPlaceCell> hiPriorityMap = new TreeMap<Integer, NumberPlaceCell>();
		for (NumberPlaceCell cell : getCells()) {
			if (cell.isBlank()) {
				hiPriorityMap.put(cell.getEnableNumberCount(), cell);
			}
		}
		return hiPriorityMap.values().iterator().next();
	}
	
	public boolean existContradictCell() {
		for (int row = 0; row < width; row++) {
			for (int clumn = 0; clumn < width; clumn++) {
				if (!getCell(row, clumn).canInputValue()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean existBlankCell() {
		for (int row = 0; row < width; row++) {
			for (int clumn = 0; clumn < width; clumn++) {
				if (getCell(row, clumn).isBlank()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean existSameNumberAtRow(NumberPlaceCell cell, int row) {
		for (int index = 0; index < width; index++) {
			if (index != cell.clumn()) {
				if (getCell(row, index).getNumber() == cell.getNumber()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean existSameNumberAtClumn(NumberPlaceCell cell, int clumn) {
		for (int index = 0; index < width; index++) {
			if (index != cell.row()) {
				if (getCell(index, clumn).getNumber() == cell.getNumber()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getBlockWidth() {
		return blockWidth;
	}
	
	public int getBlockRow(NumberPlaceCell cell) {
		return cell.row()/blockWidth;
	}
	
	public int getBlockClumn(NumberPlaceCell cell) {
		return cell.clumn()/blockWidth;
	}
	
	public boolean isBlank(int row, int clumn) {
		checkRowClumn(row, clumn);
		return (numberCells[row][clumn].getNumber() == 0 ? true : false);
	}
	
	public String toString() {
		String SEP = System.getProperty("line.separator");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				sb.append(numberCells[i][j].getNumber());
			}
			sb.append(SEP);
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
