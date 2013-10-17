package number;

import java.util.HashSet;
import java.util.Set;

public class NumberPlaceBlock {
	private NumberPlaceTable numberTable;
	private int rowNum;
	private int clumnNum;
	private int width;
	
	public NumberPlaceBlock(NumberPlaceTable numberTable, int rowNum, int clumnNum, int width) {
		this.numberTable = numberTable;
		this.rowNum = rowNum;
		this.clumnNum = clumnNum;
		this.width = width;
	}
	
	public Set<Integer> numberSet()  {
		Set<Integer> enableNumberSet = new HashSet<Integer>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (numberTable.getCell(rowNum + i, clumnNum + j).isNotBlank()) {
					enableNumberSet.add(numberTable.getCell(rowNum + i, clumnNum + j).getNumber());
//					if (!enableNumberSet.add(numberTable.getCell(rowNum + i, clumnNum + j).getNumber())) {
//						System.out.println("[" + (rowNum+i) + ", " + (clumnNum+j) + "] ブロック内で矛盾が発生しました");
//						throw new ContradictException();
//					}
				}
			}
		}
		return enableNumberSet;
	}
	
//	public boolean updateEnableNumber() {
//		Set<Integer> numberSet = numberSet();
//		boolean update = false;
//		for (int i = 0; i < width; i++) {
//			for (int j = 0; j < width; j++) {
//				if (numberTable.getCell(rowNum + i, clumnNum + j).isBlank()) {
//					if (numberTable.getCell(rowNum + i, clumnNum + j).disableNumber(numberSet)) {
//						update = true;
//					}
//				}
//			}
//		}
//		return update;
//	}
	
	public boolean canSetNumber(NumberPlaceCell cell) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (( (rowNum + i) != cell.getPoint().row() ) || ( (clumnNum + j) != cell.getPoint().clumn()) ) {
					if (numberTable.getCell(rowNum + i, clumnNum + j).isNotBlank()) {
						if (numberTable.getCell(rowNum + i, clumnNum + j).getNumber() == cell.getNumber()) {
							System.out.println("["+(rowNum + i)+","+(clumnNum + j)+"] = " + numberTable.getCell(rowNum + i, clumnNum + j).getNumber() + " は ["+cell.getPoint().row()+","+cell.getPoint().clumn()+"] = "+cell.getNumber()+"にあります");
							return false;
						}
					}
				}
			}
		}
		return true;
	}
	
	public boolean canSetNumber(Integer number) {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				if (numberTable.getCell(rowNum + i, clumnNum + j).isNotBlank()) {
					if (numberTable.getCell(rowNum + i, clumnNum + j).getNumber() == number) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
//	public boolean isEnable(int number) {
//		boolean enable = false;
//		for (int i = 0; i < width; i++) {
//			for (int j = 0; j < width; j++) {
//				NumberPlaceCell cell = numberTable.getCell(rowNum + i, clumnNum + j);
//				if (cell.isEnable(number)) {
//					if (cell.enableNumberList().size() == 1) {
//						System.out.println("[" + cell.getPoint().row() + "," + cell.getPoint().clumn() + "] に " + number + " を配置しました(Block)");
//						cell.disableNumber(number);
//						cell.setNumber(number);
//						enable = true;
//					}
//				}
//			}
//		}
//		return enable;
//	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				sb.append(numberTable.getCell(rowNum + i, clumnNum + j).getNumber());
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
