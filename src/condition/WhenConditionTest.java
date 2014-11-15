package condition;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.internet.MimeMessage;

import mailer.RegMsg;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WhenConditionTest {


	@Test
	public void testEvalBis() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
		RegMsg rmsg =null;		
		
		//1er cas 
		try {
			Condition c= new WhenCondition("24/08/1978 19:19", null);
			assertTrue(c.evalBis(rmsg)==0);
			c= new WhenCondition(sdf.format(new Date()),null);
			assertTrue(c.evalBis(rmsg)==1);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}
		
		
		//2 eme cas
		try {
			Condition c= new WhenCondition("24/08/1978 19:19", "23");
			assertTrue(c.evalBis(rmsg)==0);
			c= new WhenCondition(sdf.format(new Date()),"23");
			assertTrue(c.evalBis(rmsg)==2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}

		

	}
	
	@Test
	public void testEval() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
		RegMsg rmsg = null;
		
		try {
			Condition c= new WhenCondition("24/08/1978");
			assertFalse(c.eval(rmsg));
			c= new WhenCondition(sdf.format(new Date()));
			assertTrue(c.eval(rmsg));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			assertTrue(false);
		}
	}
	

}
