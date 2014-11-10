package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.text.ParseException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.*;

import java.util.Scanner;

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
<<<<<<< HEAD

	/* Nacer : Pour la gestion multi compte nous avons fait le choix d'utiliser 
	 * un tab d'utilisateur rempli grâce à un fichier texte 
	 * (veuillez l'appeler information_user.txt) organiser de la maniere suiv :
	 * ligne 1 : Nombre utilisateur
	 * ligne 2 : primarymail utilisateur1
	 * ligne 3 : secondarymail utilisateur1
	 * ligne 4 : passwd utilisateur1
	 * TODO ligne 5 : protocole de récupération des mail choisi IMPORTANT ! ("imaps"/"pop3")
	 * ... Ainsi dessuite 4 ligne par utilisateur
	 */
	private static int nb_User = 0;
	private static User []uTab;

	// Fonction contruction du tableau à partir du fichier texte
	public static void CreateArrayUser() throws NumberFormatException, IOException {
		/* Déclaration et initialisation des variable; nécessaire un buffer 
		 * reader pour la lecture du fichier TEXTE et des String pour 
		 * récupéré les informations nécessaire.
		 */
		BufferedReader br = null;
		String primaryemail
		,secondaryemail
		,secondarypassword
		,protocol = null;

		/* TODO AVANT DE LANCER LE PROGRAMME VEUILLER METTRE A JOUR LE CHEMIN
		 * DU FICHIER TEXTE information_user.txt
		 */
		br = new BufferedReader(new FileReader("/home/jon-snow/workspace_new/ReggaeMail/information_user.txt"));

		//récupération des donnée fichier texte
		try {
			nb_User = Integer.parseInt(br.readLine());
			uTab = new User[nb_User];

			if( nb_User == 1 )
				return;

			for (int i = 0; i < nb_User; i++) {
				System.out.println("Setting up the n° " + (i+1) + " user... ");

				primaryemail = br.readLine();
				secondaryemail = br.readLine();
				secondarypassword = br.readLine();
				protocol = br.readLine();

				uTab[i] = new User(primaryemail, secondaryemail, secondarypassword);
				uTab[i].store_protocol = protocol;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}//CreateArrayUser

=======
>>>>>>> 0ca46e5e51c180290407eec6c3e1d0fb9601faaa
	/**
	 * Fonction principale du contrôleur : récupération et traitement des
	 * mails de programmation.
	 * @param args					N'est pas utilisé.
	 * @throws MessagingException	Exceptions levées par les classes de messagerie de l'API javax.mail.
	 * @throws IOException			Exceptions produites par l'échec ou l'interruption d'opérations d'I/O de java.io.
	 * @throws ParseException		Exceptions levées par des erreurs de parsing (java.text) du mail.
	 */
	public static void main(String[] args) throws MessagingException, IOException, ParseException {
<<<<<<< HEAD
		/* Refonte du controller fonction pricipale désormais défini dans 
		 * la methode traitement_user les fonctions initialement defini dans
		 * le main sont inchangé juste une gestion au niveau des protocoles
		 * et des different utilisateur Ajouté! 
		 */
		
		/* Création tableau grâce au fichier si erreur traitement de l'user par
		 * défaut
		 */
		try {
			CreateArrayUser();

			for (int i = 0; i < uTab.length; i++) {
				switch (uTab[i].store_protocol) {
				case "pop3":
					uTab[i].Traitement_User_Pop3();
					break;
				case "imaps":
					uTab[i].Traitement_User_Imap();
					break;
				default:
					System.out.println("ERROR build session");
					break;				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			u.Traitement_User_Imap();
		}
		
		
=======
		Scanner sc = new Scanner (System.in);
		String key = "";
		
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
		
		/* -------------------------------------------------------------------------- */
		/* Lecture entrée clavier pour récupération de mot clé
		/* ((------------------------------------------------------------------------ */
		
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

		// Traitement de chaque mail
		System.out.println("Going through each mail... ");
        for (Message msg : Reader.messages) 
        {
        	RegMsg rmsg = new RegMsg((MimeMessage) msg);
	        	        	
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
	  		System.out.println("condition?: "+ c.eval());
	  		
	  		// Traitement par mot clé
        	if (key.equals(regkey)) {
				System.out.println ("---------------------------- Preparing and sending the mail (MODE : KeyMode) --------------------------------------");
				rmsg.prepare ();
				System.out.println ("Subject: " + rmsg.getSubject());
				System.out.println ("From: " + rmsg.getFrom()[0]);
				System.out.println ("To: " + rmsg.getAllRecipients()[0]);
				/* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
		         * la méthode dans documentation de la classe RegMsg) */
				System.out.println ("Date: " + msg.getReceivedDate());
				System.out.println ("Body:" + rmsg.getBody());
				Sender.sendRegMsg (rmsg);
				System.out.println ("Done sending.\n");
			}
        	
        	// Traitement Normal
        	switch (c.evalBis()){
        		
        		case 1:
        			System.out.println("-------------------------- Preparing and sending the mail (MODE : Periode1) -------------------------------------");
        			rmsg.prepare();
        			System.out.println("Subject: " + rmsg.getSubject()); 
        			System.out.println("From: " + rmsg.getFrom()[0]);
        			System.out.println("To: "+rmsg.getAllRecipients()[0]); 
        			/* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
        	         * la méthode dans documentation de la classe RegMsg) */
        			System.out.println("Date: "+ msg.getReceivedDate());
        			System.out.println("Body:" + rmsg.getBody());
        			Sender.sendRegMsg(rmsg);
        			System.out.println("Done sending.\n");
        			break;
        		
        		case 2:
        			System.out.println("---------------------------- Preparing and sending the mail (MODE : Periode2) ------------------------------------");
        			rmsg.prepare();
        			System.out.println("Subject: " + rmsg.getSubject());
        			System.out.println("From: " + rmsg.getFrom()[0]);
        			System.out.println("To: "+ rmsg.getAllRecipients()[0]);
        			/* Ne pas utiliser RegMsg.getReceivedDate() (c.f documentation de
        	         * la méthode dans documentation de la classe RegMsg) */
        			System.out.println("Date: "+ msg.getReceivedDate());
        			System.out.println("Body:" + rmsg.getBody());
        			Sender.sendRegMsg(rmsg);
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
>>>>>>> 0ca46e5e51c180290407eec6c3e1d0fb9601faaa
	}
}
