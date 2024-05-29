package message;
import java.io.*;
import java.lang.String.*;

public class send_order {

  public static void main(String[] args) throws Exception {

  try {
      String server  = "202.39.54.130"; //SMS Gateway IP
      int port	     = 8000;            //SMS Gateway Port

      if(args.length<4){
         System.out.println("Use: java send_order id passwd tel order_time message");
         System.out.println(" Ex: java send_order test test123 0910123xxx 061102153000 HiNet�w��²�T!");
         return;
      }
      String user    = args[0]; //�b��
      String passwd  = args[1]; //�K�X
      String tel     = args[2]; //������X
      String order_time = args[3]; //�w���ɶ�
      String message = new String(args[4].getBytes(),"big5"); //²�T���e

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

      //�ǰe�w����r²�T
      //�p�ݦP�ɶǰe�h��²�T�A�Цh���I�ssend_message()�Y�i�C
      ret_code=mysms.send_text_message(tel,message,order_time);
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

      System.out.println("I/O Exception : " + e);
   }
 }

}//end of class
