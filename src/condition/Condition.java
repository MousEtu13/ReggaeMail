package condition;

public class Condition {
	String regcond;
	String periode;

	
	public Condition(String regcond) {
		this.regcond = regcond;
	}
	
	public Condition(String regcond, String periode) {
		this.regcond = regcond;
		this.periode = periode;
	}

	
	public boolean eval() {
		return false;
	}
	
	public int evalBis() {
		return 0;
	};
	
	
}

