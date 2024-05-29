package tw.com.tm.erp.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;

public class BeanUtil {
	
	public static String ChangeType[] = {"java.lang.String","java.lang.Long","java.lang.Double"};
	
	/**
	 * 清除字串空白,Long 值為0的資料
	 * @param obj
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws ClassNotFoundException
	 */
    @SuppressWarnings(value = "unchecked")
    public static void changeSpace2Null(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, ClassNotFoundException{
        //取得所有field
        Field[] fields = obj.getClass().getDeclaredFields();
        for( Field field : fields ){
            //取得field name
            String fieldName = field.getName();
            String getMethodName = "get" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
            String setMethodName = "set" + fieldName.substring(0,1).toUpperCase() + fieldName.substring(1);
            for( int typeIndex = 0 ; typeIndex < ChangeType.length ; typeIndex++){
                System.out.println(fieldName + " type " + field.getType().getName() );
                if( ChangeType[typeIndex].equals(field.getType().getName()) ){
                    // 取得 getMethod method
                    Method getMethod = obj.getClass().getMethod(getMethodName, new Class[]{} );
                    //觸發 getMethod method
                    Object get = getMethod.invoke(obj);
                    switch (typeIndex){
                        case 0 : //String
                            if( "".equals(get) ){
                                // 取得 setMethod method
                                Method setMethod = obj.getClass().getMethod(setMethodName, Class.forName(ChangeType[0]) );
                                Object values[] = new Object[1];
                                values[0] = null;
                                //觸發 setMethod method
                                setMethod.invoke(obj, values);
                            }
                        break;
                        case 1 : //Long
                            if( (new Long(0)).equals(get) ){
                                // 取得 setMethod method
                                Method setMethod = obj.getClass().getMethod(setMethodName, Class.forName(ChangeType[1]) );
                                Object values[] = new Object[1];
                                values[0] = null;
                                //觸發 setMethod method
                                setMethod.invoke(obj, values);
                            }
                        break ;
                        case 2 : //Double
                            if( (new Double(0)).equals(get) ){
                                // 取得 setMethod method
                                Method setMethod = obj.getClass().getMethod(setMethodName, Class.forName(ChangeType[2]) );
                                Object values[] = new Object[1];
                                values[0] = null;
                                //觸發 setMethod method
                                setMethod.invoke(obj, values);
                            }
                        break ;                        
                    }
                }
            }           
        }
    }	


	/**
	 * 先從Map裡面找出有沒有valueName對應的參數，沒有就用valueName跟objHead取值 
	 */
    public static Object getBeanValue(Map storageMap, Object objHead, String valueName) throws Exception {
    	Object value = null;
    	if(null != storageMap && null != storageMap.get(valueName)){
    		try{
    			value = PropertyUtils.getNestedProperty(objHead, (String)storageMap.get(valueName));
    		}catch(Exception e){
    			value = storageMap.get(valueName);
    		}
    	}else{
    		try{
    			value = PropertyUtils.getNestedProperty(objHead, valueName);
    		}catch(Exception ex){
    			throw new Exception("查無 " + valueName + " 於對應值於參數/物件中");
    		}
    	}
    	return value;
    }
}
