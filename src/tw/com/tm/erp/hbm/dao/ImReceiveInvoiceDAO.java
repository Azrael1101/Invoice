package tw.com.tm.erp.hbm.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

public class ImReceiveInvoiceDAO extends BaseDAO {

	private static final Log log = LogFactory.getLog(ImReceiveInvoiceDAO.class);
	private NativeQueryDAO nativeQueryDAO;

	/**
	 * 判斷發票是否已經被ImReceive使用掉
	 * 
	 * @param invoiceNo
	 * @return true : 已被使用 , false : 未被使用
	 */
	public boolean isUsedInvoice(String brandCode, String invoiceNo, Long headId) {
		log.info("ImReceiveInvoiceDAO.isUsedInvoice brandCode=" + brandCode + ",invoiceNo=" + invoiceNo + ",headId=" + headId);
		final String iNo = invoiceNo;
		final Long hId = headId;
		final String bCode = brandCode;

		if (StringUtils.hasText(brandCode) && StringUtils.hasText(invoiceNo)) {
			//用來判斷進貨單的發票是否已被使用掉
			if (null != headId) {
				String nativeSql = "select count(head.head_id) from im_receive_head head,im_receive_invoice invoice where head.head_id = invoice.head_id and head.head_id <> '"
						+ hId
						+ "' and head.brand_code = '"
						+ bCode
						+ "' and invoice.invoice_no = '"
						+ iNo
						+ "' and head.status <> 'VOID' and substr(head.order_no,0,4) <> 'TMP_' ";
				List reList = nativeQueryDAO.executeNativeSql(nativeSql);
				if (null != reList && reList.size() > 0) {
					int count = ((BigDecimal) reList.get(0)).intValue();
					if (count > 1) {
						return true;
					}
				}
			} else {
				//判斷發票是否被使用
				String nativeSql = "select count(head.head_id) from im_receive_head head,im_receive_invoice invoice where head.head_id = invoice.head_id "
						+ " and head.brand_code = '"
						+ bCode
						+ "' and invoice.invoice_no = '"
						+ iNo
						+ "' and head.status <> 'VOID' and substr(head.order_no,0,4) <> 'TMP_' ";
				List reList = nativeQueryDAO.executeNativeSql(nativeSql);
				if (null != reList && reList.size() > 0) {
					int count = ((BigDecimal) reList.get(0)).intValue();
					if (count > 0) {
						return true;
					}
				}

			}
		}

		/*
		 * if (StringUtils.hasText(iNo)) { List temp =
		 * getHibernateTemplate().executeFind( new HibernateCallback() { public
		 * Object doInHibernate(Session session) throws HibernateException,
		 * SQLException { //20081013 shan // StringBuffer hql = new
		 * StringBuffer( // "from ImReceiveInvoice as item where ");
		 * 
		 * StringBuffer hql = new StringBuffer( "from ImReceiveInvoice as
		 * item,ImReceiveHead as head where head.status <> 'VOID' and ");
		 * hql.append(" head.brandCode = :brandCode and " ) ; hql.append("
		 * item.invoiceNo = :invoiceNo " ) ;
		 * 
		 * if( null != hId ) hql.append(" and item.imReceiveHead.headId <>
		 * :headId " ) ; //hql.append(" and FiInvoiceHead.status = :status ");
		 * Query query = session.createQuery(hql.toString());
		 * //query.setString("status", OrderStatus.FINISH );
		 * query.setString("invoiceNo", iNo); query.setString("brandCode",
		 * bCode);
		 * 
		 * if( null != hId ) query.setLong("headId", hId);
		 * query.setFirstResult(0); query.setMaxResults(1); return query.list(); }
		 * }); if ((null != temp) && (temp.size() > 0)) { return true ; } }
		 */
		return false;
	}

	public void setNativeQueryDAO(NativeQueryDAO nativeQueryDAO) {
		this.nativeQueryDAO = nativeQueryDAO;
	}
}