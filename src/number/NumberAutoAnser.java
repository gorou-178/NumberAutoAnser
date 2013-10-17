package number;

import java.util.List;

import number.strategy.NumberPlacePostulateAnalyzer;
import number.strategy.NumberPlaceStrategy;
import number.strategy.SimpleAnalyzer;


public class NumberAutoAnser implements NumberPlaceAnsable {

	private final static int NUMBER_TABLE_WIDTH = 9;
	private NumberPlaceStrategy<List<NumberPlaceTable>> strategy;
	
	public NumberAutoAnser(NumberPlaceTable numberTable) {
//		strategy = new NumberPlacePostulateAnalyzer(numberTable);
		strategy = new SimpleAnalyzer(numberTable);
	}
	
	public void setStrategy(NumberPlaceStrategy<List<NumberPlaceTable>> strategy) {
		this.strategy = strategy;
	}

	@Override
	public String calculate() {
		NumberPlaceAnsableResult<List<NumberPlaceTable>> result = strategy.calculate();
		if (result.isError()) {
			return result.state().getMessage();
		}
		
		if (result.state() == NumberPlaceState.NoAnser) {
			return result.state().getMessage();
		}
		
		System.out.println("‰ð‚Í " + result.output().size() + " ŒÂ‚ ‚è‚Ü‚µ‚½");
		System.out.println("");
		NumberPlaceTable resultTable = result.output().get(0);
		String strResult = resultTable.toString();
		return strResult;
	}
	
	public static void main(String[] args) {
		
		if (args.length != 1) {
			System.out.println("Usage: <text filepath>");
			System.exit(1);
		}
		
		NumberPlaceResult result = new NumberPlaceDataParser(NUMBER_TABLE_WIDTH, args[0]).parse();
		NumberPlaceAnsable numberAnser = new NumberAutoAnser(result.output().get(0));
		String output = numberAnser.calculate();
		System.out.println(output);
	}
	
	

}
