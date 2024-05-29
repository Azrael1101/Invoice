package tw.com.tm.erp.aop;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;

public class SystemMonitor {
	private static final LRUMap logMap = new LRUMap(100);

	// 請參考ApectJ網站
	public Object profileMethod(ProceedingJoinPoint call) throws Throwable {
		Log log = obtainLogger(call.getTarget().getClass());
		if (log.isDebugEnabled()) {
			log.debug("call " + call.getSignature().toLongString());
			log.debug("with para: "
					+ ReflectionToStringBuilder.toString(call.getArgs(),
							ToStringStyle.MULTI_LINE_STYLE, true));

			StopWatch watch = new StopWatch();
			watch.start();
			try {
				Object rt = call.proceed();
				log.debug("return : "
						+ ReflectionToStringBuilder.toString(rt,
								ToStringStyle.MULTI_LINE_STYLE, true));
				return rt;
			} catch (Throwable e) {
				log.warn("exception: " + e.getMessage(), e);
				throw e;
			} finally {
				watch.stop();
				log.debug("executing time: "
						+ DurationFormatUtils.formatDuration(watch.getTime(),
								"m:s.S"));
			}
		} else {
			return call.proceed();
		}

	}

	private Log obtainLogger(Class<?> clazz) {
		Log log = (Log) logMap.get(clazz);
		if (null == log) {
			log = LogFactory.getLog(clazz);
			logMap.put(clazz, log);
		}
		return log;
	}
}
