package condition;

import java.text.ParseException;

import mailer.RegMsg;

public class Condition {
	private String regcond;
	private String regperiode;

	
	public String getRegperiode() {
		return regperiode;
	}

	public String getRegcond() {
		return regcond;
	}

	public Condition(String regcond) {
		this.regcond = regcond;
	}
	
	public Condition(String regcond, String periode) {
		this.regcond = regcond;
		this.regperiode = periode;
	}

	
	public boolean eval(RegMsg rmsg) throws ParseException {
		return false;
	}
	
	public int evalBis(RegMsg rmsg) throws ParseException {
		return 0;
	}
	
	
}
