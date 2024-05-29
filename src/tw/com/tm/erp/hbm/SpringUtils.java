package tw.com.tm.erp.hbm;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtils {

	private static ApplicationContext context;

	private static String springConfigFile = "spring-config.xml" ;
	private static String lastSpringConfigFile = "spring-config.xml" ;

	public static ApplicationContext getApplicationContext(){
		System.out.println("Set config file ->" + springConfigFile + " , old ->" + lastSpringConfigFile);
		System.out.println("(context == null)"+(context == null));
		System.out.println("(!springConfigFile.equalsIgnoreCase(lastSpringConfigFile))"+(!springConfigFile.equalsIgnoreCase(lastSpringConfigFile)));
		if ((context == null)||(!springConfigFile.equalsIgnoreCase(lastSpringConfigFile))) {
			System.out.println("Set config file ->" + springConfigFile);
			context = null ;
			System.gc() ;
			context = new ClassPathXmlApplicationContext(springConfigFile);
			lastSpringConfigFile = springConfigFile ;
		}
		System.out.println("(context == null)"+(context == null));
		System.out.println("(!springConfigFile.equalsIgnoreCase(lastSpringConfigFile))"+(!springConfigFile.equalsIgnoreCase(lastSpringConfigFile)));
		return context;
	}

	public static String getSpringConfigFile() {
		return springConfigFile;
	}

	public static void setSpringConfigFile(String springConfigFile) {
		SpringUtils.springConfigFile = springConfigFile;
	}

//	@Override
//	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//		System.out.println("注入 ============================>" + applicationContext);
//		context = applicationContext;
//		
//	}
	 
}
