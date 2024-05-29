package tw.com.tm.erp.hbm.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import tw.com.tm.erp.hbm.bean.DbcInformationColumns;
import tw.com.tm.erp.hbm.bean.DbcInformationTables;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFHead;
import tw.com.tm.erp.hbm.bean.OmmChannelTXFLine;
import tw.com.tm.erp.hbm.service.OmmChannelTXFService;

public class OmmChannelTXFLineDAO extends BaseDAO {
	private static final Log log = LogFactory.getLog(OmmChannelTXFLineDAO.class);

	public List<OmmChannelTXFLine> findPageLine(Long pSysSno, int startPage, int pageSize) {

		final int startRecordIndexStar = startPage * pageSize;
		final int pSize = pageSize;
		List<OmmChannelTXFLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from OmmChannelTXFLine as model where 1=1 ");
				if (pSysSno != null)
					hql.append(" and model.ommChannelTXFHead.sysSno = :pSysSno order by model.columnIndex");
				Query query = session.createQuery(hql.toString());
				query.setFirstResult(startRecordIndexStar);
				query.setMaxResults(pSize);
				if (pSysSno != null)
					query.setLong("pSysSno", pSysSno);
				return query.list();
			}
		});
		log.info("ommChannelTXFLine.pSysSno:" + pSysSno);
		log.info("ommChannelTXFLine.size:" + result.size());

		return result;
	}
	
	public List<OmmChannelTXFLine> findByPSysSno(Long pSysSno){
		
		List<OmmChannelTXFLine> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from OmmChannelTXFLine as model where 1=1 ");
				if (pSysSno != null)
					hql.append(" and model.ommChannelTXFHead.sysSno = :pSysSno order by model.columnIndex");
				Query query = session.createQuery(hql.toString());
				if (pSysSno != null)
					query.setLong("pSysSno", pSysSno);
				return query.list();
			}
		});
		
		
		return result;
	};
	
	/**
	 * find page line 最後一筆 index
	 * 
	 * @param tableName
	 * @return Long
	 */
	public Long findPageLineMaxIndex(Long sysSno) {

		Long lineMaxIndex = new Long(0);
		List<OmmChannelTXFHead> result = getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				StringBuffer hql = new StringBuffer("from OmmChannelTXFHead as model where 1=1 ");
				
				log.info("======================"+ sysSno !=null );
				
				if (sysSno != null)
					hql.append(" and model.sysSno = :sysSno");
				Query query = session.createQuery(hql.toString());
				if (sysSno != null)
					query.setLong("sysSno", sysSno);
				return query.list();
			}
		});
		if (result != null && result.size() > 0) {
			List<OmmChannelTXFLine> ommChannelTXFLineList = result.get(0).getOmmChannelTXFLineList();
			if (ommChannelTXFLineList != null && ommChannelTXFLineList.size() > 0) {
				lineMaxIndex = ommChannelTXFLineList.get(ommChannelTXFLineList.size() - 1).getColumnIndex();
			}
		}
		return lineMaxIndex;
	}

}
