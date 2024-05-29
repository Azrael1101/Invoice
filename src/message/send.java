package message;
import java.io.*;
import java.lang.String.*;

public class send {

  public static void main(String[] args) throws Exception {

  try {
//	  args[0] = new String("75119469");
//	  args[1] = new String("0c2bcb54");
//	  args[2] = new String("0938767538");
//	  args[3] = new String("cow be");
      String server  = "202.39.54.130"; //SMS Gateway IP
      int port	     = 8000;            //SMS Gateway Port

//      if(args.length<4){
//         System.out.println("Use: java send id passwd tel message");
//         System.out.println(" Ex: java send test test123 0910123xxx HiNet²�T!");
//         return;
//      }
      String user    = "75119469";//args[0]; 
      String passwd  = "0c2bcb54";//args[1]; 
      String tel     = "0936111002";//args[2]; 
      String message = new String("嗨 URL~  http://60.250.137.187:58083/erp_dev_20210702/itemLogin.jsp  by Luke".getBytes(),"UTF8"); 

      
      sms2 mysms = new sms2();
      int ret_code = mysms.create_conn(server,port,user,passwd) ;
      if( ret_code == 0 ) {
           System.out.println("Login OK!");
      } else {
      	   System.out.println("XLogin Fail!");
           System.out.println("ret_code="+ret_code + ",ret_content=" + mysms.get_message());
           
           mysms.close_conn();
           return ;
      }

      //�ǰe��r²�T
      //�p�ݦP�ɶǰe�h��²�T�A�Цh���I�ssend_message()�Y�i�C
      ret_code=mysms.send_text_message(tel,message);
      if( ret_code == 0 ) {
           System.out.println("²�T�w�e��²�T����!");
           System.out.println("MessageID="+mysms.get_message()); //���oMessageID
      } else {
           System.out.println("²�T�ǰe�o�Ϳ��~!");
           System.out.print("ret_code="+ret_code+",");
           System.out.println("ret_content="+mysms.get_message());//���o���~���T��
           //�����s�u
           mysms.close_conn();
           return ;
      }

      //�����s�u
      mysms.close_conn();

  }catch (Exception e)  {
	  e.printStackTrace();
      System.out.println("I/O Exception : " + e);
   }
 }

}//end of class
