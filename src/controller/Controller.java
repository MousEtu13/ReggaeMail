package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
	public static User []uTab;
	public static int Indice_user =-1 ; 
	
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

		br = new BufferedReader(new FileReader("./information_user.txt"));

		//récupération des donnée fichier texte
		try {
			WhenCondition.interval_cron=br.readLine();
			nb_User = Integer.parseInt(br.readLine());
			uTab = new User[nb_User];


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


	/**
	 * Fonction principale du contrôleur : récupération et traitement des
	 * mails de programmation.
	 * @param args					N'est pas utilisé.
	 * @throws MessagingException	Exceptions levées par les classes de messagerie de l'API javax.mail.
	 * @throws IOException			Exceptions produites par l'échec ou l'interruption d'opérations d'I/O de java.io.
	 * @throws ParseException		Exceptions levées par des erreurs de parsing (java.text) du mail.
	 */
	public static void main(String[] args) throws MessagingException, IOException, ParseException {
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
					Indice_user = i;
					uTab[i].Traitement_User_Pop3();
					break;
				case "imaps":
					Indice_user = i;
					uTab[i].Traitement_User_Imap();
					break;
				default:
					System.out.println("ERROR build session");
					break;				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}