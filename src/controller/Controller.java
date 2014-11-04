package controller;

import java.io.IOException;
import java.text.ParseException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.Scanner;


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
		System.out.println("Setting up the default user... ");
		u= new User();
		System.out.println("Done.\n");
		
		System.out.println("Checking out his secondary mailbox...");
		Reader.readFolder(u);
        System.out.println("  No of Messages : " + Reader.folder.getMessageCount());
        System.out.println("  Retrieved : " + Reader.messages.length);
        System.out.println("  No of Unread Messages : " + Reader.folder.getUnreadMessageCount());
		System.out.println("Done.\n");
		
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
		
		System.out.println("Going through each mail... ");
		
        for (Message msg : Reader.messages) 
        {

        	System.out.println("***********************************************************************************************************************");
        	//System.out.println(msg.getMessageNumber());
        	//System.out.println(folder.getUID(Reader.messages)
        	System.out.println("Subject: " + msg.getSubject());
        	System.out.println("From: " + msg.getFrom()[0]);
        	System.out.println("To: "+msg.getAllRecipients()[0]);
        	System.out.println("Date: "+msg.getReceivedDate());
        	System.out.println("Body:" + msg.getContent() +  "\n");
        	//System.out.println(msg.getContentType());
        	//System.out.println("Size: "+msg.getSize());
        	//System.out.println(msg.getFlags());
        	System.out.println("------------------------------------------- Evaluating the Condition ---------------------------------------------------");
        	RegMsg rmsg=new RegMsg((MimeMessage) msg);
        	String regwhen = rmsg.getRegWhen();//date et heure 
        	String regkey = rmsg.getRegKey();
        	String Periode = rmsg.getPeriode();
	
        	Condition c;
        	if (regwhen==null && Periode == null) {c = new Condition("", "");}
        	else {
        		c= new WhenCondition(regwhen,Periode);
        		System.out.println("regWhen: "+regwhen);
        		System.out.println("Periode: "+rmsg.getPeriode());
        	};
        	System.out.println("condition?: cas->"+c.evalBis());
        	
        	/**Traitement par mot clé**/
        	if (key.equals(regkey)) {
				System.out.println ("---------------------------- Preparing and sending the mail (MODE : KeyMode) --------------------------------------");
				rmsg.prepare ();
				System.out.println ("Subject: " + rmsg.getSubject ());
				System.out.println ("From: " + rmsg.getFrom ()[0]);
				System.out.println ("To: " + rmsg.getAllRecipients ()[0]);
				System.out.println ("Date: " + rmsg.getReceivedDate ());
				System.out.println ("Body:" + rmsg.getContent ());
				Sender.sendRegMsg (rmsg);
				System.out.println ("Done sending.\n");
			}
        	
        	/**Traitement Normal**/
        	switch (c.evalBis()){
        		
        		case 1:
        			System.out.println("-------------------------- Preparing and sending the mail (MODE : Periode1) -------------------------------------");
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
        			System.out.println("---------------------------- Preparing and sending the mail (MODE : Periode2) ------------------------------------");
        			rmsg.prepare();
        			System.out.println("Subject: " + rmsg.getSubject());//sujet du message 
        			System.out.println("From: " + rmsg.getFrom()[0]);// de la part de qui
        			System.out.println("To: "+rmsg.getAllRecipients()[0]);// a qui envoyer le msg 
        			System.out.println("Date: "+rmsg.getReceivedDate());
        			System.out.println("Body:" + rmsg.getContent());//contenu du msg
        			Sender.sendRegMsg(rmsg);
        			System.out.println("Done sending.\n");
        			
        			System.out.println("-------------------- Preparing and sending the mail To ReggeaMail (MODE : Periode2) -------------------------------");
        			
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
