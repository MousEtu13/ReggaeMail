package condition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WhenCondition extends Condition {
	String FormatDate="dd/MM/yy";
	String FormatDateAndHeure = "dd/MM/yy HH:mm";

	SimpleDateFormat sdf = new SimpleDateFormat(FormatDate);

	
	public WhenCondition(String regcond) throws ParseException {
		super(regcond);
		
	}
	
	public WhenCondition(String regcond, String periode) throws ParseException {
		super(regcond , periode);
		
	}
	
	
	public boolean eval() {
		return (sdf.format(new Date()).compareTo(regcond)==0);
	}
	
	public int evalBis() {
		if ((sdf.format(new Date()).compareTo(regcond)==0) && (periode == null))// pas de periode
			return 1;
		else if((sdf.format(new Date()).compareTo(regcond)==0) && (periode != null))
			return 2;
		else
			return 0;
		
	}
}
