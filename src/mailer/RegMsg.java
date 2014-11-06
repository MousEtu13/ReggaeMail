package mailer;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    /** Expression régulière décrivant le format de la Periode */
    private static String PERIODE = "Periode:(.*)";
	/** Expression régulière décrivant le format de la règle regKey */
	private static String REGEXKEYWORD = "regKey:(.*)";


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
	 * Obtenir le contenu de la règle regKey.
	 * @return						la chaîne de caractères contenue dans regKey.
	 * @throws IOException			Exceptions produites par l'échec ou l'interruption d'opérations d'I/O de java.io.
	 * @throws MessagingException	Exceptions levées par les classes de messagerie de l'API javax.mail.
	 */
	public String getRegKey () throws IOException, MessagingException {
		String regkeyword = null;
		String body = null;
	
		// Construction du pattern et du matcher pour reconnaître REGEXKEYWORD
		body = getContent ().toString ();
		Pattern p = Pattern.compile (REGEXKEYWORD);
		Matcher m = p.matcher (body);

		// Recherche et récupération du contenu de la règle
		if (m.find ()) { regkeyword = m.group (1);}
		
		return regkeyword;
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
	        //Suppression de la règle Periode du corps du message
	        body = body.replaceFirst(PERIODE,"");
			
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
	
	
	/*methode qui recupere la periode*/
	public String getPeriode() throws IOException, MessagingException {
		String periode = null;
		String body = null;

		body = getContent().toString(); 
	    Pattern p = Pattern.compile(PERIODE);
	    Matcher m = p.matcher(body);
	    if (m.find()) {periode = m.group(1);}
		
		return periode;
	}
	
/**Modification du message a envoyer a ReggeaMail*/
	
	public void Modif_Message() throws IOException, MessagingException, ParseException {
		String periode = this.getPeriode();//recuperation de la periode
		String date= this.getRegWhen();// recup de la date
		String Date_suivante;
		String body = null;
		String FormatDate="dd/MM/yy";
		String FormatDateAndHeure = "dd/MM/yy HH:mm";
		
		/**Traitement Date & heure Pour boite ReggeaMail**/
		
		SimpleDateFormat sdf = new SimpleDateFormat(FormatDate);
		Date d=sdf.parse(date);
		    
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		calendar.setTime(d);
		calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(periode));
		Date_suivante = sdf.format(calendar.getTime());
		
		/**Ajout du Contenu Ds le message a envoyé**/
		body = getContent().toString();
		
		Pattern p = Pattern.compile(date);
        Matcher m = p.matcher(body);
        body = m.replaceFirst(Date_suivante);
        
        setContent(body,"text/plain");
		    
	}
	
	
	

	

}
