package controller;

import java.io.IOException;
import java.text.ParseException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import user.*;
import mailer.*;
import condition.*;

/**
 * La classe Controller permet l'éxecution des actions automatisé à effectuer lors du démarrage
 * du système de ReggaeMail. Elle contient la méthode principale main().
 */
public class Controller {
	/** 
	 * L'utilisateur par défaut de ReggaeMail
	 * @see user.User
	 */
	static User u;
	
	/**
	 * Fonction principale du contrôleur : récupération et traitement des
	 * mails de programmation.
	 * @param args					N'est pas utilisé.
	 * @throws MessagingException	Exceptions levées par les classes de messagerie de l'API javax.mail.
	 * @throws IOException			Exceptions produites par l'échec ou l'interruption d'opérations d'I/O de java.io.
	 * @throws ParseException		Exceptions levées par des erreurs de parsing (java.text) du mail.
	 */
	public static void main(String[] args) throws MessagingException, IOException, ParseException {
		System.out.println("Welcome to the ReggaeMail experience.\n");
		
		// Mise en place l'utilisateur par défaut
		System.out.println("Setting up the default user... ");
		u= new User();
		System.out.println("Done.\n");
		
		// Vérification de sa boîte aux lettres secondaire
		System.out.println("Checking out his secondary mailbox...");
		Reader.readFolder(u);
        System.out.println("  No of Messages : " + Reader.folder.getMessageCount());
        System.out.println("  Retrieved : " + Reader.messages.length);
        System.out.println("  No of Unread Messages : " + Reader.folder.getUnreadMessageCount());
		System.out.println("Done.\n");
		
		// Traitement de chaque mail
		System.out.println("Going through each mail... ");
        for (Message msg : Reader.messages) 
        {
          System.out.println("*****************************************************************************");
          //System.out.println(msg.getMessageNumber());
          //System.out.println(folder.getUID(Reader.messages));
          System.out.println("Subject: " + msg.getSubject());
          System.out.println("From: " + msg.getFrom()[0]);
          System.out.println("To: "+msg.getAllRecipients()[0]);
          System.out.println("Date: "+msg.getReceivedDate());
          System.out.println("Body:" + msg.getContent());
          //System.out.println(msg.getContentType());
          //System.out.println("Size: "+msg.getSize());
          //System.out.println(msg.getFlags());
          
          // Évaluation de la condition d'envoi RegWhen
          System.out.println("-------------------- Evaluating the Condition -------------------------------");
  			RegMsg rmsg=new RegMsg((MimeMessage) msg);
  			String regwhen = rmsg.getRegWhen();
  			Condition c;
  			if (regwhen==null) {c = new Condition("");}
  			else {
  				c= new WhenCondition(regwhen);
  	  			System.out.println("regWhen: "+regwhen);
  			};
  			System.out.println("condition?: "+c.eval());
  			
  			// Préparation et envoi du mail si la condition est remplie
  			if (c.eval()) {
  			  System.out.println("-------------------- Preparing and sending the mail  -------------------------------");
  			  rmsg.prepare();
  	          System.out.println("Subject: " + rmsg.getSubject());
  	          System.out.println("From: " + rmsg.getFrom()[0]);
  	          System.out.println("To: "+rmsg.getAllRecipients()[0]);
  	          System.out.println("Date: "+rmsg.getReceivedDate());
  	          System.out.println("Body:" + rmsg.getContent());
  	          Sender.sendRegMsg(rmsg);
  	          System.out.println("Done sending.\n");
  			}
  			System.out.println("Done with that mail.\n");
        }
        System.out.println("Done with all that mail.\n");
        Reader.closeFolder();
		
		System.out.println("Done till next cron.\n");
	}

}
