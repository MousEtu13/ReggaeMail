package user;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.MimeMessage;

import condition.Condition;
import condition.WhenCondition;

import mailer.Reader;
import mailer.RegAuthenticator;
import mailer.RegMsg;
import mailer.Sender;

/**
 * La classe User représente l'utilisateur par défaut. Elle rassemble les différents paramètres
 * utilisateur nécéssaires à la configuration et l'utilisation de ReggaeMail, ainsi que les
 * différentes méthodes qui permettent de manipuler ces paramètres.
 */
public class User {

	/** Adresse mail primaire, depuis lequel l'utilisateur envoie les mails de programmation. */
	public String primaryemail="fekari.mustapha@gmail.com";
	/** Adresse mail secondaire, vers laquelle l'utilisateur envoie les mails de programmation. */
	public String secondaryemail="fekari.mustapha.reggaemai@gmail.com";
	/** Mot de passe de l'adresse mail secondaire. */
	public String secondarypassword="Mous13Mous";
	/** Variable d'activation de l'authentification SMTP. */

	public String smtp_auth="true";
	/** Nom de domaine de l'hôte SMTP. */
	public String smtp_host="smtp.gmail.com";
	/** Numéro de port SMTP (sans authentification : 25, avec authentification : 587, ssl : 465). */
	public String smtp_port="587";
	/** Variable d'activation du protocole STARTTLS (communication texte brut encrytpée). */
	public String smtp_starttls="true";
	/** Nom du protocole de stockage */
	public String store_protocol="imaps";
	/** Nom de domaine de l'hôte IMAP. */
	public String imap_host="imap.googlemail.com";

	// Contructeur permettant d'instancier un user avec ses caractéristiques principale
	public User (String primaryemail, String secondaryemail, String secondarypassword)
	{
		this.primaryemail = new String(primaryemail);
		this.secondaryemail = new String(secondaryemail);
		this.secondarypassword = new String(secondarypassword);
	}

	public User() {	}

	/**
	 * Obtenir les propriétés de l'utilisateur.
	 * @return	le résultat de la méthode System.getProperties(), en ajoutant les propriétés pour SMTP et le protocole de stockage.
	 * @see 	<a href="http://docs.oracle.com/javase/7/docs/api/java/lang/System.html">java.lang.System.getProperties()</a>
	 * @see		<a href="http://docs.oracle.com/javase/7/docs/api/java/util/Properties.html">java.util.Properties.setProperty(key, value)</a>
	 */
	public Properties getProperties() {
		// Extraction des propriétés du système
		Properties props = System.getProperties();
		// Ajout des propriétés SMTP
		props.setProperty("mail.smtp.auth", smtp_auth);
		props.setProperty("mail.smtp.host", smtp_host);
		props.setProperty("mail.smtp.port", smtp_port);
		props.setProperty("mail.smtp.starttls.enable", smtp_starttls);
		// Ajout de la propriété pour le protocole de stockage
		props.setProperty("mail.store.protocol", store_protocol);

		return props;
	}

	public Properties getPropertiesPop3(){
		Properties props=System.getProperties();
		props.setProperty("mail.smtp.auth", smtp_auth);
		props.setProperty("mail.smtp.host", smtp_host);
		props.setProperty("mail.smtp.port", smtp_port);
		props.setProperty("mail.pop3.ssl.enable","true");
		props.setProperty("mail.store.protocol", store_protocol);
		return props;
	}

	/**
	 * Obtenir la session par défaut de l'utilisateur (crée une nouvelle session par défaut
	 * si elle n'a pas encore été initialisée).
	 * @return	la session par défaut de l'utilisateur.
	 * @see mailer.RegAuthenticator
	 * @see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/Session.html">javax.mail.Session.getDefaultInstance(props,authenticator)</a>
	 */
	public Session getSession() {
		javax.mail.Authenticator authenticator = new RegAuthenticator(this);

		return Session.getDefaultInstance(getProperties(),authenticator);
	}

	public Session getSession_pop3() {
		javax.mail.Authenticator authenticator = new RegAuthenticator(this);
		return Session.getDefaultInstance(getPropertiesPop3(),authenticator);
	}

	public void Traitement_User_Imap() throws MessagingException, IOException, ParseException {
		
		System.out.println("Welcome to the ReggaeMail experience.\n");
		System.out.println(this.secondaryemail+" "+this.store_protocol);
		
		// Mise en place l'utilisateur par défaut
		System.out.println("Setting up the default user... ");
		System.out.println("Done.\n");
		
		// Vérification de sa boîte aux lettres secondaire
		System.out.println("Checking out his secondary mailbox...");
		
		System.out.println("Connecting to server : "+"imap.googlemail.fr"+"\n" +
				"Port : "+this.smtp_port+"\n" +
				"Protocol : "+this.store_protocol);
		
		Reader.readFolder(this);
        System.out.println("  No of Messages : " + Reader.folder.getMessageCount());
        System.out.println("  Retrieved : " + Reader.messages.length);
        System.out.println("  No of Unread Messages : " + Reader.folder.getUnreadMessageCount());
		System.out.println("Done.\n");
		
		// Traitement de chaque mail
		System.out.println("Going through each mail... ");
        for (Message msg : Reader.messages) 
        {
        	RegMsg rmsg = new RegMsg((MimeMessage) msg);
        	RegMsg accuse_envoi = new RegMsg((MimeMessage) msg);
        	
	        System.out.println("*****************************************************************************");
	        //System.out.println(msg.getMessageNumber());
	        //System.out.println(folder.getUID(Reader.messages));
	        
	        System.out.println("Subject: " + rmsg.getSubject());
	        System.out.println("From: " + rmsg.getFrom()[0].toString());
	        System.out.println("To: "+ rmsg.getAllRecipients()[0].toString());
	        /* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
	         * la méthode dans documentation de la classe RegMsg) */
	        System.out.println("Date: "+ msg.getReceivedDate().toString());
	        System.out.println("Body:\n" + rmsg.getBody());
	        
	        //System.out.println(msg.getContentType());
	        //System.out.println("Size: "+msg.getSize());
	        //System.out.println(msg.getFlags());
	         
	        // Évaluation de la condition d'envoi RegWhen
	        System.out.println("-------------------- Evaluating the Condition -------------------------------");
	  		String regwhen = rmsg.getRegWhen(); // Date et heure
        	String periode = rmsg.getPeriode();
	  		Condition c;
	  		
	  		if (regwhen==null && periode == null)
	  			c = new Condition("");
	  		else {
	  			c = new WhenCondition(regwhen, periode);
	  	  		System.out.println("regWhen: "+ regwhen);
	  	  		System.out.println("periode: "+ rmsg.getPeriode());
	  		};
	  		
	  		
        	// Traitement Normal
        	switch (c.evalBis(rmsg)){
        		
        		case 1:
        			System.out.println("-------------------------- Preparing and sending the mail (MODE : Periode1) -------------------------------------");
        			rmsg.prepare();
        			accuse_envoi.accuse_envoi();
        			System.out.println("Subject: " + rmsg.getSubject()); 
        			System.out.println("From: " + rmsg.getFrom()[0]);
        			System.out.println("To: "+rmsg.getAllRecipients()[0]); 
        			/* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
        	         * la méthode dans documentation de la classe RegMsg) */
        			System.out.println("Date: "+ msg.getReceivedDate());
        			System.out.println("Body:" + rmsg.getBody());
        			Sender.sendRegMsg(rmsg);
        			Sender.sendRegMsg(accuse_envoi);
        			System.out.println("Done sending.\n");
        			break;
        		
        		case 2:
        			System.out.println("---------------------------- Preparing and sending the mail (MODE : Periode2) ------------------------------------");
        			rmsg.prepare();
        			accuse_envoi.accuse_envoi();
        			System.out.println("Subject: " + rmsg.getSubject());
        			System.out.println("From: " + rmsg.getFrom()[0]);
        			System.out.println("To: "+ rmsg.getAllRecipients()[0]);
        			/* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
        	         * la méthode dans documentation de la classe RegMsg) */
        			System.out.println("Date: "+ msg.getReceivedDate());
        			System.out.println("Body:" + rmsg.getBody());
        			Sender.sendRegMsg(rmsg);
        			Sender.sendRegMsg(accuse_envoi);
        			System.out.println("Done sending.\n");
        			
        			System.out.println("-------------------- Preparing and sending the mail To ReggeaMail (MODE : Periode2) -------------------------------");
        			
        			// Traitement du message
        			RegMsg tmp = new RegMsg((MimeMessage) msg);
        			
        			tmp.modifMessage();
        			System.out.println("Subject: " + tmp.getSubject());
        			tmp.setFrom(rmsg.getAllRecipients()[0]);
        			tmp.setSender(rmsg.getFrom()[0]);
        			System.out.println("From: " + tmp.getFrom()[0]);
        			System.out.println("To: "+ tmp.getAllRecipients()[0]);
        			/* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
        	         * la méthode dans documentation de la classe RegMsg) */
        			System.out.println("Date: "+ msg.getReceivedDate());
        			System.out.println("Body:" + tmp.getBody());
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

	}// Traitement_User_Imap
	
	public void Traitement_User_Pop3() throws MessagingException, IOException, ParseException {
		
		System.out.println("Welcome to the ReggaeMail experience.\n");
		System.out.println(this.secondaryemail+" "+this.store_protocol);
		
		// Mise en place l'utilisateur par défaut
		System.out.println("Setting up the default user... ");
		System.out.println("Done.\n");
		
		// Vérification de sa boîte aux lettres secondaire
		System.out.println("Checking out his secondary mailbox...");
		
		System.out.println("Connecting to server : "+"pop3.googlemail.fr"+"\n" +
				"Port : "+this.smtp_port+"\n" +
				"Protocol : "+this.store_protocol);
		
		Reader.readFolder_pop3(this);
        System.out.println("  No of Messages : " + Reader.folder_pop3.getMessageCount());
        System.out.println("  Retrieved : " + Reader.messages.length);
        System.out.println("  No of Unread Messages : " + Reader.folder_pop3.getUnreadMessageCount());
		System.out.println("Done.\n");
		
		// Traitement de chaque mail
		System.out.println("Going through each mail... ");
        for (Message msg : Reader.messages) 
        {
        	RegMsg rmsg = new RegMsg((MimeMessage) msg);
        	RegMsg accuse_envoi = new RegMsg((MimeMessage) msg);
	        	        	
	        System.out.println("*****************************************************************************");
	        //System.out.println(msg.getMessageNumber());
	        //System.out.println(folder.getUID(Reader.messages));
	        
	        System.out.println("Subject: " + rmsg.getSubject());
	        System.out.println("From: " + rmsg.getFrom()[0].toString());
	        System.out.println("To: "+ rmsg.getAllRecipients()[0].toString());
	        /* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
	         * la méthode dans documentation de la classe RegMsg) */
	        //System.out.println("Date: "+ msg.getReceivedDate().toString());
	        System.out.println("Body:\n" + rmsg.getBody());
	        
	        //System.out.println(msg.getContentType());
	        //System.out.println("Size: "+msg.getSize());
	        //System.out.println(msg.getFlags());
	         
	        // Évaluation de la condition d'envoi RegWhen
	        System.out.println("-------------------- Evaluating the Condition -------------------------------");
	  		String regwhen = rmsg.getRegWhen(); // Date et heure
	  		String regkey = rmsg.getRegKey();
        	String periode = rmsg.getPeriode();
	  		Condition c;
	  		
	  		if (regwhen==null && periode == null)
	  			c = new Condition("");
	  		else {
	  			c = new WhenCondition(regwhen, periode);
	  	  		System.out.println("regWhen: "+ regwhen);
	  	  		System.out.println("periode: "+ rmsg.getPeriode());
	  		};

        	
        	// Traitement Normal
        	switch (c.evalBis(rmsg)){
        		
        		case 1:
        			System.out.println("-------------------------- Preparing and sending the mail (MODE : Periode1) -------------------------------------");
        			rmsg.prepare();
        			accuse_envoi.accuse_envoi();
        			System.out.println("Subject: " + rmsg.getSubject()); 
        			System.out.println("From: " + rmsg.getFrom()[0]);
        			System.out.println("To: "+rmsg.getAllRecipients()[0]); 
        			/* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
        	         * la méthode dans documentation de la classe RegMsg) */
        			System.out.println("Date: "+ msg.getReceivedDate());
        			System.out.println("Body:" + rmsg.getBody());
        			Sender.sendRegMsg(rmsg);
        			Sender.sendRegMsg(accuse_envoi);
        			System.out.println("Done sending.\n");
        			break;
        		
        		case 2:
        			System.out.println("---------------------------- Preparing and sending the mail (MODE : Periode2) ------------------------------------");
        			rmsg.prepare();
        			accuse_envoi.accuse_envoi();
        			System.out.println("Subject: " + rmsg.getSubject());
        			System.out.println("From: " + rmsg.getFrom()[0]);
        			System.out.println("To: "+ rmsg.getAllRecipients()[0]);
        			/* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
        	         * la méthode dans documentation de la classe RegMsg) */
        			System.out.println("Date: "+ msg.getReceivedDate());
        			System.out.println("Body:" + rmsg.getBody());
        			Sender.sendRegMsg(rmsg);
        			Sender.sendRegMsg(accuse_envoi);
        			System.out.println("Done sending.\n");
        			
        			System.out.println("-------------------- Preparing and sending the mail To ReggeaMail (MODE : Periode2) -------------------------------");
        			
        			// Traitement du message
        			RegMsg tmp = new RegMsg((MimeMessage) msg);
        			
        			tmp.modifMessage();
        			System.out.println("Subject: " + tmp.getSubject());
        			tmp.setFrom(rmsg.getAllRecipients()[0]);
        			tmp.setSender(rmsg.getFrom()[0]);
        			System.out.println("From: " + tmp.getFrom()[0]);
        			System.out.println("To: "+ tmp.getAllRecipients()[0]);
        			/* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
        	         * la méthode dans documentation de la classe RegMsg) */
        			System.out.println("Date: "+ msg.getReceivedDate());
        			System.out.println("Body:" + tmp.getBody());
        			Sender.sendRegMsg(tmp);
        			System.out.println("Done sending ReggeaMail.\n");        			
        			break;
        		
        		default:
        			break;
        	}
        	System.out.println("Done with that mail.\n");
        }
        System.out.println("Done with all that mail.\n");
        Reader.closeFolder_pop3();
        System.out.println("Done till next cron.\n");

	}// Traitement_User_Pop3
	
	public User getcurrent(){
		return this;
	}
	

}
