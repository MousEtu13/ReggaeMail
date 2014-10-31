package condition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WhenCondition extends Condition {
	String FormatDate="dd/MM/yy";
	String FormatDateAndHeure = "dd/MM/yy HH:mm";

	SimpleDateFormat sdf = new SimpleDateFormat(FormatDate);

	
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
