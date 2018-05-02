package cn.tonghao.remex.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.util.Calendar.*;

/**
 * 时间处理类
 */
public class DateTimeUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);

    private static final int TIME_DAY_MILLISECOND = 86400000;

    // 定义时间日期显示格式
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String DATE_FORMAT_CN = "yyyy年MM月dd日";

    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final String TIME_FORMAT_CN = "yyyy年MM月dd日 HH:mm:ss";

    private static final String MONTH_FORMAT = "yyyy-MM";

    private static final String DAY_FORMAT = "yyyyMMdd";

    private static final String TIMESTAMP_FORMAT = "HHmmss";

    private static final String DATE_FORMAT_MS = "yyyyMMddHHmmssSSS";

    /**
     * 1秒钟的毫秒数
     */
    private static final long MILLIS_PER_SECOND = 1000;

    /**
     * 1分钟的毫秒数
     */
    private static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;

    /**
     * 1小时的毫秒数
     */
    private static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;

    private static ThreadLocal<SimpleDateFormat> localFormatter = new ThreadLocal<>();



    /**
     * 取得当前系统时间戳
     *
     * @return Timestamp 系统时间戳
     * @see Timestamp
     */
    public static Timestamp getCurrTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 将日期字符串中的日进行标准化，如2007-9-1变为2007-09-01。
     * @param date 日期字符串
     */
    public static String getFormatDateV2(String date) {
        if (date == null) {
            return null;
        }

        String[] datearr = StringUtils.split(date, "-");
        if (datearr == null || datearr.length != 3) {
            return date;
        }

        StringBuilder ret = new StringBuilder();
        ret.append(datearr[0]);
        ret.append("-");
        ret.append(Integer.parseInt(datearr[1]) < 10 ? "0" + Integer.parseInt(datearr[1]) : datearr[1]);
        ret.append("-");
        ret.append(Integer.parseInt(datearr[2]) < 10 ? "0" + Integer.parseInt(datearr[2]) : datearr[2]);
        return ret.toString();
    }

    /**
     * 从时间串中获取小时数。
     * @param timestr "2007-10-12 13:25:00"
     * @return 小时数
     */
    public static int getHourFromTimeString(String timestr) {
        if (StringUtils.isBlank(timestr)) {
            return 0;
        }
        return Integer.parseInt(timestr.substring(timestr.length() - 8, timestr.length() - 6));
    }

    /**
     * 返回当前时间是上午还是下午
     */
    public static Integer getCurrDateAMorPM() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.AM_PM);
    }

    /**
     * 得到格式化后的日期，格式为yyyy-MM-dd，如2006-02-15
     * @param currDate 要格式化的日期
     * @return String 返回格式化后的日期，默认格式为为yyyy-MM-dd
     */
    public static String getFormatDate(Date currDate) {
        return getFormatDateTime(currDate, DATE_FORMAT);
    }

    /**
     * 得到格式化后的日期，格式为yyyy年MM月dd日，如2006年02月15日
     * @param currDate 要格式化的日期，默认格式为yyyy年MM月dd日
     * @return String 返回格式化后的日期，默认格式为yyyy年MM月dd日，如2006年02月15日
     */
    public static String getFormatDate_CN(Date currDate) {
        return getFormatDateTime(currDate, DATE_FORMAT_CN);
    }

    /**
     * 得到格式化后的日期，格式为yyyy年MM月dd日，如2006年02月15日
     * @param currDate 要格式化的日期
     * @return 返回格式化后的日期，默认格式为yyyy年MM月dd日
     */
    public static Date getFormatDate_CN(String currDate) {
        return getFormatDateTime(currDate, DATE_FORMAT_CN);
    }

    /**
     * 得到格式化后的时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
     * @param currDate 要格式化的时间
     * @return String 返回格式化后的时间，默认格式为yyyy-MM-dd HH:mm:ss
     */
    public static String getFormatDateTime(Date currDate) {
        return getFormatDateTime(currDate, TIME_FORMAT);
    }

    /**
     * 得到格式化后的时间，格式为yyyy-MM-dd HH:mm:ss，如2006-02-15 15:23:45
     * @param currDate 要格式化的时间
     * @return Date 返回格式化后的时间，默认格式为yyyy-MM-dd HH:mm:ss
     */
    public static Date getFormatDateTime(String currDate) {
        return getFormatDateTime(currDate, TIME_FORMAT);
    }

    /**
     * 得到格式化后的时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
     * @param currDate 要格式化的时间
     * @return String 返回格式化后的时间，默认格式为yyyy年MM月dd日 HH:mm:ss
     */
    public static String getFormatDateTime_CN(Date currDate) {
        return getFormatDateTime(currDate, TIME_FORMAT_CN);
    }

    /**
     * 得到格式化后的时间，格式为yyyy年MM月dd日 HH:mm:ss，如2006年02月15日 15:23:45
     * @param currDate 要格式化的时间
     * @return Date 返回格式化后的时间，默认格式为yyyy年MM月dd日 HH:mm:ss
     */
    public static Date getFormatDateTime_CN(String currDate) {
        return getFormatDateTime(currDate, TIME_FORMAT_CN);
    }

    public static String getFormatDateTime_MS(Date currDate) {
        return getFormatDateTime(currDate, DATE_FORMAT_MS);
    }


    /**
     * 根据格式得到格式化后的时间
     * @param currDate 要格式化的时间
     * @param format   时间格式，如yyyy-MM-dd HH:mm:ss
     * @return String 返回格式化后的时间，格式由参数<code>format</code>定义
     */
    private static String getFormatDateTime(Date currDate, String format) {
        if (currDate == null) {
            return "";
        }
        SimpleDateFormat dtFormatdB = localFormatter.get();
        if (dtFormatdB == null) {
            dtFormatdB = new SimpleDateFormat(format);
        }
        try {
            return dtFormatdB.format(currDate);
        } catch (Exception e) {
            logger.error("DateTimeUtil-getFormatDateTime error, {}", e.getMessage());
        }
        return "";
    }

    /**
     * 根据格式得到格式化后的日期
     * @param currDate 要格式化的日期
     * @param format   日期格式，如yyyy-MM-dd
     * @return Date 返回格式化后的日期，格式由参数<code>format</code>定义
     */
    private static Date getFormatDateTime(String currDate, String format) {
        if (currDate == null) {
            return null;
        }
        SimpleDateFormat dtFormatdB = localFormatter.get();
        if (dtFormatdB == null) {
            dtFormatdB = new SimpleDateFormat(format);
        }
        try {
            return dtFormatdB.parse(currDate);
        } catch (Exception e) {
            logger.error("DateTimeUtil-getFormatDateTime error, {}", e.getMessage());
        }
        return null;
    }

    /**
     * 得到本日的上月时间 如果当日为2007-9-1,那么获得2007-8-1
     */
    public static String getDateBeforeMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return getFormatDateTime(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 得到本日的前number个月时间
     */
    public static String getDateBeforeMonth(int number) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -number);
        return getFormatDateTime(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 得到本日的前number个月时间
     */
    public static Date getTimeBeforeMonth(int number) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -number);
        return cal.getTime();
    }

    /**
     * 获取两个日期之间的天数间隔
     */
    public static long getDaysOfDates(Date first, Date second) {
        Date d1 = getFormatDateTime(getFormatDateTime(first), DATE_FORMAT);
        Date d2 = getFormatDateTime(getFormatDateTime(second), DATE_FORMAT);
        long mils = d1.getTime() - d2.getTime();
        return mils / (TIME_DAY_MILLISECOND);
    }

    /**
     * 获得两个Date型日期之间相差的天数（第2个减第1个）
     * @return int 相差的天数
     */
    public static int getDaysBetweenDates(Date first, Date second) {
        Date d1 = getFormatDateTime(getFormatDate(first), DATE_FORMAT);
        Date d2 = getFormatDateTime(getFormatDate(second), DATE_FORMAT);
        Long mils = (d2.getTime() - d1.getTime()) / (TIME_DAY_MILLISECOND);
        return mils.intValue();
    }

    /**
     * 获得两个String型日期之间相差的天数（第2个减第1个）
     * @return int 相差的天数
     */
    public static int getDaysBetweenDates(String first, String second) {
        Date d1 = getFormatDateTime(first, DATE_FORMAT);
        Date d2 = getFormatDateTime(second, DATE_FORMAT);
        Long mils = (d2.getTime() - d1.getTime()) / (TIME_DAY_MILLISECOND);
        return mils.intValue();
    }

    /**
     * @return 获取两个Date之间的天数的列表
     */
    public static List<Date> getDaysListBetweenDates(Date first, Date second) {
        List<Date> dateList = new ArrayList<>();
        Date d1 = getFormatDateTime(getFormatDate(first), DATE_FORMAT);
        Date d2 = getFormatDateTime(getFormatDate(second), DATE_FORMAT);
        if (d1.compareTo(d2) > 0) {
            return dateList;
        }
        do {
            dateList.add(d1);
            d1 = getDateBeforeOrAfter(d1, 1);
        } while (d1.compareTo(d2) <= 0);
        return dateList;
    }

    /**
     * 获取昨天日期
     */
    public static String getDateBeforeDay() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -1);
        return getFormatDateTime(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 得到系统当前日期的前或者后几天
     * @param iDate 如果要获得前几天日期，该参数为负数； 如果要获得后几天日期，该参数为正数
     * @return Date 返回系统当前日期的前或者后几天
     */
    public static Date getDateBeforeOrAfter(int iDate) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, iDate);
        return cal.getTime();
    }

    /**
     * 得到日期的前或者后几天
     * @param iDate 如果要获得前几天日期，该参数为负数； 如果要获得后几天日期，该参数为正数
     * @return Date 返回参数<code>curDate</code>定义日期的前或者后几天
     * @see Calendar#add(int, int)
     */
    public static Date getDateBeforeOrAfter(Date curDate, int iDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curDate);
        cal.add(Calendar.DAY_OF_MONTH, iDate);
        return cal.getTime();
    }

    /**
     * 得到格式化后的月份，格式为yyyy-MM，如2006-02
     * @param currDate 要格式化的日期
     * @return String 返回格式化后的月份，格式为yyyy-MM，如2006-02
     * @see #getFormatDateTime(java.util.Date, String)
     */
    public static String getFormatMonth(Date currDate) {
        return getFormatDateTime(currDate, MONTH_FORMAT);
    }

    /**
     * 得到格式化后的日，格式为yyyyMMdd，如20060210
     * @param currDate 要格式化的日期
     * @return String 返回格式化后的日，格式为yyyyMMdd，如20060210
     */
    public static String getFormatDay(Date currDate) {
        return getFormatDateTime(currDate, DAY_FORMAT);
    }

    public static String getTimestampDay(Date currDate) {
        return getFormatDateTime(currDate, TIMESTAMP_FORMAT);
    }

    public static String getFirstDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        return getFormatDateTime(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 得到格式化后的当月第一天，格式为yyyy-MM-dd，如2006-02-01
     * @param currDate 要格式化的日期
     * @return String 返回格式化后的当月第一天，格式为yyyy-MM-dd，如2006-02-01
     */
    public static String getFirstDayOfMonth(Date currDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currDate);
        int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        return getFormatDateTime(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 得到格式化后的下月第一天，格式为yyyy-MM-dd，如2006-02-01
     * @return String 返回格式化后的下月第一天，格式为yyyy-MM-dd，如2006-02-01
     */
    public static String getFirstDayOfNextMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, +1);
        int firstDay = cal.getMinimum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        return getFormatDateTime(cal.getTime(), DATE_FORMAT);
    }

    public static String getLastDayOfMonth() {
        Calendar cal = Calendar.getInstance();
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return getFormatDateTime(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 得到格式化后的当月最后一天，格式为yyyy-MM-dd，如2006-02-28
     * @param currDate 要格式化的日期
     * @return String 返回格式化后的当月最后一天，格式为yyyy-MM-dd，如2006-02-28
     */
    public static String getLastDayOfMonth(Date currDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currDate);
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        return getFormatDateTime(cal.getTime(), DATE_FORMAT);
    }


    /**
     * 得到日期的前或者后几小时
     * @param iHour 如果要获得前几小时日期，该参数为负数； 如果要获得后几小时日期，该参数为正数
     * @return Date 返回参数<code>curDate</code>定义日期的前或者后几小时
     */
    public static Date getDateBeforeOrAfterHours(Date curDate, int iHour) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(curDate);
        cal.add(Calendar.HOUR_OF_DAY, iHour);
        return cal.getTime();
    }

    // date1加上compday天数以后的日期与当前时间比较，如果大于当前时间返回true，否则false
    public static Boolean compareDay(Date date1, int compday) {
        if (date1 == null)
            return false;
        Date dateComp = getDateBeforeOrAfter(date1, compday);
        Date nowdate = new Date();
        if (dateComp.after(nowdate))
            return true;
        else
            return false;
    }

    /**
     * 计算指定日期+addMonth月+15号 返回格式"2008-02-15"
     */
    public static String genericSpecdate(Date date, int addMonth, int monthDay) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, addMonth);
        cal.set(Calendar.DAY_OF_MONTH, monthDay);
        return getFormatDateTime(cal.getTime(), DATE_FORMAT);
    }

    /**
     * 获得给定时间若干秒以前或以后的日期的标准格式。
     *
     * @param curDate 指定时间
     * @param seconds   间隔时间秒数
     * @return curDate 指定时间加上间隔秒数的时间
     */
    public static Date getSpecifiedDateTimeBySeconds(Date curDate, int seconds) {
        long time = (curDate.getTime() / 1000) + seconds;
        curDate.setTime(time * 1000);
        return curDate;
    }

    public static String getSpecifiedDateTime_month(Date curDate) {
        return getFormatDateTime(curDate, "MM.dd");
    }

    /**
     * 取得多个日期中间隔的最大天数
     */
    public static int maxContinuousDays(Date[][] startDateAndEndDate) {
        // 冒泡排序
        for (int i = 0; i < startDateAndEndDate.length - 1; i++) {
            for (int j = 0; j < startDateAndEndDate.length - i - 1; j++) {
                if (DateTimeUtil.getDaysBetweenDates(startDateAndEndDate[j + 1][0],
                        startDateAndEndDate[j][0]) > 0) {
                    Date[] tempDate = startDateAndEndDate[j];
                    startDateAndEndDate[j] = startDateAndEndDate[j + 1];
                    startDateAndEndDate[j + 1] = tempDate;
                }
            }
        }

        // 合并连续的时间段
        int j = 0;
        Date[][] startDateAndEndDateNew = new Date[startDateAndEndDate.length][2];
        for (int i = 0; i < startDateAndEndDateNew.length; i++) {
            if (j >= startDateAndEndDate.length)
                break;

            startDateAndEndDateNew[i] = startDateAndEndDate[j];
            j++;
            while (j < startDateAndEndDate.length) {
                if (DateTimeUtil.getDaysBetweenDates(startDateAndEndDateNew[i][1],
                        startDateAndEndDate[j][0]) > 0) {
                    break;
                } else if (DateTimeUtil.getDaysBetweenDates(startDateAndEndDateNew[i][1],
                        startDateAndEndDate[j][1]) > 0) {
                    startDateAndEndDateNew[i][1] = startDateAndEndDate[j][1];
                    j++;
                } else if (DateTimeUtil.getDaysBetweenDates(startDateAndEndDateNew[i][1],
                        startDateAndEndDate[j][1]) <= 0) {
                    j++;
                }

            }
        }

        // 选择法排序
        int maxDays = 0;
        for (int i = 0; i < startDateAndEndDateNew.length - 1; i++) {
            Date curEndDate = startDateAndEndDateNew[i][1];
            Date nextStartDate = startDateAndEndDateNew[i + 1][0];
            if (curEndDate == null || nextStartDate == null) {
                break;
            }

            int temDays = DateTimeUtil.getDaysBetweenDates(curEndDate, nextStartDate);
            if (temDays > maxDays) {
                maxDays = temDays;
            }
        }
        return maxDays;
    }

    /**
     * 取得多个日期中间隔的最大天数,这里的参数是用 ","和";"分割的字符字符串例如 "2008-08-03,2008-08-04;"
     */
    public static int maxContinuousDays(String dateStr) {
        String[] seDate = dateStr.split(";");
        Date[][] startDateAndEndDate = new Date[seDate.length][2];

        for (int i = 0; i < seDate.length; i++) {
            String[] tempDate = seDate[i].split(",");
            startDateAndEndDate[i][0] = DateTimeUtil.getFormatDateTime(tempDate[0]);
            startDateAndEndDate[i][1] = DateTimeUtil.getFormatDateTime(tempDate[1]);
        }

        return maxContinuousDays(startDateAndEndDate);

    }


    /**
     * 判断时间段1和时间段2是否有交集
     * @return true:有交集,false:没有交集
     */
    public static boolean isConfilct(String begintimeOne, String endtimeOne, String begintimeTwo,
                                     String endtimeTwo) {
        Date beginOne = getFormatDateTime(begintimeOne);
        Date endOne = getFormatDateTime(endtimeOne);
        Date beginTwo = getFormatDateTime(begintimeTwo);
        Date endTwo = getFormatDateTime(endtimeTwo);
        if ((beginOne.compareTo(beginTwo) <= 0 && endOne.compareTo(beginTwo) >= 0)
                || (beginOne.compareTo(endTwo) <= 0 && endOne.compareTo(endTwo) >= 0)
                || (beginTwo.compareTo(beginOne) <= 0 && endTwo.compareTo(beginOne) >= 0)
                || (beginTwo.compareTo(endOne) <= 0 && endTwo.compareTo(endOne) >= 0)) {
            return true;
        }
        return false;
    }

    /**
     * 取得最早可购买时间
     * @param busytimes 被购买时间,格式为2008-08-06,2008-08-06;2008-08-9,2008-08-12;2008-08-14,2008-08-22;2008-09-04,2008-09-04
     * @param days      购买时长
     * @return 最高可购买时间
     */
    public static String getCansellTime(String busytimes, int days) {
        Map<String, Integer> dayMap = new HashMap<String, Integer>();
        String[] busytimeArr = StringUtils.split(busytimes, ";");
        for (String aBusytimeArr : busytimeArr) {
            String[] time = StringUtils.split(aBusytimeArr, ",");
            Date d1 = getFormatDateTime(time[0], DATE_FORMAT);
            Date d2 = getFormatDateTime(time[1], DATE_FORMAT);
            while (d1.compareTo(d2) <= 0) {
                dayMap.put(getFormatDate(d1), null);
                d1 = getDateBeforeOrAfter(d1, 1);
            }
        }

        Date lastDate = getFormatDateTime(getFormatDate(getDateBeforeOrAfter(29)), DATE_FORMAT);
        Date beginDate = getFormatDateTime(getFormatDate(getDateBeforeOrAfter(2)), DATE_FORMAT);
        Date endDate = getDateBeforeOrAfter(beginDate, days - 1);

        while (beginDate.compareTo(lastDate) <= 0) {
            boolean conflict = false;
            List<Date> daysList = getDaysListBetweenDates(beginDate, endDate);
            for (Date d : daysList) {
                if (dayMap.containsKey(getFormatDate(d))) {
                    conflict = true;
                    break;
                }
            }
            if (!conflict) {
                break;
            }
            beginDate = getDateBeforeOrAfter(beginDate, 1);
            endDate = getDateBeforeOrAfter(beginDate, days - 1);
        }
        return getFormatDate(beginDate);
    }

    public static boolean isWeekend(Date date) {

        Calendar c = getInstance();
        if (date != null) {
            c.setTime(date);
        }
        int dayOfWeek = c.get(DAY_OF_WEEK);
        return dayOfWeek == SUNDAY || dayOfWeek == SATURDAY;
    }

    /**
     * 计算两个日期之间的小时数，不足一小时的被舍去，d1和d2都不能为null
     * @param d1 --日期1
     * @param d2 --日期2
     * @return d1和d2之间相差的小时数
     */
    public static int minusToHours(Date d1, Date d2) {
        return (int) (minusToMilliSecond(d1, d2) / MILLIS_PER_HOUR);
    }

    /**
     * 计算两个日期之间的毫秒数，d1和d2都不能为null
     * @param d1 --日期1
     * @param d2 --日期2
     * @return d1和d2之间相差的毫秒数
     */
    public static long minusToMilliSecond(Date d1, Date d2) {
        if (null == d1 || null == d2) {
            throw new IllegalArgumentException("d1和d2都不能为null");
        }
        return d1.getTime() - d2.getTime();
    }

}

