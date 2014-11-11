package mailer;

import javax.mail.BodyPart;
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
 * @see	<a href="http://fr.wikipedia.org/wiki/Multipurpose_Internet_Mail_Extensions">Article Wikipédia sur le format MIME</a>
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
	private static String REGEXPERIODE = "Periode:(.*)";

	/**
	 * Détermine si un type de contenu est un type multipart.
	 * @param contentType le type à tester.
	 * @return true si c'est un type multipart, false sinon.
	 */
	public static boolean isMultipartContentType(String contentType) {
		return contentType.startsWith("multipart/");
	}
	
	/**
	 * Détermine si un type de contenu est un type texte.
	 * @param contentType le type à tester.
	 * @return true si c'est un type texte, false sinon.
	 */
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
	 * @see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/internet/MimeMessage.html#getContent()">javax.mail.internet.MimeMessage.getContent()</a>
	 */
	public Object getContent() throws MessagingException, IOException {
		return super.getContent();
	}
	
	/**
	 * Modifie le contenu de type MIME du message.
	 * @deprecated À ne pas utiliser pour modifier le corps du mail : le contenu peut ne pas être du texte.
	 * @see <a href="http://fr.wikipedia.org/wiki/Multipurpose_Internet_Mail_Extensions">Article Wikipédia sur le format MIME</a>
	 * @see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/internet/MimeMessage.html#setContent(java.lang.Object,%20java.lang.String)">javax.mail.internet.MimeMessage.setContent(Object o, String type)</a>
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
	 * @return L'identifiant de type du contenu sous forme de chaîne de caractères, null si le type est inconnu.
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
	 * Test si le message est de type texte HTML .
	 * @return true si c'est un message en texte HTML, false sinon.
	 * @throws MessagingException erreur venant de javax.mail.internet.MimeMessage.getContent().
	 */
	public boolean isTextHtmlMsg() throws MessagingException {
		return getContentType().startsWith("text/html");
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
		
		// Tant que 
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
				part.setText(text);
				
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
	 * @deprecated À ne plus utiliser : modifie uniquement la version texte brut du corps (pas la version html).
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
	 * Appel récursif de printMultipartContent(). 
	 * @param content
	 * @param niv
	 * @throws IOException
	 * @throws MessagingException
	 */
	private void printMultipartContentRecursive(MimeMultipart content, String niv) throws IOException, MessagingException {
		int i = 0;
		int nbParts = content.getCount();
		String contentType;
		BodyPart part;
		
		for(i = 0; i < nbParts; i++) {
			part = content.getBodyPart(i);
			contentType = part.getContentType();
			
			if(contentType.startsWith("text/")) {
				System.out.println("===================");
				System.out.println(part.getContent());
				System.out.println("===================");
			}
			else if(contentType.startsWith("multipart/")) {
				System.out.println(niv + " MULTIPART");
				printMultipartContentRecursive((MimeMultipart) part.getContent(), niv + "*");
			}
			else {
				System.out.println("===================");
				System.out.println("CONTENU NON-TEXTUEL");
				System.out.println("===================");
			}
		}
	}
	
	/**
	 * Affiche un contenu de type multiart.
	 * @param content le contenu à afficher.
	 * @throws IOException
	 * @throws MessagingException
	 */
	private void printMultipartContent(MimeMultipart content) throws IOException, MessagingException {
			printMultipartContentRecursive(content, "*");
	}
	
	/**
	 * Retourne la première sous-séquence d'une chaîne de caractères, débutant par beginningString
	 * et se terminant par endingString.
	 * @param beginningString	la chaîne de début (si null, recherche du début jusqu'à endingString).
	 * @param endingString		la chaîne de fin (si null, recherche de BeginningString jusqu'à la fin).
	 * @param s					la chaîne dans laquelle faire la recherche.
	 * @return la sous-chaîne débutant par beginningString et se terminant par endingString,
	 * 			null si une telle chaîne n'existe pas.
	 */
	private String substring(String beginningString, String endingString, String s) {
		int begin, end;
		String substring;
		
		// Calcul index première occurrence chaîne de début
		if(beginningString != null)
			begin = s.indexOf(beginningString);
		else
			begin = 0;
		
		// Calcul index première occurrence chaîne de fin
		if(endingString != null)
			end = s.indexOf(endingString, begin);
		else
			end = s.length();
				
		if((begin >= 0) && (end >= 0)) {
			// Récupération et ajustement
			substring = s.substring(begin, end);
			return substring + endingString;
		}
		else
			return null;
	}
	
	/**
	 * Recherche une éventuelle balise HTML fermante dans une règle issue d'un texte HTML.
	 * @param rule			la règle dans laquelle faire la recherche.
	 * @return Le mot clé contenu dans la balise fermante s'il y en a une, null sinon.
	 */
	private String searchClosingTag(String rule) {
		String tag = null;
	    Pattern p = Pattern.compile("</([a-z]*)>");
	    Matcher m = p.matcher(rule);
	    
	    // Recherche et récupération
	    if (m.find()) {tag = m.group(1);}
		
		return tag;
	}
	
	/**
	 * Retourne la balise ouvrante correspondant à une balise fermante qui apparaît
	 * dans le membre droit d'une règle donnée.
	 * @param htmlTag			le mot clé HTML de la balise fermante (sans chevrons, ni '/').
	 * @param ruleLeftMember	le membre gauche de la règle.
	 * @param htmlText			le texte HTML dans lequel faire la recherche.
	 * @return la balise ouvrante trouvée.
	 */
	private String getHtmlOpeningTag(String htmlTag, String ruleLeftMember, String htmlText) {
		int tagIndex, ruleIndex = htmlText.indexOf(ruleLeftMember);
		String textBeforeRule = htmlText.substring(0, ruleIndex);
		StringBuffer tag = new StringBuffer("<" + htmlTag);
		StringBuffer textSB = new StringBuffer(textBeforeRule);
		String openningTag;
		
		// Inversement du texte et du tag
		tag = tag.reverse();		
		textSB = textSB.reverse();
		// Recherche
		tagIndex = textSB.indexOf(tag.toString());		
		// Ré-inversement
		textSB = textSB.reverse();
		tag = tag.reverse();
		
		// Récupération
		openningTag = tag.toString() + htmlText.substring(ruleIndex - tagIndex, ruleIndex);
		
		return openningTag;
	}
	
	/**
	 * Supprimer une règle qui apparait dans un texte HTML.
	 * @param regex		l'expression régulière qui décrit la règle.
	 * @param html		le texte HTML à nettoyer.
	 * @return Le texte obtenu après suppression de la règle.
	 */
	private String htmlRuleRemoval(String regex, String html) {
		String htmlTag = null;
		String ruleLeftMember = substring(null, ":", regex);
		String rule = substring(ruleLeftMember, "<br>", html);
		
		// Si la règle apparaît
		if(rule != null) {
			// Si la règle contient une balise fermante
			htmlTag = searchClosingTag(rule);
			if(htmlTag != null) {
				// Inclusion de la balise ouvrante correspondante
				rule = getHtmlOpeningTag(htmlTag, ruleLeftMember, html) + rule;
			}
			
			// Suppression de la règle
			html = html.replace(rule, "");
		}
		
		return html;
	}
	
	/**
	 * Appel récursif de removeRulesOfBody().
	 * @param regexTab
	 * @throws MessagingException 
	 * @throws IOException 
	 */
	public void removeRulesOfBodyRecursive(MimeMultipart content, String[] regexTab) throws IOException, MessagingException {
		int partIndex, regexIndex;
		int length = regexTab.length;
		int nbParts = content.getCount();
		String contentType, text;
		BodyPart part;
		Pattern p;
		Matcher m;
		
		// Pour chaque partie du contenu
		for(partIndex = 0; partIndex < nbParts; partIndex++) {
			part = content.getBodyPart(partIndex);
			contentType = part.getContentType();
			
			// Si elle contient du texte brut
			if(contentType.startsWith("text/plain")) {
				text = (String) part.getContent();
				
				// Suppression simple des règles
				for(regexIndex = 0; regexIndex < length; regexIndex++) {
					p = Pattern.compile(regexTab[regexIndex]);
			        m = p.matcher(text);
			        text = m.replaceFirst("");
				}
				
				// Modification du texte
				part.setContent(text, "text/plain");
			}
			// Si elle contient du texte HTML
			else if(contentType.startsWith("text/html")) {
				text = (String) part.getContent();
				
				// Suppression spéciale des règles
				for(regexIndex = 0; regexIndex < length; regexIndex++)
					text = htmlRuleRemoval(regexTab[regexIndex], text);
				
				// Modification du texte
				part.setContent(text, "text/html");
			}
			// Si elle contient un contenu multipart
			else if(contentType.startsWith("multipart/"))
				removeRulesOfBodyRecursive((MimeMultipart) part.getContent(), regexTab);
		}
	}
	
	/**
	 * Supprime, du corps du message, la première occurrence de règles décrites par
	 * des expressions régulières.
	 * @param regexTab	un tableau contenant les expressions régulières décrivant les règles.
	 * @see <a href="https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html#sum">Expressions régulières</a>
	 * @return true si la suppression à réussie, false sinon.
	 * @throws MessagingException
	 * @throws IOException
	 */
	public boolean removeRulesOfBody(String[] regexTab) throws MessagingException, IOException {
		String text;
		int regexIndex;
		int length = regexTab.length;
		
		// Si c'est un message multipart
		if(isMultipartMsg()) {
			removeRulesOfBodyRecursive((MimeMultipart) getContent(), regexTab); // Appel récursif
			
			return true;
		}
		// Si c'est un message texte brut
		else if (isTextPlainMsg()) {
			text = (String) getContent();
			// Suppression simple
			for(regexIndex = 0; regexIndex < length; regexIndex++)
				text.replaceFirst(regexTab[regexIndex], "");
			setContent(text, "text/plain");
			
			return true;
		}
		// Si c'est un message en texte HTML
		else if(isTextHtmlMsg()) {
			text = (String) getContent();
			// Suppression Spéciale
			for(regexIndex = 0; regexIndex < length; regexIndex++)
				text = htmlRuleRemoval(regexTab[regexIndex], text);
			setContent(text, "text/html");
			
			return true;
		}
		// Sinon, type de message inconnu
		else
			return false;
	}
	
	/**
	 * Appel récursif de replaceRightMember.
	 * @param content	contenu multipart
	 * @param rule		la règle (avec sa valeur).
	 * @param newRule	la nouvelle règle (avec sa valeur).
	 * @throws IOException
	 * @throws MessagingException
	 */
	private void replaceRightMemberRecursive(MimeMultipart content, String rule, String newRule) throws IOException, MessagingException {
		int partIndex;
		int nbParts = content.getCount();
		String contentType, text;
		BodyPart part;
		
		// Pour chaque partie du contenu
		for(partIndex = 0; partIndex < nbParts; partIndex++) {
			part = content.getBodyPart(partIndex);
			contentType = part.getContentType();
			
			// Si elle contient du texte brut
			if(contentType.startsWith("text/plain")) {
				text = (String) part.getContent();
				if(text.contains(rule)) {
					text = text.replace(rule, newRule);
				
					// Modification du texte
					part.setContent(text, "text/plain");
				}
			}
			// Si elle contient du texte HTML
			else if(contentType.startsWith("text/html")) {
				text = (String) part.getContent();
				if(text.contains(rule)) {
					text = text.replace(rule, newRule);
					
					// Modification du texte
					part.setContent(text, "text/html");
				}
			}
			// Si elle contient un contenu multipart
			else if(contentType.startsWith("multipart/"))
				replaceRightMemberRecursive((MimeMultipart) part.getContent(), rule, newRule);
		}
	}
	
	/**
	 * Remplace le membre droit (la valeur) d'une règle, définie par une expression régulière
	 *  par une autre valeur, dans le corps du message.
	 * @param regex		l'expression régulière décrivant la règle concernée.
	 * @param oldValue	l'ancienne valeur de la règle.
	 * @param newValue	la nouvelle valeur de la règle.
	 * @return true si l'opération est un succés, false sinon.
	 * @throws MessagingException
	 * @throws IOException
	 */
	private boolean replaceRightMember(String regex, String oldValue, String newValue) throws MessagingException, IOException {
		String text;
		String leftMember = substring(null, ":", regex);
		String rule = leftMember + oldValue;
		String newRule = leftMember + newValue;
		
		// Si c'est un message multipart
		if(isMultipartMsg()) {
			replaceRightMemberRecursive((MimeMultipart) getContent(), rule, newRule); // Appel récursif
			
			return true;
		}
		// Si c'est un message texte brut
		else if (isTextPlainMsg()) {
			text = (String) getContent();
			if(text.contains(rule)) {
				text = text.replace(rule, newRule);
				setContent(text, "text/plain");
			}
			
			return true;
		}
		else if(isTextHtmlMsg()) {
			text = (String) getContent();
			if(text.contains(rule)) {
				text = text.replace(rule, newRule);
				setContent(text, "text/html");
			}
				
			return true;
		}
		// Sinon, type de message inconnu
		else
			return false;
	}
	
	/**
	 * Modification du message a envoyer a ReggeaMail (Periode)
	 */
	public void modifMessage() throws IOException, MessagingException, ParseException {
		String periode = this.getPeriode();//recuperation de la periode
		String date= this.getRegWhen();// recup de la date
		String dateSuivante;
		String body = null;
		String FormatDate="dd/MM/yy";
		
		// Traitement Date & heure Pour boite ReggeaMail
		SimpleDateFormat sdf = new SimpleDateFormat(FormatDate);
		Date d=sdf.parse(date);
		    
		GregorianCalendar calendar = new java.util.GregorianCalendar();
		calendar.setTime(d);
		calendar.add(Calendar.DAY_OF_YEAR, Integer.parseInt(periode));
		dateSuivante = sdf.format(calendar.getTime());
		
		// Remplacement de la valeur de RegWhen par la nouvelle date
		replaceRightMember(REGEXWHEN, date, dateSuivante);
	}
	
	/**
	 * Prépare le message pour un envoi automatique (élimination des règles du corps du message
	 * et modification des champs From et To).
	 */
	public void prepare(){
		try{
			// Récupération membre droit règle regTo
	        Pattern p = Pattern.compile(REGEXTO);
	        Matcher m = p.matcher(getBody());
	        m.find();
			
			// Suppression des règles du corps du message
	        String[] tab = {REGEXWHEN, REGEXTO, REGEXPERIODE};
	        removeRulesOfBody(tab);
	        
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
