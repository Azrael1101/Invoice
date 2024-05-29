package tw.com.tm.erp.hbm.bean;

public class Sequence  implements java.io.Serializable {

	private static final long serialVersionUID = -4312303996453625355L;
	private String name;
	private Long current_value;
	private Long increment;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCurrent_value() {
		return current_value;
	}
	public void setCurrent_value(Long current_value) {
		this.current_value = current_value;
	}
	public Long getIncrement() {
		return increment;
	}
	public void setIncrement(Long increment) {
		this.increment = increment;
	}
	
}
