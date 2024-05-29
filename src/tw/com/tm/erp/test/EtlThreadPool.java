package tw.com.tm.erp.test;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.inet.report.ex;

import tw.com.tm.erp.exceptions.NoSuchDataException;
import tw.com.tm.erp.hbm.bean.EtlEventViewTable;
import tw.com.tm.erp.hbm.service.EtlEventViewTableService;

public class EtlThreadPool {
	private static final Log log = LogFactory.getLog(EtlThreadPool.class);
	
	private List<EtlEventViewTable> etlEventViewTableList = null;
	
	private EtlEventViewTableService etlEventViewTableServiceTmp;
	
	private static EtlEventViewTableService etlEventViewTableService;
	
	public void setEtlEventViewTableServiceTmp(EtlEventViewTableService etlEventViewTableServiceTmp) {
		this.etlEventViewTableServiceTmp = etlEventViewTableServiceTmp;
	}

	private QueueTest queueTest;
	
	public void init() throws InterruptedException{
//		Long startSec; 
		int corePoolSize = 4;
		
		Runtime runtime = Runtime.getRuntime();
		log.info("初始化測試" + ";記憶體總量 = " + runtime.totalMemory() + ";剩餘量 = " + runtime.freeMemory());
	
		etlEventViewTableService = etlEventViewTableServiceTmp;
		queueTest = new QueueTest();
		
		//建立執行者的執行緒，並啟動
		for(int i = 1; i <= corePoolSize ; i++) {
			Thread th =  new Thread(new Executor(queueTest, etlEventViewTableService, "工作奴隸"+i+"號"));
			Thread.sleep(1000);
			th.start();
		}
		//建立派工者的執行緒，並執行
		Thread th = new Thread(new Dispatcher(queueTest, etlEventViewTableService, "工頭"));
		Thread.sleep(1000);
		th.start();
		
		log.info("初始化測試" + ";記憶體總量 = " + runtime.totalMemory() + ";剩餘量 = " + runtime.freeMemory());
		
//		ExecutorService ex = Executors.newScheduledThreadPool(5);
//		ex.execute(e1);
	}
	
	class QueueTest{
		private Queue<EtlEventViewTable> queue;
		
		public QueueTest(){
			queue = new LinkedList<EtlEventViewTable>();
		}
		
		//取出資料
		synchronized EtlEventViewTable get() {
			while(queue.size() == 0) {
	            try {
	            	log.info("佇列已空，等待中");
	                wait();
	                log.info("被叫醒了");
	            } catch(InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	        return queue.poll();
		}
		
		//放入資料
		synchronized void put(EtlEventViewTable etlEventViewTable) {
			log.info("放入資料");
			queue.offer(etlEventViewTable);
			log.info("準備叫醒所有人");
			notifyAll();
			log.info("已叫醒所有人");
		}
		
		synchronized int size() {
			return queue.size();
		}
		
	}
	
	class Executor implements Runnable {
		private QueueTest queueTest;
		private EtlEventViewTableService etlEventViewTableService;
		private String name;
		private EtlEventViewTable etlEventViewTable;
		
		public Executor(QueueTest queueTest, EtlEventViewTableService etlEventViewTableService, String name){
			this.queueTest = queueTest;
			this.etlEventViewTableService = etlEventViewTableService;
			this.name = name;
		}

		@Override
		public void run() {
			try {
				log.info("先睡個30秒再開始工作");
				Thread.sleep(30000);
				log.info(name+ "已起床");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			while(true) {
				try {
					log.info(name + " : 目前剩餘" + queueTest.size() + "，記憶體位置" + queueTest);
					etlEventViewTable = queueTest.get();
					
					switch (etlEventViewTable.getModule()) {
					case "ImMovement":
						//依據載入的模組設定執行事件
						
						/**
						 * 調撥模組工作內容順序(後續要依據設定檔執行)
						 * 1.海關上傳
						 * 2.異動庫存
						 * 3.???(不知道)
						 */
						
						etlEventViewTable.setNumberOfExecutions(etlEventViewTable.getNumberOfExecutions()+1);
						etlEventViewTableService.update(etlEventViewTable);
						log.info(name+" : 更新成功，單號為"+etlEventViewTable.getOrderNo() + ",被執行次數" + etlEventViewTable.getNumberOfExecutions());
						
						break;
					
					case "SoSale":
						//依據載入的模組設定執行事件
						break;
					
					case "ImAdjustment":
						//依據載入的模組設定執行事件
						break;
					
						

					default:
						log.error("查無此模組設定");
						break;
					}
					log.info(name+"做完一件工作休息5秒");
					Thread.sleep(5000);
					log.info(name+"休息完畢開始工作");
				} catch (Exception e) {
					e.printStackTrace();
					log.error("更新狀態發生錯誤"+etlEventViewTable.getOrderNo() + "，原因為："+ e.toString());
				} finally {
					etlEventViewTable = null;
				}
			}
//			Runtime runtime = Runtime.getRuntime();
//			log.info("初始化測試" + ";記憶體總量 = " + runtime.totalMemory() + ";剩餘量 = " + runtime.freeMemory());
//			log.info(name+"執行完畢");
		}
		
	}
	
	
	//每1分鐘執行一次，將事件檢視簿中尚未處理的資料，狀態修改為處理中，並加入佇列清單中
	class Dispatcher implements Runnable {
		private QueueTest queueTest;
		private EtlEventViewTableService etlEventViewTableService;
		private String name;
		
		Dispatcher(QueueTest queueTest, EtlEventViewTableService etlEventViewTableService, String name){
			this.queueTest = queueTest;
			this.etlEventViewTableService = etlEventViewTableService;
			this.name = name;
		}

		@Override
		public void run() {
			while(true) {
				try {
					etlEventViewTableList = etlEventViewTableService.findUnprocessedEtlEventViewTable();
					for(EtlEventViewTable etlEventViewTable : etlEventViewTableList) {
						queueTest.put(etlEventViewTable);
					}
					log.info(name + "塞了" + etlEventViewTableList.size() + "筆資料進佇列，準備偷懶一分鐘在工作");
					Thread.sleep(60000);
				} catch (Exception e) {
					// TODO: handle exception
				} finally {
					// TODO: handle finally clause
				}
			}
		}
		
	}
	
	

}