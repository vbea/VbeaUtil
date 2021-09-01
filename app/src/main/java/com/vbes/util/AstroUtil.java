package com.vbes.util;

import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * 根据日期显示星座(线程不安全)
 */
public class AstroUtil {
	String[] astro = new String[] {"摩羯座", "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座"};
	int[] arr = new int[]{20, 19, 21, 20, 21, 22, 23, 23, 23, 24, 23, 22};// 两个星座分割日
	private int year=0, month, day;
	private static AstroUtil astroUtil;

	private AstroUtil() {}
	
	public synchronized static AstroUtil getInstance() {
		if (astroUtil == null)
			astroUtil = new AstroUtil();
		return astroUtil;
	}

	public synchronized int getAge(String date) {
		try {
			if (!VbeUtil.isNullOrEmpty(date)) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date _date = format.parse(date);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(_date);
				year = calendar.get(Calendar.YEAR);
				month = calendar.get(Calendar.MONTH) + 1;
				day = calendar.get(Calendar.DATE);
				return getAge();
			}
		} catch (Exception e) {
			year = month = day = 0;
		}
		return 0;
	}

	/**
	 * 获取年龄
	 * @return
	 */
	private int getAge() {
		//Date now = new Date();
		//int nowyear = now.getYear(); //获取年份
		//int nowmonth = now.getMonth() + 1; //获取月份
		//Change on 2018/11/23
		Calendar calendar = Calendar.getInstance();
		int nowyear = calendar.get(Calendar.YEAR);
		int nowmonth = calendar.get(Calendar.MONTH) + 1;
		// 用文本框输入年龄
		int age = nowyear - year;
		if (month > nowmonth)
			age-=1;
		return age;
	}

	/**
	 * 获取星座
	 * @return
	 */
	public String getAstro() {
        int index = month;
        // 所查询日期在分割日之前，索引-1，否则不变
        if (day < arr[month - 1]) {
            index = index - 1;
        }
        // 返回索引指向的星座string
        return astro[index];
    }
}
