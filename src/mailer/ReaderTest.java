package mailer;

import user.*;
import static org.junit.Assert.*;

import javax.mail.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class ReaderTest {
	User u = null;

	@Before
	public void setUp()  {
		u = new User();
	}

	@After
	public void tearDown()  {
		u = null;
	}

	@Test
	public void testReadFolder() throws MessagingException {
          Reader.readFolder(u);

          //Print them out
          try {
	          System.out.println("No of Messages : " + Reader.folder.getMessageCount());
	          System.out.println("Retrieved : " + Reader.messages.length);
	          System.out.println("No of Unread Messages : " + Reader.folder.getUnreadMessageCount());
	
	          for (Message msg : Reader.messages) 
	          {
	
	            System.out.println("*****************************************************************************");
	            //System.out.println(msg.getMessageNumber());
	            //System.out.println(folder.getUID(Reader.messages)
	            System.out.println("Subject: " + msg.getSubject());
	            System.out.println("From: " + msg.getFrom()[0]);
	            System.out.println("To: "+msg.getAllRecipients()[0]);
	            System.out.println("Date: "+msg.getReceivedDate());
	            System.out.println("Body:" + msg.getContent());
	            //System.out.println(msg.getContentType());
	            //System.out.println("Size: "+msg.getSize());
	            //System.out.println(msg.getFlags());
	          }
	          Reader.closeFolder();
          } 
          catch (Exception e) {
  			e.printStackTrace();
  			assertTrue(false);
  		  }
          assertTrue(true);
	}
	
	
	

}
