import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import user.*;
import mailer.*;
import condition.*;

public class Controller {
	static User u;

	public static void main (String[] args) throws MessagingException, IOException, ParseException {
		System.out.println ("Welcome to the ReggaeMail experience.\n");

		System.out.println ("Setting up the default user... ");
		u= new User ();
		System.out.println ("Done.\n");

		System.out.println ("Checking out his secondary mailbox...");
		Reader.readFolder (u);
		System.out.println ("No of Messages : " + Reader.folder.getMessageCount());
		System.out.println ("Retrieved : " + Reader.messages.length);
		System.out.println ("No of Unread Messages : " + Reader.folder.getUnreadMessageCount());
		System.out.println ("Done.\n");

/* -------------------------------------------------------------------------- */
/* Lecture entrée clavier pour récupération de mot clé
/* ((------------------------------------------------------------------------ */
		Scanner sc = new Scanner (System.in);
		String key = "";

		do {
			System.out.print ("Souhaitez vous envoyer des mails à partir d'un mot clé ? [o/N] ");
			key = sc.nextLine ();
			System.out.println (key);
		} while (key.equals ("") && key.equals ("O") && key.equals ("O") && key.equals ("n") && key.equals ("N"));

		if (key.equals ("O") || key.equals ("o")) {
			System.out.print ("Entrez le mot clé: ");
			key = sc.nextLine ();
			System.out.println();
		}
		else {
			key = "";
		}
/* -------------------------------------------------------------------------- */
/* -------------------------------------------------------------------------- */

		System.out.println ("Going through each mail... ");

		for (Message msg : Reader.messages) {
			System.out.println("*****************************************************************************");
			//System.out.println (msg.getMessageNumber());
			//System.out.println (folder.getUID(Reader.messages)
			System.out.println ("Subject: " + msg.getSubject ());
			System.out.println ("From: " + msg.getFrom ()[0]);
			System.out.println ("To: " + msg.getAllRecipients ()[0]);
			System.out.println ("Date: " + msg.getReceivedDate ());
			System.out.println ("Body:" + msg.getContent () +  "\n");
			//System.out.println (msg.getContentType());
			//System.out.println ("Size: "+msg.getSize());
			//System.out.println (msg.getFlags());

			System.out.println ("-------------------- Evaluating the Condition -------------------------------");
			RegMsg rmsg = new RegMsg ((MimeMessage) msg);
			String regwhen = rmsg.getRegWhen ();
			String regkey = rmsg.getRegKey();
			Condition c;

			if (regwhen == null) {
				c = new Condition ("");
			}
			else {
				c = new WhenCondition (regwhen);
				System.out.println ("regWhen: " + regwhen);
			};

			System.out.println ("condition?: " + c.eval ());
			System.out.println();
			
			if (c.eval () || key.equals(regkey)) {
				System.out.println ("-------------------- Preparing and sending the mail  -------------------------------");
				rmsg.prepare ();
				System.out.println ("Subject: " + rmsg.getSubject ());
				System.out.println ("From: " + rmsg.getFrom ()[0]);
				System.out.println ("To: " + rmsg.getAllRecipients ()[0]);
				System.out.println ("Date: " + rmsg.getReceivedDate ());
				System.out.println ("Body:" + rmsg.getContent ());
				Sender.sendRegMsg (rmsg);
				System.out.println ("Done sending.\n");
			}

			System.out.println ("Done with that mail.\n");
		}

		System.out.println ("Done with all that mail.\n");

		Reader.closeFolder ();
		System.out.println ("Done till next cron.\n");
	}
}