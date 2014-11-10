package mailer;

import user.*;

import javax.mail.*;

import com.sun.mail.imap.IMAPFolder;
<<<<<<< HEAD
import com.sun.mail.imap.IMAPMessage;
import com.sun.mail.pop3.POP3Folder;
=======
>>>>>>> 0ca46e5e51c180290407eec6c3e1d0fb9601faaa

/**
 * La classe Reader implémente un lecteur des dossiers IMAP d'un utilisateur ReggaeMail (adresse mail secondaire).
 * @see <a href="https://javamail.java.net/nonav/docs/api/com/sun/mail/imap/IMAPFolder.html">com.sun.mail.imap.IMAPFolder</a>
 * @see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/Store.html">javax.mail.Store</a>
 * @see <a href="https://javamail.java.net/nonav/docs/api/javax/mail/Message.html">javax.mail.Message</a>
 */
public class Reader {
	/** Dossier IMAP à lire (boîte de réception) */
	public static IMAPFolder folder = null;
	/** Dossier POP3 à lire (boîte de réception) */
	public static POP3Folder folder_pop3 = null;
	/** Dossier de stockage du dossier IMAP */
	public static Store store = null;
	/** Tableau des messages mails */
	public static Message[] messages = null;
	
	/**
	 * Lit le contenu de la boîte de réception du mail secondaire d'un l'utilisateur via protocol IMAP
	 * ReggaeMail donné en paramètre de la méthode.
	 * @param u						L'utilisateur ReggaeMail dont le dossier doit être lu.
	 * @throws MessagingException	Exceptions levées par les classes de messagerie de l'API javax.mail.
	 */
	public static void readFolder(User u) throws MessagingException{
        Session session = u.getSession();

        //Get the messages
        store = session.getStore(u.store_protocol);
        store.connect(u.imap_host,u.secondaryemail, u.secondarypassword);
        folder = (IMAPFolder) store.getFolder("inbox");
        if (!folder.isOpen()) folder.open(Folder.READ_WRITE);
        messages = Reader.folder.getMessages();
	}
	
	/**
	 * Lit le contenu de la boîte de réception du mail secondaire d'un l'utilisateur via protocol POP3
	 * ReggaeMail donné en paramètre de la méthode.
	 * @param u						L'utilisateur ReggaeMail dont le dossier doit être lu.
	 * @throws MessagingException	Exceptions levées par les classes de messagerie de l'API javax.mail.
	 */
	public static void readFolder_pop3(User u) throws MessagingException{
        Session session = u.getSession_pop3();

            //Get the messages
            store = session.getStore("pop3");
            store.connect("pop.gmail.com",u.secondaryemail, u.secondarypassword);
            folder_pop3 = (POP3Folder) store.getFolder("inbox");
            if (!folder_pop3.isOpen()) folder_pop3.open(Folder.READ_WRITE);
            messages = Reader.folder_pop3.getMessages();
	}
	
	/**
	 * Ferme les dossiers ouvert du Reader.
	 */
	public static void closeFolder(){
        try {
        	if (Reader.folder != null && Reader.folder.isOpen()) { Reader.folder.close(true); }
        	if (Reader.store != null) { Reader.store.close(); }
        } 
        catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Ferme les dossiers ouvert du Reader.
	 */
	public static void closeFolder_pop3(){
        try {
        if (Reader.folder_pop3 != null && Reader.folder_pop3.isOpen()) { Reader.folder_pop3.close(true); }
        if (Reader.store != null) { Reader.store.close(); }
        } 
        catch (MessagingException e) {
			e.printStackTrace();
		}
	}


}
