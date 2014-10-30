
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import user.*;
import mailer.*;
import condition.*;

public class Controller {
	static User u;
	
	

	
	public static void main(String[] args) throws MessagingException, IOException, ParseException {
		System.out.println("Welcome to the ReggaeMail experience.\n");
		System.out.println("Setting up the default user... ");
		u= new User();
		System.out.println("Done.\n");
		
		System.out.println("Checking out his secondary mailbox...");
		Reader.readFolder(u);
        System.out.println("  No of Messages : " + Reader.folder.getMessageCount());
        System.out.println("  Retrieved : " + Reader.messages.length);
        System.out.println("  No of Unread Messages : " + Reader.folder.getUnreadMessageCount());
		System.out.println("Done.\n");
		
		System.out.println("Going through each mail... ");
		
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
        	System.out.println("-------------------- Evaluating the Condition -------------------------------");
        	RegMsg rmsg=new RegMsg((MimeMessage) msg);
        	String regwhen = rmsg.getRegWhen();//date et heure 
        	String Periode = rmsg.getPeriode();
	
        	Condition c;
        	if (regwhen==null && Periode == null) {c = new Condition("", "");}//manque d'autre cas
        	else {
        		c= new WhenCondition(regwhen,Periode);
        		System.out.println("regWhen: "+regwhen);
        		System.out.println("Periode: "+rmsg.getPeriode());
        	};
        	System.out.println("condition?: cas->"+c.eval());
        	switch (c.eval()){
        		
        		case 1:
        			System.out.println("-------------------- Preparing and sending the mail  -------------------------------");
        			rmsg.prepare();
        			System.out.println("Subject: " + rmsg.getSubject());//sujet du message 
        			System.out.println("From: " + rmsg.getFrom()[0]);// de la part de qui
        			System.out.println("To: "+rmsg.getAllRecipients()[0]);// a qui envoyer le msg 
        			System.out.println("Date: "+rmsg.getReceivedDate());
        			System.out.println("Body:" + rmsg.getContent());//contenu du msg
        			Sender.sendRegMsg(rmsg);
        			System.out.println("Done sending.\n");
        			break;
        		
        		case 2:
        			System.out.println("-------------------- Preparing and sending the mail  -------------------------------");
        			rmsg.prepare();
        			System.out.println("Subject: " + rmsg.getSubject());//sujet du message 
        			System.out.println("From: " + rmsg.getFrom()[0]);// de la part de qui
        			System.out.println("To: "+rmsg.getAllRecipients()[0]);// a qui envoyer le msg 
        			System.out.println("Date: "+rmsg.getReceivedDate());
        			System.out.println("Body:" + rmsg.getContent());//contenu du msg
        			Sender.sendRegMsg(rmsg);
        			System.out.println("Done sending.\n");
        			
        			System.out.println("-------------------- Preparing and sending the mail To ReggeaMail -------------------------------");
        			
        			/**Traitement du message**/
        			RegMsg tmp=new RegMsg((MimeMessage) msg);
        			tmp.Modif_Message();
        			System.out.println("Subject: " + tmp.getSubject());//sujet du message 
        			tmp.setFrom(rmsg.getAllRecipients()[0]);//
        			tmp.setSender(rmsg.getFrom()[0]);//
        			System.out.println("From: " + tmp.getFrom()[0]);
        			System.out.println("To: "+tmp.getAllRecipients()[0]);
        			System.out.println("Date: "+tmp.getReceivedDate());
        			System.out.println("Body:" + tmp.getContent());
        			/****/
        			
        			Sender.sendRegMsg(tmp);
        			System.out.println("Done sending ReggeaMail.\n");
        			
        			
        			break;
        		
        		default:
        			break;
        	}
        	System.out.println("Done with that mail.\n");
        }
        System.out.println("Done with all that mail.\n");
        Reader.closeFolder();
		
        System.out.println("Done till next cron.\n");
		

	}

}
