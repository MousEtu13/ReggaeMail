package condition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class WhenCondition extends Condition {

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
	
	public WhenCondition(String regcond) throws ParseException {
		super(regcond);
	}

	public boolean eval() {
		return (sdf.format(new Date()).compareTo(regcond)==0);
	}

}
