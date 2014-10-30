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
    			
		
		/**1er cas **/
		try {
			Condition c= new WhenCondition("24/08/1978", null);
			assertTrue(c.eval()==0);
			c= new WhenCondition(sdf.format(new Date()),null);
			assertTrue(c.eval()==1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}
		
		
		/**2 eme cas**/
		try {
			Condition c= new WhenCondition("24/08/1978", "23");
			assertTrue(c.eval()==0);
			c= new WhenCondition(sdf.format(new Date()),"23");
			assertTrue(c.eval()==2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}

		

	}

}
