package condition;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;
import javax.swing.text.html.MinimalHTMLWriter;

import mailer.RegMsg;


public class WhenCondition extends Condition {
	
	public static String interval_cron = "00:30";//interval de temps de Cron
	private String FormatHeure="HH:mm";
	private String FormatDateAndHeure = "dd/MM/yy HH:mm";
	
	private static boolean envoi_message_prevention = false;
	
	private SimpleDateFormat sdf = new SimpleDateFormat(FormatDateAndHeure);//creation format date heure
	private SimpleDateFormat shf = new SimpleDateFormat(FormatHeure);//creation format heure
	
	private Date interval = shf.parse(interval_cron);//intervale en heure et minute
	
	public WhenCondition(String regcond) throws ParseException {
		super(regcond);
		
	}
	
	public WhenCondition(String regcond, String periode) throws ParseException {
		super(regcond , periode);
		
	}
	
	
	
	public boolean eval(RegMsg regmsg) throws ParseException {
		
		Date deb =null;//deb = date d'envoi du message
		Date resultat = null; //resultat = diff entre mtn et date d'envoi
		Date fin_interval = null, tmp = null;//utiliser pour l'envoi de message pr prevenir d'envoi prochain
		int minutes = 0;
		int hours = 0 ;
		Date mtn = null;
		mtn=new Date() ;// date actuel
		
		deb = sdf.parse(getRegcond());//on appplique le format a la date deb
		tmp = shf.parse(deb.getHours()+":"+deb.getMinutes());
		
		/*calcul de la fin de l'interval de cron*/
		hours= mtn.getHours()+interval.getHours();
		minutes = mtn.getMinutes() + interval.getMinutes();
		//System.out.println("prochaine : "+hours+":"+minutes);
		fin_interval = shf.parse(hours+":"+minutes);
		
		/* Fin du calcul*/
		
		if(deb.after(mtn)){//message futur
			if(deb.getDay()== mtn.getDay() && deb.getMonth() == mtn.getMonth() && deb.getYear() == mtn.getYear() && tmp.before(fin_interval))//meme jour
				envoi_message_prevention = true;
			return false;//a traité envoi de message pr prevenir de l'envoi prochaine
		}
		else if(deb.getDay()== mtn.getDay() && deb.getMonth() == mtn.getMonth() && deb.getYear() == mtn.getYear())//on verifi que les dates sont les memes
		{
			
			if (mtn.getMinutes() < deb.getMinutes())//on verifie si "l'unité" des heures s'est incrementé
			{
				minutes = deb.getMinutes() - mtn.getMinutes();
				hours = mtn.getHours() - deb.getHours() - 1;//on decrement d'une heure pr voir la bonne diff d'heure
			} 
			
			else
			{
				minutes = mtn.getMinutes() - deb.getMinutes();
				hours = mtn.getHours() - deb.getHours();
			}
			resultat = shf.parse(hours+":"+minutes);//on recupere le resultat
			
			if(resultat.before(interval))//heure < heure de l'intervalle
				return true;
			else
				return false;
		}
		else{
			return false;
		}
		
	}
	
	
	
	
	
	
	public int evalBis(RegMsg regmsg) throws ParseException {
			
		if ((eval(regmsg)) && (getRegperiode() == null)){// pas de periode
			envoi_message_prevention = false;
			return 1;
		}
		else if(eval(regmsg) && (getRegperiode() != null)){
			envoi_message_prevention = false;
			return 2;
		}
		else if(!eval(regmsg) && envoi_message_prevention ){
			System.out.println("je vais envoyé votre message ");
			envoi_message_prevention = false;
			return 0;
		}
		else
			return 0;
		
	}
}
