package mailer;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.IOException;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
 * @see	<a href="http://fr.wikipedia.org/wiki/Multipurpose_Internet_Mail_Extensions>Article Wikipédia sur le format MIME</a>
 * @see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/internet/MimeMessage.html">javax.mail.internet.MimeMessage</a>
*/
public class RegMsg extends MimeMessage {
	/** Expression regulière décrivant le format de la règle regWhen */
    private static String REGEXTO = "regTo:(.*)";
    /** Expression régulière décrivant le format de la règle regTo */
    private static String REGEXWHEN = "regWhen:(.*)";
    /** Expression régulière décrivant le format de la règle regKey */
	private static String REGEXKEYWORD = "regKey:(.*)";
	/** Expression régulière décrivant le format de la règle Periode */
	private static String REGEXPERIODE = "regPeriode:(.*)";

	public static boolean isMultipartContentType(String contentType) {
		return contentType.startsWith("multipart/");
	}
	
	public static boolean isTextContentType(String contentType) {
		return contentType.startsWith("text/");
	}
	
	/**
     * Construit un objet RegMsg à partir d'un objet MimeMessage (javax.mail.internet.MimeMessage).
     * @param m						Le message MIME source.
     * @throws MessagingException	Exception lévée par le constructeur de la classe MimeMessage.
     */
	public RegMsg(MimeMessage m) throws MessagingException {
		super(m);
	}
	
	/**
	 * Retourne la date de réception du message (résultat de super.getReceivedDate()). 
	 * @deprecated	L'implémentation de MimeMessage.getReceivedDate() retourne automatiquement null.
	 * @see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/internet/MimeMessage.html#getReceivedDate()">javax.mail.internet.MimeMessage.getReceivedDate()</a>
	 */
	public Date getReceivedDate() throws MessagingException {
		return super.getReceivedDate();
	}
	
	/**
	 * Retourne le contenu de type MIME du message.
	 * @deprecated À ne pas utiliser pour obtenir le corps du mail : le contenu retourné peut ne pas être du texte.
	 * @see <a href="http://fr.wikipedia.org/wiki/Multipurpose_Internet_Mail_Extensions">Article Wikipédia sur le format MIME</a>
	 * @see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/internet/MimeMessage.html#getContent()>javax.mail.internet.MimeMessage.getContent()</a>
	 */
	public Object getContent() throws MessagingException, IOException {
		return super.getContent();
	}
	
	/**
	 * Modifie le contenu de type MIME du message.
	 * @deprecated À ne pas utiliser pour modifier le corps du mail : le contenu peut ne pas être du texte.
	 * @see <a href="http://fr.wikipedia.org/wiki/Multipurpose_Internet_Mail_Extensions">Article Wikipédia sur le format MIME</a>
	 * @see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/internet/MimeMessage.html#setContent(java.lang.Object,%20java.lang.String)>javax.mail.internet.MimeMessage.setContent(Object o, String type)</a>
	 */
	public void setContent(Object o, String type) throws MessagingException {
		super.setContent(o, type);
	}
	
	/**
	 * Obtenir l'identifiant de type du contenu du message. Cet identifiant peut être :
	 * - "text/plain" pour un contenu en texte brut
	 * - "multipart/mixed" pour le type MIME multipart avec texte brut et pièce jointe
	 * - "multipart/alternative" pour le type MIME multipart avec contenu alternatif
	 * 
	 * Si le contenu n'a pas été identifié, la méthode retourne null.
	 * @see <a href="http://fr.wikipedia.org/wiki/Multipurpose_Internet_Mail_Extensions#Content-Type">Article Wikipédia sur le format MIME</a>
	 * @throws MessagingException	Exception levée par MimeMessage.getContentType().
	 * @return 						L'identifiant de type du contenu sous forme de chaîne de caractères, null si le type est inconnu.
	 */
	public String getContentTypeID() throws MessagingException {
		String contentType = getContentType();
		
		if(contentType.startsWith("text/plain;"))
        	return "text/plain";
		else if(contentType.startsWith("text/html;"))
        	return "text/html";
        else if(contentType.startsWith("multipart/mixed;"))
        	return "multipart/mixed";
        else if(contentType.startsWith("multipart/alternative;"))
        	return "multipart/alternative";
        else
        	return null;
	}
	
	/**
	 * Test si le message est de type texte brut .
	 * @return true si c'est un message en texte brut, false sinon.
	 * @throws MessagingException erreur venant de javax.mail.internet.MimeMessage.getContent().
	 */
	public boolean isTextPlainMsg() throws MessagingException {
		return getContentType().startsWith("text/plain");
	}
	
	/**
	 * Test si le message est de type multipart.
	 * @return true si c'est un message multipart, false sinon.
	 * @throws MessagingException erreur venant de javax.mail.internet.MimeMessage.getContent().
	 */
	public boolean isMultipartMsg() throws MessagingException {
		return getContentType().startsWith("multipart/");
	}
	
	/**
	 * Obtenir le texte du premier contenu en texte brut rencontré dans un contenu multipart.
	 * @param content	le contenu multipart à explorer.
	 * @return le texte trouvé, null en cas d'échec.
	 * @throws MessagingException	erreur venant de javax.mail.internet.MimeMessage.getContent().
	 * @throws IOException			exception typiquement levée par le DataHandler (c.f documentation de javax.activation.DataHandler).
	 */
	private String getFirstTextPlainContent(MimeMultipart content) throws MessagingException, IOException {
		int i = 0;
		int nbParts = content.getCount();
		String contentType;
		String text = null;
		BodyPart part;
		
		while((text == null) && i < (nbParts)) {
			part = content.getBodyPart(i);
			contentType = part.getContentType();
			i++;
			
			if(contentType.startsWith("text/plain"))
				text = part.getContent().toString();
			else if(contentType.startsWith("multipart/"))
				text = getFirstTextPlainContent((MimeMultipart) part.getContent());
		}
		
		return text;
	}
	
	/**
	 * Modifier le premier contenu en texte brut rencontré dans un contenu multipart.
	 * @param content	le contenu multipart à modifier.
	 * @param text		le texte à écrire dans le contenu.
	 * @param type		type de texte (doit être "text/plain" ou "text/htmp").
	 * @return true si la modification à réussi, false sinon.
	 * @throws MessagingException	erreur venant de javax.mail.internet.MimeMessage.getContent().
	 * @throws IOException			exception typiquement levée par le DataHandler (c.f documentation de javax.activation.DataHandler).
	 */
	private boolean setFirstTextPlainContent(MimeMultipart content, String text, String type) throws MessagingException, IOException {
		int i = 0;
		int nbParts = content.getCount();
		boolean find = false;
		String contentType;
		BodyPart part;
		
		while(!find && (i < nbParts)) {
			part = content.getBodyPart(i);
			contentType = part.getContentType();
			i++;
			
			if(contentType.startsWith("text/plain")) {
				part.setContent(text, type);
				find = true;
			}
			else if(contentType.startsWith("multipart/"))
				find = setFirstTextPlainContent((MimeMultipart) part.getContent(), text, type);
		}
		
		return find;
	}
	
	/**
	 * Obtenir le texte contenu dans le corps du message.
	 * @return le texte contenu dans le corps du message.
	 * @throws MessagingException	erreur venant de javax.mail.internet.MimeMessage.getContent().
	 * @throws IOException			exception typiquement levée par le DataHandler (c.f documentation de javax.activation.DataHandler).
	 */
	public String getBody() throws MessagingException, IOException {
		if(isMultipartMsg())
			return getFirstTextPlainContent((MimeMultipart) getContent());
		else
			return getContent().toString();
	}
	
	/**
	 * Modifie le texte contenu dans le corps du message.
	 * @param text	le texte à mettre dans le corps du message.
	 * @param type	type de texte (doit être "text/plain" ou "text/htmp").
	 * @return true si la modification à réussi, false sinon.
	 * @throws MessagingException	erreur venant de javax.mail.internet.MimeMessage.getContent().
	 * @throws IOException			exception typiquement levée par le DataHandler (c.f documentation de javax.activation.DataHandler).
	 * @see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/internet/MimeMessage.html#setContent(java.lang.Object,%20java.lang.String)">javax.mail.internet.MimeMessage.setContent()</a>
	 */
	public boolean setBody(String text, String type) throws MessagingException, IOException {
		if(isMultipartMsg())
			return setFirstTextPlainContent((MimeMultipart) getContent(), text, type);
		else {
			setContent(text, type);
			return true;
		}
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
		body = getBody();
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
		body = getBody();
		Pattern p = Pattern.compile (REGEXKEYWORD);
		Matcher m = p.matcher (body);

		// Recherche et récupération du contenu de la règle
		if (m.find ()) { regkeyword = m.group (1);}
		
		return regkeyword;
	}
	
	/**
	 * Méthode qui recupère la période.
	 * @return la période.
	 */
	public String getPeriode() throws IOException, MessagingException {
		String periode = null;
		String body = null;

		body = getBody(); 
	    Pattern p = Pattern.compile(REGEXPERIODE);
	    Matcher m = p.matcher(body);
	    if (m.find()) {periode = m.group(1);}
		
		return periode;
	}
	
	/**
	 * Modification du message a envoyer a ReggeaMail
	 */
	public void modifMessage() throws IOException, MessagingException, ParseException {
		String periode = this.getPeriode();//recuperation de la periode
		String date= this.getRegWhen();// recup de la date
		String Date_suivante;
		String body = null;
		@SuppressWarnings("unused")
		String FormatDate="dd/MM/yy";
		String FormatDateAndHeure = "dd/MM/yy HH:mm";
		
		// Traitement Date & heure Pour boite ReggeaMail
		SimpleDateFormat sdf = new SimpleDateFormat(FormatDateAndHeure);
		Date d=sdf.parse(date);
		    
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		calendar.setTime(d);
		calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(periode));
		Date_suivante = sdf.format(calendar.getTime());
		
		// Ajout du Contenu Ds le message a envoyé
		body = getBody();		
		Pattern p = Pattern.compile(date);
        Matcher m = p.matcher(body);
        body = m.replaceFirst(Date_suivante);
        
        setBody(body,"text/plain");
	}
	
	/**
	 * Prépare le message pour un envoi automatique (élimination des règles du corps du message
	 * et modification des champs From et To).
	 */
	public void prepare(){
		try{
			// Obtention du corps du message
			String body = null;
	        body = getBody();
	        
	        // Supression de la règle regWhen du corps du message
	        Pattern p = Pattern.compile(REGEXWHEN);
	        Matcher m = p.matcher(body);
	        body = m.replaceFirst("");
	        // Suppression de la règle regTo du corps du message
	        p = Pattern.compile(REGEXTO);
	        m = p.matcher(body);
	        body = m.replaceFirst("");
	        //Suppression de la règle Periode du corps du message
	        body = body.replaceFirst(REGEXPERIODE, "");
			
	        // Remplacement du corps du message
	        setBody(body, "text/html");
	        // Modification des champs From et To
			setFrom(getAllRecipients()[0]);
			setRecipients(Message.RecipientType.TO,InternetAddress.parse(m.group(1)));
		}
		catch (Exception e)
		{
	    	e.printStackTrace();
		}
	}
	
	
	
	

	  public String getText()
	  {

	    Multipart myMulti;
	    BodyPart myBody;

	    try
	    {

	      if(this.isMimeType("text/plain"))
	      {
	    	this.setFlag(Flags.Flag.SEEN,true);
	        return (String)this.getContent();
	      }
	      else
	      {
	        myMulti = (Multipart)(this.getContent());
	        myBody = myMulti.getBodyPart(0);
	        this.setFlag(Flags.Flag.SEEN,true);
	        return (String)myBody.getContent();
	      }

	    }
	    catch(MessagingException e)
	    {
	       return null;
	    }

	    catch(IOException io)
	    {
	       return null;
	    }
	  }
}
