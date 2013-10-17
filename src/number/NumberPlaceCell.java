package number;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NumberPlaceCell {
	private NumberPlacePoint point;
	private int width;
	private int number;
	private Map<Integer, Integer> enableNumberMap;
	
	public NumberPlaceCell(int row, int clumn, int width) {
		this.point = new NumberPlacePoint(row, clumn);
		this.width = width;
		this.enableNumberMap = new HashMap<Integer, Integer>();
		for (int i = 1; i <= width; i++) {
			enableNumberMap.put(i, i);
		}
	}

	public int getNumber() {
		return number;
	}
	
	public NumberPlacePoint getPoint() {
		return point;
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
	
	public boolean isEnable(int number) {
		checkNumber(number);
		return enableNumberMap.containsKey(number);
	}
	
	public void enableNumber(int number) {
		checkNumber(number);
		enableNumberMap.put(number, number);
	}
	
	public void enableNumber(Set<Integer> numberSet) {
		for (Integer number : numberSet) {
			enableNumber(number);
		}
	}
	
	public Set<Integer> enableNumberList() {
		return enableNumberMap.keySet();
	}
	
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
		result = prime * result + width;
		result = prime * result + number;
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
		if (width != other.width)
			return false;
		if (number != other.number)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NumberPlaceCell [width=" + width + ", number=" + number
				+ "]";
	}
	
}
