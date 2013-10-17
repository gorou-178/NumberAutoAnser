package number;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NumberPlaceCell {
	private int row;
	private int clumn;
	private int width;
	private int number;
	private Map<Integer, Integer> enableNumberMap;
	
	public NumberPlaceCell(int row, int clumn, int width) {
		this.row = row;
		this.clumn = clumn;
		this.width = width;
		this.enableNumberMap = new HashMap<Integer, Integer>();
		for (int i = 1; i <= width; i++) {
			enableNumberMap.put(i, i);
		}
	}

	public int getNumber() {
		return number;
	}
	
	public int row() {
		return row;
	}
	
	public int clumn() {
		return clumn;
	}
	
	/**
	 * 入力可能数字の個数を取得
	 * @return
	 */
	public int getEnableNumberCount() {
		return enableNumberMap.size();
	}

	public void setNumber(int number) {
		this.number = number;
	}
	
	public boolean isBlank() {
		return (number == 0 ? true : false);
	}
	
	public boolean isNotBlank() {
		return !isBlank();
	}
	
	/**
	 * セルが空欄でかつ入力可能数字が1つ以上あるか確認
	 * セルが矛盾していないかを確認する
	 * @return いずれかの数字が入力可能の場合true, それ以外false
	 */
	public boolean canInputValue() {
		if (isBlank()) {
			return (enableNumberMap.size() == 0 ? false : true);
		}
		return false;
	}
	
	/**
	 * セルが空欄でかつ入力可能数字が1つのみか確認
	 * @return セルが空欄で入力可能数字が1つの場合true, それ以外false
	 */
	public boolean canInputOnlyValue() {
		return (isBlank() ? (enableNumberMap.size() == 1 ? true : false) : false);
	}
	
	public boolean disableNumber(int number) {
		checkNumber(number);
		if (enableNumberMap.containsKey(number)) {
			 enableNumberMap.remove(number);
			 return true;
		}
		return false;
	}
	
	public boolean disableNumber(Set<Integer> numberSet) {
		boolean update = false;
		for (Integer number : numberSet) {
			if (disableNumber(number)) {
				update = true;
			}
		}
		return update;
	}
	
	/**
	 * 指定した数字が入力可能かどうか確認
	 * @param number
	 * @return 入力可能であればtrue, それ以外false
	 */
	public boolean isEnable(int number) {
		checkNumber(number);
		return enableNumberMap.containsKey(number);
	}
	
	/**
	 * 入力可能な数字のリストを返す
	 * @return
	 */
	public List<Integer> enableNumberList() {
		return new ArrayList<Integer>(enableNumberMap.keySet());
	}
	
	/**
	 * 入力不可能な数字のリストを返す
	 * @return
	 */
	public Set<Integer> disableNumberList() {
		Set<Integer> disableNumberList = new HashSet<Integer>();
		for (int i = 1; i <= width; i++) {
			disableNumberList.add(i);
		}
		disableNumberList.removeAll(enableNumberMap.keySet());
		return disableNumberList;
	}
	
	private void checkNumber(int number) {
		if (number < 0 || number > width) {
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + clumn;
		result = prime * result + number;
		result = prime * result + row;
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
		NumberPlaceCell other = (NumberPlaceCell) obj;
		if (clumn != other.clumn)
			return false;
		if (number != other.number)
			return false;
		if (row != other.row)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NumberPlaceCell [row=" + row + ", clumn=" + clumn + ", width="
				+ width + ", number=" + number + ", enableNumberMap="
				+ enableNumberMap + "]";
	}
	
}
