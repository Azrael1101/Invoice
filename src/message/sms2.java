package message;
import java.io.*;
import java.net.*;
import java.lang.String.*;
import java.util.StringTokenizer;

public class sms2 {

private Socket sock ;
private DataInputStream  din ;
private DataOutputStream dout ;
private String ret_message = "" ;
private String ret_msisdn = "" ;


  public sms2() {} ;

  // �Q���i����Q�i��
  public int HexToDec(String input){
     int sum=0;
     for (int i=0;i<input.length() ;i++ ){
        if (input.charAt(i)>='0' && input.charAt(i)<='9')
           sum=sum*16+input.charAt(i)-48;
        else if (input.charAt(i)>='A' && input.charAt(i)<='F')
           sum=sum*16+input.charAt(i)-55;
     }
     return sum;
  }


  //�إ�Socket�s�u�A�ð��b���K�X�ˬd
  public int create_conn(String host, int port, String user, String passwd) {

    //---�]�w�e�X�T���T����buffer
    byte out_buffer[] = new byte[266]; //�ǰe���׬�266

    //---�]�w�����T����buffer
    byte ret_code = 99;
    byte ret_coding = 0;
    byte ret_set_len = 0;
    byte ret_content_len = 0;
    byte ret_set[] = new byte[80];
    byte ret_content[] = new byte[160];

     try {
         //---�� socket
         this.sock = new Socket(host , port);

         this.din  = new DataInputStream(this.sock.getInputStream());
         this.dout = new DataOutputStream(this.sock.getOutputStream());

        //---�}�l�b���K�X�ˬd
        int i;
        //----�M�� buffer
        for( i=0 ; i < 266 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 80 ; i++ ) ret_set[i] = 0 ;
        for( i=0 ; i < 160 ; i++ ) ret_content[i] = 0 ;

        //---�]�w�b���P�K�X
        String acc_pwd_str = user.trim() + "\0" + passwd.trim() + "\0" ;
        byte   acc_pwd_byte[] = acc_pwd_str.getBytes();
        byte   acc_pwd_size = (byte)acc_pwd_byte.length ;
 
        out_buffer[0] = 0; //�ˬd�K�X
        out_buffer[1] = 1; //big�s�X
        out_buffer[2] = 0; //priority
        out_buffer[3] = 0; //��X 0:�x�W
        out_buffer[4] = acc_pwd_size ; //msg_set_len
        out_buffer[5] = 0; //msg_content_len, ���ұK�X�ɤ���msg_content
        //�]�wmsg_set ���e "�b��"+"�K�X"
        for( i = 0; i < acc_pwd_size ; i++ )
              out_buffer[i + 6] = acc_pwd_byte[i] ;
        
        //----�e�X�T��
        //this.dout.write(out_buffer , 0 , acc_pwd_size + 3 );
        this.dout.write(out_buffer );
        //---Ū return code
        ret_code = this.din.readByte();
	      ret_coding = this.din.readByte();
        ret_set_len = this.din.readByte();
        ret_content_len = this.din.readByte();
        
        //---Ū return message
        this.din.read(ret_set,0,80);
        this.din.read(ret_content,0,160);
        this.ret_message = new String(ret_content);
        return ret_code ;

     } catch( UnknownHostException e) {
          this.ret_message = "Cannot find the host!";
          return 70 ;
     } catch( IOException ex) {
          this.ret_message = "Socket Error: " + ex.getMessage();
          return 71 ;
     }

  }//end of function

  //����Socket�s�u
  public void close_conn() {
     try {
         if( this.din  != null) this.din.close();
         if( this.dout != null) this.dout.close();
         if( this.sock != null) this.sock.close();

         this.din = null ;
         this.dout = null;
         this.sock = null ;

     } catch( UnknownHostException e) {
          this.ret_message = "Cannot find the host!";
     } catch( IOException ex) {
          this.ret_message = "Socket Error: " + ex.getMessage();
     }

  }//end of function


  //�ǰe��r²�T (�Y�ɶǰe)
  public int send_text_message( String sms_tel, String message) {

    //---�]�w�e�X�T���T����buffer
    byte out_buffer[] = new byte[266]; //�ǰe���׬�266

    //----�]�w������buffer
    byte ret_code = 99;
    byte ret_coding = 0;
    byte ret_set_len = 0;
    byte ret_content_len = 0;
    byte ret_set[] = new byte[80];
    byte ret_content[] = new byte[160];

    try {
        int i ;
        //----�M�� buffer
        for( i=0 ; i < 266 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 80 ; i++ ) ret_set[i] = 0 ;
        for( i=0 ; i < 160 ; i++ ) ret_content[i] = 0 ;
      
        //---�]�w�ǰe�T�������e 01:�Y�ɶǰe
        String msg_set = sms_tel.trim() + "\0" + "01" + "\0" ;
        byte msg_set_byte[] = msg_set.getBytes();
        int msg_set_size = msg_set_byte.length ;

        String msg_content = message.trim() + "\0" ;
        byte msg_content_byte[] = msg_content.getBytes("Big5"); //�ݫ��w��X��Big5�A���M�|�L�X??
        int msg_content_size = msg_content_byte.length - 1 ; //send_type=1��,���פ��]�t'\0'

      	if(msg_set_size > 80){
                this.ret_message = "msg_set > max limit!";
                return 80 ;
      	}
      	if(msg_content_size > 159){
                this.ret_message = "msg_content > max limit!";
                return 81 ;
      	}
	
        //---�]�w�e�X�T���� buffer
        if(sms_tel.startsWith("+"))
           out_buffer[0] = 15; //send text ���²�T
        else
           out_buffer[0] = 1; //send text �ꤺ²�T
        out_buffer[1] = 1; //big5�s�X
        out_buffer[2] = 0; //priority
        out_buffer[3] = 0; //��X 0:�x�W
        out_buffer[4] = (byte)msg_set_size ; //msg_set_len
        out_buffer[5] = (byte)msg_content_size; //msg_content_len

        //�]�wmsg_set ���e "������X"+"�ǰe�Φ�"
        for( i = 0; i < msg_set_size ; i++ )
              out_buffer[i+6] = msg_set_byte[i] ;

        //�]�wmsg_content ���e "�T�����e"
        for( i = 0; i < msg_content_size ; i++ )
              out_buffer[i+106] = msg_content_byte[i] ;

        //----�e�X�T��
        this.dout.write(out_buffer);

        //---Ū return code
        ret_code = this.din.readByte();
  	    ret_coding = this.din.readByte();
        ret_set_len = this.din.readByte();
        ret_content_len = this.din.readByte();
        
        //---Ū return message
        this.din.read(ret_set,0,80);
        this.din.read(ret_content,0,160);
        this.ret_message = new String(ret_content);
        this.ret_message = this.ret_message.trim();
        return ret_code ;

    } catch( UnknownHostException eu) {
         this.ret_message = "Cannot find the host!";
         return 70 ;
    } catch( IOException ex) {
         this.ret_message = " Socket Error: " + ex.getMessage();
         return 71 ;
    }
  }//end of function


  //�ǰe��r²�T (�w���ǰe)
  public int send_text_message( String sms_tel, String message, String order_time) {

    //---�]�w�e�X�T���T����buffer
    byte out_buffer[] = new byte[266]; //�ǰe���׬�266

    //----�]�w������buffer
    byte ret_code = 99;
    byte ret_coding = 0;
    byte ret_set_len = 0;
    byte ret_content_len = 0;
    byte ret_set[] = new byte[80];
    byte ret_content[] = new byte[160];

    try {
        int i ;
        //----�M�� buffer
        for( i=0 ; i < 266 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 80 ; i++ ) ret_set[i] = 0 ;
        for( i=0 ; i < 160 ; i++ ) ret_content[i] = 0 ;
      
        //---�]�w�ǰe�T�������e 03:�w���ǰe
        String msg_set = sms_tel.trim() + "\0" + "03" + "\0" + order_time.trim();
        byte msg_set_byte[] = msg_set.getBytes();
        int msg_set_size = msg_set_byte.length ;

        String msg_content = message.trim() + "\0" ;
        byte msg_content_byte[] = msg_content.getBytes("Big5"); //�ݫ��w��X��Big5�A���M�|�L�X??
        int msg_content_size = msg_content_byte.length - 1 ; //send_type=1��,���פ��]�t'\0'

        if(msg_set_size > 80){
                 this.ret_message = "msg_set > max limit!";
                 return 80 ;
        }
        if(msg_content_size > 159){
                 this.ret_message = "msg_content > max limit!";
                 return 81 ;
        }
	
        //---�]�w�e�X�T���� buffer
        if(sms_tel.startsWith("+"))
           out_buffer[0] = 15; //send text ���²�T
        else
           out_buffer[0] = 1; //send text �ꤺ²�T
        out_buffer[1] = 1; //big5�s�X
        out_buffer[2] = 0; //priority
        out_buffer[3] = 0; //��X 0:�x�W
        out_buffer[4] = (byte)msg_set_size ; //msg_set_len
        out_buffer[5] = (byte)msg_content_size; //msg_content_len

        //�]�wmsg_set ���e "������X"+"�ǰe�Φ�"+"�w���ɶ�"
        for( i = 0; i < msg_set_size ; i++ )
              out_buffer[i+6] = msg_set_byte[i] ;

        //�]�wmsg_content ���e "�T�����e"
        for( i = 0; i < msg_content_size ; i++ )
              out_buffer[i+106] = msg_content_byte[i] ;

        //----�e�X�T��
        this.dout.write(out_buffer);

        //---Ū return code
        ret_code = this.din.readByte();
	      ret_coding = this.din.readByte();
        ret_set_len = this.din.readByte();
        ret_content_len = this.din.readByte();
        
        //---Ū return message
        this.din.read(ret_set,0,80);
        this.din.read(ret_content,0,160);
        this.ret_message = new String(ret_content);
        this.ret_message = this.ret_message.trim();
        return ret_code ;

    } catch( UnknownHostException eu) {
         this.ret_message = "Cannot find the host!";
         return 70 ;
    } catch( IOException ex) {
         this.ret_message = " Socket Error: " + ex.getMessage();
         return 71 ;
    }
  }//end of function


  //�d�ߤ�r²�T���ǰe���G
  //type -> 2:text ,6:logo, 8:ringtone, 10:picmsg, 14:wappush
  public int query_message(int type, String messageid) {

    //---�]�w�e�X�T����buffer
    byte out_buffer[] = new byte[266]; //�ǰe���׬�266
    //----�]�w������buffer
    byte ret_code = 99;
    byte ret_coding = 0;
    byte ret_set_len = 0;
    byte ret_content_len = 0;
    byte ret_set[] = new byte[80];
    byte ret_content[] = new byte[160];

    try {
        int i ;
        //----�M�� buffer
        for( i=0 ; i < 266 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 80 ; i++ ) ret_set[i] = 0 ;
        for( i=0 ; i < 160 ; i++ ) ret_content[i] = 0 ;
        
        //---�]�wmessage id
        String msg_set = messageid.trim() + "\0";
        byte msg_set_byte[] = msg_set.getBytes();
        int msg_set_size = msg_set_byte.length ;

        if(msg_set_size > 80){
                 this.ret_message = "msg_set > max limit!";
                 return 80 ;
        }

        //---�]�w�e�X�T���� buffer
        out_buffer[0] = (byte)type; //query type  02:text ,06:logo, 08 ringtone, 10:picmsg, 14:wappush
        out_buffer[1] = 1; //big5�s�X
        out_buffer[2] = 0; //priority
        out_buffer[3] = 0; //��X 0:�x�W
        out_buffer[4] = (byte)msg_set_size ; //msg_set_len
        out_buffer[5] = 0;  //msg_content_len

        //�]�wmessageid
        for( i = 0; i < msg_set_size ; i++ )
              out_buffer[i+6] = msg_set_byte[i] ;

        //----�e�X�T��
        this.dout.write(out_buffer);

        //---Ū return code
        ret_code = this.din.readByte();
	      ret_coding = this.din.readByte();
        ret_set_len = this.din.readByte();
        ret_content_len = this.din.readByte();
        
        //---Ū return message
        this.din.read(ret_set,0,80);
        this.din.read(ret_content,0,160);
        this.ret_message = new String(ret_content);
        this.ret_message = this.ret_message.trim();
        return ret_code ;

    } catch( UnknownHostException eu) {
         this.ret_message = "Cannot find the host!";
         return 70 ;
    } catch( IOException ex) {
         this.ret_message = " Socket Error: " + ex.getMessage();
         return 71 ;
    }
  }//end of function


  //������r²�T
  public int recv_text_message() {

    //---�]�w�e�X�T���T����buffer
    byte out_buffer[] = new byte[266]; //�ǰe���׬�266

    //----�]�w������buffer
    byte ret_code = 99;
    byte ret_coding = 0;
    byte ret_set_len = 0;
    byte ret_content_len = 0;
    byte ret_set[] = new byte[80];
    byte ret_content[] = new byte[160];

    try {
        int i ;
        //----�M�� buffer
        for( i=0 ; i < 266 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 80 ; i++ ) ret_set[i] = 0 ;
        for( i=0 ; i < 160 ; i++ ) ret_content[i] = 0 ;
      
        //---�]�w�e�X�T���� buffer
        out_buffer[0] = 3; //recv text message
        out_buffer[1] = 1; //big5�s�X
        out_buffer[2] = 0; //priority
        out_buffer[3] = 0; //��X 0:�x�W
        out_buffer[4] = 0; //msg_set_len
        out_buffer[5] = 0; //msg_content_len

        //----�e�X�T��
        this.dout.write(out_buffer);

        //---Ū return code
        ret_code = this.din.readByte();
        ret_coding = this.din.readByte();
        ret_set_len = this.din.readByte();
        ret_content_len = this.din.readByte();

        //---Ū return message
        this.din.read(ret_set,0,80);
        this.din.read(ret_content,0,160);
        this.ret_message = new String(ret_content,"big5");
        this.ret_message = this.ret_message.trim();

        this.ret_msisdn="";
        //ret_code==0 ��ܦ���ơA�h���X�ǰe�ݪ�������X
        if(ret_code==0){
           String ret_set_msg = new String(ret_set);
           //�Nstring��'\0'���}�A
           StringTokenizer tok = new StringTokenizer(ret_set_msg,"\0");
           if(tok.hasMoreTokens()){
              this.ret_msisdn=tok.nextToken();
           }
        }

        return ret_code ;

    } catch( UnknownHostException eu) {
         this.ret_message = "Cannot find the host!";
         return 70 ;
    } catch( IOException ex) {
         this.ret_message = " Socket Error: " + ex.getMessage();
         return 71 ;
    }
  }//end of function


  //�����w����r²�T
  public int cancel_text_message(String messageid) {

    //---�]�w�e�X�T����buffer
    byte out_buffer[] = new byte[266]; //�ǰe���׬�266
    //----�]�w������buffer
    byte ret_code = 99;
    byte ret_coding = 0;
    byte ret_set_len = 0;
    byte ret_content_len = 0;
    byte ret_set[] = new byte[80];
    byte ret_content[] = new byte[160];

    try {
        int i ;
        //----�M�� buffer
        for( i=0 ; i < 266 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 80 ; i++ ) ret_set[i] = 0 ;
        for( i=0 ; i < 160 ; i++ ) ret_content[i] = 0 ;
        
        //---�]�wmessage id
        String msg_set = messageid.trim() + "\0";
        byte msg_set_byte[] = msg_set.getBytes();
        int msg_set_size = msg_set_byte.length ;

        if(msg_set_size > 80){
                 this.ret_message = "msg_set > max limit!";
                 return 80 ;
        }

        //---�]�w�e�X�T���� buffer
        out_buffer[0] = 16; //�����w��²�T
        out_buffer[1] = 1; //big5�s�X
        out_buffer[2] = 0; //priority
        out_buffer[3] = 0; //��X 0:�x�W
        out_buffer[4] = (byte)msg_set_size ; //msg_set_len
        out_buffer[5] = 0;  //msg_content_len

        //�]�wmessageid
        for( i = 0; i < msg_set_size ; i++ )
              out_buffer[i+6] = msg_set_byte[i] ;

        //----�e�X�T��
        this.dout.write(out_buffer);

        //---Ū return code
        ret_code = this.din.readByte();
	      ret_coding = this.din.readByte();
        ret_set_len = this.din.readByte();
        ret_content_len = this.din.readByte();
        
        //---Ū return message
        this.din.read(ret_set,0,80);
        this.din.read(ret_content,0,160);
        this.ret_message = new String(ret_content);
        this.ret_message = this.ret_message.trim();
        return ret_code ;

    } catch( UnknownHostException eu) {
         this.ret_message = "Cannot find the host!";
         return 70 ;
    } catch( IOException ex) {
         this.ret_message = " Socket Error: " + ex.getMessage();
         return 71 ;
    }
  }//end of function


  //�ǰewappush
  public int send_wappush_message( String sms_tel, String sms_url, String message) {

    //---�]�w�e�X�T���T����buffer
    byte out_buffer[] = new byte[266]; //�ǰe���׬�266

    //----�]�w������buffer
    byte ret_code = 99;
    byte ret_coding = 0;
    byte ret_set_len = 0;
    byte ret_content_len = 0;
    byte ret_set[] = new byte[80];
    byte ret_content[] = new byte[160];

    try {
        int i ;
        //----�M�� buffer
        for( i=0 ; i < 266 ; i++ ) out_buffer[i] = 0 ;
        for( i=0 ; i < 80 ; i++ ) ret_set[i] = 0 ;
        for( i=0 ; i < 160 ; i++ ) ret_content[i] = 0 ;
      
        //---�]�w�ǰe�T�������e 01:SI
        String msg_set = sms_tel.trim() + "\0" + "01" + "\0" ;
        byte msg_set_byte[] = msg_set.getBytes();
        int msg_set_size = msg_set_byte.length ;

        String msg_content = sms_url.trim() + "\0" + message.trim() + "\0" ;
        byte msg_content_byte[] = msg_content.getBytes("Big5"); //�ݫ��w��X��Big5�A���M�|�L�X??
        int msg_content_size = msg_content_byte.length ;

        //---�]�w�e�X�T���� buffer
        out_buffer[0] = 13; //send wappush
        out_buffer[1] = 1; //big�s�X
        out_buffer[2] = 0; //priority
        out_buffer[3] = 0; //��X 0:�x�W
        out_buffer[4] = (byte)msg_set_size ; //msg_set_len
        out_buffer[5] = (byte)msg_content_size; //msg_content_len

        //�]�wmsg_set ���e "������X"+"�ǰe�Φ�"
        for( i = 0; i < msg_set_size ; i++ )
              out_buffer[i+6] = msg_set_byte[i] ;

        //�]�wmsg_content ���e "url"+"�T�����e"
        for( i = 0; i < msg_content_size ; i++ )
              out_buffer[i+106] = msg_content_byte[i] ;

        //----�e�X�T��
        this.dout.write(out_buffer);

        //---Ū return code
        ret_code = this.din.readByte();
        ret_coding = this.din.readByte();
        ret_set_len = this.din.readByte();
        ret_content_len = this.din.readByte();
        
        //---Ū return message
        this.din.read(ret_set,0,80);
        this.din.read(ret_content,0,160);
        this.ret_message = new String(ret_content);
        this.ret_message = this.ret_message.trim();
        return ret_code ;

    } catch( UnknownHostException eu) {
         System.out.println(" Cannot find the host ");
         return 70 ;
    } catch( IOException ex) {
         System.out.println(" Socket Error: " + ex.getMessage());
         return 71 ;
    }
  }//end of function


  public String get_message() {

     return ret_message;
  }


  public String get_msisdn() {

     return ret_msisdn;
  }

  //�D�禡 - �ϥΤ�r²�T�d��
  public static void main(String[] args) throws Exception {

  try {
      String server  = "202.39.54.130"; //hiAirV2 Gateway IP
      int port	     = 8000;            //Socket to Air Gateway Port

      if(args.length<4){
         System.out.println("Use: java sms2 id passwd tel message");
         System.out.println(" Ex: java sms2 test test123 0910123xxx HiNet²�T!");
         return;
      }
      String user    = args[0]; //�b��
      String passwd  = args[1]; //�K�X
      String tel     = args[2]; //������X
      String message = new String(args[3].getBytes(),"big5"); //²�T���e

      //----�إ߳s�u and �ˬd�b���K�X�O�_���~
      sms2 mysms = new sms2();
      int k = mysms.create_conn(server,port,user,passwd) ;
      if( k == 0 ) {
           System.out.println("�b���K�Xcheck ok!");
      } else {
           System.out.println(mysms.get_message());
           //�����s�u
           mysms.close_conn();
           return ;
      }

      k=mysms.send_text_message(tel,message);
      if( k == 0 ) {
           System.out.println("²�T�w�e��²�T����!");
           System.out.println("MessageID="+mysms.get_message());
      } else {
           System.out.println("²�T�ǰe�o�Ϳ��~!");
           System.out.print("ret_code="+k+",");
           System.out.println("ret_content="+mysms.get_message());
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
