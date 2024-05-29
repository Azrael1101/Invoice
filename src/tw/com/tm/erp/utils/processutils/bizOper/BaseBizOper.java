/*
 * Created by cEAP BPM Business Object Creator Code Generator
 */
package tw.com.tm.erp.utils.processutils.bizOper;

import javax.naming.InitialContext;
import javax.naming.NamingException;



public abstract class BaseBizOper 
{
    

    public static Object JNDILookup(String aLookupName) throws NamingException
    {
        InitialContext context = null;

        try {
            context = new InitialContext();

            Object ref = context.lookup(aLookupName);

            return ref;
        }
        finally {
            if (context != null) {
                context.close();
            }
        }
    }
    
}
