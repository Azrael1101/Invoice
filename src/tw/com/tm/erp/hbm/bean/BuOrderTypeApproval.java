package tw.com.tm.erp.hbm.bean;

import java.util.Date;

/**
 * BuOrderTypeApproval entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class BuOrderTypeApproval implements java.io.Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5174704239804529113L;
    
    // Fields
    private String code;
    private BuOrderType buOrderType;
    private String name;
    private Long roleApproveLevel01;
    private Long roleApproveLevel02;
    private Long roleApproveLevel03;
    private Long roleApproveLevel04;
    private Long roleApproveLevel05;
    private Long roleApproveLevel06;
    private Long roleApproveLevel07;
    private Long roleApproveLevel08;
    private Long roleApproveLevel09;
    private Long roleApproveLevel10;
    private Long roleApproveLevel11;
    private Long roleApproveLevel12;
    private Long roleApproveLevel13;
    private Long roleApproveLevel14;
    private Long roleApproveLevel15;
    private Long roleApproveLevel16;
    private Long roleApproveLevel17;
    private Long roleApproveLevel18;
    private Long roleApproveLevel19;
    private Long roleApproveLevel20;
    private String reserve1;
    private String reserve2;
    private String reserve3;
    private String reserve4;
    private String reserve5;
    private String createdBy;
    private Date creationDate;
    private String lastUpdatedBy;
    private Date lastUpdateDate;
    private Long indexNo;

    // Constructors

    /** default constructor */
    public BuOrderTypeApproval() {
    }

    /** minimal constructor */
    public BuOrderTypeApproval(String code) {
	this.code = code;
    }

    /** full constructor */
    public BuOrderTypeApproval(String code, BuOrderType buOrderType,
	    String name, Long roleApproveLevel01, Long roleApproveLevel02,
	    Long roleApproveLevel03, Long roleApproveLevel04,
	    Long roleApproveLevel05, Long roleApproveLevel06,
	    Long roleApproveLevel07, Long roleApproveLevel08,
	    Long roleApproveLevel09, Long roleApproveLevel10,
	    Long roleApproveLevel11, Long roleApproveLevel12,
	    Long roleApproveLevel13, Long roleApproveLevel14,
	    Long roleApproveLevel15, Long roleApproveLevel16,
	    Long roleApproveLevel17, Long roleApproveLevel18,
	    Long roleApproveLevel19, Long roleApproveLevel20, String reserve1,
	    String reserve2, String reserve3, String reserve4, String reserve5,
	    String createdBy, Date creationDate, String lastUpdatedBy,
	    Date lastUpdateDate, Long indexNo) {
	this.code = code;
	this.buOrderType = buOrderType;
	this.name = name;
	this.roleApproveLevel01 = roleApproveLevel01;
	this.roleApproveLevel02 = roleApproveLevel02;
	this.roleApproveLevel03 = roleApproveLevel03;
	this.roleApproveLevel04 = roleApproveLevel04;
	this.roleApproveLevel05 = roleApproveLevel05;
	this.roleApproveLevel06 = roleApproveLevel06;
	this.roleApproveLevel07 = roleApproveLevel07;
	this.roleApproveLevel08 = roleApproveLevel08;
	this.roleApproveLevel09 = roleApproveLevel09;
	this.roleApproveLevel10 = roleApproveLevel10;
	this.roleApproveLevel11 = roleApproveLevel11;
	this.roleApproveLevel12 = roleApproveLevel12;
	this.roleApproveLevel13 = roleApproveLevel13;
	this.roleApproveLevel14 = roleApproveLevel14;
	this.roleApproveLevel15 = roleApproveLevel15;
	this.roleApproveLevel16 = roleApproveLevel16;
	this.roleApproveLevel17 = roleApproveLevel17;
	this.roleApproveLevel18 = roleApproveLevel18;
	this.roleApproveLevel19 = roleApproveLevel19;
	this.roleApproveLevel20 = roleApproveLevel20;
	this.reserve1 = reserve1;
	this.reserve2 = reserve2;
	this.reserve3 = reserve3;
	this.reserve4 = reserve4;
	this.reserve5 = reserve5;
	this.createdBy = createdBy;
	this.creationDate = creationDate;
	this.lastUpdatedBy = lastUpdatedBy;
	this.lastUpdateDate = lastUpdateDate;
	this.indexNo = indexNo;
    }

    // Property accessors

    public String getCode() {
	return this.code;
    }

    public void setCode(String code) {
	this.code = code;
    }

    public BuOrderType getBuOrderType() {
	return this.buOrderType;
    }

    public void setBuOrderType(BuOrderType buOrderType) {
	this.buOrderType = buOrderType;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Long getRoleApproveLevel01() {
	return this.roleApproveLevel01;
    }

    public void setRoleApproveLevel01(Long roleApproveLevel01) {
	this.roleApproveLevel01 = roleApproveLevel01;
    }

    public Long getRoleApproveLevel02() {
	return this.roleApproveLevel02;
    }

    public void setRoleApproveLevel02(Long roleApproveLevel02) {
	this.roleApproveLevel02 = roleApproveLevel02;
    }

    public Long getRoleApproveLevel03() {
	return this.roleApproveLevel03;
    }

    public void setRoleApproveLevel03(Long roleApproveLevel03) {
	this.roleApproveLevel03 = roleApproveLevel03;
    }

    public Long getRoleApproveLevel04() {
	return this.roleApproveLevel04;
    }

    public void setRoleApproveLevel04(Long roleApproveLevel04) {
	this.roleApproveLevel04 = roleApproveLevel04;
    }

    public Long getRoleApproveLevel05() {
	return this.roleApproveLevel05;
    }

    public void setRoleApproveLevel05(Long roleApproveLevel05) {
	this.roleApproveLevel05 = roleApproveLevel05;
    }

    public Long getRoleApproveLevel06() {
	return this.roleApproveLevel06;
    }

    public void setRoleApproveLevel06(Long roleApproveLevel06) {
	this.roleApproveLevel06 = roleApproveLevel06;
    }

    public Long getRoleApproveLevel07() {
	return this.roleApproveLevel07;
    }

    public void setRoleApproveLevel07(Long roleApproveLevel07) {
	this.roleApproveLevel07 = roleApproveLevel07;
    }

    public Long getRoleApproveLevel08() {
	return this.roleApproveLevel08;
    }

    public void setRoleApproveLevel08(Long roleApproveLevel08) {
	this.roleApproveLevel08 = roleApproveLevel08;
    }

    public Long getRoleApproveLevel09() {
	return this.roleApproveLevel09;
    }

    public void setRoleApproveLevel09(Long roleApproveLevel09) {
	this.roleApproveLevel09 = roleApproveLevel09;
    }

    public Long getRoleApproveLevel10() {
	return this.roleApproveLevel10;
    }

    public void setRoleApproveLevel10(Long roleApproveLevel10) {
	this.roleApproveLevel10 = roleApproveLevel10;
    }

    public Long getRoleApproveLevel11() {
	return this.roleApproveLevel11;
    }

    public void setRoleApproveLevel11(Long roleApproveLevel11) {
	this.roleApproveLevel11 = roleApproveLevel11;
    }

    public Long getRoleApproveLevel12() {
	return this.roleApproveLevel12;
    }

    public void setRoleApproveLevel12(Long roleApproveLevel12) {
	this.roleApproveLevel12 = roleApproveLevel12;
    }

    public Long getRoleApproveLevel13() {
	return this.roleApproveLevel13;
    }

    public void setRoleApproveLevel13(Long roleApproveLevel13) {
	this.roleApproveLevel13 = roleApproveLevel13;
    }

    public Long getRoleApproveLevel14() {
	return this.roleApproveLevel14;
    }

    public void setRoleApproveLevel14(Long roleApproveLevel14) {
	this.roleApproveLevel14 = roleApproveLevel14;
    }

    public Long getRoleApproveLevel15() {
	return this.roleApproveLevel15;
    }

    public void setRoleApproveLevel15(Long roleApproveLevel15) {
	this.roleApproveLevel15 = roleApproveLevel15;
    }

    public Long getRoleApproveLevel16() {
	return this.roleApproveLevel16;
    }

    public void setRoleApproveLevel16(Long roleApproveLevel16) {
	this.roleApproveLevel16 = roleApproveLevel16;
    }

    public Long getRoleApproveLevel17() {
	return this.roleApproveLevel17;
    }

    public void setRoleApproveLevel17(Long roleApproveLevel17) {
	this.roleApproveLevel17 = roleApproveLevel17;
    }

    public Long getRoleApproveLevel18() {
	return this.roleApproveLevel18;
    }

    public void setRoleApproveLevel18(Long roleApproveLevel18) {
	this.roleApproveLevel18 = roleApproveLevel18;
    }

    public Long getRoleApproveLevel19() {
	return this.roleApproveLevel19;
    }

    public void setRoleApproveLevel19(Long roleApproveLevel19) {
	this.roleApproveLevel19 = roleApproveLevel19;
    }

    public Long getRoleApproveLevel20() {
	return this.roleApproveLevel20;
    }

    public void setRoleApproveLevel20(Long roleApproveLevel20) {
	this.roleApproveLevel20 = roleApproveLevel20;
    }

    public String getReserve1() {
	return this.reserve1;
    }

    public void setReserve1(String reserve1) {
	this.reserve1 = reserve1;
    }

    public String getReserve2() {
	return this.reserve2;
    }

    public void setReserve2(String reserve2) {
	this.reserve2 = reserve2;
    }

    public String getReserve3() {
	return this.reserve3;
    }

    public void setReserve3(String reserve3) {
	this.reserve3 = reserve3;
    }

    public String getReserve4() {
	return this.reserve4;
    }

    public void setReserve4(String reserve4) {
	this.reserve4 = reserve4;
    }

    public String getReserve5() {
	return this.reserve5;
    }

    public void setReserve5(String reserve5) {
	this.reserve5 = reserve5;
    }

    public String getCreatedBy() {
	return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    public Date getCreationDate() {
	return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
	this.creationDate = creationDate;
    }

    public String getLastUpdatedBy() {
	return this.lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
	this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
	return this.lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
	this.lastUpdateDate = lastUpdateDate;
    }

    public Long getIndexNo() {
	return this.indexNo;
    }

    public void setIndexNo(Long indexNo) {
	this.indexNo = indexNo;
    }

}