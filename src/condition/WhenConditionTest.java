package condition;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WhenConditionTest {


	@Test
	public void testEval() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
    	
		try {
			Condition c= new WhenCondition("24/08/1978");
			assertFalse(c.eval());
			c= new WhenCondition(sdf.format(new Date()));
			assertTrue(c.eval());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}
	}

}
