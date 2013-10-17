package number;

import java.util.ArrayList;
import java.util.List;

public class NumberPlaceResult implements NumberPlaceAnsableResult<List<NumberPlaceTable>> {

	private NumberPlaceState state;
	private List<NumberPlaceTable> resultList; 
	
	public NumberPlaceResult() {
		this.state = null;
		this.resultList = new ArrayList<NumberPlaceTable>();
	}
	
	public void addResult(NumberPlaceTable numberTable) {
		resultList.add(numberTable);
	}
	
	public void addResult(NumberPlaceResult result) {
		resultList.addAll(result.resultList);
	}
	
	@Override
	public void setState(NumberPlaceState state) {
		this.state = state;
	}
	
	@Override
	public NumberPlaceState state() {
		return state;
	}
	
	@Override
	public boolean isError() {
		return (state == null ? false : state.getErrorCode() < 0 ? true : false);
	}
	
	@Override
	public List<NumberPlaceTable> output() {
		return resultList;
	}

}
