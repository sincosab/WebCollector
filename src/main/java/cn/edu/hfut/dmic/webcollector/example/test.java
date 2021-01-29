package cn.edu.hfut.dmic.webcollector.example;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

	public static void main(String[] args) {
		String str = "1988-05-20";
		String pattern = "\\d{4}(\\-|\\/|.)\\d{1,2}\\1\\d{1,2}";
		str = "http://www.ccgp.gov.cn/cggg/zygg/gkzb/202006/t20200601_14386111.htm";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		System.out.println(m.matches());

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date); // 设置为当前时间
		//DateFormat format = new SimpleDateFormat("yyyyMMdd");
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		System.out.println(format.format(date));
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
		date = calendar.getTime();
		System.out.println(format.format(date));
		
		str = "201607280023";
		str = "20160708";
		// String pattern = "\\d{4}(\\-|\\/|.)\\d{1,2}\\1\\d{1,2}";
		pattern = ".*2020(?:05|06)(\\-|\\/|.|)\\d{1,2}\\1\\d{1,2}.*";
		pattern = "^201607(0[0-9]|1[0-9]|2[0-9]).*"; 
	
		r = Pattern.compile(pattern);
		m = r.matcher(str);
		System.out.println(m.matches());

	}

}
