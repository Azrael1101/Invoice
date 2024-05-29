package tw.com.tm.erp.hbm.dao;

import java.util.List;

import tw.com.tm.erp.hbm.bean.DbcInformationTables;
import tw.com.tm.erp.hbm.bean.OmmChannel;

public class OmmChannelDAO extends BaseDAO{
	
	public OmmChannel findByIdentification(String channelType, String categoryType) {
		StringBuffer hql = new StringBuffer("from OmmChannel as model where 1 = 1");
		hql.append(" and model.channelType = ? ");
		hql.append(" and model.categoryType = ? ");
		List<OmmChannel> result = getHibernateTemplate().find(hql.toString(),
				new Object[] { channelType, categoryType });

		return (result != null && result.size() > 0 ? result.get(0) : null);
		
		
	}

}
