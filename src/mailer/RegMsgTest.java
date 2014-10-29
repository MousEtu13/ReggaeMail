package mailer;

import user.*;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RegMsgTest {
	
	User u = null;

	@Before
	public void setUp() {
		u= new User();
	}

	@After
	public void tearDown()  {
		u= null;
	}

	@Test
	public void testPrepare() {
		String textBody = "regTo:maman.arrighi@gmail.com\nregWhen:tuesday\nThis is a new Text Message.";
		try {      		
		//make a message
		MimeMessage msg = new MimeMessage(u.getSession());
		msg.setFrom(new InternetAddress(u.primaryemail));
		msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse("pablo.arrighi.reggaemail@gmail.com"));
		msg.setSubject("Test Subject");
		msg.setContent(textBody, "text/html");
		
		//spell it out
        System.out.println("*** Original message ***");
        System.out.println("Subject: " + msg.getSubject());
        System.out.println("From: " + msg.getFrom()[0]);
        System.out.println("To: "+msg.getAllRecipients()[0]);
        System.out.println("Body:\n" + msg.getContent());
        
        //make it into a RegMsg and prepare it
		RegMsg rmsg=new RegMsg(msg);
		rmsg.prepare();
		
		//spell it out
        System.out.println("*** Prepared message ***");
        System.out.println("Subject: " + rmsg.getSubject());
        System.out.println("From: " + rmsg.getFrom()[0]);
        System.out.println("To: "+rmsg.getAllRecipients()[0]);
        System.out.println("Body:\n" + rmsg.getContent());

	    } catch (Exception e) {
	        e.printStackTrace();
	        assertTrue(false);
	    }
		assertTrue(true);
	}
	
	@Test
	public void testGetRegWhen() {
		String textBody = "regTo:maman.arrighi@gmail.com\nregWhen:tuesday\nThis is a new Text Message.";
		try {      		
		//make a message
		MimeMessage msg = new MimeMessage(u.getSession());
		msg.setFrom(new InternetAddress(u.primaryemail));
		msg.setRecipients(Message.RecipientType.TO,InternetAddress.parse("pablo.arrighi.reggaemail@gmail.com"));
		msg.setSubject("Test Subject");
		msg.setContent(textBody, "text/html");
		
		//spell it out
        System.out.println("*** Original message ***");
        System.out.println("Subject: " + msg.getSubject());
        System.out.println("From: " + msg.getFrom()[0]);
        System.out.println("To: "+msg.getAllRecipients()[0]);
        System.out.println("Body:\n" + msg.getContent());
        
        //make it into a RegMsg
		RegMsg rmsg=new RegMsg(msg);
        System.out.println("*** When? ***");
        System.out.println(rmsg.getRegWhen());
        System.out.println("*** RegMsg ***");
        System.out.println("Body:\n" + rmsg.getContent());

	    } catch (Exception e) {
	        e.printStackTrace();
	        assertTrue(false);
	    }
		assertTrue(true);
	}

}
