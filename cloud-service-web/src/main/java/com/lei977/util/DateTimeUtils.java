package com.lei977.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计算日期
 *
 * @author zhangz
 */
public class DateTimeUtils {
	/**
	 * 获取本周周一和周日
	 *
	 * @param time
	 * @return
	 */
	public static String convertWeekByDate(Date time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00"); // 设置时间格式
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String imptimeBegin = sdf.format(cal.getTime());
		System.out.println("所在周星期一的日期：" + imptimeBegin);
		cal.add(Calendar.DATE, 6);
		String imptimeEnd = sdf1.format(cal.getTime());
		System.out.println("所在周星期日的日期：" + imptimeEnd);
		return imptimeBegin + "," + imptimeEnd;

	}

	/**
	 * 获取本月最后一天
	 *
	 * @return
	 */
	public static String getLastDay() {

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		// 获取当前月最后一天
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = sdf1.format(ca.getTime());
		return last;
	}

	/**
	 * 上月今天现在
	 *
	 * @return
	 */
	public static String getupLastDay() {

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// 获取当前月最后一天
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MONTH, -1);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = sdf1.format(ca.getTime());
		return last;
	}

	/**
	 * 去年的这个时间段
	 *
	 * @param date
	 * @return
	 */
	public static String getupYear(String date) {

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// 获取当前月最后一天
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.YEAR, -1);
		Date dd = null;
		try {
			dd = sdf1.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = sdf1.format(dd.getTime());
		return last;
	}

	/**
	 * 获取去年本月最后一天
	 *
	 * @return
	 */
	public static String getOldLastDay() {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		// 获取当前月最后一天
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.YEAR, -1);
		ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = sdf1.format(ca.getTime());
		System.out.println(last);
		return last;
	}

	/**
	 * 获取本月第一天
	 *
	 * @return
	 */
	public static String getFirstDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		// 获取当前月第一天：
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String first = sdf.format(c.getTime());
		System.out.println(first);
		return first;

	}

	/**
	 * 获取当年的第一天
	 *
	 * @param
	 * @return
	 */
	public static String getCurrYearFirst() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return sdf.format(getYearFirst(currentYear));
	}

	/**
	 * 获取当年的最后一天
	 *
	 * @param
	 * @return
	 */
	public static String getCurrYearLast() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return sdf.format(getYearLast(currentYear));
	}

	/**
	 * 获取某年第一天日期
	 *
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 *
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearLast(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	public static String getupFirstDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		// 获取当前月第一天：
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, -1);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String first = sdf.format(c.getTime());
		System.out.println(first);
		return first;

	}

	/**
	 * 去年的开始时间段从00开始
	 *
	 * @return
	 */
	public static String getupYar(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		// 获取当前月最后一天
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.YEAR, -1);
		Date dd = null;
		try {
			dd = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
		String last = sdf.format(dd.getTime());
		return last;

	}

	/**
	 * 去年本月第一天
	 *
	 * @return
	 */
	public static String getOldFirstDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		// 获取当前月第一天：
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -1);
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String first = sdf.format(c.getTime());
		System.out.println(first);
		return first;

	}

	public static List<Map<String, String>> getFirstday_Lastday_Month() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		String[] strs = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
		for (int i = 0; i < strs.length; i++) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-" + strs[i] + "-01");
			Calendar calendar = Calendar.getInstance();
			// calendar.setTime(date);
			calendar.add(Calendar.MONTH, 0);
			Date theDate = calendar.getTime();

			// 上个月第一天
			GregorianCalendar gcLast = (GregorianCalendar) Calendar.getInstance();
			gcLast.setTime(theDate);
			gcLast.set(Calendar.DAY_OF_MONTH, 1);
			String day_first = df.format(gcLast.getTime());
			StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
			day_first = str.toString();

			// 上个月最后一天
			calendar.add(Calendar.MONTH, 1); // 加一个月
			calendar.set(Calendar.DATE, 1); // 设置为该月第一天
			calendar.add(Calendar.DATE, -1); // 再减一天即为上个月最后一天
			String day_last = df.format(calendar.getTime());
			StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
			day_last = endStr.toString();

			Map<String, String> map = new HashMap<String, String>();
			map.put("first", day_first);
			map.put("last", day_last);
			System.out.println(map.toString());
			list.add(map);
		}

		return list;
	}

	/**
	 * 获取上周周一和周日
	 *
	 * @return
	 */
	public static String getSZ() {
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		int offset1 = 1 - dayOfWeek;
		int offset2 = 7 - dayOfWeek;
		calendar1.add(Calendar.DATE, offset1 - 7);
		calendar2.add(Calendar.DATE, offset2 - 7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		System.out.println(sdf.format(calendar1.getTime()) + "," + sdf.format(calendar2.getTime()));
		return sdf.format(calendar1.getTime()) + "," + sdf1.format(calendar2.getTime());// last
		// Sunday
	}

	/**
	 * 获取去年该周周一和周日
	 *
	 * @return
	 */
	public static String geqntSZ() {
		Calendar calendar1 = Calendar.getInstance();

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.YEAR, -1);
		calendar1.add(Calendar.YEAR, -1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.add(Calendar.YEAR, -1);
		int dayOfWeek = calendar1.get(Calendar.DAY_OF_WEEK) - 1;
		int offset1 = 1 - dayOfWeek;
		int offset2 = 7 - dayOfWeek;
		calendar1.add(canlendar.DATE, offset1 - 7);
		calendar2.add(canlendar.DATE, offset2 - 7);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		System.out.println(sdf.format(calendar1.getTime()) + "," + sdf.format(calendar2.getTime()));
		return sdf.format(calendar1.getTime()) + "," + sdf1.format(calendar2.getTime());// last
		// Sunday
	}

	public static void main(String[] args) {
		// printWeekdays();
		// convertWeekByDate(new Date());
		// printsWeeks();
		// printWeeks();
		// convertWeekByDate(new Date());
		// dateAdds();
		// geqntSZ();
		// BigDecimal bd = new BigDecimal("0.14159265");
		// bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);
		// System.out.println(printWeekdays());
		// getupFirstDay();
		// getupLastDay();
		// getupYar("2016/06/01");
		// System.out.println(getYesterday("yyyy-MM-dd HH:mm:ss"));
		System.out.println("args = " + getPastTime(8, "yyyy-MM-dd HH:mm:ss"));
	}

	/**
	 * 取一个月的所有周
	 */
	public static Map<String, String> printWeeks() {
		Map<String, String> mp = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
		// SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd
		// 00:00:00");
		Calendar calendar = Calendar.getInstance();
		// calendar.add(Calendar.MONTH, -1);
		// calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		int month = calendar.get(Calendar.MONTH);
		int count = 0;
		while (calendar.get(Calendar.MONTH) == month) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				StringBuilder builder = new StringBuilder();
				builder.append(format.format(calendar.getTime()));
				builder.append(",");
				calendar.add(Calendar.DATE, 6);
				builder.append(format.format(calendar.getTime()));
				System.out.println(String.valueOf(++count) + "--" + builder.toString());

				mp.put("" + (0 + count), builder.toString());

			}
			calendar.add(Calendar.DATE, 1);
		}
		return mp;
	}

	public static String dateAdd(int days) {
		// 日期处理模块 (将日期加上某些天或减去天数)返回字符串
		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, days); // 日期减 如果不够减会将月变动
		SimpleDateFormat sdfd = new SimpleDateFormat("yyyyMMdd");
		return sdfd.format(canlendar.getTime());
	}

	/**
	 * 日期处理模块 (将日期加上某些天或减去天数)返回字符串
	 *
	 * @param
	 * @return
	 */
	public static String dateAdds() {
		// 日期处理模块 (将日期加上某些天或减去天数)返回字符串
		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.YEAR, -1);
		canlendar.add(canlendar.DATE, 1); // 日期减 如果不够减会将月变动
		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy/MM/dd 23:00:00");
		System.out.println(sdfd.format(canlendar.getTime()));
		return sdfd.format(canlendar.getTime());
	}

	/**
	 * 去年的今天
	 *
	 * @return
	 */
	public static String getNowOfLastYear() {
		// Date Format will be display
		SimpleDateFormat aSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd 00:00:00");

		GregorianCalendar aGregorianCalendar = new GregorianCalendar();
		// Get last month GregorianCalendar object
		aGregorianCalendar.set(Calendar.YEAR, aGregorianCalendar.get(Calendar.YEAR) - 1);
		// Format the date to get year and month
		String currentYearAndMonth = aSimpleDateFormat.format(aGregorianCalendar.getTime());
		System.out.println(currentYearAndMonth);
		return currentYearAndMonth;
	}

	/**
	 * 取一个月的所有周
	 */
	public static Map<String, String> printsWeeks() {
		Map<String, String> mp = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		// calendar.add(Calendar.MONTH, -1);
		// calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		int month = calendar.get(Calendar.MONTH);
		int count = 0;
		while (calendar.get(Calendar.MONTH) == month) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				StringBuilder builder = new StringBuilder();
				builder.append(format.format(calendar.getTime()));
				builder.append(",");
				calendar.add(Calendar.DATE, 6);
				builder.append(format1.format(calendar.getTime()));
				System.out.println(builder.toString());

				mp.put(String.valueOf(++count), builder.toString());

			}
			calendar.add(Calendar.DATE, 1);
		}
		return mp;
	}

	/**
	 * 取一个月的所有周 格式 yyyy-MM-dd
	 */
	public static Map<String, String> printWeekss() {
		Map<String, String> mp = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		// calendar.add(Calendar.MONTH, -1);
		// calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.DATE, 1);
		int month = calendar.get(Calendar.MONTH);
		int count = 0;
		while (calendar.get(Calendar.MONTH) == month) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				StringBuilder builder = new StringBuilder();
				builder.append(format.format(calendar.getTime()));
				builder.append(",");
				calendar.add(Calendar.DATE, 6);
				builder.append(format1.format(calendar.getTime()));
				System.out.println(builder.toString());

				mp.put(String.valueOf(++count), builder.toString());

			}
			calendar.add(Calendar.DATE, 1);
		}
		return mp;
	}

	/**
	 * 获取上个月所有周
	 *
	 * @return
	 */
	public static Map<String, String> findOweek() {
		Map<String, String> mp = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		// calendar.set(Calendar.DATE, 1);
		int month = calendar.get(Calendar.MONTH);
		int count = 0;
		while (calendar.get(Calendar.MONTH) == month) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				StringBuilder builder = new StringBuilder();
				builder.append(format.format(calendar.getTime()));
				builder.append(",");
				calendar.add(Calendar.DATE, 6);
				builder.append(format1.format(calendar.getTime()));
				System.out.println(builder.toString());
				mp.put(String.valueOf(++count), builder.toString());

			}
			calendar.add(Calendar.DATE, 1);
		}
		return mp;
	}

	/**
	 * 获取去年本月月所有周
	 *
	 * @return
	 */
	public static Map<String, String> findQnweek() {
		Map<String, String> mp = new HashMap<String, String>();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, -1);
		calendar.set(Calendar.DATE, 1);
		int month = calendar.get(Calendar.MONTH);
		int count = 0;
		while (calendar.get(Calendar.MONTH) == month) {
			if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
				StringBuilder builder = new StringBuilder();
				builder.append(format.format(calendar.getTime()));
				builder.append(",");
				calendar.add(Calendar.DATE, 6);
				builder.append(format1.format(calendar.getTime()));
				System.out.println(builder.toString());

				mp.put(String.valueOf(++count), builder.toString());

			}
			calendar.add(Calendar.DATE, 1);
		}
		return mp;
	}

	/**
	 * 获取周一至周五
	 */
	private static final int FIRST_DAY = Calendar.MONDAY;

	public static String printWeekdays() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		String datestr = "";
		setToFirstDay(calendar);
		for (int i = 0; i < 7; i++) {
			printDay(calendar);
			if (i == 6) {
				datestr += dateFormat.format(calendar.getTime());
			} else {
				datestr += dateFormat.format(calendar.getTime()) + ",";

			}
			calendar.add(Calendar.DATE, 1);
		}
		return datestr;
	}

	public static String printsWeekdays() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		String datestr = "";
		setToFirstDay(calendar);
		for (int i = 0; i < 7; i++) {
			printDay(calendar);
			if (i == 6) {
				datestr += dateFormat.format(calendar.getTime());
			} else {
				datestr += dateFormat.format(calendar.getTime()) + ",";

			}
			calendar.add(Calendar.DATE, 1);
		}
		return datestr;
	}

	private static void setToFirstDay(Calendar calendar) {
		while (calendar.get(Calendar.DAY_OF_WEEK) != FIRST_DAY) {
			calendar.add(Calendar.DATE, -1);
		}
	}

	private static void printDay(Calendar calendar) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(dateFormat.format(calendar.getTime()));
	}

	/**
	 * 获取昨天的日期
	 *
	 * @param fromatStr 日期格式
	 * @return
	 */
	public static String getYesterday(String fromatStr) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, -1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat(fromatStr);
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 获得今天的日期
	 *
	 * @param formatStr 日期格式
	 * @return
	 */
	public static String getToday(String formatStr) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		Date date = new Date();
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 获取明天的日期
	 *
	 * @param fromatStr 日期格式
	 * @return
	 */
	public static String getTomorrow(String fromatStr) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat(fromatStr);
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 获取过去第几天的日期
	 *
	 * @param past
	 * @return
	 */
	public static String getPastDate(int past, String formatStr) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		String result = format.format(today);
		return result;
	}

	/**
	 * 获取未来 第 past 天的日期
	 *
	 * @param past
	 * @return
	 */
	public static String getFetureDate(int past, String formatStr) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
		Date today = calendar.getTime();
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		String result = format.format(today);
		return result;
	}

	/**
	 * 获取过去n个小时的时间
	 * 
	 * @param pastTime
	 * @param formaStr
	 * @return
	 */
	public static String getPastTime(int pastTime, String formaStr) {
		String result = "";
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - pastTime);
		SimpleDateFormat df = new SimpleDateFormat(formaStr);
		Date date = calendar.getTime();
		result = df.format(date);
		return result;
	}

}
