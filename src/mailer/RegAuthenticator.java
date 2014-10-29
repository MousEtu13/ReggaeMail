package mailer;

import user.*;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class RegAuthenticator extends Authenticator {
	
	User u;
	
	public RegAuthenticator(User u) {this.u=u;}
	
	protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(u.secondaryemail, u.secondarypassword);
    }

}
