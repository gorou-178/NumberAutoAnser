package number.strategy;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsAnything.anything;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import number.NumberPlaceDataParser;
import number.NumberPlaceTable;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SimpleAnalyzerTest {

	@Test
	public void testCalcBlockRowClumn() {
		
		NumberPlaceTable table = new NumberPlaceDataParser(9, "sample.txt").parse().output().get(0);
		SimpleAnalyzer analyzer = new SimpleAnalyzer(table);
		
		assertThat(analyzer.calcStartRow(0), is(0));
		assertThat(analyzer.calcStartRow(1), is(0));
		assertThat(analyzer.calcStartRow(2), is(0));
		assertThat(analyzer.calcStartRow(3), is(3));
		assertThat(analyzer.calcStartRow(4), is(3));
		assertThat(analyzer.calcStartRow(5), is(3));
		assertThat(analyzer.calcStartRow(6), is(6));
		assertThat(analyzer.calcStartRow(7), is(6));
		assertThat(analyzer.calcStartRow(8), is(6));
		
		assertThat(analyzer.calcEndRow(0), is(2));
		assertThat(analyzer.calcEndRow(1), is(2));
		assertThat(analyzer.calcEndRow(2), is(2));
		assertThat(analyzer.calcEndRow(3), is(5));
		assertThat(analyzer.calcEndRow(4), is(5));
		assertThat(analyzer.calcEndRow(5), is(5));
		assertThat(analyzer.calcEndRow(6), is(8));
		assertThat(analyzer.calcEndRow(7), is(8));
		assertThat(analyzer.calcEndRow(8), is(8));
		
		assertThat(analyzer.calcStartClumn(0), is(0));
		assertThat(analyzer.calcStartClumn(1), is(0));
		assertThat(analyzer.calcStartClumn(2), is(0));
		assertThat(analyzer.calcStartClumn(3), is(3));
		assertThat(analyzer.calcStartClumn(4), is(3));
		assertThat(analyzer.calcStartClumn(5), is(3));
		assertThat(analyzer.calcStartClumn(6), is(6));
		assertThat(analyzer.calcStartClumn(7), is(6));
		assertThat(analyzer.calcStartClumn(8), is(6));
		
		assertThat(analyzer.calcEndClumn(0), is(2));
		assertThat(analyzer.calcEndClumn(1), is(2));
		assertThat(analyzer.calcEndClumn(2), is(2));
		assertThat(analyzer.calcEndClumn(3), is(5));
		assertThat(analyzer.calcEndClumn(4), is(5));
		assertThat(analyzer.calcEndClumn(5), is(5));
		assertThat(analyzer.calcEndClumn(6), is(8));
		assertThat(analyzer.calcEndClumn(7), is(8));
		assertThat(analyzer.calcEndClumn(8), is(8));
	}
	
}
