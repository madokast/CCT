package cn.edu.hust.zrx.cct;

import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Description
 * 对 org.slf4j 的包装
 * 使用 Logger.getLogger()即可获得 org.slf4j.Logger
 * <p>
 * Data
 * 17:10
 *
 * @author zrx
 * @version 1.0
 * @since 2020年2月10日
 */

public class Logger {
    //本类的Logger
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Logger.class);
    //放置全部Logger的MAP
    private static final Map<Class<?>, org.slf4j.Logger> MAP = new HashMap<>();
    static {
        //放入本例的Logger
        MAP.put(Logger.class,LOGGER);
    }

    /**
     * 获得调用类的LOGGER
     * @return 获得调用类的LOGGER
     * @since 2020年2月10日
     */
    public static org.slf4j.Logger getLogger(){
        //找到调用的类
        final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        final StackTraceElement traceElement = stackTrace[2];
        Class<?> klass = null;
        try {
            klass = Class.forName(traceElement.getClassName());
        } catch (ClassNotFoundException ignored) {
            klass = Logger.class;
        }

        //注册
        if(!MAP.containsKey(klass))
            MAP.put(klass,LoggerFactory.getLogger(klass));

        return MAP.get(klass);
    }
}
