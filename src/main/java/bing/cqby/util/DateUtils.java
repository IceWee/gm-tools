package bing.cqby.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 *
 * @author: bing
 * @date: 2019-01-01
 */
public final class DateUtils {

    /**
     * 流水号格式
     */
    private static final String FORMAT_SERIAL_NO = "yyyyMMddHHmmssSSS";

    /**
     * 年月日时分秒格式
     */
    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    private static final ThreadLocal<SimpleDateFormat> serialNoThreadLocal = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(FORMAT_SERIAL_NO);
        }
    };

    private static final ThreadLocal<SimpleDateFormat> dateTimeThreadLocal = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return new SimpleDateFormat(FORMAT_DATE_TIME);
        }
    };

    private DateUtils() {
        super();
    }

    private static SimpleDateFormat getSerialNoFormat() {
        return serialNoThreadLocal.get();
    }

    private static SimpleDateFormat getDateTimeFormat() {
        return dateTimeThreadLocal.get();
    }

    /**
     * 生成流水号
     *
     * @return
     */
    public static String createSerialNo() {
        Date now = new Date();
        String serialNo = getSerialNoFormat().format(now);
        return serialNo;
    }

    /**
     * 生成时间戳
     *
     * @return
     */
    public static String createTimestamp() {
        Date now = new Date();
        String timestamp = getDateTimeFormat().format(now);
        return timestamp;
    }

}