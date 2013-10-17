package number.strategy;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import number.NumberPlaceAnsableResult;
import number.NumberPlaceCell;
import number.NumberPlaceResult;
import number.NumberPlaceState;
import number.NumberPlaceTable;

public class SimpleAnalyzer implements NumberPlaceStrategy<List<NumberPlaceTable>> {

	private NumberPlaceTable numberTable;
	private NumberPlaceResult result;
	
	public SimpleAnalyzer(NumberPlaceTable numberTable) {
		this.numberTable = numberTable;
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
		
		// 確定している数字をすべて埋める
		updateCells();
		
		// 完了しているか確認
		if (!checkFinish()) {
			// 数値を仮定して解を求める(再帰処理)
			if (!postulateNumber()) {
				result.setState(NumberPlaceState.NoAnser);
				return;
			}
		} else {
			result.addResult(numberTable);
			result.setState(NumberPlaceState.OneAnser);
		}
	}
	
	/**
	 * 全セルの更新処理
	 * 1.各セルの入力可能数字を更新
	 * 2.各セルの入力可能数字が1つだけのセルを埋める
	 * 3.各セルが属するブロック・行・列に入力されている数字からセルの入力可能数字を割り出して埋める
	 * 4.1～3が無くなるまで行う
	 */
	private void updateCells() {
		boolean update = true;
		while(update) {
			update = false;
			checkEnableNumber();
			update = (update || searchInputOnlyValue()); // ブロック・列・行で、1つだけ埋まっていない数字を探して埋める
			update = (update || searchCanInputOnlyValue()); // セルに配置できる数字が確定している場所を探して埋める
		}
	}
	
	/**
	 * 各セルに入力可能数字を1つずつ仮配置して解を求める(再帰処理)
	 * 仮配置した結果、エラー(矛盾)が発生した時点で、仮配置まえの状態に戻して、別の入力可能数字を仮配置する
	 * すべての解を求めるため、解が1つでても処理を止めない
	 * @return 結果
	 */
	private boolean postulateNumber() {
//		System.out.println("postulateNumber");
		boolean bResult = false;
		// 現状をバックアップ
		NumberPlaceTable backup = new NumberPlaceTable(numberTable);
		NumberPlaceCell blankCell = numberTable.getHiPriorityCell();
		int row = blankCell.row();
		int clumn = blankCell.clumn();
		List<Integer> enableNumberList = blankCell.enableNumberList();
		
		// 配置できる数字をそれぞれ試す
		for (Integer number : enableNumberList) {
			System.out.println("["+row+","+clumn+"] に "+number+" を仮配置します");
			numberTable.getCell(row, clumn).setNumber(number);
			
			updateCells();
			
			if (checkError()) {
				System.out.println("エラーのためロールバックします");
				numberTable = backup;
			} else {
				if (checkFinish()) {
					System.out.println("解答が見つかりました");
					result.addResult(numberTable);
					if (result.output().size() > 1) {
						result.setState(NumberPlaceState.ManyAnser);
					} else if (result.output().size() == 1) {
						result.setState(NumberPlaceState.OneAnser);
					}
					bResult = true;
				} else {
					if (postulateNumber()) {
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
	
	private boolean checkError() {
//		System.out.println("checkError");
		// 初期入力値が10個未満はエラーにする(解けないらしい)
		int numCount = 0;
		for (NumberPlaceCell cell : numberTable.getCells()) {
			if (cell.isNotBlank()) {
				numCount++;
			}
		}
		if (numCount < 10) {
			System.out.println("エラー: 初期入力値は最低でも10個入力してください");
			return true;
		}
		
		for (NumberPlaceCell cell : numberTable.getCells()) {
			if (cell.isNotBlank()) {
				// 横方向で数値重複チェック
				if (numberTable.existSameNumberAtRow(cell, cell.row())) {
					System.out.println(cell.row() + "行で重複エラー");
					return true;
				}
				
				// 縦方向で数値重複チェック
				if (numberTable.existSameNumberAtClumn(cell, cell.clumn())) {
					System.out.println(cell.clumn() + "列で重複エラー");
					return true;
				}
				
				// ブロック内で数値重複チェック
				if (numberTable.getBlock(cell).existSameNumber(cell)) {
					System.out.println("{"+numberTable.getBlockRow(cell)+","+numberTable.getBlockClumn(cell)+"}ブロック内の重複エラー");
					return true;
				}
			}
		}
		
		// 未入力かつ入力可能な数値がないセルのチェック
		if (numberTable.existContradictCell()) {
			System.out.println("未入力セルに対して入力可能な数値がない矛盾エラー");
		}
		
		return false;
	}
	
	private boolean checkFinish() {
//		System.out.println("checkFinish");
		return !numberTable.existBlankCell();
	}
	
	private void checkEnableNumber() {
//		System.out.println("checkEnableNumber");
		for (NumberPlaceCell cell : numberTable.getCells()) {
			if (cell.isBlank()) {
				// セルが属するブロックが利用している数字からセルの入力可能な数字を絞り込む
				cell.disableNumber(numberTable.getBlock(cell).numberSet(cell));
				
				// 横方向で利用している数字からセルの入力可能な数字を絞り込む
				cell.disableNumber(numberTable.numberSetRow(cell));
				
				// 縦方向で利用している数字からセルの入力可能な数字を絞り込む
				cell.disableNumber(numberTable.numberSetClumn(cell));
			}
		}
	}
	
	private boolean searchInputOnlyValue() {
//		System.out.println("searchInputOnlyValue");
		boolean bResult = false;
		for (NumberPlaceCell cell : numberTable.getCells()) {
			if (cell.isBlank()) {
				if (cell.getEnableNumberCount() == 1) {
					int number = cell.enableNumberList().get(0);
					System.out.println("["+cell.row()+","+cell.clumn()+"] に "+number+" を配置(OnlyValue)");
					numberTable.setNumber(cell.row(), cell.clumn(), number);
					bResult = true;
				}
			}
		}
		return bResult;
	}
	
	private boolean searchCanInputOnlyValue() {
//		System.out.println("searchCanInputOnlyValue");
		boolean bResult = false;
		for (NumberPlaceCell cell : numberTable.getCells()) {
			if (cell.isBlank()) {
				// 横方向に入力されている数字を集計
				Set<Integer> inputNumbers = new HashSet<Integer>();
				inputNumbers.addAll(numberTable.numberSetRow(cell));
				
				// 縦方向に入力されている数字を集計
				inputNumbers.addAll(numberTable.numberSetClumn(cell));
				
				// ブロック内で入力されている数字を集計
				inputNumbers.addAll(numberTable.getBlock(cell).numberSet(cell));
				
				// 入力されている数字をすべて無効にする
				cell.disableNumber(inputNumbers);
				
				// 入力可能な数字が1つの場合
				if (cell.canInputOnlyValue()) {
					int number = cell.enableNumberList().get(0);
					System.out.println("["+cell.row()+","+cell.clumn()+"] に "+number+" を配置(CanOnlyValue)");
					cell.setNumber(number);
					bResult = true;
				}
			}
		}
		return bResult;
	}
}
