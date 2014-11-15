package mailer;

import java.io.IOException;

import javax.mail.*;
import javax.mail.internet.MimeMessage;

public class Sender {
	public static void sendRegMsg(RegMsg m) throws MessagingException, IOException{			
			//Session object   
		
			Transport.send(m);
			System.out.println("Done");
	}
}