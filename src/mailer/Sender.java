package mailer;

import javax.mail.*;
import javax.mail.internet.MimeMessage;

public class Sender {
	public static void sendRegMsg(Message m) throws MessagingException{
			//Session object   
		
			Transport.send(m);
			System.out.println("Done");
	}
}