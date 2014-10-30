package user;

import java.util.*;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.*;

import mailer.RegAuthenticator;



public class User {
	
	public String primaryemail = "fekari.mustapha@gmail.com";
	public String secondaryemail = "fekari.mustapha.reggaemai@gmail.com";
	public String secondarypassword = "Mous13Mous";
	public String smtp_auth="true";
	public String smtp_host="smtp.gmail.com";
	public String smtp_port="587";
	public String smtp_starttls="true";
	public String store_protocol="imaps";
	public String imap_host="imap.googlemail.com";
	
	public Properties getProperties() {
		Properties props=System.getProperties();
        props.setProperty("mail.smtp.auth", smtp_auth);
        props.setProperty("mail.smtp.host", smtp_host);
        props.setProperty("mail.smtp.port", smtp_port);
        props.setProperty("mail.smtp.starttls.enable", smtp_starttls);
        props.setProperty("mail.store.protocol", store_protocol);
		return props;
	}
	
	public Session getSession() {
	    javax.mail.Authenticator authenticator = new RegAuthenticator(this);
	    return Session.getDefaultInstance(getProperties(),authenticator);
	}
	

}
