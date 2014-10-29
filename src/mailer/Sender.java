package mailer;



import javax.mail.*;


public class Sender {
	
	
	public static void sendRegMsg(Message m) throws MessagingException{
			//Session object   

			Transport.send(m);
			System.out.println("Done");

		
	}
	

}
