package number.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import number.NumberPlaceAnsableResult;
import number.NumberPlaceCell;
import number.NumberPlaceResult;
import number.NumberPlaceState;
import number.NumberPlaceTable;

public class NumberPlacePostulateAnalyzer implements NumberPlaceStrategy<List<NumberPlaceTable>> {

	private NumberPlaceTable numberTable;
	
//	private static class Worker {
//		private String name;
//		private AtomicInteger uniqId;
//		private NumberPlaceTable numberTable;
//		private NumberPlaceResult result;
//		public Worker(String parentName, NumberPlaceTable numberTable) {
//			this.uniqId = new AtomicInteger(1);
//			this.name = parentName + "-" + uniqId.incrementAndGet();
//			this.numberTable = new NumberPlaceTable(numberTable);
//			this.result = new NumberPlaceResult();
//			this.result.setState(NumberPlaceState.NoAnser);
//		}
//		public NumberPlaceResult work(boolean parent) throws ContradictException {
//			
////			if (parent) {
//				while(innerWork()) {}
////			} else {
////				innerWork();
////			}
//			
//			return result; 
//		}
//		
//		private boolean checkOnlyNumber() throws ContradictException {
//			boolean update = false;
//			List<NumberPlaceCell> blankCells = findBlankCell();
//			for (int i = 0; i < blankCells.size(); i++) {
//				NumberPlaceCell cell = blankCells.get(i);
//				List<Integer> enableNumberList = cell.enableNumberList();
//				
////				// おける数字が無いということは、矛盾しているため解析を終了させる
////				if (enableNumberList.isEmpty()) {
////					System.out.println(this.name + " >> おける数値が無いため矛盾になりました: [" + cell.getPoint().row() + ", " + cell.getPoint().clumn() + "]");
////					throw new ContradictException();
////				}
//				
//				// 数字が確定している場合
//				if (enableNumberList.size() == 1) {
//					if (!cell.isEnable(enableNumberList.get(0))) {
//						System.out.println(this.name + " >> 数値が矛盾しています: [" + cell.getPoint().row() + ", " + cell.getPoint().clumn() + "]");
//						throw new ContradictException();
//					}
//					System.out.println(this.name + " >> [" + cell.getPoint().row() + "," + cell.getPoint().clumn() + "] に " + enableNumberList.get(0) + " を配置しました");
//					numberTable.setNumber(cell.getPoint().row(), cell.getPoint().clumn(), enableNumberList.get(0));
//					updateEnableNumber(cell.getPoint().row(), cell.getPoint().clumn());
//					update = true;
//				} else {
//					for (int j = 0; j < enableNumberList.size(); j++) {
//						Integer number = enableNumberList.get(j);
//						boolean disableRow = checkDisableNumberRow(cell, number);
//						boolean disableClumn = checkDisableNumberClumn(cell, number);
//						boolean disableBlock = checkDisableNumberBlock(cell, number);
//						if (disableRow && disableClumn && disableBlock) {
//							System.out.println(this.name + " >> [" + cell.getPoint().row() + "," + cell.getPoint().clumn() + "] に " + number + " を配置しました");
//							numberTable.setNumber(cell.getPoint().row(), cell.getPoint().clumn(), number);
//							updateEnableNumber(cell.getPoint().row(), cell.getPoint().clumn());
//							continue;
//						}
//					}
//				}
//			}
//			return update;
//		}
//		
//		
//		
//		private boolean innerWork() throws ContradictException {
//			List<NumberPlaceCell> blankCells = findBlankCell();
//			if (blankCells.isEmpty()) {
//				this.result.addResult(numberTable);
//				if (result.output().size() > 1) {
//					result.setState(NumberPlaceState.ManyAnser);
//				} else if (result.output().size() == 1){
//					result.setState(NumberPlaceState.OneAnser);
//				} else {
//					result.setState(NumberPlaceState.NoAnser);
//				}
//				return false;
//			}
//			
//			updateEnableNumber();
//			
//			// 各空白毎に処理を進める
//			blankCells = findBlankCell();
//			if (blankCells.isEmpty()) {
//				this.result.addResult(numberTable);
//				if (result.output().size() > 1) {
//					result.setState(NumberPlaceState.ManyAnser);
//				} else if (result.output().size() == 1){
//					result.setState(NumberPlaceState.OneAnser);
//				} else {
//					result.setState(NumberPlaceState.NoAnser);
//				}
//				return false;
//			}
//			
//			for (int i = 0; i < blankCells.size(); i++) {
//				NumberPlaceCell cell = blankCells.get(i);
//				List<Integer> enableNumberList = cell.enableNumberList();
//				
//				// おける数字が無いということは、矛盾しているため解析を終了させる
//				if (enableNumberList.isEmpty()) {
//					System.out.println(this.name + " >> おける数値が無いため矛盾になりました: [" + cell.getPoint().row() + ", " + cell.getPoint().clumn() + "]");
//					throw new ContradictException();
//				}
//				
//				// 数字が確定している場合
//				if (enableNumberList.size() == 1) {
//					if (!cell.isEnable(enableNumberList.get(0))) {
//						return false;
//					}
//					System.out.println(this.name + " >> [" + cell.getPoint().row() + "," + cell.getPoint().clumn() + "] に " + enableNumberList.get(0) + " を配置しました(解法1)");
//					numberTable.setNumber(cell.getPoint().row(), cell.getPoint().clumn(), enableNumberList.get(0));
//					updateEnableNumber(cell.getPoint().row(), cell.getPoint().clumn());
//				} else {
//					for (int j = 0; j < enableNumberList.size(); j++) {
//						Integer number = enableNumberList.get(j);
//						if (!cell.isEnable(number)) {
//							continue;
//						}
//						NumberPlaceTable oldTable = new NumberPlaceTable(numberTable);
//						System.out.println(this.name + " >> [" + cell.getPoint().row() + ", " + cell.getPoint().clumn() + "] に " + number + "を仮配置します");
//						numberTable.setNumber(cell.getPoint().row(), cell.getPoint().clumn(), number);
//						updateEnableNumber(cell.getPoint().row(), cell.getPoint().clumn());
//						Worker worker = new Worker(this.name, numberTable);
//						try {
//							NumberPlaceResult workerResult = worker.work(false);
//							if (workerResult.isError()) {
//								System.out.println(this.name + " >> エラーが発生しました: " + workerResult.state().getMessage());
//							} else {
//								this.result.addResult(workerResult);
//								if (result.output().size() > 1) {
//									result.setState(NumberPlaceState.ManyAnser);
//								} else if (result.output().size() == 1){
//									result.setState(NumberPlaceState.OneAnser);
//								} else {
//									result.setState(workerResult.state());
//								}
//								return true;
//							}
//						} catch (ContradictException e) {
//							// 矛盾が発生した場合はテーブルを借り配置前の状態に戻す
//							System.out.println(this.name + " >> 矛盾が発生しました: [" + cell.getPoint().row() + ", " + cell.getPoint().clumn() + "]");
//							numberTable = oldTable;
//						}
//					}
//				}
//			}
//			return false;
//		}
//		
//		private Map<Integer, Integer> createEnableNumberMap() {
//			Map<Integer, Integer> enableNumberMap = new HashMap<Integer, Integer>();
//			for (int i = 1; i <= numberTable.getWidth(); i++) {
//				enableNumberMap.put(i, i);
//			}
//			return enableNumberMap;
//		}
//		
//		private void updateEnableNumber() throws ContradictException {
//			boolean update = true;
//			while(update) {
//				update = updateAllEnableNumberAtBlock();
//			}
//			
//			update = true;
//			while(update) {
//				update = updateAllEnableNumberAtRow();
//			}
//			
//			update = true;
//			while(update) {
//				update = updateAllEnableNumberAtClumn();
//			}
//			
//			while(update) {
//				update = checkOnlyNumber();
//			}
//		}
//		
//		private void updateEnableNumber(int row, int clumn) throws ContradictException {
//			boolean update = true;
//			while(update) {
//				update = updateEnableNumberAtBlock(row, clumn);
//			}
//			
//			update = true;
//			while(update) {
//				update = updateEnableNumberAtRow(row);
//			}
//			
//			update = true;
//			while(update) {
//				update = updateEnableNumberAtClumn(clumn);
//			}
//			
//			update = true;
//			while(update) {
//				update = checkOnlyNumber();
//			}
//		}
//		
//		private boolean updateEnableNumberAtRow(int row) throws ContradictException {
//			Set<Integer> numberSet = new HashSet<Integer>();
//			
//			// 縦に配置された数字を取り出す
//			for (int i = 0; i < numberTable.getWidth(); i++) {
//				if (numberTable.getCell(row, i).isNotBlank()) {
//					if (!numberSet.add(numberTable.getCell(row, i).getNumber())) {
//						System.out.println(this.name + " >> 矛盾が発生しました [" + row + ", " + i + "] " + numberTable.getCell(row, i).getNumber() + " は既に配置されています");
//						throw new ContradictException();
//					}
//				}
//			}
//			
//			boolean update = false;
//			for (int i = 0; i < numberTable.getWidth(); i++) {
//				if (numberTable.getCell(row, i).isBlank()) {
//					if (numberTable.getCell(row, i).disableNumber(numberSet)) {
//						update = true;
//					}
//				}
//			}
//			
//			return update;
//		}
//		
//		private boolean updateAllEnableNumberAtRow() throws ContradictException {
//			boolean update = false;
//			for (int i = 0; i < numberTable.getWidth(); i++) {
//				if (updateEnableNumberAtRow(i)) {
//					update = true;
//				}
//			}
//			return update;
//		}
//		
//		private boolean updateEnableNumberAtClumn(int clumn) throws ContradictException {
//			Set<Integer> numberSet = new HashSet<Integer>();
//			
//			// 縦に配置された数字を取り出す
//			for (int i = 0; i < numberTable.getWidth(); i++) {
//				if (numberTable.getCell(i, clumn).isNotBlank()) {
//					if (!numberSet.add(numberTable.getCell(i, clumn).getNumber())) {
//						System.out.println(this.name + " >> 矛盾が発生しました [" + i + ", " + clumn + "] " + numberTable.getCell(i, clumn).getNumber() + " は既に配置されています");
//						throw new ContradictException();
//					}
//				}
//			}
//			
//			boolean update = false;
//			for (int i = 0; i < numberTable.getWidth(); i++) {
//				if (numberTable.getCell(i, clumn).isBlank()) {
//					if (numberTable.getCell(i, clumn).disableNumber(numberSet)) {
//						update = true;
//					}
//				}
//			}
//			return update;
//		}
//		
//		private boolean updateAllEnableNumberAtClumn() throws ContradictException {
//			boolean update = false;
//			for (int i = 0; i < numberTable.getWidth(); i++) {
//				if (updateEnableNumberAtClumn(i)) {
//					update = true;
//				}
//			}
//			return update;
//		}
//		
//		private boolean updateEnableNumberAtBlock(int row, int clumn) throws ContradictException {
//			int blockWidth = (int)Math.sqrt(numberTable.getWidth());
//			int blockRow = row / blockWidth;
//			int blockClumn = clumn / blockWidth;
//			return updateEnableNumberAtBlock(numberTable.getBlock(blockRow, blockClumn));
//		}
//		
//		private boolean updateEnableNumberAtBlock(NumberPlaceBlock block) throws ContradictException {
//			return block.updateEnableNumber();
//		}
//		
//		private boolean updateAllEnableNumberAtBlock() throws ContradictException {
//			int blockWidth = (int)Math.sqrt(numberTable.getWidth());
//			boolean update = false;
//			for (int i = 0; i < blockWidth; i++) {
//				for (int j = 0; j < blockWidth; j++) {
//					if (updateEnableNumberAtBlock(numberTable.getBlock(i, j))) {
//						update = true;
//					}
//				}
//			}
//			return update;
//		}
//		
//		private boolean updateEnableNumberAtBlockRow(int blockRow) throws ContradictException {
//			int blockWidth = (int)Math.sqrt(numberTable.getWidth());
//			boolean update = false;
//			for (int i = 0; i < blockWidth; i++) {
//				if (updateEnableNumberAtBlock(numberTable.getBlock(blockRow, i))) {
//					update = true;
//				}
//			}
//			return update;
//		}
//		
//		private boolean updateEnableNumberAtBlockClumn(int blockClumn) throws ContradictException {
//			int blockWidth = (int)Math.sqrt(numberTable.getWidth());
//			boolean update = false;
//			for (int i = 0; i < blockWidth; i++) {
//				if (updateEnableNumberAtBlock(numberTable.getBlock(i, blockClumn))) {
//					update = true;
//				}
//			}
//			return update;
//		}
//	}
	
	public NumberPlacePostulateAnalyzer(NumberPlaceTable numberTable) {
		this.numberTable = numberTable;
	}
	
	@Override
	public NumberPlaceAnsableResult<List<NumberPlaceTable>> calculate() {
		return mainCalculate();
	}

	/**
	 * 1. 空欄を探す(空欄の利用できる数字が少ない所を優先的に処理対象にする)
	 * 2. 空欄に対して、空欄を含む縦横の利用できない数字データを更新 → 絞り込めた場合は数値を確定して1に戻る
	 * 3. 空欄に対して、空欄を含むブロックの利用できない数字データを更新 → 絞り込めた場合は数値を確定して1に戻る
	 * 4. 2と3で絞り込めない場合、1つずつ仮においたテーブルデータを新しいアナライザーに渡して解析させる
	 * 5. 矛盾が生じた場合はそのアナライザーを破棄して、別のアナライザーを試す
	 * 6. 一番親のアナライザーで矛盾が生じた場合は、解析不能とする
	 * 7. アナライザーが解答を返した場合、別の解答がないかを探す
	 */
	private NumberPlaceResult mainCalculate() {
		NumberPlaceResult result = new NumberPlaceResult();
		result.setState(NumberPlaceState.NoAnser);
		
//		try {
//			Worker mainWorker = new Worker("worker", numberTable);
//			result = mainWorker.work(true);
//		} catch (ContradictException e) {
//			System.out.println("矛盾が発生しました: mainWorker");
//		}
		
		// このへんのチェックがちゃんとできていないみたい
		boolean update = true;
		while(update) {
			update = false;
			update = (update || checkGroupRow());
			update = (update || checkGroupClumn());
		}
		
		update = true;
		while(update) {
			update = false;
			update = (update || checkOnlyValue()); // 縦・横・グループ内で1つだけマスが開いている場合
			update = (update || checkCanInputOnlyValue()); // 縦・横・グループ内で配置できる数字が1つだけの場合
			update = (update || checkGroupRow());
			update = (update || checkGroupClumn());
		}
		
		if (!checkFinish()) {
			if(!postulateNumber(result)) {
				result.setState(NumberPlaceState.NoAnser);
			}
		}
		
		return result;
	}
	private boolean checkGroupRow() {
		System.out.println("debug: checkGroupRow");
		boolean bResult = false;
		int width = numberTable.getWidth();
		int blockWidth = (int)Math.sqrt(width);
		for (int number = 1; number <= width; number++) {
			for (int row = 0; row < blockWidth; row++) {

				boolean g1 = !checkNumberPositionGroup(numberTable.getCell(row*blockWidth, 0), number);
				boolean g2 = !checkNumberPositionGroup(numberTable.getCell(row*blockWidth, 3), number);
				boolean g3 = !checkNumberPositionGroup(numberTable.getCell(row*blockWidth, 6), number);
				
				if ((!g1) && g2 && g3) bResult = checkGroupSub(row*blockWidth, 0, number);
				if (g1 && (!g2) && g3) bResult = checkGroupSub(row*blockWidth, 3, number);
				if (g1 && g2 && (!g3)) bResult = checkGroupSub(row*blockWidth, 6, number);
			}
		}
		return bResult;
	}
	
	private boolean checkGroupSub(int row, int clumn, int number) {
		boolean bResult = false;
		int startRow = calcStartRow(row);
		int endRow = calcEndRow(row);
		int startClumn = calcStartClumn(clumn);
		int endClumn = calcEndClumn(clumn);
		int inputCount = 0;
		NumberPlaceCell cell = null;
		
		for (int k = startRow; k <= endRow; k++) {
			for (int k2 = startClumn; k2 <= endClumn; k2++) {
				if (checkNumberPositionAll(numberTable.getCell(k, k2), number)) {
					++inputCount;
					cell = numberTable.getCell(k, k2);
				}
			}
		}
		
		if (inputCount == 1) {
			System.out.println("["+cell.getPoint().row()+","+cell.getPoint().clumn()+"] に "+number+" を配置");
			cell.setNumber(number);
			bResult = true;
		}
		
		return bResult;
	}
	
	private boolean checkNumberPosition(NumberPlaceCell cell, Integer number) {
		if (cell.isNotBlank()) {
			return false;
		}
		return true;
	}
	
	private boolean checkNumberPositionRow(NumberPlaceCell cell, Integer number) {
		for (int i = 0; i < numberTable.getWidth(); i++) {
			if (numberTable.getCell(cell.getPoint().row(), i).getNumber() == number) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkNumberPositionClumn(NumberPlaceCell cell, Integer number) {
		for (int i = 0; i < numberTable.getWidth(); i++) {
			if (numberTable.getCell(i, cell.getPoint().clumn()).getNumber() == number) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkNumberPositionGroup(NumberPlaceCell cell, Integer number) {
		int row = cell.getPoint().row();
		int clumn = cell.getPoint().clumn();
		int startRow = calcStartRow(row);
		int endRow = calcEndRow(row);
		int startClumn = calcStartClumn(clumn);
		int endClumn = calcEndClumn(clumn);
		for (int k = startRow; k <= endRow; k++) {
			for (int k2 = startClumn; k2 <= endClumn; k2++) {
				if (numberTable.getCell(k, k2).getNumber() == number) {
					return false;
				}
			}
		}
		return true;
	}
	
	private boolean checkNumberPositionAll(NumberPlaceCell cell, Integer number) {
		int width = numberTable.getWidth();
		int blockWidth = (int)Math.sqrt(width);
		int row = cell.getPoint().row();
		int clumn = cell.getPoint().clumn();
		
		if (cell.isNotBlank()) {
			return false;
		}
		
		for (int i = 0; i < width; i++) {
			if (numberTable.getCell(row, i).getNumber() == number) {
				return false;
			}
		}
		
		for (int i = 0; i < width; i++) {
			if (numberTable.getCell(i, clumn).getNumber() == number) {
				return false;
			}
		}
		
		int startRow = calcStartRow(row);
		int endRow = calcEndRow(row);
		int startClumn = calcStartClumn(clumn);
		int endClumn = calcEndClumn(clumn);
		for (int k = startRow; k <= endRow; k++) {
			for (int k2 = startClumn; k2 <= endClumn; k2++) {
				if (numberTable.getCell(k, k2).getNumber() == number) {
					return false;
				}
			}
		}
		
		return true;
	}
	private boolean checkGroupClumn() {
		System.out.println("debug: checkGroupClumn");
		boolean bResult = false;
		int width = numberTable.getWidth();
		int blockWidth = (int)Math.sqrt(width);
		for (int number = 1; number <= width; number++) {
			for (int clumn = 0; clumn < blockWidth; clumn++) {
				
				boolean g1 = !checkNumberPositionGroup(numberTable.getCell(0, clumn*blockWidth), number);
				boolean g2 = !checkNumberPositionGroup(numberTable.getCell(3, clumn*blockWidth), number);
				boolean g3 = !checkNumberPositionGroup(numberTable.getCell(6, clumn*blockWidth), number);
				
				if ((!g1) && g2 && g3) bResult = checkGroupSub(0, clumn*blockWidth, number);
				if (g1 && (!g2) && g3) bResult = checkGroupSub(3, clumn*blockWidth, number);
				if (g1 && g2 && (!g3)) bResult = checkGroupSub(6, clumn*blockWidth, number);
				
			}
		}
		return bResult;
	}
	private boolean checkOnlyValue() {
		System.out.println("debug: checkOnlyValue");
		boolean bResult = false;
		int width = numberTable.getWidth();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				NumberPlaceCell cell = numberTable.getCell(i, j);
				if (cell.isBlank()) {
					Set<Integer> enableNumberList = enableNumberList(cell);
					if (enableNumberList.size() == 1) {
						Integer number = enableNumberList.iterator().next();
						System.out.println("[" + cell.getPoint().row() + "," + cell.getPoint().clumn() + "] に " + number + " を配置しました(OnlyNumber)");
						cell.setNumber(number);
						bResult = true;
					}
				}
			}
		}
		return bResult;
	}
	
	private boolean checkCanInputOnlyValueSub(NumberPlaceCell cell, Set<Integer> cellEnableList, Set<Integer> enableList) {
		boolean update = false;
//		for (int i = 1; i <= numberTable.getWidth(); i++) {
		cellEnableList.removeAll(enableList);
		if (cellEnableList.size() != 1) {
			return update;
		}
		
		for (Integer number : cellEnableList) {
			System.out.println("[" + cell.getPoint().row() + "," + cell.getPoint().clumn() + "] に " + number + " を配置しました(CanInputOnly)");
			cell.setNumber(number);
			update = true;
		}
//			if (cellEnableList.size() == 1 && enableList.size() == 1) {
//				if (cellEnableList.contains(i) && enableList.contains(i)) {
//					System.out.println("[" + cell.getPoint().row() + "," + cell.getPoint().clumn() + "] に " + i + " を配置しました(CanInputOnly)");
//					cell.setNumber(i);
//					update = true;
//				}
//			}
//		}
		return update;
	}
	
	private boolean checkCanInputOnlyValue() {
		System.out.println("debug: checkCanInputOnlyValue");
		boolean bResult = false;
		int width = numberTable.getWidth();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				NumberPlaceCell cell = numberTable.getCell(i, j);
				Set<Integer> cellEnableList = enableNumberList(cell);
				Set<Integer> disableList = new HashSet<Integer>();
				Set<Integer> enableList = new HashSet<Integer>();
				for (int k = 0; k < width; k++) {
					if (i != k) {
						enableList.addAll(enableNumberList(numberTable.getCell(k, j)));
//						disableList.addAll(disableNumberList(numberTable.getCell(k, j)));
					}
				}
				if (checkCanInputOnlyValueSub(cell, cellEnableList, enableList)) {
					cellEnableList = enableNumberList(cell);
					bResult = true;
				}
				
				enableList = new HashSet<Integer>();
				for (int k = 0; k < width; k++) {
					if (j != k) {
						enableList.addAll(enableNumberList(numberTable.getCell(i, k)));
//						disableList.addAll(disableNumberList(numberTable.getCell(i, k)));
					}
				}
				if (checkCanInputOnlyValueSub(cell, cellEnableList, enableList)) {
					cellEnableList = enableNumberList(cell);
					bResult = true;
				}
				
				int startRow = calcStartRow(i);
				int endRow = calcEndRow(i);
				int startClumn = calcStartClumn(j);
				int endClumn = calcEndClumn(j);
				enableList = new HashSet<Integer>();
				for (int k = startRow; k <= endRow; k++) {
					for (int k2 = startClumn; k2 <= endClumn; k2++) {
						if ((i != k) || (j != k2)) {
							enableList.addAll(enableNumberList(numberTable.getCell(k, k2)));
//							disableList.addAll(disableNumberList(numberTable.getCell(k, k2)));
						}
					}
				}
				if (checkCanInputOnlyValueSub(cell, cellEnableList, enableList)) {
					cellEnableList = enableNumberList(cell);
					bResult = true;
				}
			}
		}
		return bResult;		
//					
//					
//					numberSet = new HashSet<Integer>();
//					for (int k = 0; k < width; k++) {
//						if (numberTable.getCell(k, j).isNotBlank()) {
//							numberSet.add(numberTable.getCell(k, j).getNumber());
//						}
//					}
//					if (cell.disableNumber(numberSet)) {
//						if (cell.enableNumberList().size() == 1) {
//							System.out.println("[" + cell.getPoint().row() + "," + cell.getPoint().clumn() + "] に " + cell.enableNumberList().get(0) + " を配置しました(LineClumn)");
//							cell.disableNumber(cell.enableNumberList().get(0));
//							cell.setNumber(cell.enableNumberList().get(0));
//						}
//						bResult = true;
//					}
					
//					numberSet = new HashSet<Integer>();
//					if (numberTable.getBlock(i/blockWidth, j/blockWidth).updateEnableNumber()) {
//						if (cell.enableNumberList().size() == 1) {
//							System.out.println("[" + cell.getPoint().row() + "," + cell.getPoint().clumn() + "] に " + cell.enableNumberList().get(0) + " を配置しました(Block)");
//							cell.disableNumber(cell.enableNumberList().get(0));
//							cell.setNumber(cell.enableNumberList().get(0));
//						}
//						bResult = true;
//					}
//				}
//			}
//		}
//		return bResult;
	}
	
	private Set<Integer> enableNumberList(NumberPlaceCell cell) {
		Set<Integer> enableList = new HashSet<Integer>();
		for (int i = 1; i <= numberTable.getWidth(); i++) {
			if (checkNumberPositionAll(cell, i)) {
				enableList.add(i);
			}
		}
		return enableList;
	}
	
	private Set<Integer> disableNumberList(NumberPlaceCell cell) {
		Set<Integer> disableList = new HashSet<Integer>();
		for (int i = 1; i <= numberTable.getWidth(); i++) {
			disableList.add(i);
		}
		Set<Integer> enableList = enableNumberList(cell);
		disableList.removeAll(enableList);
		return disableList;
	}
	
	private boolean checkFinish() {
		return (findBlankCell().size() == 0 ? true : false);
	}
	private boolean checkError() {
		int width = numberTable.getWidth();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				NumberPlaceCell cell = numberTable.getCell(i, j);
				if (cell.isBlank()) {
					continue;
				}
				
				for (int k = j + 1; k < width; k++) {
					if (numberTable.getCell(i, k).isNotBlank()) {
						if (numberTable.getCell(i, k).getNumber() == cell.getNumber()) {
							System.out.println("["+i+","+k+"] = " + numberTable.getCell(i, k).getNumber() + " は ["+i+","+j+"] = "+cell.getNumber()+"にあります");
							return true;
						}
					}
				}
				
				for (int k = i + 1; k < width; k++) {
					if (numberTable.getCell(k, j).isNotBlank()) {
						if (numberTable.getCell(k, j).getNumber() == cell.getNumber()) {
							System.out.println("["+k+","+j+"] = " + numberTable.getCell(k, j).getNumber() + " は ["+i+","+j+"] = "+cell.getNumber()+"にあります");
							return true;
						}
					}
				}
				
				int startRow = calcStartRow(i);
				int endRow = calcEndRow(i);
				int startClumn = calcStartClumn(j);
				int endClumn = calcEndClumn(j);
				
				for (int k = startRow; k <= endRow; k++) {
					for (int k2 = startClumn; k2 <= endClumn; k2++) {
						if ((i != k) || (j != k2)) {
							if (numberTable.getCell(k, k2).isNotBlank()) {
								if (numberTable.getCell(k, k2).getNumber() == cell.getNumber()) {
									System.out.println("["+k+","+k2+"] = " + numberTable.getCell(k, k2).getNumber() + " は ["+i+","+j+"] = "+cell.getNumber()+"にあります");
									return true;
								}
							}
						}
					}
				}
				
			}
		}
		
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < width; j++) {
				NumberPlaceCell cell = numberTable.getCell(i, j);
				if (cell.isBlank() && enableNumberList(cell).size() == 0) {
					System.out.println("おける数値が無いエラーが発生しました");
					return true;
				}
			}
		}
		
		return false;
	}
	private boolean postulateNumber(NumberPlaceResult result) {
		System.out.println("debug: postulateNumber");
		boolean bResult = false;
		NumberPlaceTable clone = new NumberPlaceTable(numberTable);
		List<NumberPlaceCell> blankCells = findBlankCell();
		NumberPlaceCell cell = blankCells.get(0);
		for (Integer number : enableNumberList(cell)) {
			System.out.println("[" + cell.getPoint().row() + ", " + cell.getPoint().clumn() + "] に " + number + "を仮配置します");
			cell.setNumber(number);
			boolean update = true;
			while(update) {
				update = false;
				update = (update || checkOnlyValue()); // 縦・横・グループ内で1つだけマスが開いている場合
				update = (update || checkCanInputOnlyValue()); // 縦・横・グループ内で配置できる数字が1つだけの場合
				update = (update || checkGroupRow());
				update = (update || checkGroupClumn());
			}
			
			if (checkError()) {
				System.out.println("エラーのためロールバックします");
				numberTable = clone;
			} else {
				if (checkFinish()) {
					System.out.println("解答が見つかりました");
					result.addResult(numberTable);
					if (result.output().size() > 1) {
						result.setState(NumberPlaceState.ManyAnser);
					} else if (result.output().size() == 1) {
						result.setState(NumberPlaceState.OneAnser);
					}
					return true;
				} else {
					if (postulateNumber(result)) {
						System.out.println("解答が見つかりました");
						result.addResult(numberTable);
						if (result.output().size() > 1) {
							result.setState(NumberPlaceState.ManyAnser);
						} else if (result.output().size() == 1) {
							result.setState(NumberPlaceState.OneAnser);
						}
						return true;
					} else {
						System.out.println("エラーのためロールバックします");
						numberTable = clone;
					}
				}
			}
		}
		return bResult;
	}
	
	private List<NumberPlaceCell> findBlankCell() {
		List<NumberPlaceCell> result = new ArrayList<NumberPlaceCell>();
		for (int i = 0; i < numberTable.getWidth(); i++) {
			for (int j = 0; j < numberTable.getWidth(); j++) {
				if (numberTable.isBlank(i, j)) {
					result.add(numberTable.getCell(i, j));
				}
			}
		}
		
		Collections.sort(result, new Comparator<NumberPlaceCell>() {
			@Override
			public int compare(NumberPlaceCell o1, NumberPlaceCell o2) {
				return enableNumberList(o1).size() - enableNumberList(o2).size();
			}
		});
		
		return result;
	}
	
	//グループ列開始位置算出
	private int calcStartClumn(int clumn)
	{
		return ((clumn - (clumn % 3)) / 3) * 3;
	}

	//グループ列終了位置算出
	private int calcEndClumn(int clumn)
	{
		return ((clumn - (clumn % 3)) / 3) * 3 + 2;
	}

	//グループ行開始位置算出
	private int calcStartRow(int row)
	{
		return ((row - (row % 3)) / 3) * 3;
	}

	//グループ行終了位置算出
	private int calcEndRow(int row)
	{
		return ((row - (row % 3)) / 3) * 3 + 2;
	}
	
//	private boolean updateEnableNumberAtRow(int row) {
//		Set<Integer> numberSet = new HashSet<Integer>();
//
//		// 縦に配置された数字を取り出す
//		for (int i = 0; i < numberTable.getWidth(); i++) {
//			if (numberTable.getCell(row, i).isNotBlank()) {
//				numberSet.add(numberTable.getCell(row, i).getNumber());
//			}
//		}
//
//		boolean update = false;
//		for (int i = 0; i < numberTable.getWidth(); i++) {
//			if (numberTable.getCell(row, i).isBlank()) {
//				if (numberTable.getCell(row, i).disableNumber(numberSet)) {
//					update = true;
//				}
//			}
//		}
//
//		return update;
//	}
//
//	private boolean updateAllEnableNumberAtRow() {
//		boolean update = false;
//		for (int i = 0; i < numberTable.getWidth(); i++) {
//			if (updateEnableNumberAtRow(i)) {
//				update = true;
//			}
//		}
//		return update;
//	}
//
//	private boolean updateEnableNumberAtClumn(int clumn) {
//		Set<Integer> numberSet = new HashSet<Integer>();
//
//		// 縦に配置された数字を取り出す
//		for (int i = 0; i < numberTable.getWidth(); i++) {
//			if (numberTable.getCell(i, clumn).isNotBlank()) {
//				numberSet.add(numberTable.getCell(i, clumn).getNumber());
//			}
//		}
//
//		boolean update = false;
//		for (int i = 0; i < numberTable.getWidth(); i++) {
//			if (numberTable.getCell(i, clumn).isBlank()) {
//				if (numberTable.getCell(i, clumn).disableNumber(numberSet)) {
//					update = true;
//				}
//			}
//		}
//		return update;
//	}
//
//	private boolean updateAllEnableNumberAtClumn() {
//		boolean update = false;
//		for (int i = 0; i < numberTable.getWidth(); i++) {
//			if (updateEnableNumberAtClumn(i)) {
//				update = true;
//			}
//		}
//		return update;
//	}
}
