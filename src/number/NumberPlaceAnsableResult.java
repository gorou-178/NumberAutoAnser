package number;

public interface NumberPlaceAnsableResult<T> {
	T output();
	void setState(NumberPlaceState state);
	NumberPlaceState state();
	boolean isError();
}
