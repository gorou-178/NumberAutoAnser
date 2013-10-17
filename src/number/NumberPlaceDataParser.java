package number;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class NumberPlaceDataParser {

	private final static int WIDTH_9 = 9;
	private final static int WIDTH_16 = 16;
	private final static int WIDTH_25 = 25;
	private final static int[] SUPPORT_WIDTH = {WIDTH_9};
	
	private int width;
	private String filePath;
	public NumberPlaceDataParser(int width, String filePath) {
		this.width = width;
		this.filePath = filePath;
	}
	
	private boolean checkSupportWidth(int width) {
		for (int i = 0; i < SUPPORT_WIDTH.length; i++) {
			if (width == SUPPORT_WIDTH[i]) {
				return true;
			}
		}
		return false;
	}
	
	public NumberPlaceResult parse() {
		
		NumberPlaceTable numberTable = new NumberPlaceTable(width);
		NumberPlaceResult result = new NumberPlaceResult();
		result.addResult(numberTable);
		
		if (!checkSupportWidth(width)) {
			result.setState(NumberPlaceState.UnsupportWidth);
			return result;
		}
		
		BufferedReader br = null;
		try {
			File inputFile = new File(filePath);
			if (!inputFile.exists()) {
				result.setState(NumberPlaceState.NoExistFile);
				return result;
			} else if (!inputFile.canRead()) {
				result.setState(NumberPlaceState.CantReadFile);
				return result;
			} else if (!inputFile.isFile()) {
				result.setState(NumberPlaceState.NoFile);
				return result;
			}
			
			String fileName = inputFile.getName();
			if (fileName.length() <= 0) {
				result.setState(NumberPlaceState.NoFile);
				return result;
			}
			
			int index = fileName.lastIndexOf(".");
			if (index <= 0) {
				result.setState(NumberPlaceState.NoExtension);
				return result;
			}
			
			if (!"txt".equals(fileName.substring(index+1))) {
				result.setState(NumberPlaceState.OtherExtension);
				return result;
			}
			
			br = new BufferedReader(new FileReader(inputFile));
			boolean startData = false;
			String line = null;
			int lineNumber = 0;
			while ((line = br.readLine()) != null) {
				
				if (lineNumber >= 9) {
					result.setState(NumberPlaceState.IllegalLineNumber);
					return result;
				}
				
				if (line.length() <= 0) {
					if (startData) {
						if (br != null) {
							try {br.close();} catch (IOException e1) {}
						}
						result.setState(NumberPlaceState.BetweenBlankLine);
						return result;
					}
				} else {
					
					if (startData) {
						++lineNumber;
					} else {
						startData = true;
					}
					
					if (!checkSupportWidth(line.length())) {
						if (line.length() < 9) {
							result.setState(NumberPlaceState.IllegalLineLength);
							return result;
						}
						result.setState(NumberPlaceState.UnsupportWidth);
						return result;
					} 
					
					if (!checkNumber(line)) {
						if (br != null) {
							try {br.close();} catch (IOException e1) {}
						}
						result.setState(NumberPlaceState.IllegalNumberFormat);
						return result;
					}
					
					for (int i = 0; i < line.length(); i++) {
						int number = Integer.parseInt(""+line.charAt(i));
						numberTable.setNumber(lineNumber, i, number);
					}
					
					
				}
			}
			
			// Žw’ès”ˆÈ‰º
			if (lineNumber < 9) {
				result.setState(NumberPlaceState.IllegalLineNumber);
				return result;
			}
			
			br.close();
			return result;
			
		} catch (IOException e) {
			e.printStackTrace();
			if (br != null) {
				try {br.close();} catch (IOException e1) {}
			}
			result.setState(NumberPlaceState.IOException);
			return result;
		}
	}
	
	private boolean checkNumber(String line) {
		if (!line.matches("^[0-9]+$")) {
			return false;
		}
		return true;
	}
	
}
