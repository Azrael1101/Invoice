package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.BuLocationArea;

public class BuLocationAreaDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(BuLocationAreaDAO.class);

	public List<BuLocationArea> findByValue(String city, String area,
			String zipCode) throws Exception {

		city = "%" + city + "%";
		area = "%" + area + "%";
		zipCode = "%" + zipCode + "%";
		StringBuffer hql = new StringBuffer(
				"from BuLocationArea as model where 1=1 ");
		hql.append("and model.city like ? ");
		hql.append("and model.area like ? ");
		hql.append("and model.zipCode like ? ");
		hql.append("order by locationId ");
		System.out.println(hql.toString());
		System.out.println(city);
		List<BuLocationArea> lists = getHibernateTemplate().find(
				hql.toString(), new String[] { city, area, zipCode });

		return lists;

	}
}