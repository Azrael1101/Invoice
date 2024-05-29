package tw.com.tm.erp.hbm.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.sql.DataSource;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

public class PosControlDAO extends BaseDAO {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public List findPosControl(final String brandCode, final String dataType,
			final String posMachineCode) {

		List result = getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						StringBuffer hql = new StringBuffer(
								"from PosControl as model where");
						hql.append(" model.brandCode = :brandCode");
						hql.append(" and model.dataType = :dataType");
						hql.append(" and model.machineCode = :machineCode");

						Query query = session.createQuery(hql.toString());
						query.setString("brandCode", brandCode);
						query.setString("dataType", dataType);
						query.setString("machineCode", posMachineCode);

						return query.list();
					}
				});

		return result;
	}

	public boolean checkInsertOrUpdate(String brandCode, String dataType,
			String machineCode) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rst = null;
		try {
			conn = dataSource.getConnection();
			StringBuffer sql = new StringBuffer(
					"SELECT BRAND_CODE FROM POS.POS_CONTROL WHERE");
			sql.append(" BRAND_CODE = '" + brandCode + "'");
			sql.append(" AND DATA_TYPE = '" + dataType + "'");
			sql.append(" AND MACHINE_CODE = '" + machineCode + "'");
			stmt = conn.createStatement();
			rst = stmt.executeQuery(sql.toString());
			if (rst.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			return false;
		}finally{
			if(rst!=null){
				try {
					rst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
		}
	}
	
	public boolean checkDataTpye(String dataType,
			String machineCode) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rst = null;
		String status = "W_POS";
		try {
			System.out.println("UU"+dataSource);
			conn = dataSource.getConnection();
			StringBuffer sql = new StringBuffer(
					"SELECT DATA_ID FROM POS.ERP_COMMAND WHERE");
			sql.append(" DATA_TYPE = '" + dataType + "'");
			sql.append(" AND MACHINE_CODE = '" + machineCode + "'");
			sql.append(" AND STATUS = '" + status + "'");
			System.out.println("SQLL:"+sql.toString());
			stmt = conn.createStatement();
			rst = stmt.executeQuery(sql.toString());
			if (rst.next()) {
				return true;
			} else {
				return false;
			}
		}catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}finally{
			if(rst!=null){
				try {
					rst.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
			if(stmt!=null){
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}			
		}
	}

}
