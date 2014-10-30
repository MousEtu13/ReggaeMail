package condition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WhenCondition extends Condition {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");//ajout de l'heure et minute d'envoi (format 24h)  HH:mm
	
	public WhenCondition(String regcond, String Periode) throws ParseException {
		super(regcond , Periode);
		
	}

	public int eval() {
		if ((sdf.format(new Date()).compareTo(regcond)==0) && (Periode == null))// pas de periode
			return 1;
		else if((sdf.format(new Date()).compareTo(regcond)==0) && (Periode != null))
			return 2;
		else
			return 0;
		
	}

}
