package number.strategy;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import number.NumberPlaceAnsableResult;
import number.NumberPlaceCell;
import number.NumberPlacePoint;
import number.NumberPlaceResult;
import number.NumberPlaceState;
import number.NumberPlaceTable;

public class SimpleAnalyzer implements NumberPlaceStrategy<List<NumberPlaceTable>> {

	private int tableWidth;
	private NumberPlaceTable numberTable;
	private NumberPlaceResult result;
	
	public SimpleAnalyzer(NumberPlaceTable numberTable) {
		this.numberTable = numberTable;
		this.tableWidth = numberTable.getWidth();
		this.result = new NumberPlaceResult();
		this.result.setState(NumberPlaceState.NoAnser);
	}
	
	@Override
	public NumberPlaceAnsableResult<List<NumberPlaceTable>> calculate() {
		innerCalculate();
		return result;
	}

	private void innerCalculate() {
//		System.out.println("innerCalculate");
		// ���̓f�[�^�ɕs�����Ȃ����`�F�b�N���s��
		if (checkError()) {
			result.setState(NumberPlaceState.NoAnser);
			return;
		}
		
		updateCells();
		
		// ���l�����肵�ĉ������߂�
		if (!checkFinish()) {
			if (!postulateNumber()) {
				result.setState(NumberPlaceState.NoAnser);
				return;
			}
		} else {
			result.addResult(numberTable);
			result.setState(NumberPlaceState.OneAnser);
		}
	}
	
	private void updateCells() {
		boolean update = true;
		while(update) {
			update = false;
			checkEnableNumber();
			update = (update || searchInputOnlyValue()); // �u���b�N�E��E�s�ŁA1�������܂��Ă��Ȃ�������T���Ė��߂�
			update = (update || searchCanInputOnlyValue()); // �Z���ɔz�u�ł��鐔�����m�肵�Ă���ꏊ��T���Ė��߂�
		}
	}
	
	private boolean postulateNumber() {
//		System.out.println("postulateNumber");
		boolean bResult = false;
		NumberPlaceTable backup = new NumberPlaceTable(numberTable);
		Map<Integer, NumberPlaceCell> blankCellMap = new TreeMap<Integer, NumberPlaceCell>();
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				if (numberTable.getCell(row, clumn).isBlank()) {
					blankCellMap.put(numberTable.getCell(row, clumn).enableNumberList().size(), numberTable.getCell(row, clumn));
				}
			}
		}
		
		NumberPlaceCell blankCell = blankCellMap.values().iterator().next();
		Set<Integer> enableNumberList = blankCell.enableNumberList();
		NumberPlacePoint point = blankCell.getPoint();
		//�ċA�����̂��߁A�����ŎQ�Ƃ�؂��Ă���
		blankCellMap = null;
		blankCell = null;
		
		for (Integer number : enableNumberList) {
			System.out.println("["+point.row()+","+point.clumn()+"] �� "+number+" �����z�u���܂�");
			numberTable.getCell(point.row(), point.clumn()).setNumber(number);
			
			updateCells();
			
			if (checkError()) {
				System.out.println("�G���[�̂��߃��[���o�b�N���܂�");
				numberTable = backup;
			} else {
				if (checkFinish()) {
					System.out.println("���𓚂�������܂���");
					result.addResult(numberTable);
					if (result.output().size() > 1) {
						result.setState(NumberPlaceState.ManyAnser);
					} else if (result.output().size() == 1) {
						result.setState(NumberPlaceState.OneAnser);
					}
					bResult = true;
				} else {
					if (postulateNumber()) {
						System.out.println("���𓚂�������܂���");
						result.addResult(numberTable);
						if (result.output().size() > 1) {
							result.setState(NumberPlaceState.ManyAnser);
						} else if (result.output().size() == 1) {
							result.setState(NumberPlaceState.OneAnser);
						}
						bResult = true;
					} else {
						System.out.println("�G���[�̂��߃��[���o�b�N���܂�");
						numberTable = backup;
					}
				}
			}
		}
		return bResult;
	}
	
//	private NumberPlaceCell findBlankCell() {
//		System.out.println("findBlankCell");
//		List<NumberPlaceCell> blankCells = new ArrayList<NumberPlaceCell>();
//		for (int row = 0; row < tableWidth; row++) {
//			for (int clumn = 0; clumn < tableWidth; clumn++) {
//				NumberPlaceCell cell = numberTable.getCell(row, clumn);
//				if (cell.isBlank()) {
//					blankCells.add(cell);
//				}
//			}
//		}
//		if (blankCells.isEmpty()) {
//			return null;
//		}
//		return blankCells.get(0);
//	}
	
	private boolean checkError() {
//		System.out.println("checkError");
		// �������͒l��10�����̓G���[�ɂ���(�����Ȃ��炵��)
		int numCount = 0;
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				if (numberTable.getCell(row, clumn).isNotBlank()) {
					numCount++;
				}
			}
		}
		if (numCount < 10) {
			System.out.println("�G���[: �������͒l�͍Œ�ł�10���͂��Ă�������");
			return true;
		}
		
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				NumberPlaceCell cell = numberTable.getCell(row, clumn);
				if (cell.isNotBlank()) {
					
					// �������Ő��l�d���`�F�b�N
					for (int index = 0; index < tableWidth; index++) {
						if (index != clumn) {
							if (numberTable.getCell(row, index).getNumber() == cell.getNumber()) {
								System.out.println(row + "�s�ŃG���[: ["+row+","+index+"] �� ["+row+","+clumn+"] �̒l���d���������߃G���[");
								return true;
							}
						}
					}
					
					// �c�����Ő��l�d���`�F�b�N
					for (int index = 0; index < tableWidth; index++) {
						if (index != row) {
							if (numberTable.getCell(index, clumn).getNumber() == cell.getNumber()) {
								System.out.println(clumn + "��ŃG���[: ["+index+","+clumn+"] �� ["+row+","+clumn+"] �̒l���d���������߃G���[");
								return true;
							}
						}
					}
					
					// �u���b�N���Ő��l�d���`�F�b�N
					int startRow = calcStartRow(row);
					int endRow = calcEndRow(row);
					int startClumn = calcStartClumn(clumn);
					int endClumn = calcEndClumn(clumn);
					for (int blockRow = startRow; blockRow <= endRow; blockRow++) {
						for (int blockClumn = startClumn; blockClumn <= endClumn; blockClumn++) {
							if ((row != blockRow) || (clumn != blockClumn)) {
								if (numberTable.getCell(blockRow, blockClumn).isNotBlank()) {
									if (numberTable.getCell(blockRow, blockClumn).getNumber() == cell.getNumber()) {
										System.out.println("{"+startRow+","+startClumn+"}�u���b�N�G���[: ["+blockRow+","+blockClumn+"] �� ["+row+","+clumn+"] �̒l���d���������߃G���[");
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		
		// �����͂����͉\�Ȑ��l���Ȃ��Z���̃`�F�b�N
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				NumberPlaceCell cell = numberTable.getCell(row, clumn);
				if (cell.isBlank() && (cell.enableNumberList().size() == 0)) {
					System.out.println("�����G���[: ["+row+","+clumn+"] �ɑ΂��ē��͉\�Ȑ��l������܂���");
					return true;
				}
			}
		}
		
		return false;
	}
	
	private boolean checkFinish() {
//		System.out.println("checkFinish");
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				if (numberTable.getCell(row, clumn).isBlank()) {
					return false;
				}
			}
		}
		return true;
	}
	
	private void checkEnableNumber() {
//		System.out.println("checkEnableNumber");
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				NumberPlaceCell cell = numberTable.getCell(row, clumn);
				if (cell.isBlank()) {
					
					// �Z����������u���b�N�����p���Ă��鐔������Z���̓��͉\�Ȑ������i�荞��
					int startRow = calcStartRow(row);
					int endRow = calcEndRow(row);
					int startClumn = calcStartClumn(clumn);
					int endClumn = calcEndClumn(clumn);
					Set<Integer> inputNumbers = new HashSet<Integer>();
					for (int blockRow = startRow; blockRow <= endRow; blockRow++) {
						for (int blockClumn = startClumn; blockClumn <= endClumn; blockClumn++) {
							if (numberTable.getCell(blockRow, blockClumn).isNotBlank()) {
								inputNumbers.add(numberTable.getCell(blockRow, blockClumn).getNumber());
							}
						}
					}
					cell.disableNumber(inputNumbers);
					
					
					// �������ŗ��p���Ă��鐔������Z���̓��͉\�Ȑ������i�荞��
					inputNumbers = new HashSet<Integer>();
					for (int index = 0; index < tableWidth; index++) {
						if (index != clumn) {
							if (numberTable.getCell(row, index).isNotBlank()) {
								inputNumbers.add(numberTable.getCell(row, index).getNumber());
							}
						}
					}
					cell.disableNumber(inputNumbers);
					
					
					// �c�����ŗ��p���Ă��鐔������Z���̓��͉\�Ȑ������i�荞��
					inputNumbers = new HashSet<Integer>();
					for (int index = 0; index < tableWidth; index++) {
						if (index != row) {
							if (numberTable.getCell(index, clumn).isNotBlank()) {
								inputNumbers.add(numberTable.getCell(index, clumn).getNumber());
							}
						}
					}
					cell.disableNumber(inputNumbers);
				}
			}
		}
	}
	
	private boolean searchInputOnlyValue() {
//		System.out.println("searchInputOnlyValue");
		boolean bResult = false;
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				NumberPlaceCell cell = numberTable.getCell(row, clumn);
				if (cell.isBlank()) {
					Set<Integer> numberSet = cell.enableNumberList();
					if (numberSet.size() == 1) {
						int number = numberSet.iterator().next();
						System.out.println("["+row+","+clumn+"] ��  "+number+" ��z�u(OnlyValue)");
						numberTable.setNumber(row, clumn, number);
						bResult = true;
					}
				}
			}
		}
		return bResult;
	}
	
	private boolean searchCanInputOnlyValue() {
//		System.out.println("searchCanInputOnlyValue");
		boolean bResult = false;
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				NumberPlaceCell cell = numberTable.getCell(row, clumn);
				if (cell.isBlank()) {
					
					// �������Ő��l�d���`�F�b�N
					Set<Integer> inputNumbers = new HashSet<Integer>();
					for (int index = 0; index < tableWidth; index++) {
						if (index != clumn) {
							NumberPlaceCell rowCell = numberTable.getCell(row, index);
							if (rowCell.isNotBlank()) {
								inputNumbers.add(rowCell.getNumber());
							}
						}
					}
					
					// �c�����Ő��l�d���`�F�b�N
					for (int index = 0; index < tableWidth; index++) {
						if (index != row) {
							NumberPlaceCell clumnCell = numberTable.getCell(index, clumn);
							if (clumnCell.isNotBlank()) {
								inputNumbers.add(clumnCell.getNumber());
							}
						}
					}
					
					// �u���b�N���Ő��l�d���`�F�b�N
					int startRow = calcStartRow(row);
					int endRow = calcEndRow(row);
					int startClumn = calcStartClumn(clumn);
					int endClumn = calcEndClumn(clumn);
					for (int blockRow = startRow; blockRow <= endRow; blockRow++) {
						for (int blockClumn = startClumn; blockClumn <= endClumn; blockClumn++) {
							if ((row != blockRow) || (clumn != blockClumn)) {
								NumberPlaceCell blockCell = numberTable.getCell(blockRow, blockClumn);
								if (blockCell.isNotBlank()) {
									inputNumbers.add(blockCell.getNumber());
								}
							}
						}
					}
					
					cell.disableNumber(inputNumbers);
					Set<Integer> numberSet = cell.enableNumberList();
					if (numberSet.size() == 1) {
						int number = numberSet.iterator().next();
						System.out.println("["+row+","+clumn+"] ��  "+number+" ��z�u(CanOnlyValue)");
						cell.setNumber(number);
						bResult = true;
					}
				}
			}
		}
		return bResult;
	}
	
	public int calcStartClumn(int clumn) {
		return ((clumn - (clumn % 3)) / 3) * 3;
	}

	public int calcEndClumn(int clumn) {
		return ((clumn - (clumn % 3)) / 3) * 3 + 2;
	}

	public int calcStartRow(int row) {
		return ((row - (row % 3)) / 3) * 3;
	}

	public int calcEndRow(int row) {
		return ((row - (row % 3)) / 3) * 3 + 2;
	}
	
}
