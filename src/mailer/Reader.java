package mailer;

import user.*;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.Flags.Flag;
import javax.mail.internet.*;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPMessage;

public class Reader {
	
	public static IMAPFolder folder = null;
	public static Store store = null;
	public static Message[] messages = null;  
	
	public static void readFolder(User u) throws MessagingException{
        Session session = u.getSession();

            //Get the messages
            store = session.getStore(u.store_protocol);
            store.connect(u.imap_host,u.secondaryemail, u.secondarypassword);
            folder = (IMAPFolder) store.getFolder("inbox");
            if (!folder.isOpen()) folder.open(Folder.READ_WRITE);
            messages = Reader.folder.getMessages();
	}
	
	public static void closeFolder(){
        try {
        if (Reader.folder != null && Reader.folder.isOpen()) { Reader.folder.close(true); }
        if (Reader.store != null) { Reader.store.close(); }
        } 
        catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
