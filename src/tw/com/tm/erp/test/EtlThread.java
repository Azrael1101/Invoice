package tw.com.tm.erp.test;

import java.util.List;
import java.util.Queue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import tw.com.tm.erp.hbm.bean.EtlEventViewTable;
import tw.com.tm.erp.hbm.bean.ImMovementHead;
import tw.com.tm.erp.hbm.dao.EtlEventViewTableDAO;
import tw.com.tm.erp.hbm.service.EtlEventViewTableService;

public class EtlThread implements Runnable {
	private static final Log log = LogFactory.getLog(EtlThread.class);
	
	private EtlEventViewTableService etlEventViewTableService;

	private Queue<EtlEventViewTable> queue;
	
	private String name;
	
	private EtlEventViewTable etlEventViewTable;
	
	public EtlThread(Queue queue, EtlEventViewTableService etlEventViewTableService, String name) {
		this.etlEventViewTableService = etlEventViewTableService;
		this.queue = queue;
		this.name = name;
	}

	
	@Override
	public void run() {
		
		while(queue.size() != 0) {
			try {
				log.info(name + " : 目前剩餘" + queue.size());
				etlEventViewTable = queue.poll();
				etlEventViewTable.setNumberOfExecutions(etlEventViewTable.getNumberOfExecutions()+1);
				etlEventViewTableService.update(etlEventViewTable);
				log.info(name+" : 更新成功，單號為"+etlEventViewTable.getOrderNo() + ",被執行次數" + etlEventViewTable.getNumberOfExecutions());
			} catch (Exception e) {
				log.error("更新狀態發生錯誤"+etlEventViewTable.getOrderNo() + "，原因為："+ e.toString());

			}	
		}
//		log.info(name + "執行緒工作結束");
		
	}

}
