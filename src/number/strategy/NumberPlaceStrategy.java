package number.strategy;

import number.NumberPlaceAnsableResult;

public interface NumberPlaceStrategy<T> {
	NumberPlaceAnsableResult<T> calculate();
}
