package tw.com.tm.erp.utils;
import javax.crypto.*;

public class DES {
	private static String strDefaultKey = "Tasameng";    //預設的金鑰
    private StringBuffer objSb             = null;
    private Cipher objCipher             = null;
    private java.security.Key objKey    = null;
    private int intStringLength            = 0;
    private int intTemp                    = 0;
  //預設建構子
    public DES() throws Exception
    {        
        this(strDefaultKey);
    }

    //自訂密鑰
    public DES(String arg_strKey) throws Exception 
    {
        setKey(arg_strKey.getBytes());
        objCipher = Cipher.getInstance("DES");
    }

    //從指定的字串製成密鑰，密鑰所需的字元陣列長度為8位，不足及超過都要處理
     private void setKey(byte[] arg_strPrivateKey) throws Exception 
     {         
         byte[] arrTempByteArray = new byte[8];
         // 將原始字元陣列轉換為8位
         for (int i = 0; i < arg_strPrivateKey.length && i < arrTempByteArray.length; i++)
         {
             arrTempByteArray[i] = arg_strPrivateKey[i];
         }
         // 設定密鑰
         objKey = new javax.crypto.spec.SecretKeySpec(arrTempByteArray, "DES");    
     }
     
     //將byte陣列轉換16進制值的字串，如：byte[]{1,18}轉換為：0112     
    public String byte2Hex(byte[] arg_bteArray) throws Exception 
    {
        intStringLength = arg_bteArray.length;    
        objSb = new StringBuffer(intStringLength * 2);
        for (int i = 0; i < intStringLength; i++)
        {
            intTemp = (int)arg_bteArray[i];
            //負數需要轉成正數
            if(intTemp < 0) 
            {
                intTemp = intTemp + 256;
            }
            // 小於0F需要補0
            if (intTemp < 16)
            {
                objSb.append("0");
            }
            objSb.append(Integer.toString(intTemp, 16));
        }
        return objSb.toString();
     }

    
    //將16進制值的字串轉成byte陣列        
    public byte[] hex2Byte(String arg_strHexString) throws Exception 
    {
        byte[] arrByteDAta = arg_strHexString.getBytes();
        intStringLength = arrByteDAta.length;
        byte[] aryRetuenData = new byte[intStringLength / 2];
        for (int i = 0; i < intStringLength; i = i + 2)
        {
            aryRetuenData[i / 2] =  (byte)Integer.parseInt(new String(arrByteDAta, i, 2), 16);
        }
        return aryRetuenData;
    }

    //加密字串
     public byte[] doEncrypt(byte[] arg_bteArray) throws Exception
     {
         objCipher.init(Cipher.ENCRYPT_MODE, objKey);
         return objCipher.doFinal(arg_bteArray);
     }
     
     public String encrypt(String arg_strToEncriptString) throws Exception 
     {
         return byte2Hex(doEncrypt(arg_strToEncriptString.getBytes()));
     }
     
     public byte[] doDecrypt(byte[] arg_bteArray) throws Exception 
     {
        objCipher.init(Cipher.DECRYPT_MODE, objKey);     
        return objCipher.doFinal(arg_bteArray);
     }
     //解密字串
     public String decrypt(String arg_strToDecriptString) throws Exception 
     {
         return new String(doDecrypt(hex2Byte(arg_strToDecriptString)));
     }

     /**public static void main(String[] args) 
     {
         try 
         {
             String test = "SaM"+"@@"+(java.lang.String.valueOf(new java.util.Date().getTime()));
             //DES des = new DES("Sam");//自定義密鑰
             DES des = new DES();
             System.out.println("加密前的字符："+test);
             System.out.println("加密後的字符："+des.encrypt(test));
             System.out.println("解密後的字符："+des.decrypt(des.encrypt(test)));
             System.out.println("解密後的字符："+des.decrypt("8bc728365222e666f08c791bd585b03ceed423ce29743a82"));
             String tempRes=des.decrypt(des.encrypt(test));
             System.out.println(new java.util.Date((new java.lang.Long(tempRes.split("@@")[1]).longValue())));
             System.out.println(new java.util.Date().getTime());
             
         } 
         catch (Exception e)
         {
             
             e.printStackTrace();
         }
         
     }*/

     


}
