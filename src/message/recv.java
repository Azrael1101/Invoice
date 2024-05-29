package message;
import java.io.*;
import java.lang.String.*;

public class recv {

  public static void main(String[] args) throws Exception {

  try {
      String server  = "202.39.54.130"; //SMS Gateway IP
      int port	     = 8000;            //SMS Gateway Port

      if(args.length<2){
         System.out.println("Use: java recv id passwd");
         System.out.println(" Ex: java recv test test123");
         return;
      }
      String user    = args[0]; //�b��
      String passwd  = args[1]; //�K�X

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

      //�ǰe��r²�T
      //�p�ݦP�ɱ����h��²�T�A�Цh���I�srecv_text_message()�Y�i�C
      ret_code=mysms.recv_text_message();
      if( ret_code == 0 ) {
           System.out.println("�t�Φ�����^�Ǫ���ơA��Ʀp�U:");
           System.out.println("message="+mysms.get_message()); //���oMessageID
      }else if(ret_code == 1){
           System.out.println("�t�εL����^�Ǫ����!");
      }else {
           System.out.println("����²�T����!");
           System.out.print("ret_code="+ret_code+",");
           System.out.println("ret_content="+mysms.get_message());//���o���~���T��
      }

      //�����s�u
      mysms.close_conn();

  }catch (Exception e)  {

      System.out.println("I/O Exception : " + e);
   }
 }

}//end of class
