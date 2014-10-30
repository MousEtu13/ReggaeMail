package mailer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.util.regex.Pattern; 
import java.util.regex.Matcher;

/**
 * La classe RegMsg représente un message ReggaeMail. Elle rassemble les expressions régulières
 * qui décrivent les formats des différentes règles disponible dans ReggaeMail, ainsi que les
 * méthode qui permettent la gestion d'un message au format ReggaeMail.</br>
 * </br>
 * Syntaxe des règles :</br>
 * </br>
 * <ul>
 * 		<li><pre>Paramétrage de la date d'envoi :	regWhen:jj/mm/aa hh:mm</pre>
 * 		<li><pre>Paramétrage du destinataire : 		regTo:dest@mail.com</pre>
 * </ul>
 *@see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/internet/MimeMessage.html">javax.mail.internet.MimeMessage</a>
 */
public class RegMsg extends MimeMessage {
	/** Expression regulière décrivant le format de la règle regWhen */
    private static String REGEXTO = "regTo:(.*)";
    /** Expression régulière décrivant le format de la règle regTo */
    private static String REGEXWHEN = "regWhen:(.*)";

    /**
     * Construit un objet RegMsg à partir d'un objet MimeMessage (javax.mail.internet.MimeMessage).
     * @param m						Le message MIME source.
     * @throws MessagingException	Exception lévée par le constructeur de la classe MimeMessage.
     */
	public RegMsg(MimeMessage m) throws MessagingException {
		super(m);
	}
	
	/**
	 * Obtenir le contenu de la règle regWhen.
	 * @return						la chaîne de caractères contenue dans regWhen.
	 * @throws IOException			Exceptions produites par l'échec ou l'interruption d'opérations d'I/O de java.io.
	 * @throws MessagingException	Exceptions levées par les classes de messagerie de l'API javax.mail.
	 */
	public String getRegWhen() throws IOException, MessagingException{
		String regwhen = null;
		String body = null;
		
		// Construction du pattern et du matcher pour reconnaître REGEXWHEN
		body = getContent().toString();
	    Pattern p = Pattern.compile(REGEXWHEN);
	    Matcher m = p.matcher(body);
	    // Recherche et récupération du contenu de la règle
	    if (m.find()) {regwhen = m.group(1);}
	
		return regwhen;
	}
	
	/**
	 * Prépare le message pour un envoi automatique (élimination des règles du corps du message
	 * et modification des champs From et To).
	 */
	public void prepare(){
		try{
			// Obtention du corps du message
			String body = null;
	        body = getContent().toString();
	        
	        // Supression de la règle regWhen du corps du message
	        Pattern p = Pattern.compile(REGEXWHEN);
	        Matcher m = p.matcher(body);
	        body = m.replaceFirst("");
	        // Suppression de la règle regTo du corps du message
	        p = Pattern.compile(REGEXTO);
	        m = p.matcher(body);
	        body = m.replaceFirst("");
			
	        // Remplacement du corps du message
	        setContent(body, "text/html");
	        // Modification des champs From et To
			setFrom(getAllRecipients()[0]);
			setRecipients(Message.RecipientType.TO,InternetAddress.parse(m.group(1)));
	
		}
		catch (Exception e)
		{
        	e.printStackTrace();
		}
	}
}
