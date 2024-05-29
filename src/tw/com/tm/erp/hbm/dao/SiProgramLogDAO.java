package tw.com.tm.erp.hbm.dao;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.hbm.bean.SiProgramLog;

public class SiProgramLogDAO extends BaseDAO {
    
    private static final Log log = LogFactory.getLog(SiProgramLogDAO.class);
    
    
    /**
     * 依據programId、levelType、identification查詢
     * 
     * @param programId
     * @param levelType
     * @param identification
     * @return List<SiProgramLog>
     */
    public List<SiProgramLog> findByIdentification(String programId, String levelType, String identification) {
	
	StringBuffer hql = new StringBuffer("from SiProgramLog as model where model.programId = ?");
        hql.append(" and model.identification = ?");
        if (StringUtils.hasText(levelType)) {
            hql.append(" and model.levelType = ?");			    
	}
        hql.append(" order by model.id");
        Object[] objArray = null;
        if (StringUtils.hasText(levelType)) {
            objArray = new Object[] {programId, identification, levelType};
        }else{
            objArray = new Object[] {programId, identification};
        }      
        List<SiProgramLog> result = getHibernateTemplate().find(hql.toString(), objArray);
        
        return result;
    }

}