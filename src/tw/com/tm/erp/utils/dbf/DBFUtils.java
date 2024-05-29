package tw.com.tm.erp.utils.dbf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.linuxense.javadbf.DBFException;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
//import com.linuxense.javadbf.DBFWriter;

public class DBFUtils {
	private static final Log log = LogFactory.getLog(DBFUtils.class);

	public static DBFField[] getDBFField(String fileName) throws DBFException, FileNotFoundException {
		log.info("DBFUtils.getDBFField fileName=" + fileName);
		DBFReader dbfreader = new DBFReader(new FileInputStream(new File(fileName)));
		int fieldCount = dbfreader.getFieldCount();
		DBFField ajdbfield[] = new DBFField[fieldCount];
		for (int index = 0; index < fieldCount; index++) {
			ajdbfield[index] = dbfreader.getField(index);
			System.out.println(index + " name:" + ajdbfield[index].getName() + ",type:" + ajdbfield[index].getDataType() + ",length:"
					+ ajdbfield[index].getFieldLength());
		}
		return ajdbfield;
	}

	public static List<Object[]> readDBF(String fileName) throws DBFException, FileNotFoundException {
		log.info("DBFUtils.readDBF fileName=" + fileName);
		InputStream inputStream = new FileInputStream(new File(fileName));
		try{
		    List<Object[]> records = new ArrayList();
		    DBFReader dbfreader = new DBFReader(inputStream);
		    dbfreader.setCharactersetName("Big5");
		    int recordCount = dbfreader.getRecordCount();
		    for (int index = 0; index < recordCount; index++) {
	                records.add(dbfreader.nextRecord());
		    }
		    return records;
		}finally{
		    try{
		        inputStream.close();
		    }catch(IOException ioe){
			log.error("close stream fail!" );
		    }
		}
	}
	
	public static List<Object[]> readAndDelDBF(String fileName) throws DBFException, FileNotFoundException {
		log.info("DBFUtils.readDBF fileName=" + fileName);
		File newFile = new File(fileName);
		List<Object[]> records = new ArrayList();
		DBFReader dbfreader = new DBFReader(new FileInputStream(newFile));
		int recordCount = dbfreader.getRecordCount();
		for (int index = 0; index < recordCount; index++) {
			records.add(dbfreader.nextRecord());
		}
		newFile.deleteOnExit();
		return records;
	}	

	/**
	 * 如果FIELD SIZE 有問題可以使用這個METHOD
	 * 
	 * @param fileName
	 * @param fieldLength
	 * @return
	 * @throws JDBFException 
	 * @throws JDBFException 
	 * @throws tw.com.tm.erp.utils.dbf.JDBFException 
	 * @throws IOException
	 * @throws JDBFException
	 */
	/*
	 * public static List<Object[]> readDBF(String fileName,int fieldLength[])
	 * throws JDBFException { List<Object[]> records = new ArrayList();
	 * DBFReader dbfreader = new DBFReader(fileName); while
	 * (dbfreader.hasNextRecord()) {
	 * records.add(dbfreader.nextRecord(fieldLength)); } return records; }
	 */
	/*
	public static void writeDBF(String fieldName, List<Object[]> records, DBFField ajdbfield[]) throws IOException {
		log.info("DBFUtils.getDBFField writeDBF=" + fieldName);
		File f = new File(fieldName);
		File pf = f.getParentFile();
		if (!pf.exists())
			pf.mkdirs();

		DBFWriter dbfwriter = new DBFWriter();
		dbfwriter.setCharactersetName("big5");
		dbfwriter.setFields(ajdbfield);
		for (Object[] record : records) {
			dbfwriter.addRecord(record);
		}
		FileOutputStream fos = new FileOutputStream(f);
		dbfwriter.write(fos);
		if (fos != null)
			fos.close();
	}
	*/
	
	public static JDBField[] getJDBField(String fileName) throws DBFException, FileNotFoundException, JDBFException {
		log.info("DBFUtils.getDBFField fileName=" + fileName);
		JDBFReader dbfreader = new JDBFReader(new FileInputStream(new File(fileName)));
		int fieldCount = dbfreader.getFieldCount();
		JDBField ajdbfield[] = new JDBField[fieldCount];
		for (int index = 0; index < fieldCount; index++) {
			ajdbfield[index] = dbfreader.getField(index);
			System.out.println(index + " name:" + ajdbfield[index].getName() + ",type:" + ajdbfield[index].getType() + ",length:"
					+ ajdbfield[index].getLength());
		}
		return ajdbfield;
	}
	
	public static void writeDBF(String fieldName, List<Object[]> records, JDBField ajdbfield[]) throws JDBFException{
	    File f = new File(fieldName);
	    File pf = f.getParentFile();
	    if (!pf.exists())
	      pf.mkdirs();	    
		JDBFWriter dbfwriter = new JDBFWriter(fieldName, ajdbfield);
		for (Object[] record : records) {
			dbfwriter.addRecord(record);
		}
		dbfwriter.close();
		f = null ;
		pf = null ;
	}	
}
