package tw.com.tm.erp.hbm.bean;

public class SoBonus implements java.io.Serializable {
	private static final long serialVersionUID = -4846738533702553573L;
	private String employeeCode;
	private String goalCode;
	private String year;
	private String month;
	private Double amountA;
	private Double commissionA;
	private Double amountB;
	private Double commissionB;
	private Double amountC;
	private Double commissionC;
	private Double amountD;
	private Double commissionD;
	private Double amountE;
	private Double commissionE;
	private Double amountF;
	private Double commissionF;
	private Double amountG;
	private Double commissionG;
	private Double amountX;
	private Double commissionX;
	private Double totalAmount;
	private Double totalCommission;
	private Double achevement;
	private Double goalAmount;	//個人業績(非所屬群組的業績不算)
	private Double employeeGoal;	//個人業績目標	
	
	public boolean equals(Object obj) {
		if((this == obj)) return true;
		if((obj == null)) return false;
		if(!(obj instanceof SoBonus)) return false;
		SoBonus castOther = (SoBonus) obj;
		return (this.getEmployeeCode() != null && castOther.getEmployeeCode() != null && this.getEmployeeCode().trim().equals(castOther.getEmployeeCode().trim())) && 
			(this.getGoalCode() != null && castOther.getGoalCode() != null && this.getGoalCode().trim().equals(castOther.getGoalCode().trim())) && 
			(this.getYear() != null && castOther.getYear() != null && this.getYear().trim().equals(castOther.getYear().trim())) && 
			(this.getMonth() != null && castOther.getMonth() != null && this.getMonth().trim().equals(castOther.getMonth().trim()));		
	}
	
	// getter...
	public String getEmployeeCode() {
		return employeeCode;
	}
	
	public String getGoalCode() {
		return goalCode;
	}
	
	public String getYear() {
		return year;
	}
	
	public String getMonth() {
		return month;
	}
	
	public Double getAmountA() {
		return amountA;
	}
	public Double getCommissionA() {
		return commissionA;
	}
	
	public Double getAmountB() {
		return amountB;
	}
	
	public Double getCommissionB() {
		return commissionB;
	}
	
	public Double getAmountC() {
		return amountC;
	}
	
	public Double getCommissionC() {
		return commissionC;
	}
	
	public Double getAmountD() {
		return amountD;
	}
	
	public Double getCommissionD() {
		return commissionD;
	}
	
	public Double getAmountE() {
		return amountE;
	}
	
	public Double getCommissionE() {
		return commissionE;
	}
	
	public Double getAmountF() {
		return amountF;
	}
	
	public Double getCommissionF() {
		return commissionF;
	}
	
	public Double getAmountG() {
		return amountG;
	}
	
	public Double getCommissionG() {
		return commissionG;
	}
	
	public Double getAmountX() {
		return amountX;
	}
	
	public Double getCommissionX() {
		return commissionX;
	}
	
	public Double getTotalAmount() {
		return totalAmount;
	}
	
	public Double getTotalCommission() {
		return totalCommission;
	}

	public Double getAchevement() {
		return achevement;
	}
	
	public Double getGoalAmount() {
	    return goalAmount;
	}

	public Double getEmployeeGoal() {
	    return employeeGoal;
	}

	// setter...
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public void setGoalCode(String goalCode) {
		this.goalCode = goalCode;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public void setAmountA(Double amountA) {
		this.amountA = amountA;
	}

	public void setCommissionA(Double commissionA) {
		this.commissionA = commissionA;
	}

	public void setAmountB(Double amountB) {
		this.amountB = amountB;
	}

	public void setCommissionB(Double commissionB) {
		this.commissionB = commissionB;
	}

	public void setAmountC(Double amountC) {
		this.amountC = amountC;
	}

	public void setCommissionC(Double commissionC) {
		this.commissionC = commissionC;
	}

	public void setAmountD(Double amountD) {
		this.amountD = amountD;
	}

	public void setCommissionD(Double commissionD) {
		this.commissionD = commissionD;
	}

	public void setAmountE(Double amountE) {
		this.amountE = amountE;
	}

	public void setCommissionE(Double commissionE) {
		this.commissionE = commissionE;
	}

	public void setAmountF(Double amountF) {
		this.amountF = amountF;
	}

	public void setCommissionF(Double commissionF) {
		this.commissionF = commissionF;
	}

	public void setAmountG(Double amountG) {
		this.amountG = amountG;
	}

	public void setCommissionG(Double commissionG) {
		this.commissionG = commissionG;
	}

	public void setAmountX(Double amountX) {
		this.amountX = amountX;
	}

	public void setCommissionX(Double commissionX) {
		this.commissionX = commissionX;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public void setTotalCommission(Double totalCommission) {
		this.totalCommission = totalCommission;
	}

	public void setAchevement(Double achevement) {
		this.achevement = achevement;
	}

	public void setGoalAmount(Double goalAmount) {
	    this.goalAmount = goalAmount;
	}

	public void setEmployeeGoal(Double employeeGoal) {
	    this.employeeGoal = employeeGoal;
	}
}
