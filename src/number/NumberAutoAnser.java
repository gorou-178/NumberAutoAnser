package number;

import java.util.List;

import number.strategy.NumberPlaceStrategy;
import number.strategy.SimpleAnalyzer;


public class NumberAutoAnser implements NumberPlaceAnsable {

	private final static int NUMBER_TABLE_WIDTH = 9;
	private NumberPlaceStrategy<List<NumberPlaceTable>> strategy;
	private boolean allAnser;
	
	public NumberAutoAnser(NumberPlaceTable numberTable, boolean allAnser) {
//		strategy = new NumberPlacePostulateAnalyzer(numberTable);
		this.strategy = new SimpleAnalyzer(numberTable);
		this.allAnser = allAnser;
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
		
		StringBuilder sb = new StringBuilder();
		if (allAnser) {
			System.out.println("‚·‚×‚Ä‚Ì‰ð‚ð•\Ž¦‚µ‚Ü‚·");
			System.out.println("");
			String SEP = System.getProperty("line.separator");
			List<NumberPlaceTable> results = result.output();
			for (int i = 0; i < results.size(); i++) {
				sb.append("‰ð"+(i+1));
				sb.append(SEP);
				sb.append(results.get(i).toString());
				sb.append(SEP);
			}
		} else {
			System.out.println("‚»‚Ì‚¤‚¿1‚Â‚Ì‰ð‚ð•\Ž¦‚µ‚Ü‚·");
			System.out.println("");
			sb.append(result.output().get(0).toString());
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		
		if (args.length != 1 && args.length != 2) {
			System.out.println("Usage: <text filepath>");
			System.exit(1);
		}
		
		boolean allAnser = false;
		if (args.length == 2) {
			if ("-all".equals(args[0])) {
				allAnser = true;
			} else {
				System.out.println("Usage: <text filepath>");
				System.exit(1);
			}
		}
		
		String filePath = "";
		if (args.length == 1) {
			filePath = args[0];
		} else if (args.length == 2) {
			filePath = args[1];
		}
		
		NumberPlaceResult result = new NumberPlaceDataParser(NUMBER_TABLE_WIDTH, filePath).parse();
		NumberPlaceAnsable numberAnser = new NumberAutoAnser(result.output().get(0), allAnser);
		String output = numberAnser.calculate();
		System.out.println(output);
	}
	
	

}
