package tw.com.tm.erp.test;
import java.sql.*;
import java.text.SimpleDateFormat; 


public class DBConnect
{

    public DBConnect()
    {
    }

    public static Connection getERPConnection()
    {
        Connection conn = null;
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:KWEDB1", "ceap", "ceap");
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return conn;
    }

   
    public static void main(String args[])
    {
    	try{
        System.out.println("Query Start:"+java.lang.String.valueOf(new java.util.Date().getTime()));
        Statement stmt = getERPConnection().createStatement();
        //ResultSet rs = stmt.executeQuery("select buaddressb0_.ADDRESS_BOOK_ID as ADDRESS1_1_, buaddressb0_.ORGANIZATION_CODE as ORGANIZA2_1_, buaddressb0_.IDENTITY_CODE as IDENTITY3_1_, buaddressb0_.TYPE as TYPE1_, buaddressb0_.CHINESE_NAME as  CHINESE5_1_, buaddressb0_.ENGLISH_NAME as ENGLISH6_1_, buaddressb0_.SHORT_NAME as SHORT7_1_, buaddressb0_.COMPANY_NAME as COMPANY8_1_, buaddressb0_.DEPARTMENT as DEPARTMENT1_, buaddressb0_.COUNTRY_CODE as COUNTRY10_1_, buaddressb0_.GENDER as GENDER1_, buaddressb0_.BIRTHDAY_YEAR as BIRTHDAY12_1_, buaddressb0_.BIRTHDAY_MONTH as BIRTHDAY13_1_, buaddressb0_.BIRTHDAY_DAY as BIRTHDAY14_1_, buaddressb0_.E_MAIL as E15_1_, buaddressb0_.URL as URL1_, buaddressb0_.CAPITALIZATION as CAPITAL17_1_, buaddressb0_.INCOME as INCOME1_, buaddressb0_.EMPLOYEES as EMPLOYEES1_, buaddressb0_.INDUSTRY_CODE as INDUSTRY20_1_, buaddressb0_.CITY as CITY1_, buaddressb0_.AREA as AREA1_, buaddressb0_.ZIP_CODE as ZIP23_1_, buaddressb0_.ADDRESS as ADDRESS1_, buaddressb0_.ROUTE_CODE as ROUTE25_1_, buaddressb0_.CONTRACT_PERSON as CONTRACT26_1_, buaddressb0_.TEL1 as TEL27_1_, buaddressb0_.TEL2 as TEL28_1_, buaddressb0_.FAX1 as FAX29_1_, buaddressb0_.FAX2 as FAX30_1_, buaddressb0_.MOBILE_PHONE as MOBILE31_1_, buaddressb0_.REMARK1 as REMARK32_1_, buaddressb0_.REMARK2 as REMARK33_1_, buaddressb0_.REMARK3 as REMARK34_1_, buaddressb0_.CREATED_BY as CREATED35_1_, buaddressb0_.CREATION_DATE as CREATION36_1_, buaddressb0_.LAST_UPDATED_BY as LAST37_1_, buaddressb0_.LAST_UPDATE_DATE as LAST38_1_ from ERP.BU_ADDRESS_BOOK buaddressb0_ where buaddressb0_.ADDRESS_BOOK_ID is not null");
        ResultSet rs = stmt.executeQuery("select * from ERP.SO_SALES_ORDER_ITEM");
        while(rs.next()) {     
			
			String SEQ_NO = String.valueOf(rs.getInt(2));
			String SDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(rs.getTimestamp(3));
			
			System.out.println("SEQ_NO: " + SEQ_NO);
			System.out.println("SDate: "+SDate);
        }
        System.out.println("Query end:"+java.lang.String.valueOf(new java.util.Date().getTime()));
    	}
    	catch(Exception e){System.out.println(e);}

    }

}