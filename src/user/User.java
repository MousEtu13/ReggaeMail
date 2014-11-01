package user;

import java.util.*;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.*;

import mailer.RegAuthenticator;

/**
 * La classe User représente l'utilisateur par défaut. Elle rassemble les différents paramètres
 * utilisateur nécéssaires à la configuration et l'utilisation de ReggaeMail, ainsi que les
 * différentes méthodes qui permettent de manipuler ces paramètres.
 */
public class User {
	/** Adresse mail primaire, depuis lequel l'utilisateur envoie les mails de programmation. */
	public String primaryemail="USER@gmail.com";
	/** Adresse mail secondaire, vers laquelle l'utilisateur envoie les mails de programmation. */
	public String secondaryemail="USER@gmail.com";
	/** Mot de passe de l'adresse mail secondaire. */
	public String secondarypassword="USERPASS";
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
}
