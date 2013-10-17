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
		
	}
	
}
