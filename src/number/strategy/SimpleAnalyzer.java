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
		// 入力データに不備がないかチェックを行う
		if (checkError()) {
			result.setState(NumberPlaceState.NoAnser);
			return;
		}
		
		updateCells();
		
		// 数値を仮定して解を求める
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
			update = (update || searchInputOnlyValue()); // ブロック・列・行で、1つだけ埋まっていない数字を探して埋める
			update = (update || searchCanInputOnlyValue()); // セルに配置できる数字が確定している場所を探して埋める
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
		//再帰処理のため、ここで参照を切っておく
		blankCellMap = null;
		blankCell = null;
		
		for (Integer number : enableNumberList) {
			System.out.println("["+point.row()+","+point.clumn()+"] に "+number+" を仮配置します");
			numberTable.getCell(point.row(), point.clumn()).setNumber(number);
			
			updateCells();
			
			if (checkError()) {
				System.out.println("エラーのためロールバックします");
				numberTable = backup;
			} else {
				if (checkFinish()) {
					System.out.println("★解答が見つかりました");
					result.addResult(numberTable);
					if (result.output().size() > 1) {
						result.setState(NumberPlaceState.ManyAnser);
					} else if (result.output().size() == 1) {
						result.setState(NumberPlaceState.OneAnser);
					}
					bResult = true;
				} else {
					if (postulateNumber()) {
						System.out.println("★解答が見つかりました");
						result.addResult(numberTable);
						if (result.output().size() > 1) {
							result.setState(NumberPlaceState.ManyAnser);
						} else if (result.output().size() == 1) {
							result.setState(NumberPlaceState.OneAnser);
						}
						bResult = true;
					} else {
						System.out.println("エラーのためロールバックします");
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
		// 初期入力値が10個未満はエラーにする(解けないらしい)
		int numCount = 0;
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				if (numberTable.getCell(row, clumn).isNotBlank()) {
					numCount++;
				}
			}
		}
		if (numCount < 10) {
			System.out.println("エラー: 初期入力値は最低でも10個入力してください");
			return true;
		}
		
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				NumberPlaceCell cell = numberTable.getCell(row, clumn);
				if (cell.isNotBlank()) {
					
					// 横方向で数値重複チェック
					for (int index = 0; index < tableWidth; index++) {
						if (index != clumn) {
							if (numberTable.getCell(row, index).getNumber() == cell.getNumber()) {
								System.out.println(row + "行でエラー: ["+row+","+index+"] と ["+row+","+clumn+"] の値が重複したためエラー");
								return true;
							}
						}
					}
					
					// 縦方向で数値重複チェック
					for (int index = 0; index < tableWidth; index++) {
						if (index != row) {
							if (numberTable.getCell(index, clumn).getNumber() == cell.getNumber()) {
								System.out.println(clumn + "列でエラー: ["+index+","+clumn+"] と ["+row+","+clumn+"] の値が重複したためエラー");
								return true;
							}
						}
					}
					
					// ブロック内で数値重複チェック
					int startRow = calcStartRow(row);
					int endRow = calcEndRow(row);
					int startClumn = calcStartClumn(clumn);
					int endClumn = calcEndClumn(clumn);
					for (int blockRow = startRow; blockRow <= endRow; blockRow++) {
						for (int blockClumn = startClumn; blockClumn <= endClumn; blockClumn++) {
							if ((row != blockRow) || (clumn != blockClumn)) {
								if (numberTable.getCell(blockRow, blockClumn).isNotBlank()) {
									if (numberTable.getCell(blockRow, blockClumn).getNumber() == cell.getNumber()) {
										System.out.println("{"+startRow+","+startClumn+"}ブロックエラー: ["+blockRow+","+blockClumn+"] と ["+row+","+clumn+"] の値が重複したためエラー");
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		
		// 未入力かつ入力可能な数値がないセルのチェック
		for (int row = 0; row < tableWidth; row++) {
			for (int clumn = 0; clumn < tableWidth; clumn++) {
				NumberPlaceCell cell = numberTable.getCell(row, clumn);
				if (cell.isBlank() && (cell.enableNumberList().size() == 0)) {
					System.out.println("矛盾エラー: ["+row+","+clumn+"] に対して入力可能な数値がありません");
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
					
					// セルが属するブロックが利用している数字からセルの入力可能な数字を絞り込む
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
					
					
					// 横方向で利用している数字からセルの入力可能な数字を絞り込む
					inputNumbers = new HashSet<Integer>();
					for (int index = 0; index < tableWidth; index++) {
						if (index != clumn) {
							if (numberTable.getCell(row, index).isNotBlank()) {
								inputNumbers.add(numberTable.getCell(row, index).getNumber());
							}
						}
					}
					cell.disableNumber(inputNumbers);
					
					
					// 縦方向で利用している数字からセルの入力可能な数字を絞り込む
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
						System.out.println("["+row+","+clumn+"] に  "+number+" を配置(OnlyValue)");
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
					
					// 横方向で数値重複チェック
					Set<Integer> inputNumbers = new HashSet<Integer>();
					for (int index = 0; index < tableWidth; index++) {
						if (index != clumn) {
							NumberPlaceCell rowCell = numberTable.getCell(row, index);
							if (rowCell.isNotBlank()) {
								inputNumbers.add(rowCell.getNumber());
							}
						}
					}
					
					// 縦方向で数値重複チェック
					for (int index = 0; index < tableWidth; index++) {
						if (index != row) {
							NumberPlaceCell clumnCell = numberTable.getCell(index, clumn);
							if (clumnCell.isNotBlank()) {
								inputNumbers.add(clumnCell.getNumber());
							}
						}
					}
					
					// ブロック内で数値重複チェック
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
						System.out.println("["+row+","+clumn+"] に  "+number+" を配置(CanOnlyValue)");
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
