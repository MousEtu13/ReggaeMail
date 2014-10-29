package mailer;

import user.*;

import static org.junit.Assert.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import user.User;

public class SenderTest {
	
	User u = null;

	@Before
	public void setUp() throws Exception {
		u = new User();
	}

	@After
	public void tearDown() throws Exception {
		u = null;
	}

	@Test
	public void testSendRegMsg() {
		String textBody = "This is a new Text Message.";
		try {      		
		Message message = new MimeMessage(u.getSession());
		message.setFrom(new InternetAddress(u.secondaryemail));
		message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("pablo.arrighi@gmail.com"));
		message.setSubject("New Testing Subject");
		//MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		//mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		message.setContent(textBody, "text/html");
		Sender.sendRegMsg(message);	
	    } catch (MessagingException e) {
	        e.printStackTrace();
	        assertTrue(false);
	    }
		assertTrue(true);
	}

}
