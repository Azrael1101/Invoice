package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.constants.SystemConfig;
import tw.com.tm.erp.hbm.bean.BuAddressBook;
import tw.com.tm.erp.hbm.bean.BuCustomer;
import tw.com.tm.erp.hbm.bean.ImItemEancode;

public class BuAddressBookDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuAddressBookDAO.class);

	/**
	 * 依據客戶資料查詢螢幕的輸入條件進行查詢
	 * 
	 * @param conditionMap
	 * @return List
	 * @throws Exception
	 */
	public List findCustomerList(HashMap conditionMap) {

		final String brandCode = (String) conditionMap.get("brandCode");
		final String type = (String) conditionMap.get("type");
		final String identityCode = (String) conditionMap.get("identityCode");
		final String chineseName = (String) conditionMap.get("chineseName");
		final String englishName = (String) conditionMap.get("englishName");
		final String birthdayYear = (String) conditionMap.get("birthdayYear");
		final Long birthdayMonth = (Long) conditionMap.get("birthdayMonth");
		final Long birthdayDay = (Long) conditionMap.get("birthdayDay");
		final String gender = (String) conditionMap.get("gender");
		final String customerTypeCode = (String) conditionMap.get("customerTypeCode");
		final String customerCode = (String) conditionMap.get("customerCode");
		final String vipTypeCode = (String) conditionMap.get("vipTypeCode");
		final Date vipStartDate = (Date) conditionMap.get("vipStartDate");
		final Date vipEndDate = (Date) conditionMap.get("vipEndDate");
		final Date applicationDate = (Date) conditionMap.get("applicationDate");
		final String enable = (String) conditionMap.get("enable");
		
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select model, model2 from BuAddressBook as model, BuCustomer as model2 ");
				hql.append("where model.addressBookId = model2.addressBookId and model2.id.brandCode = :brandCode");

				if (StringUtils.hasText(type))
					hql.append(" and model.type = :type");

				if (StringUtils.hasText(identityCode))
					hql.append(" and model.identityCode = :identityCode");

				if (StringUtils.hasText(chineseName))
					hql.append(" and model.chineseName like :chineseName");

				if (StringUtils.hasText(englishName))
					hql.append(" and model.englishName like :englishName");

				if (StringUtils.hasText(gender))
					hql.append(" and model.gender = :gender");

				if (StringUtils.hasText(birthdayYear))
					hql.append(" and model.birthdayYear = :birthdayYear");

				if (birthdayMonth != null && birthdayMonth != 0L)
					hql.append(" and model.birthdayMonth = :birthdayMonth");

				if (birthdayDay != null && birthdayDay != 0L)
					hql.append(" and model.birthdayDay = :birthdayDay");

				if (StringUtils.hasText(customerTypeCode))
					hql.append(" and model2.customerTypeCode = :customerTypeCode");

				if (StringUtils.hasText(customerCode))
					hql.append(" and model2.id.customerCode like :customerCode");

				if (StringUtils.hasText(vipTypeCode))
					hql.append(" and model2.vipTypeCode = :vipTypeCode");

				if (vipStartDate != null)
					hql.append(" and model2.vipStartDate >= :vipStartDate");

				if (vipEndDate != null)
					hql.append(" and model2.vipEndDate <= :vipEndDate");

				if (applicationDate != null)
					hql.append(" and model2.applicationDate = :applicationDate");

				if (enable != null)
					hql.append(" and model2.enable = :enable");
				
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
				query.setString("brandCode", brandCode);

				if (StringUtils.hasText(type))
					query.setString("type", type);

				if (StringUtils.hasText(identityCode))
					query.setString("identityCode", identityCode);

				if (StringUtils.hasText(chineseName))
					query.setString("chineseName", "%" + chineseName + "%");

				if (StringUtils.hasText(englishName))
					query.setString("englishName", "%" + englishName + "%");

				if (StringUtils.hasText(gender))
					query.setString("gender", gender);

				if (StringUtils.hasText(birthdayYear))
					query.setLong("birthdayYear", Long.parseLong(birthdayYear));

				if (birthdayMonth != null && birthdayMonth != 0L)
					query.setLong("birthdayMonth", birthdayMonth);

				if (birthdayDay != null && birthdayDay != 0L)
					query.setLong("birthdayDay", birthdayDay);

				if (StringUtils.hasText(customerTypeCode))
					query.setString("customerTypeCode", customerTypeCode);

				if (StringUtils.hasText(customerCode))
					query.setString("customerCode", "%" + customerCode + "%");

				if (StringUtils.hasText(vipTypeCode))
					query.setString("vipTypeCode", vipTypeCode);

				if (vipStartDate != null)
					query.setDate("vipStartDate", vipStartDate);

				if (vipEndDate != null)
					query.setDate("vipEndDate", vipEndDate);

				if (applicationDate != null)
					query.setDate("applicationDate", applicationDate);
				log.info("enable"+enable);
				if (enable != null)
					query.setString("enable", enable);
				log.info("query"+query);
				return query.list();
			}
		});

		return result;
	}

	public List findCustomerListNoLimit(HashMap conditionMap) {
		final String brandCode = (String) conditionMap.get("brandCode");
		final String dataDate = (String) conditionMap.get("dataDate");
		final String dataDateEnd = (String) conditionMap.get("dataDateEnd");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model, model2 from BuAddressBook as model, BuCustomer as model2 ");
				hql.append("where model.addressBookId = model2.addressBookId and model2.id.brandCode = :brandCode ");
				hql.append("and to_char(model2.lastUpdateDate, 'YYYYMMDD') >= :dataDate ");
				hql.append("and to_char(model2.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
				hql.append("AND LENGTH(model.identityCode) <= 10 ");
				hql.append("order by model2.id.customerCode");
				Query query = session.createQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("dataDate", dataDate);
				query.setString("dataDateEnd", dataDateEnd);
				return query.list();
			}
		});

		return result;
	}

	/**
	 * 依據通訊錄查詢螢幕的輸入條件進行查詢
	 * 
	 * @param conditionMap
	 * @return List
	 * @throws Exception
	 */
	public List findAddressBookList(HashMap conditionMap) {

		final String type = (String) conditionMap.get("type");
		final String identityCode = (String) conditionMap.get("identityCode");
		final String chineseName = (String) conditionMap.get("chineseName");
		final String englishName = (String) conditionMap.get("englishName");
		final String birthdayYear = (String) conditionMap.get("birthdayYear");
		final Long birthdayMonth = (Long) conditionMap.get("birthdayMonth");
		final Long birthdayDay = (Long) conditionMap.get("birthdayDay");
		final String gender = (String) conditionMap.get("gender");
		final String tel1 = (String) conditionMap.get("tel1");
		final String tel2 = (String) conditionMap.get("tel2");
		final String mobilePhone = (String) conditionMap.get("mobilePhone");

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

			    
			        StringBuffer hql = new StringBuffer("select a.address_book_id, a.type, a.identity_code, a.chinese_name, a.english_name," +
			        		" a.tel1, a.tel2, a.mobile_phone, a.last_update_date, e.employee_code");				
				hql.append(" from erp.bu_address_book a, erp.bu_employee e where a.address_book_id = e.address_book_id(+)");

				if (StringUtils.hasText(type))
					hql.append(" and a.type = :type");

				if (StringUtils.hasText(identityCode))
					hql.append(" and a.identity_code = :identityCode");

				if (StringUtils.hasText(chineseName))
					hql.append(" and a.chinese_name like :chineseName");

				if (StringUtils.hasText(englishName))
					hql.append(" and a.english_name like :englishName");

				if (StringUtils.hasText(gender))
					hql.append(" and a.gender = :gender");

				if (StringUtils.hasText(birthdayYear))
					hql.append(" and a.birthday_year = :birthdayYear");

				if (birthdayMonth != null && birthdayMonth != 0L)
					hql.append(" and a.birthday_month = :birthdayMonth");

				if (birthdayDay != null && birthdayDay != 0L)
					hql.append(" and a.birthday_day = :birthdayDay");

				if (StringUtils.hasText(tel1))
					hql.append(" and a.tel1 = :tel1");

				if (StringUtils.hasText(tel2))
					hql.append(" and a.tel2 = :tel2");

				if (StringUtils.hasText(mobilePhone))
					hql.append(" and a.mobile_phone = :mobilePhone");

				Query query = session.createSQLQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				if (StringUtils.hasText(type))
					query.setString("type", type);

				if (StringUtils.hasText(identityCode))
					query.setString("identityCode", identityCode);

				if (StringUtils.hasText(chineseName))
					query.setString("chineseName", "%" + chineseName + "%");

				if (StringUtils.hasText(englishName))
					query.setString("englishName", "%" + englishName + "%");

				if (StringUtils.hasText(gender))
					query.setString("gender", gender);

				if (StringUtils.hasText(birthdayYear))
					query.setLong("birthdayYear", Long.parseLong(birthdayYear));

				if (birthdayMonth != null && birthdayMonth != 0L)
					query.setLong("birthdayMonth", birthdayMonth);

				if (birthdayDay != null && birthdayDay != 0L)
					query.setLong("birthdayDay", birthdayDay);

				if (StringUtils.hasText(tel1))
					query.setString("tel1", tel1);

				if (StringUtils.hasText(tel2))
					query.setString("tel2", tel2);

				if (StringUtils.hasText(mobilePhone))
					query.setString("mobilePhone", mobilePhone);

				return query.list();
			}
		});

		return result;
	}

	/**
	 * 依據供應商資料查詢螢幕的輸入條件進行查詢
	 * 
	 * @param conditionMap
	 * @return List
	 * @throws Exception
	 */
	public List findSupplierList(HashMap conditionMap) {

		final String brandCode = (String) conditionMap.get("brandCode");
		final String type = (String) conditionMap.get("type");
		final String identityCode = (String) conditionMap.get("identityCode");
		final String chineseName = (String) conditionMap.get("chineseName");
		final String supplierTypeCode = (String) conditionMap.get("supplierTypeCode");
		final String supplierCode = (String) conditionMap.get("supplierCode");
		final String categoryCode = (String) conditionMap.get("categoryCode");
		final String customsBroker = (String) conditionMap.get("customsBroker");
		final String agent = (String) conditionMap.get("agent");
		final String commissionRate_Start = (String) conditionMap.get("commissionRate_Start");
		final String commissionRate_End = (String) conditionMap.get("commissionRate_End");

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer("select model, model2 from BuAddressBook as model, BuSupplier as model2 ");
				hql.append("where model.addressBookId = model2.addressBookId and model2.id.brandCode = :brandCode");

				if (StringUtils.hasText(type))
					hql.append(" and model.type = :type");

				if (StringUtils.hasText(identityCode))
					hql.append(" and model.identityCode = :identityCode");

				if (StringUtils.hasText(chineseName))
					hql.append(" and model.chineseName like :chineseName");

				if (StringUtils.hasText(supplierTypeCode))
					hql.append(" and model2.supplierTypeCode = :supplierTypeCode");

				if (StringUtils.hasText(supplierCode))
					hql.append(" and model2.id.supplierCode = :supplierCode");

				if (StringUtils.hasText(categoryCode))
					hql.append(" and model2.categoryCode = :categoryCode");

				if (StringUtils.hasText(customsBroker))
					hql.append(" and model2.customsBroker = :customsBroker");

				if (StringUtils.hasText(agent))
					hql.append(" and model2.agent = :agent");

				if (StringUtils.hasText(commissionRate_Start))
					hql.append(" and model2.commissionRate >= :commissionRate_Start");

				if (StringUtils.hasText(commissionRate_End))
					hql.append(" and model2.commissionRate <= :commissionRate_End");

				Query query = session.createQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);
				query.setString("brandCode", brandCode);

				if (StringUtils.hasText(type))
					query.setString("type", type);

				if (StringUtils.hasText(identityCode))
					query.setString("identityCode", identityCode);

				if (StringUtils.hasText(chineseName))
					query.setString("chineseName", "%" + chineseName + "%");

				if (StringUtils.hasText(supplierTypeCode))
					query.setString("supplierTypeCode", supplierTypeCode);

				if (StringUtils.hasText(supplierCode))
					query.setString("supplierCode", supplierCode);

				if (StringUtils.hasText(categoryCode))
					query.setString("categoryCode", categoryCode);

				if (StringUtils.hasText(customsBroker))
					query.setString("customsBroker", customsBroker);

				if (StringUtils.hasText(agent))
					query.setString("agent", agent);

				if (StringUtils.hasText(commissionRate_Start))
					query.setDouble("commissionRate_Start", Double.parseDouble(commissionRate_Start));

				if (StringUtils.hasText(commissionRate_End))
					query.setDouble("commissionRate_End", Double.parseDouble(commissionRate_End));

				return query.list();
			}
		});

		return result;
	}

	/**
	 * 依據員工資料查詢螢幕的輸入條件進行查詢
	 * 
	 * @param conditionMap
	 * @return List
	 * @throws Exception
	 */
	public List findEmployeeList(HashMap conditionMap) {

		final String brandCode = (String) conditionMap.get("brandCode");
		final String type = (String) conditionMap.get("type");
		final String identityCode = (String) conditionMap.get("identityCode");
		final String chineseName = (String) conditionMap.get("chineseName");
		final String englishName = (String) conditionMap.get("englishName");
		final String birthdayYear = (String) conditionMap.get("birthdayYear");
		final Long birthdayMonth = (Long) conditionMap.get("birthdayMonth");
		final Long birthdayDay = (Long) conditionMap.get("birthdayDay");
		final String gender = (String) conditionMap.get("gender");
		final String employeeCode = (String) conditionMap.get("employeeCode");
		final String employeePosition = (String) conditionMap.get("employeePosition");
		final String isDepartmentManager = (String) conditionMap.get("isDepartmentManager");
		final Date arriveDate_Start = (Date) conditionMap.get("arriveDate_Start");
		final Date arriveDate_End = (Date) conditionMap.get("arriveDate_End");

		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer(
						"select a.address_book_id, a.type, a.identity_code, a.chinese_name, e.employee_code, c.brand_code, e.arrive_date, e.leave_date, a.last_Update_Date from erp.bu_address_book a, erp.bu_employee e, ");

				if (StringUtils.hasText(brandCode)) {
					hql.append("(select b.brand_code, b.employee_code from erp.bu_employee_brand b where b.brand_code = :brandCode) c ");
					hql.append("where a.address_book_id = e.address_book_id and e.employee_code = c.employee_code");
				} else {
					hql.append("(select b.brand_code, b.employee_code from erp.bu_employee_brand b where b.brand_code like '%') c ");
					hql.append("where a.address_book_id = e.address_book_id and e.employee_code = c.employee_code(+)");
				}

				if (StringUtils.hasText(type))
					hql.append(" and a.type = :type");

				if (StringUtils.hasText(identityCode))
					hql.append(" and a.identity_code = :identityCode");

				if (StringUtils.hasText(chineseName))
					hql.append(" and a.chinese_name like :chineseName");

				if (StringUtils.hasText(englishName))
					hql.append(" and a.english_name like :englishName");

				if (StringUtils.hasText(gender))
					hql.append(" and a.gender = :gender");

				if (StringUtils.hasText(birthdayYear))
					hql.append(" and a.birthday_year = :birthdayYear");

				if (birthdayMonth != null && birthdayMonth != 0L)
					hql.append(" and a.birthday_month = :birthdayMonth");

				if (birthdayDay != null && birthdayDay != 0L)
					hql.append(" and a.birthday_day = :birthdayDay");

				if (StringUtils.hasText(employeeCode))
					hql.append(" and e.employee_code = :employeeCode");

				if (StringUtils.hasText(employeePosition))
					hql.append(" and e.employee_position = :employeePosition");

				if (StringUtils.hasText(isDepartmentManager))
					hql.append(" and e.is_department_manager = :isDepartmentManager");

				if (arriveDate_Start != null)
					hql.append(" and e.arrive_date >= :arriveDate_Start");

				if (arriveDate_End != null)
					hql.append(" and e.arrive_date <= :arriveDate_End");

				Query query = session.createSQLQuery(hql.toString());
				query.setFirstResult(0);
				query.setMaxResults(SystemConfig.SEARCH_PAGE_MAX_COUNT);

				if (StringUtils.hasText(brandCode))
					query.setString("brandCode", brandCode);

				if (StringUtils.hasText(type))
					query.setString("type", type);

				if (StringUtils.hasText(identityCode))
					query.setString("identityCode", identityCode);

				if (StringUtils.hasText(chineseName))
					query.setString("chineseName", "%" + chineseName + "%");

				if (StringUtils.hasText(englishName))
					query.setString("englishName", "%" + englishName + "%");

				if (StringUtils.hasText(gender))
					query.setString("gender", gender);

				if (StringUtils.hasText(birthdayYear))
					query.setLong("birthdayYear", Long.parseLong(birthdayYear));

				if (birthdayMonth != null && birthdayMonth != 0L)
					query.setLong("birthdayMonth", birthdayMonth);

				if (birthdayDay != null && birthdayDay != 0L)
					query.setLong("birthdayDay", birthdayDay);

				if (StringUtils.hasText(employeeCode))
					query.setString("employeeCode", employeeCode);

				if (StringUtils.hasText(employeePosition))
					query.setString("employeePosition", employeePosition);

				if (StringUtils.hasText(isDepartmentManager))
					query.setString("isDepartmentManager", isDepartmentManager);

				if (arriveDate_Start != null)
					query.setDate("arriveDate_Start", arriveDate_Start);

				if (arriveDate_End != null)
					query.setDate("arriveDate_End", arriveDate_End);

				return query.list();
			}
		});

		return result;
	}

	public List findEmployeeListNoLimit(HashMap conditionMap) {
		final String brandCode = (String) conditionMap.get("brandCode");
		final String isLeave = (String) conditionMap.get("isLeave");
		final String isShopEmployee = (String) conditionMap.get("isShopEmployee");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {

				StringBuffer hql = new StringBuffer(
						"select a.address_book_id, a.type, a.identity_code, a.chinese_name, e.employee_code, c.brand_code, e.arrive_date, e.leave_date, a.last_Update_Date from erp.bu_address_book a, erp.bu_employee e, ");
				if (StringUtils.hasText(brandCode)) {
					hql.append("(select b.brand_code, b.employee_code from erp.bu_employee_brand b where b.brand_code = :brandCode) c ");
					hql.append("where a.address_book_id = e.address_book_id and e.employee_code = c.employee_code");
				} else {
					hql.append("(select b.brand_code, b.employee_code from erp.bu_employee_brand b where b.brand_code like '%') c ");
					hql.append("where a.address_book_id = e.address_book_id and e.employee_code = c.employee_code(+)");
				}
				if("N".equals(isLeave)){
				        hql.append(" and e.leave_date is null");
				}
				if("Y".equals(isShopEmployee)){
				        hql.append(" and e.remark2 = 'Y'");
				}
				Query query = session.createSQLQuery(hql.toString());
				if (StringUtils.hasText(brandCode)){
					query.setString("brandCode", brandCode);
				}

				return query.list();
			}
		});

		return result;
	}
	
	public List findShopEmployeeList(HashMap conditionMap) {
		final String brandCode = (String) conditionMap.get("brandCode");
		final String isLeave = (String) conditionMap.get("isLeave");
		final String isShopEmployee = (String) conditionMap.get("isShopEmployee");
		final String isEnable = (String) conditionMap.get("isEnable");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				//MACO 2016/11/04 解決編碼問題，下傳抓取CHINESE_NAME_BAK2
				StringBuffer hql = new StringBuffer(
						"select a.address_book_id, a.type, a.identity_code, a.chinese_name_bak2, e.employee_code, c.brand_code, e.arrive_date, e.leave_date, a.last_Update_Date from erp.bu_address_book a, erp.bu_employee e, ");				
				hql.append("(select s.brand_code, e.employee_code from erp.bu_shop s, erp.bu_shop_employee e where s.shop_code = e.shop_code");
				if (StringUtils.hasText(brandCode)) {
				        hql.append(" and s.brand_code = :brandCode");
				}
				
				if (StringUtils.hasText(isEnable)){
					hql.append(" and s.enable = :enable and e.enable = :enable");
				}	
				
				hql.append(" group by s.brand_code, e.employee_code) c where a.address_book_id = e.address_book_id and e.employee_code = c.employee_code");
				
				if("N".equals(isLeave)){
				        hql.append(" and e.leave_date is null");
				}
				
				if("Y".equals(isShopEmployee)){
				        hql.append(" and e.remark2 = 'Y'");
				}
				Query query = session.createSQLQuery(hql.toString());
				if (StringUtils.hasText(brandCode)){
					query.setString("brandCode", brandCode);
				}
				
				if (StringUtils.hasText(isEnable)){
					query.setString("enable", isEnable);
				}

				return query.list();
			}
		});

		return result;
	}
	
	public List findEmployeeByCondition(HashMap conditionMap) {
	        final String brandCode = (String) conditionMap.get("brandCode");
		final String dataDate = (String) conditionMap.get("dataDate");
		final String dataDateEnd = (String) conditionMap.get("dataDateEnd");
		final String leaveDate = (String) conditionMap.get("leaveDate");
		
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model, model2 from BuAddressBook as model, BuEmployee as model2, BuEmployeeBrand as model3 ");			
				hql.append("where model.addressBookId = model2.addressBookId and model2.employeeCode = model3.id.employeeCode and model3.id.brandCode = :brandCode ");
				hql.append("and ( to_char(model2.lastUpdateDate, 'YYYYMMDD') >= :dataDate ");
				hql.append("and to_char(model2.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
				hql.append("or to_char(model2.leaveDate, 'YYYYMMDD') = :leaveDate )");
				
				hql.append("order by model2.employeeCode");
				Query query = session.createQuery(hql.toString());
				query.setString("brandCode", brandCode);
				query.setString("dataDate", dataDate);
				query.setString("dataDateEnd", dataDateEnd);
				query.setString("leaveDate", leaveDate);
				
				
				return query.list();
			}
		});

		return result;
	}
	
	public List findEmpByCondition(HashMap conditionMap) {
	        final String brandCode = (String) conditionMap.get("brandCode");
		final String dataDate = (String) conditionMap.get("dataDate");
		final String dataDateEnd = (String) conditionMap.get("dataDateEnd");
		final String empCode = conditionMap.get("employeeCode") == null? "":(String) conditionMap.get("employeeCode");
		List result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("select model, model2 from BuAddressBook as model, BuEmployee as model2 ");			
				hql.append("where model.addressBookId = model2.addressBookId ");
				hql.append("and model2.brandCode = :brandCode ");
				if("".equals(empCode)){
				    hql.append("and to_char(model2.lastUpdateDate, 'YYYYMMDD') >= :dataDate ");
				    hql.append("and to_char(model2.lastUpdateDate, 'YYYYMMDD') <= :dataDateEnd ");
				}else{
				    hql.append("and model2.employeeCode = :employeeCode ");
				}
				Query query = session.createQuery(hql.toString());
				    query.setString("brandCode", brandCode);
				if("".equals(empCode)){
				    query.setString("dataDate", dataDate);
				    query.setString("dataDateEnd", dataDateEnd);
				}else{
				    query.setString("employeeCode", empCode);
				}
				return query.list();
			}
		});

		return result;
	}
	public BuAddressBook findByAddressBookId(Long addressBookId) {
		StringBuffer hql = new StringBuffer("from BuAddressBook as model ");
		hql.append("where model.addressBookId = ? ");

		List<BuAddressBook> result = getHibernateTemplate().find(hql.toString(),
			new Object[] { addressBookId});
		return (result != null && result.size() > 0 ? result.get(0) : null);
	}
	public List<BuAddressBook> findByIdentityCode(String identityCode) {
		StringBuffer hql = new StringBuffer("from BuAddressBook as model ");
		hql.append("where model.identityCode = ?  order by lastUpdateDate desc ");

		List<BuAddressBook> result = getHibernateTemplate().find(hql.toString(),
			new Object[] { identityCode});
		return result;
	}
	
	public BuAddressBook findIdentityCodeByProperty(String identityCode){
    	return (BuAddressBook)findFirstByProperty("BuAddressBook", "and identityCode = ?", new Object[]{identityCode} );
    }
}