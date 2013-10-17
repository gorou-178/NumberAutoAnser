package number;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NumberPlaceDataParserTest {
	
	@BeforeClass
	public static void beforeTestClass() {
		System.out.println("beforeClass");
	}
	
	@Before
	public void beforeTest() {
		System.out.println("beforeTest");
	}
	
	@After
	public void afterTest() {
		System.out.println("afterTest");
	}
	
	@AfterClass
	public static void afterTestClass() {
		System.out.println("afterClass");
	}
	
	@Test
	public void testParse_noExistFile() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./test99").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.NoExistFile, is(result.state()));
	}
	
	@Ignore
	public void testParse_noReadFile() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./noRead.txt").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.CantReadFile, is(result.state()));
	}
	
	@Test
	public void testParse_noFile() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./src").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.NoFile, is(result.state()));
	}
	
	@Test
	public void testParse_noExtensionFile() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./noExtension").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.NoExtension, is(result.state()));
	}
	
	@Test
	public void testParse_noTextFile() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./noText.jpg").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.OtherExtension, is(result.state()));
	}
	
	@Test
	public void testParse_emptyFile() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./emptyFile.txt").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.IllegalLineNumber, is(result.state()));
	}
	
	@Test
	public void testParse_lineLengthOverError() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./lineLength_over.txt").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.UnsupportWidth, is(result.state()));
	}
	
	@Test
	public void testParse_lineLengthUnderError() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./lineLength_under.txt").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.IllegalLineLength, is(result.state()));
	}
	
	@Test
	public void testParse_lineNumberOverError() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./lineNumber_over.txt").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.IllegalLineNumber, is(result.state()));
	}
	
	@Test
	public void testParse_lineNumberUnderError() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./lineNumber_under.txt").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.IllegalLineNumber, is(result.state()));
	}
	
	@Test
	public void testParse_lineFormatError_zennkaku() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./lineFormat_zenkaku.txt").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.IllegalNumberFormat, is(result.state()));
	}
	
	@Test
	public void testParse_lineFormatError_alpha() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./lineFormat_alpha.txt").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.IllegalNumberFormat, is(result.state()));
	}
	
	@Test
	public void testParse_blankLine_between() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./lineFormat_blank.txt").parse();
		assertThat(true, is(result.isError()));
		assertThat(NumberPlaceState.BetweenBlankLine, is(result.state()));
	}
	
	@Test
	public void testParse_ok() {
		NumberPlaceResult result = new NumberPlaceDataParser(9, "./test_ok.txt").parse();
		assertThat(result.isError(), is(false));
		assertThat(result.state(), nullValue());
		assertThat(result.output(), notNullValue());
		
		NumberPlaceTable zeroTable = new NumberPlaceTable(9);
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				zeroTable.setNumber(i, j, 0);
			}
		}
		
		for (NumberPlaceTable nuberTable : result.output()) {
			assertThat(nuberTable.hashCode(), is(zeroTable.hashCode()));
		}
	}
	
}
