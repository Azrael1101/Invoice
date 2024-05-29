package message;
import java.io.*;
import java.lang.String.*;

public class query {

  public static void main(String[] args) throws Exception {

  try {
      String server  = "202.39.54.130"; //Socket to Air Gateway IP
      int port	     = 8000;            //Socket to Air Gateway Port

      if(args.length<3){
         System.out.println("Use: java query id passwd messageid");
         System.out.println(" Ex: java query test test123 A2967053126752930574");
         return;
      }
      String user    = args[0]; //�b��
      String passwd  = args[1]; //�K�X
      String messageid = args[2];//�T��ID
      int    query_type = 2; //query ��r²�T
      
      //----�إ߳s�u and �ˬd�b���K�X�O�_���~
      sms2 mysms = new sms2();
      int ret_code = mysms.create_conn(server,port,user,passwd) ;
      if( ret_code == 0 ) {
           System.out.println("�b���K�XLogin OK!");
      } else {
      	   System.out.println("�b���K�XLogin Fail!");
           System.out.println("ret_code="+ret_code + ",ret_content=" + mysms.get_message());
           //�����s�u
           mysms.close_conn();
           return ;
      }

      //�d�߶ǰe���G
      //�p�ݦP�ɬd�ߦh��²�T�A�Цh���I�squery_message()�Y�i�C
      ret_code=mysms.query_message(query_type,messageid);
      if( ret_code == 0 ) {
           System.out.println("²�T�w�e�챵���ݤ��!");
           System.out.println("MessageID="+mysms.get_message()); //���oMessageID
      } else {
           System.out.println("²�T�|���ǰe�챵���ݤ��!");
           System.out.print("ret_code="+ret_code+",");
           System.out.println("ret_content="+mysms.get_message());//���o���~���T��
           //�����s�u
           mysms.close_conn();
           return ;
      }

      //�����s�u
      mysms.close_conn();

  }catch (Exception e)  {

      System.out.println("I/O Exception : " + e);
   }
 }

}//end of class
