package mailer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

import java.io.IOException;
import java.util.regex.Pattern; 
import java.util.regex.Matcher;

public class RegMsg extends MimeMessage {

	private static String REGEXTO = "regTo:(.*)";
	private static String REGEXWHEN = "regWhen:(.*)";
	private static String REGEXKEYWORD = "regKey:(.*)";

	public RegMsg(MimeMessage m) throws MessagingException {
		super(m);
	}


	public String getRegWhen() throws IOException, MessagingException{
		String regwhen = null;
		String body = null;

		body = getContent ().toString ();
		Pattern p = Pattern.compile (REGEXWHEN);
		Matcher m = p.matcher (body);
		
		if (m.find ()) { regwhen = m.group (1); }

		return regwhen;
	}
	
	public String getRegKey () throws IOException, MessagingException {
		String regkeyword = null;
		String body = null;
		
		body = getContent ().toString ();
		Pattern p = Pattern.compile (REGEXKEYWORD);
		Matcher m = p.matcher (body);

		if (m.find ()) { regkeyword = m.group (1);}
		
		return regkeyword;
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
			setContent(body, "text/html");
			setFrom(getAllRecipients()[0]);
			setRecipients(Message.RecipientType.TO,InternetAddress.parse(m.group(1)));

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}



}
