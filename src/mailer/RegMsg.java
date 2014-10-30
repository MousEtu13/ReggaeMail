package mailer;

import javax.mail.Message;
import javax.mail.MessagingException;
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

public class RegMsg extends MimeMessage {
	
    private static String REGEXTO = "regTo:(.*)";
    private static String REGEXWHEN = "regWhen:(.*)";
    private static String PERIODE = "Periode:(.*)";

	public RegMsg(MimeMessage m) throws MessagingException {
		super(m);
	}
	
	
	public String getRegWhen() throws IOException, MessagingException{
		String regwhen = null;
		String body = null;

		body = getContent().toString();
	    Pattern p = Pattern.compile(REGEXWHEN);
	    Matcher m = p.matcher(body);
	    if (m.find()) {regwhen = m.group(1);}
	        
		return regwhen;
	}
	
	
	public void prepare(){
		try{
			String body = null;
	        body = getContent().toString();
	        
	        Pattern p = Pattern.compile(REGEXWHEN);
	        Matcher m = p.matcher(body);
	        body = m.replaceFirst("");
	        
	        p = Pattern.compile(REGEXTO);
	        m = p.matcher(body);
	        body = m.replaceFirst("");
	        
	        body = body.replaceFirst(PERIODE,"");//supprime du buffer le mot cle Periode
	      
	        setContent(body, "text/html");
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

		body = getContent().toString(); //System.out.println("body>>>>>>>>>>>>>"+getContent().toString());
	    Pattern p = Pattern.compile(PERIODE);//System.out.println("pattern <<<<<<<<<<<<<<<<<<<<"+Pattern.compile(REGEXWHEN));
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
		
		/**Traitement Date & heure Pour boite ReggeaMail**/
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
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
